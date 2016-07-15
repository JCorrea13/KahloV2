package vista;

import com.lynden.gmapsfx.javascript.JavaFxWebEngine;
import com.lynden.gmapsfx.javascript.JavascriptRuntime;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import util.ManejadorArchivos;

import java.io.IOException;

/**
 * Esta clase modela el componente visible del mapa
 * usando la API de Google Maps
 * @author Jose Corra
 * @version 1.0.0
 */
public class GoogleMapView extends AnchorPane {

    protected WebView webview;
    protected JavaFxWebEngine webengine;

    public GoogleMapView(String pathHtml) throws IOException {
        ManejadorArchivos ma = new ManejadorArchivos();
        String html = ma.getContenidoArchivo(pathHtml);

        webview = new WebView();
        webengine = new JavaFxWebEngine(webview.getEngine());

        setTopAnchor(webview, 1.0);
        setLeftAnchor(webview, 1.0);
        setRightAnchor(webview, 1.0);
        setBottomAnchor(webview, 1.0);
        getChildren().add(webview);



        webengine.loadContent(html);

    }

    /**
     * Este metodo ejecuta el script que se pasa como parametro
     * @param script script que se desea ejecutar
     * @return JSObject
     */
    private JSObject executeScript(String script) {
        Object returnObject = webengine.executeScript(script);
        return (JSObject) returnObject;
    }
}
