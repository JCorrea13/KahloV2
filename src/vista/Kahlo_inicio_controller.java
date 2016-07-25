package vista;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import kahlo_configuraciones.ConfiguracionPuertoSerial;
import kahlo_configuraciones.ConfiguracionTelemetria;
import kahlo_mision.Configuracion;
import serialport.Puerto;
import serialport.PuertoSerial;
import util.ManejadorArchivos;
import util.ManejadorPreferencias;

import javax.xml.ws.Action;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.Stack;
import java.util.prefs.Preferences;

import static vista.Configuracion_telemetria_controller.CLAVE_ARCHIVO_TELEMETRIA;
import static vista.configuracion_uart_controller.CLAVE_PUERTO_SERIAL;

/**
 * Esta clase es el controlador de la interfaz
 * de inicio para la aplicacion de kahlo
 * @author Jose Correa
 * @version 1.0.0
 */
public class Kahlo_inicio_controller {

    @FXML
    private ImageView imagen;
    @FXML
    private RadioButton rbtn_mision;
    @FXML
    private RadioButton rbtn_prueba;
    @FXML
    private RadioButton rbtn_archivo;
    @FXML
    private Pane pnl_mision;
    @FXML
    private Pane pnl_puerto;
    @FXML
    private ComboBox cbo_puertos;
    @FXML
    private TextField txt_nombremision;
    @FXML
    private TextField txt_alturamaxima;
    @FXML
    private TextArea txt_descripcion;
    @FXML
    private Button btn_aceptar;

    private static CallBack_Configuracion callBack_configuracion;
    private final ToggleGroup tipos_ejecucion = new ToggleGroup();
    private File archivo;

