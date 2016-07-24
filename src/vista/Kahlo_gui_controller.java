package vista;

import gnu.io.PortInUseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import kahlo_configuraciones.ConfiguracionTelemetria;
import kahlo_mision.Configuracion;
import kahlo_configuraciones.ConfiguracionSensor;
import kahlo_mision.Manejador_de_Datos;
import vista.Panel_datos_controller.*;

import java.io.IOException;
import java.util.*;

/**
 * Esta clase es el controlador de la interfaz
 * grafica de usuario de kahlo
 * @author Jose Correa
 * @version 1.0.0
 */
public class Kahlo_gui_controller implements CallBackClick{

    @FXML
    private SplitPane splitPane;
    @FXML
    private SplitPane splitPane_mapa;

    private VistaMapa mapa;
    private ArrayList<ConfiguracionSensor> sensores;
    private HashMap<String, Grafica> graficas = new HashMap<>();
    private Configuracion configuracion;
    private Manejador_de_Datos mada;
    private CallBackFinSesion  callBackFinSesion;
    private ConfiguracionTelemetria configuracionTelemetria;

    //objetos para manejo de lata
    private FXMLLoader loader_lata3d;
    private Panel_lata3D_controller panel_lata3D_controller;

    //objetos para manejo de panel de datos
    @FXML
    private PanelDatos panel_datos;
    private Timer lectoDatos;

    @FXML
    private void initialize() throws IOException {
        mapa = new VistaMapa("src/vista/mapa.html");

        //creamos los componentes graficos
        loader_lata3d = new FXMLLoader(getClass().getResource("panel_lata3D.fxml"));
        Pane panel_lata3d = loader_lata3d.load();
        panel_datos = new PanelDatos(this);
        //recuperamos los controladores
        panel_lata3D_controller = loader_lata3d.getController();

        splitPane.getItems().set(0, panel_lata3d);
        splitPane.getItems().set(1, panel_datos);
        splitPane_mapa.getItems().set(0, mapa);

    }

    private void initDatos() throws IOException {

        sensores = new ArrayList<>();
        ArrayList<ConfiguracionSensor> configuracionSensores = configuracionTelemetria.getSensoresHabilitados();
        for(ConfiguracionSensor c: configuracionSensores) {
            sensores.add(c);
            if(c.isGrafica())
                graficas.put(c.getId_sensor_property(), new Grafica(c.getEtiqueta_sensor_property()
                                                            , c.getValor_max_property()
                                                            , c.getValor_min_property()
                                                            , configuracion.getAltura_maxima_esperada()
                                                            , configuracionTelemetria.getPeriodo()));
        }
        panel_datos.setSensores(sensores);

        mapa.iniciaAnimacion(configuracionTelemetria.getPeriodo());
    }


    /**
     * Este metodo sirve para setear la configuracion en la que se
     * usara la consola y en este momento se inicia la lectura de datos
     * @param configuracion configuracion de la consola deseada
     */
    public void setConfiguracion(Configuracion configuracion, ConfiguracionTelemetria configuracionTelemetria) throws PortInUseException, IOException {
        this.configuracion = configuracion;
        this.configuracionTelemetria = configuracionTelemetria;
        mada = new Manejador_de_Datos(configuracion, configuracionTelemetria);
        lectoDatos = new Timer(true);
        lectoDatos.scheduleAtFixedRate(new LectorDatos(), 100, configuracionTelemetria.getPeriodo());
        initDatos();
    }

    @Override
    public void onClickEtiqueta(String grafica) {
        if(graficas.containsKey(grafica))
            splitPane_mapa.getItems().set(1, graficas.get(grafica));
    }

    private void actualizaDatos(HashMap<String,String> telemetria){

        double altitud = 0;
        if(telemetria.containsKey("ALTITUD"))
            altitud = Double.valueOf(telemetria.get("ALTITUD"));

        //actualizamos los datos de las graficas
        for (Map.Entry<String,Grafica> g : graficas.entrySet()) {
            if(telemetria.containsKey(g.getKey()))
                g.getValue().agrega_datos(Double.valueOf(telemetria.get(g.getKey())), altitud);
        }

        for(ConfiguracionSensor c: sensores)
            if(telemetria.containsKey(c.getId_sensor_property()))
                panel_datos.actualiza_dato(c.getId_sensor_property(), telemetria.get(c.getId_sensor_property()));

        //verificamos si esta habilitado el campo de CONTADOR
        if(telemetria.containsKey("CONTADOR"))
            panel_datos.actualiza_dato("CONTADOR", getEficiencia(Double.valueOf(telemetria.get("CONTADOR")), mada.getCadenas_recibidas()));

        //actualizamos los datos del mapa
        if(telemetria.containsKey("LATITUD") && telemetria.containsKey("LONGITUD"))
            mapa.centra(Double.valueOf(telemetria.get("LATITUD")), Double.valueOf(telemetria.get("LONGITUD")));

        //actualizamos los datos del modelo 3D de la lata
        if(telemetria.containsKey("ROLL") && telemetria.containsKey("YAW") && telemetria.containsKey("PITCH"))
        panel_lata3D_controller.rota(Double.valueOf(telemetria.get("ROLL")), Double.valueOf(telemetria.get("YAW")) ,Double.valueOf(telemetria.get("PITCH")));
        //System.out.println("actualizando datos");
    }

    private String getEficiencia(double contador, int cadenas_recibidas){
        double eficiencia = (100/contador)*cadenas_recibidas;
        return eficiencia + "%";
    }

    @FXML
    private void termianaSesion(){
        mada.cierraPuertoSerial();
        lectoDatos.cancel();
        if(configuracion.getTipo() == Configuracion.Tipo.MISION)
            try {mada.guardaMision();
            } catch (IOException e) {e.printStackTrace();}
        callBackFinSesion.onTerminaSesion();
    }

    public void setCallBackFinSesion(CallBackFinSesion callBackFinSesion) {
        this.callBackFinSesion = callBackFinSesion;
    }

    /**
     * Esta clase se encarga de leer periodicamente
     * los datos desde el Manejador de datos
     */
    private class LectorDatos extends TimerTask {

        @Override
        public void run() {
            try {actualizaDatos(mada.getTelemetria());
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    public interface CallBackFinSesion{
        void onTerminaSesion();
    }
}
