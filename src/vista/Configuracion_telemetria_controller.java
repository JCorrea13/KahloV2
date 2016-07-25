package vista;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import kahlo_configuraciones.ConfiguracionSensor;
import kahlo_configuraciones.ConfiguracionTelemetria;
import kahlo_mision.Configuracion;
import util.ManejadorPreferencias;
import util.ManejadorSerializables;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Esta clase es el contorlador del dialogo
 * de configuracion de telemetria
 *
 * @author Jose Correa
 * @version 1.0.0
 */
public class Configuracion_telemetria_controller {

    public static final String CLAVE_ARCHIVO_TELEMETRIA = "K_AT";
    @FXML
    private TextField txt_ruta;
    @FXML
    private TextField txt_periodo;
    @FXML
    private TextField txt_tamanio;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btn_agregar;
    @FXML
    private Button btn_remover;

    private File archivo_configuracion;
    private TablaConfiguracionTelemetria tablaConfiguracionTelemetria;
    private final ObservableList<ConfiguracionSensor> datos = FXCollections.observableArrayList();

    private final List<String> SENIALES_BASICAS = Arrays.asList(new String[] {"K","CONTADOR", "LONGITUD",
            "LATITUD", "ALTITUD", "ROLL", "YAW", "PITCH"});

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        txt_ruta.setText(ManejadorPreferencias.getPreferencia(this.getClass().getName(),CLAVE_ARCHIVO_TELEMETRIA, ""));
        txt_tamanio.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txt_tamanio.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        txt_periodo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txt_periodo.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        tablaConfiguracionTelemetria = new TablaConfiguracionTelemetria();
        tablaConfiguracionTelemetria.setEditable(true);
        tablaConfiguracionTelemetria.setPrefWidth(700);
        tablaConfiguracionTelemetria.setPrefHeight(100);

        borderPane.setCenter(tablaConfiguracionTelemetria);
        cargaConfiguracion();
    }

    private void cargaConfiguracion() throws IOException, ClassNotFoundException {
        archivo_configuracion = new File(txt_ruta.getText().trim());
        ConfiguracionTelemetria ct = null;
        datos.removeAll(datos);

        if(archivo_configuracion.exists()){
            ct = (ConfiguracionTelemetria) ManejadorSerializables.leeObjeto(archivo_configuracion.getAbsolutePath(), ConfiguracionTelemetria.class);
            if(ct != null){
                ct.init();
                datos.addAll(ct.getSensores());
                txt_tamanio.setText(String.valueOf(ct.getTamanio_telemetria()));
                txt_periodo.setText(String.valueOf(ct.getPeriodo()));
            }
        }

        //Agregamos las seniales fijas de la consola
        for(String s: SENIALES_BASICAS)
            if(ct == null || !ct.contiene(s))
                datos.add(new ConfiguracionSensor(s));

        tablaConfiguracionTelemetria.setItems(datos);
    }

    @FXML
    private void onAgregarClick(){
        if(tablaConfiguracionTelemetria != null)
            datos.add(new ConfiguracionSensor("  -----  "));
    }

    @FXML
    private void onRemoverClick(){
        ConfiguracionSensor cs = (ConfiguracionSensor) tablaConfiguracionTelemetria.getSelectionModel().getSelectedItem();
        if(datos != null && !SENIALES_BASICAS.contains(cs.getId_sensor_property()))
            datos.remove(cs);
    }

    @FXML
    private void lanzaDialogoSeleccionArchivo() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona ruta");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mision Kahlo", "*.kT"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mision Kahlo", "*.*"));
        if(archivo_configuracion != null) fileChooser.setInitialDirectory(archivo_configuracion.getParentFile());
        archivo_configuracion = fileChooser.showOpenDialog(null);
        if(archivo_configuracion != null) txt_ruta.setText(archivo_configuracion.getAbsolutePath().toString());
        cargaConfiguracion();
    }

    @FXML
    public void guardaCambios() throws IOException {

        if(!validaCampos())
            return;

        ManejadorSerializables.escribeObjeto(txt_ruta.getText().trim()
                ,new ConfiguracionTelemetria(getConfigs()
                        ,Integer.valueOf(txt_tamanio.getText())
                        ,Integer.valueOf(txt_periodo.getText())));

        //se guarda la ruta como la ultima preferida
        ManejadorPreferencias.setPreferencia(this.getClass().getName()
                ,CLAVE_ARCHIVO_TELEMETRIA, String.valueOf(archivo_configuracion.getAbsolutePath()));
    }

    /**
     * Este metodo valida todos los campos del dialogo
     * para configurar la telemetria incluyendo la tabla
     * @return True si todos los dialogos son conrrectos False en otro caso
     */
    private boolean validaCampos(){
        Alert alert;
        if(txt_tamanio.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Configura el tamaño de la telemetria");
            alert.showAndWait();
            txt_tamanio.requestFocus();
            return false;
        }

        if(txt_periodo.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Configura el periodo de la telemetria");
            alert.showAndWait();
            txt_periodo.requestFocus();
            return false;
        }

        ArrayList<ConfiguracionSensor> a = getConfigs();
        for(ConfiguracionSensor c: a)
            if(hayMayorIgual(c.getBytes_property(),Integer.valueOf(txt_tamanio.getText().trim()))) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Existen indices de bytes fuera del rango" +
                        "verifica de acuerdo al tamanio de telemetria establecido");
                alert.showAndWait();
                return false;
            }

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Configuración guardada con exito");
        alert.showAndWait();
        return true;
    }

    /**
     * Este metodo valida si algun numero del arreglo que pasa como parametro
     * es mayot o igual al numero que pasa como mayor
     * @param bytes arreglo de enteros
     * @param mayor numeros mayor
     * @return True si existe algun numero mayor en el arregle False si no existe
     */
    private boolean hayMayorIgual(int [] bytes, int mayor){

        for(int i = 0; i < bytes.length; i++)
            if(bytes[i] >= mayor)
                return true;

        return false;
    }

    private ArrayList<ConfiguracionSensor> getConfigs(){

        ArrayList<ConfiguracionSensor> configuraciones = new ArrayList();

        tablaConfiguracionTelemetria.setItems(datos);
        for(int i = 0; i < datos.size(); i ++)
            configuraciones.add((ConfiguracionSensor) datos.get(i));

        return configuraciones;
    }
}
