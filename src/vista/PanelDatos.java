package vista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import kahlo_configuraciones.ConfiguracionSensor;
import vista.Panel_datos_controller.*;

/**
 * Esta clase sirve como contenedor de los paneles
 *  de datos.
 *
 *  @author Jose Correa
 *  @version 1.0.0
 */
public class PanelDatos extends ListView {

    HashMap<String, Panel_datos_controller> sensores;
    ObservableList<Pane> data = FXCollections.observableArrayList();
    CallBackClick callBackClick;

    public PanelDatos(CallBackClick callBackClick){
        sensores = new HashMap<>();
        this.callBackClick = callBackClick;
    }

    public void setSensores(ArrayList<ConfiguracionSensor> configuraciones) throws IOException {
        inicializaPanel(configuraciones);
    }


    private void inicializaPanel(ArrayList<ConfiguracionSensor> configuraciones) throws IOException {
        for(ConfiguracionSensor config: configuraciones){
            Pane p = getPanelDatos(config);
            data.add(p);
        }
        this.setItems(data);
    }

    private Pane getPanelDatos(ConfiguracionSensor c) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("panel_dato.fxml"));
        Pane p = loader.load();
        Panel_datos_controller controller = loader.getController();

        controller.setConfiguracion(c);
        controller.setCallBackClick(callBackClick);
        sensores.put(c.getId_sensor_property(), controller);

        return p;
    }

    /**
     * Este metodo actualiza el valor del panel con el identificador
     * que pasa como parametro
     * @param identificador id del panel
     * @param valor nuevo valor
     */
    public void actualiza_dato(String identificador, String valor){
        sensores.get(identificador).setValor(valor);
    }

}
