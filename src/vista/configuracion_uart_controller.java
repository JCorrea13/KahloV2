package vista;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import kahlo_configuraciones.ConfiguracionPuertoSerial;
import kahlo_configuraciones.ConfiguracionTelemetria;
import kahlo_mision.KahloV2_Static;
import util.ManejadorPreferencias;
import util.ManejadorSerializables;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Esta clase es el controlador del dialogo
 * de configuracion del puerto serial
 *
 * @author Jose Correa
 * @version 1.0.0
 */
public class configuracion_uart_controller {

    public static final String CLAVE_PUERTO_SERIAL = "K_PS";
    ObservableList<Integer> velocidades = FXCollections.observableArrayList();
    ObservableList<String> pardidades = FXCollections.observableArrayList();

    @FXML
    private ComboBox cbo_velocidad;
    @FXML
    private TextField txt_parada;
    @FXML
    private TextField txt_datos;
    @FXML
    private TextField txt_ruta;
    @FXML
    private ComboBox cbo_paridad;

    private File archivo_configuracion;


    @FXML
    private void initialize(){
        cargaCombos();
        txt_datos.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txt_datos.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        txt_parada.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txt_parada.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        cbo_velocidad.setItems(velocidades);
        cbo_paridad.setItems(pardidades);

        //Se carga la configuracion establecida si existe
        txt_ruta.setText(ManejadorPreferencias.getPreferencia(this.getClass().getName(),CLAVE_PUERTO_SERIAL, ""));
        cargaConfiguracion();
    }

    private void cargaCombos(){
        int [] d = new int []{ 110, 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 56000
                ,57600, 115200, 128000, 153600, 230400, 256000, 230400, 256000, 460800, 921600};
        velocidades.addAll(IntStream.of(d).boxed().collect(Collectors.toList()));

        String [] s = new String []{"PARITY_NONE","PARITY_ODD","PARITY_EVEN","PARITY_MARK", "PARITY_SPACE"};
        List<String> lst = Arrays.asList(s);
        pardidades.addAll(lst);
    }

    private boolean valida(){
        if(txt_datos.getText().trim().isEmpty()){
            txt_datos.requestFocus();
            return false;
        }

        if(txt_parada.getText().trim().isEmpty()){
            txt_parada.requestFocus();
            return false;
        }

        if(cbo_velocidad.getSelectionModel().getSelectedItem() == null){
            cbo_velocidad.requestFocus();
            return false;
        }

        if(cbo_velocidad.getSelectionModel().getSelectedItem() == null){
            cbo_velocidad.requestFocus();
            return false;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Configuración guardada con exito");
        alert.showAndWait();
        return true;
    }

    @FXML
    private void onClickGuardar(){
        if(!valida()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Configuración");
            a.setContentText("Todos los campos deben ser configurados");
            a.show();
        }

        //Guardamos todos los datos
        ManejadorSerializables.escribeObjeto(txt_ruta.getText().trim()
                ,new ConfiguracionPuertoSerial(
                                                (int) cbo_velocidad.getSelectionModel().getSelectedItem()
                                                ,Integer.valueOf(txt_parada.getText().trim())
                                                ,Integer.valueOf(txt_datos.getText().trim())
                                                ,cbo_paridad.getSelectionModel().getSelectedIndex()
                                                ));

        //se guarda la ruta como la ultima preferida
        ManejadorPreferencias.setPreferencia(this.getClass().getName()
                ,CLAVE_PUERTO_SERIAL, String.valueOf(archivo_configuracion.getAbsolutePath()));
    }

    @FXML
    private void lanzaDialogoSeleccionArchivo() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona ruta");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mision Kahlo", "*.kS"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mision Kahlo", "*.*"));
        if(archivo_configuracion != null && archivo_configuracion.exists()) fileChooser.setInitialDirectory(archivo_configuracion.getParentFile());
        archivo_configuracion = fileChooser.showOpenDialog(null);
        if(archivo_configuracion != null) txt_ruta.setText(archivo_configuracion.getAbsolutePath().toString());
        cargaConfiguracion();
    }

    private void cargaConfiguracion(){
        archivo_configuracion = new File(txt_ruta.getText().trim());
        ConfiguracionPuertoSerial cs = null;

        if(archivo_configuracion.exists()){
            cs = (ConfiguracionPuertoSerial) ManejadorSerializables.leeObjeto(archivo_configuracion.getAbsolutePath(), ConfiguracionPuertoSerial.class);
            if(cs != null){
                txt_datos.setText(String.valueOf(cs.getBits_datos()));
                txt_parada.setText(String.valueOf(cs.getBits_parada()));
                cbo_velocidad.getSelectionModel().select((Object) cs.getVelocidad());
                cbo_paridad.getSelectionModel().select(cs.getParidad());
            }
        }
    }
}
