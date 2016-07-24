import com.sun.istack.internal.NotNull;
import gnu.io.PortInUseException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kahlo_configuraciones.ConfiguracionTelemetria;
import kahlo_mision.Configuracion;
import vista.Kahlo_gui_controller;
import vista.Kahlo_inicio_controller;
import vista.Kahlo_inicio_controller.CallBack_Configuracion;
import vista.Kahlo_gui_controller.CallBackFinSesion;

import java.io.IOException;

/**
 * Esta clase es la clase principal de la aplicacion
 * vista.KahloV2
 * @author Jose Correa
 * @version 1.0.0
 */
public class KahloV2 extends Application implements CallBack_Configuracion, CallBackFinSesion {

    public static final String KEY_NODO_PREFS = "KAHLOV2";
    private Stage primaryStage;

    //Objetos para el manejo de la interfaz grafica
    private FXMLLoader loader_inicio;
    private FXMLLoader loader_consola;

    private Parent inicio;
    private Parent consola;

    //Escenas
    private Scene escena_inicio;
    private Scene escena_consola;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        loader_inicio = new FXMLLoader(getClass().getResource("vista/kahlo_inicio.fxml"));
        loader_consola = new FXMLLoader(getClass().getResource("vista/kahlo_gui.fxml"));

        inicio = loader_inicio.load();
        consola = loader_consola.load();
        Kahlo_inicio_controller.setCallBack_configuracion(this);

        escena_inicio = new Scene(inicio, 930, 491);
        escena_consola = new Scene(consola);

        primaryStage.setTitle("Kahlo V2");
        primaryStage.setScene(escena_inicio);
        primaryStage.getIcons().add(new Image("file:recursos/KahloIcono.png"));

        primaryStage.show();
    }

    public static void main(String [] args){
        System.setProperty("java.net.useSystemProxies", "true");
        launch(args);
    }

    @Override
    public void onGetConfiguracion(Configuracion configuracion, ConfiguracionTelemetria configuracionTelemetria){
        primaryStage.setScene(escena_consola);
        primaryStage.setMaximized(true);
        Kahlo_gui_controller controller_consola = loader_consola.getController();
        controller_consola.setCallBackFinSesion(this);
        try {
            controller_consola.setConfiguracion(configuracion, configuracionTelemetria);
        } catch (PortInUseException e) {e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}

    }

    @Override
    public void onTerminaSesion() {
        try {
            start(primaryStage);
        } catch (Exception e) {e.printStackTrace();}
    }
}
