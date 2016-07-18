package vista;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.ManejadorArchivos;

import java.io.IOException;

/**
 * Esta clase modela el componente visible del mapa
 * usando la API de Google Maps
 * @author Jose Corra
 * @version 1.0.0
 */
public class GoogleMapView extends AnchorPane {

    private WebView webview;
    private WebEngine webengine;

    public GoogleMapView(String pathHtml) throws IOException {
        ManejadorArchivos ma = new ManejadorArchivos();
        String html = ma.getContenidoArchivo(pathHtml);

        webview = new WebView();
        webengine = webview.getEngine();

        setTopAnchor(webview, 1.0);
        setLeftAnchor(webview, 1.0);
        setRightAnchor(webview, 1.0);
        setBottomAnchor(webview, 1.0);
        getChildren().add(webview);

        webengine.loadContent(html);
        init();
    }


    /**
     * Este metodo inicializa el mapa en el visor web
     * lo centra y lo lanza
     */
    private void init(){
        webengine.documentProperty().addListener(new ChangeListener<org.w3c.dom.Document>() {
            @Override
            public void changed(ObservableValue<? extends org.w3c.dom.Document> observable, org.w3c.dom.Document oldValue, org.w3c.dom.Document newValue) {
                webengine.executeScript("initialize();");
            }
        });
    }

    /**
     * Este metodo centra y pone el marcador en las coordenadas
     * que se pasan como parametro
     * @param lati latitud
     * @param longi longitud
     */
    public void centrar(double lati, double longi){
        webengine.executeScript("centrar("+ lati +","+ longi +");");
    }

}
