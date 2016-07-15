package vista;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Esta clase es el controlador de la interfaz
 * grafica de usuario de kahlo
 * @author Jose Correa
 * @version 1.0.0
 */
public class kahlo_gui_opc1_controller {

    @FXML
    private AnchorPane pnl_mapa;
    @FXML
    private SplitPane split_graficasMapa;

    private GoogleMapView mapComponent;
    private GoogleMap map;


    @FXML
    private void initialize() throws IOException {
        mapComponent = new GoogleMapView("src/vista/mapa.html");
        split_graficasMapa.getItems().set(1, mapComponent);
        split_graficasMapa.setMaxHeight(Double.MAX_VALUE);
        split_graficasMapa.setMaxWidth(Double.MAX_VALUE);
        split_graficasMapa.setMinHeight(Double.MAX_VALUE);
        split_graficasMapa.setMinWidth(Double.MAX_VALUE);
    }
}
