package vista;

import com.lynden.gmapsfx.javascript.object.*;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Esta clase es el controlador de la interfaz
 * grafica de usuario de kahlo
 * @author Jose Correa
 * @version 1.0.0
 */
public class kahlo_gui_controller {

    @FXML
    private BorderPane borderPane;

    private GoogleMapView mapComponent;
    private GoogleMap map;


    @FXML
    private void initialize() throws IOException {
        mapComponent = new GoogleMapView("src/vista/mapa.html");
        borderPane.setCenter(mapComponent);
    }
}
