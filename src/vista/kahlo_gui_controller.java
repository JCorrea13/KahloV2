package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

/**
 * Esta clase es el controlador de la interfaz
 * grafica de usuario de kahlo
 * @author Jose Correa
 * @version 1.0.0
 */
public class kahlo_gui_controller implements panel_datos_controller.CallBack_ClickLabel{

    @FXML
    private BorderPane borderPane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private SplitPane splitPane_mapa;

    private GoogleMapView mapComponent;
    HashMap<Graficas_e, Grafica> graficas = new HashMap<>();


    @FXML
    private void initialize() throws IOException {
        mapComponent = new GoogleMapView("src/vista/mapa.html");
        AnchorPane panel_datos = FXMLLoader.load(getClass().getResource("panel_datos.fxml"));

        Pane panel_lata3D = FXMLLoader.load(getClass().getResource("panel_lata3D.fxml"));
        initGraficas();



        splitPane.getItems().set(0, panel_lata3D);
        splitPane.getItems().set(1, panel_datos);
        splitPane_mapa.getItems().set(0, mapComponent);
        action_clickLabel_datos(Graficas_e.TEMPERATURA);

        prueba_grafica(graficas.get(Graficas_e.TEMPERATURA));
    }

    private void prueba_grafica(Grafica grafica){
        grafica.agrega_datos(5,1);
        grafica.agrega_datos(10,3);
        grafica.agrega_datos(13,7);
        grafica.agrega_datos(14,8);
        grafica.agrega_datos(13,19);
        grafica.agrega_datos(5,1);
        grafica.agrega_datos(10,3);
        grafica.agrega_datos(13,7);
        grafica.agrega_datos(14,8);
        grafica.agrega_datos(13,19);
        grafica.agrega_datos(5,1);
        grafica.agrega_datos(10,3);
        grafica.agrega_datos(13,7);
        grafica.agrega_datos(14,8);
        grafica.agrega_datos(13,19);
        grafica.agrega_datos(5,1);
        grafica.agrega_datos(10,3);
        grafica.agrega_datos(13,7);
        grafica.agrega_datos(14,8);
        grafica.agrega_datos(13,19);
        grafica.agrega_datos(5,1);
        grafica.agrega_datos(10,3);
        grafica.agrega_datos(13,7);
        grafica.agrega_datos(14,8);
        grafica.agrega_datos(13,19);
        grafica.agrega_datos(5,1);
        grafica.agrega_datos(10,3);
        grafica.agrega_datos(13,7);
        grafica.agrega_datos(14,8);
        grafica.agrega_datos(13,19);
    }


    private void initGraficas(){
        panel_datos_controller.setCallBack(this);
        graficas.put(Graficas_e.PRESION_BAROMETRICA, new Grafica("Presión Barométrica", 30, 0, 20));
        graficas.put(Graficas_e.TEMPERATURA, new Grafica("Temperatura", 30, 0, 20));
        graficas.put(Graficas_e.HUMEDAD, new Grafica("Humedad", 30, 0, 20));
        graficas.put(Graficas_e.TEMPERATURA_EXTERNA, new Grafica("Temperatura Externa", 30, 0, 20));
    }

    @Override
    public void action_clickLabel_datos(Graficas_e g){
        splitPane_mapa.getItems().set(1, graficas.get(g));
    }

    public enum Graficas_e{
        PRESION_BAROMETRICA,
        TEMPERATURA,
        HUMEDAD,
        TEMPERATURA_EXTERNA
    }
}