    @FXML
    private void initialize() throws IOException {
        imagen.setImage(new Image("file:recursos/KahloIcono.png"));
        imagen.setVisible(true);

        cbo_puertos.requestFocus();
        txt_alturamaxima.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txt_alturamaxima.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        rbtn_mision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(rbtn_mision.isSelected()){
                    rbtn_prueba.setSelected(false);
                    rbtn_archivo.setSelected(false);
                    pnl_puerto.setDisable(false);
                    pnl_mision.setDisable(false);
                    setDatosEditables(true);
                }else{
                    rbtn_mision.setSelected(true);
                }
            }
        });
        rbtn_prueba.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(rbtn_prueba.isSelected()) {
                    rbtn_mision.setSelected(false);
                    rbtn_archivo.setSelected(false);
                    pnl_puerto.setDisable(false);
                    pnl_mision.setDisable(true);
                }else{
                    rbtn_prueba.setSelected(true);
                }
            }
        });
        rbtn_archivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(rbtn_archivo.isSelected()){
                    rbtn_prueba.setSelected(false);
                    rbtn_mision.setSelected(false);
                    pnl_puerto.setDisable(true);
                    pnl_mision.setDisable(true);
                }else{
                    rbtn_archivo.setSelected(true);
                }
                lanzaDialogoSeleccionArchivo();
            }
        });
        cargaPuertos();
    }

    @FXML
    private void cargaPuertos(){

        Stack<Puerto> puertos = null;
        try {puertos = PuertoSerial.getListaPuertos();
        }catch (Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Falta instalación");
            a.setContentText("Es posible que la aplicación no este instalada correcatamente" +
                    "da click en el menu Configuración e instala la aplicación");
            a.showAndWait();
        }

        ObservableList p = FXCollections.observableArrayList();
        for (Puerto puerto: puertos) {
            p.add(puerto);
        }
        cbo_puertos.setItems(p);
    }

    @FXML
    private void onTerminarConfiguracion() throws IOException, ClassNotFoundException {
        if(validaCamposConfiguracion())
            if(callBack_configuracion != null) {

                ConfiguracionTelemetria ct = getConfiguracionTelemetria();
                ConfiguracionPuertoSerial cps = getConfiguracionPuertoSerial();

                if(ct == null ||(!rbtn_archivo.isSelected() && cps == null)){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText("Algo anda mal :|");
                    a.setContentText("Verifica la configuraciones: Telemetria, Puerto Serial");
                    a.setTitle("Error Configuración");
                    a.show();
                }


                callBack_configuracion.onGetConfiguracion(getConfiguracion(), ct, cps);
            }
    }

    private ConfiguracionTelemetria getConfiguracionTelemetria() throws IOException, ClassNotFoundException {
        File f = null;
        ConfiguracionTelemetria ct;

        //intentamos recuperar el arvhivo de telemetria correspondiente en caso de ser sesion de archivo
        if(rbtn_archivo.isSelected()){
            String archivo_t = archivo.getParent() + File.separator + archivo.getName().substring(0,archivo.getName().lastIndexOf(".")) + ".kT";
            f = new File(archivo_t);
            ct = null;

            if(f.exists()){
                ct = (ConfiguracionTelemetria) new ObjectInputStream(new FileInputStream(f)).readObject();
                ct.init();

                return ct;
            }
        }

        f = new File(ManejadorPreferencias.getPreferencia(Configuracion_telemetria_controller.class.getName()
                ,CLAVE_ARCHIVO_TELEMETRIA, "").trim());
        ct = null;
        if(f.exists()){
            ct = (ConfiguracionTelemetria) new ObjectInputStream(new FileInputStream(f)).readObject();
            ct.init();
        }

        return ct;
    }

    private ConfiguracionPuertoSerial getConfiguracionPuertoSerial() throws IOException, ClassNotFoundException {
        File f = new File(ManejadorPreferencias.getPreferencia(configuracion_uart_controller.class.getName()
                ,CLAVE_PUERTO_SERIAL, "").trim());
        ConfiguracionPuertoSerial cps = null;
        if(f.exists()){
            cps = (ConfiguracionPuertoSerial) new ObjectInputStream(new FileInputStream(f)).readObject();
        }

        return cps;
    }

    public interface CallBack_Configuracion {
        void onGetConfiguracion(Configuracion configuracion
                , ConfiguracionTelemetria configuracionTelemetria
                , ConfiguracionPuertoSerial configuracionPuertoSerial);
    }

    public static void setCallBack_configuracion(CallBack_Configuracion callBack_configuracion){
        Kahlo_inicio_controller.callBack_configuracion = callBack_configuracion;
    }

    /**
     * Este metodo regresa la Configuracion de la consola
     * @return Configuracion
     */
    private Configuracion getConfiguracion(){
        Configuracion configuracion = null;

        if(rbtn_mision.isSelected()) { //se setean la configuraciones de mision
            configuracion = new Configuracion(Configuracion.Tipo.MISION);
            configuracion.setCommPortIdentifier(((Puerto) cbo_puertos.getSelectionModel().getSelectedItem()).getCommPortIdentifier());
            configuracion.setNombre(txt_nombremision.getText());
            configuracion.setDescripcion(txt_descripcion.getText());
            configuracion.setAltura_maxima_esperada(Integer.valueOf(txt_alturamaxima.getText()));
            configuracion.setInicioMision(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        }else if (rbtn_prueba.isSelected()){ // se setean las configuraciones de prueba
            configuracion = new Configuracion(Configuracion.Tipo.PRUEBA);
            configuracion.setCommPortIdentifier(((Puerto) cbo_puertos.getSelectionModel().getSelectedItem()).getCommPortIdentifier());
        }else{
            configuracion = new Configuracion(Configuracion.Tipo.ARCHIVO);
            configuracion.setRutaArchivo(archivo.getAbsolutePath());
        }

        return configuracion;
    }

    /**
     * Este metodo valida que todas las configuraciones de la sesion
     * sean validas
     * @return True si las configuraciones son validas
     */
    private boolean validaCamposConfiguracion() {
        if(rbtn_mision.isSelected()){ //validamos el caso de que sea mision
            if (txt_nombremision.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error de configuración");
                a.setContentText("La misión debe tener un nombre");
                a.showAndWait();
                txt_nombremision.requestFocus();
                return false;
            }
            if (txt_alturamaxima.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error de configuración");
                a.setContentText("Se debe establecer una altura maxima esperada");
                a.showAndWait();
                txt_alturamaxima.requestFocus();
                return false;
            }
            if (txt_descripcion.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error de configuración");
                a.setContentText("Agrege una descripció de la misión");
                a.showAndWait();
                txt_descripcion.requestFocus();
                return false;
            }
            if (!validaEspaciTrabajo()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error de configuración");
                a.setContentText("El espacio de trabajo establecido no existe" +
                        "abra el menu configuraciones y despues Espacio de trabajo para configurar");
                a.showAndWait();
                return false;
            }
        }

        //se hacen la validaciones para las sesiones tipo MISION y PRUEBA
        if(rbtn_mision.isSelected() || rbtn_prueba.isSelected())
            if(cbo_puertos.getSelectionModel().getSelectedItem() == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error de configuración");
                a.setContentText("Seleccione el puerto en que trabajara la aplicaión");
                a.showAndWait();
                cbo_puertos.requestFocus();
                return false;
            }

        //se hacen las validacione para misiones tipo ARCHIVO
        if(rbtn_archivo.isSelected())
            if(archivo == null || !archivo.exists()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error de configuración");
                a.setContentText("El archivo que seleccionado no existe");
                a.showAndWait();
                lanzaDialogoSeleccionArchivo();
                return false;
            }


        return true;
    }

    /**
     * Este metodo valida que la ruta que esta establecida
     * como directorio de trabajo exista
     * @return True si la ruta existe False en caso contrario
     */
    private boolean validaEspaciTrabajo(){
        Preferences pref = Preferences.userRoot().node(Espacio_trabajo_controller.class.getName());
        return new File(pref.get(Espacio_trabajo_controller.CLAVE_ESPACIO_TRABAJO, "")).exists();
    }

    @FXML
    private void onClickEspaciodeTrabajo() throws IOException {
        FXMLLoader loader_dialog = new FXMLLoader(getClass().getResource("espacio_trabajo.fxml"));
        DialogPane d = loader_dialog.load();
        Espacio_trabajo_controller cet = loader_dialog.getController();

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Configuración");
        a.setDialogPane(d);
        a.showAndWait();
        cet.guardaPreferencia();
    }

    @FXML
    private void onClickIntalar() throws IOException {
        FXMLLoader loader_dialog = new FXMLLoader(getClass().getResource("intalacion_kahlov2.fxml"));
        DialogPane d = loader_dialog.load();
        Instalacion_kahlov2_controller ikc = loader_dialog.getController();

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Configuración");
        a.setDialogPane(d);
        a.showAndWait();
        ikc.guardaPreferencia();
    }

    /**
     * Este metodo lanza el dialogo para
     * seleccionar el archivo del que se leera la mision
     * en caso de ser una sesion de tipo archvo
     */
    private void lanzaDialogoSeleccionArchivo(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona ruta");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mision Kahlo", "*.kab", "*.kar"));
        if(archivo != null) fileChooser.setInitialDirectory(archivo.getParentFile());
        archivo = fileChooser.showOpenDialog(null);
        muestraInformacionArchivoDescripcion();
    }

    @FXML
    private void onClickConfiguraTelemetria() throws IOException {
        FXMLLoader loader_dialog = new FXMLLoader(getClass().getResource("configuracion_telemetria.fxml"));
        DialogPane d = loader_dialog.load();

        Alert a = new Alert(Alert.AlertType.NONE);
        a.setTitle("Configuración");
        a.setDialogPane(d);
        a.showAndWait();
    }

    @FXML
    private void onClickConfiguraPuertoSerial() throws IOException {
        FXMLLoader loader_dialog = new FXMLLoader(getClass().getResource("configuracion_uart.fxml"));
        DialogPane d = loader_dialog.load();

        Alert a = new Alert(Alert.AlertType.NONE);
        a.setTitle("Configuración");
        a.setDialogPane(d);
        a.showAndWait();
    }

    /**
     * Este metodo verifica si existe el archivo de descripcion (*.ka)
     * de la mision que se desea abrir y si existe muestra los
     * datos en la interfaz grafica
     */
    private void muestraInformacionArchivoDescripcion(){
        if(archivo == null) return;
        ManejadorArchivos ma = new ManejadorArchivos();
        String archivo_ka = archivo.getAbsolutePath().substring(0, archivo.getAbsolutePath().length() -1);
        if(ma.existeAchivo(archivo_ka)){
            ArrayList<String> contenido = null;
            try {
                contenido = ma.getContenidoArchivoToArray(archivo_ka);
                if(contenido.size() < 4) return;
                txt_nombremision.setText(contenido.get(1)); //seteamos el titulo
                txt_alturamaxima.setText(contenido.get(2));
                StringBuilder sb = new StringBuilder();
                for (int i = 3; i < contenido.size(); i++) {
                    sb.append(contenido.get(i));
                    sb.append("\n");
                }
                txt_descripcion.setText(sb.toString());
                setDatosEditables(false);
                pnl_mision.setDisable(false);
            } catch (IOException e) {
                e.printStackTrace();}
        }
    }

    /**
     * Este metodo habilita y desabilita los componentes
     * de descripocion de la mision
     * @param b True para habilitar False deshabilitar
     */
    private void setDatosEditables(boolean b){
        txt_nombremision.setEditable(b);
        txt_alturamaxima.setEditable(b);
        txt_descripcion.setEditable(b);
    }
}
