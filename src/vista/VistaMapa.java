package vista;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.ManejadorArchivos;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Esta clase modela el componente visible del mapa
 * usando la API de Google Maps
 * @author Jose Corra
 * @version 1.0.0
 */
public class VistaMapa extends AnchorPane {

    private WebView webview;
    private WebEngine webengine;
    private ConcurrentLinkedQueue<Coordenada> datos = new ConcurrentLinkedQueue();
    private long periodo;

    public VistaMapa(String pathHtml) throws IOException {
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

    public void iniciaAnimacion(int periodo){
        this.periodo = periodo * 1000000; //lo pasamos de milisegundo a nanosengudos
        // Iniciamos la animacion de la grafica de tiempo real
        new VistaMapa.Actulizador().start();
    }

    /**
     * Este metodo centra y pone el marcador en las coordenadas
     * que se pasan como parametro
     * @param lati latitud
     * @param longi longitud
     */
    private void centrar(double lati, double longi){
        webengine.executeScript("centrar("+ lati +","+ longi +");");
    }

    /**
     * Este metodo acutaliza las coordenadas en el mapa
     * @param latitud latitud
     * @param longitud longitud
     */
    public void centra(double latitud, double longitud){
        datos.add(new VistaMapa.Coordenada(latitud, longitud));
    }

    /**
     * Esta clase es ejecutada por la el Thread de JavaFx
     * que actualiza las coordenadas en las que se
     * centrara el mapa
     */
    private class Actulizador extends AnimationTimer {
        private long lastUpdate = 0 ;

        @Override
        public void handle(long now) {
            if (now - lastUpdate >= periodo) {
                run();
                lastUpdate = now;
            }
        }

        private VistaMapa.Coordenada coordenada;
        public void run() {
            if (!datos.isEmpty()) {
                coordenada = datos.remove();
                centrar(coordenada.getLatitud(), coordenada.getLongitud());
            }
        }
    }

    public class Coordenada{

        double latitud;
        double longitud;

        public Coordenada(double latitud, double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        public double getLatitud() {
            return latitud;
        }

        public void setLatitud(double latitud) {
            this.latitud = latitud;
        }

        public double getLongitud() {
            return longitud;
        }

        public void setLongitud(double longitud) {
            this.longitud = longitud;
        }
    }
}
