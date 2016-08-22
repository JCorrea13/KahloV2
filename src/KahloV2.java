import com.sun.istack.internal.NotNull;
import gnu.io.PortInUseException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import kahlo_configuraciones.ConfiguracionPuertoSerial;
import kahlo_configuraciones.ConfiguracionTelemetria;
import kahlo_mision.Configuracion;
import kahlo_mision.KahloV2_Static;
import vista.Kahlo_gui_controller;
import vista.Kahlo_inicio_controller;

import java.io.IOException;

/**
 * Esta clase es la clase principal de la aplicacion
 * vista.KahloV2
 * @author Jose Correa
 * @version 1.0.0
 */
public class KahloV2 extends Application implements Kahlo_inicio_controller.CallBack_Configuracion, Kahlo_gui_controller.CallBackFinSesion {

    public static final String KEY_NODO_PREFS = "KAHLOV2";
    //private Stage primaryStage;

    //Objetos para el manejo de la interfaz grafica
    private FXMLLoader loader_inicio;
    private FXMLLoader loader_consola;

    private Parent inicio;
    private Parent consola;

    //Escenas
    private Scene escena_inicio;
    private Scene escena_consola;

    //control de cerrado de apliacion
    private boolean puedeSalir = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        KahloV2_Static.primaryStage = primaryStage;

        loader_inicio = new FXMLLoader(getClass().getResource("vista/kahlo_inicio.fxml"));
        loader_consola = new FXMLLoader(getClass().getResource("vista/kahlo_gui.fxml"));

        inicio = loader_inicio.load();
        consola = loader_consola.load();
        Kahlo_inicio_controller.setCallBack_configuracion(this);

        escena_inicio = new Scene(inicio, 930, 491);
        escena_consola = new Scene(consola);
        puedeSalir = true;

        primaryStage.setTitle("Kahlo V2");
        primaryStage.setScene(escena_inicio);
        primaryStage.getIcons().add(new Image("file:recursos/KahloIcono.png"));

        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(puedeSalir)
                    Platform.exit();
                else{
                    event.consume();
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Para cerrar la consola debes terminar la sesión");
                    a.setTitle("Consola");
                    a.show();
                }
            }
        });
        primaryStage.show();
    }

    public static void main(String [] args){
        System.setProperty("java.net.useSystemProxies", "true");
        launch(args);
    }

    @Override
    public void onGetConfiguracion(Configuracion configuracion, ConfiguracionTelemetria configuracionTelemetria
            , ConfiguracionPuertoSerial configuracionPuertoSerial){

        KahloV2_Static.primaryStage.setScene(escena_consola);
        puedeSalir = false;
        KahloV2_Static.primaryStage.setMaximized(true);
        Kahlo_gui_controller controller_consola = loader_consola.getController();
        controller_consola.setCallBackFinSesion(this);
        try {
            controller_consola.setConfiguracion(configuracion, configuracionTelemetria, configuracionPuertoSerial);
        } catch (PortInUseException e) {e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}

    }

    @Override
    public void onTerminaSesion() {
        try {
            start(KahloV2_Static.primaryStage);
        } catch (Exception e) {e.printStackTrace();}
    }
}
