import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import kahlo_mision.Configuracion;
import vista.kahlo_inicio_controller;
import vista.kahlo_inicio_controller.*;

import java.io.IOException;

/**
 * Esta clase es la clase principal de la aplicacion
 * vista.KahloV2
 * @author Jose Correa
 * @version 1.0.0
 */
public class KahloV2 extends Application implements CallBack_Configuracion {

    private Stage primaryStage;
    Parent inicio;
    Parent consola;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        inicio = FXMLLoader.load(getClass().getResource("vista/kahlo_inicio.fxml"));
        consola = FXMLLoader.load(getClass().getResource("vista/kahlo_gui.fxml"));
        kahlo_inicio_controller.setCallBack_configuracion(this);

        primaryStage.setTitle("Kahlo V2");
        primaryStage.setScene(new Scene(inicio, 930, 468));
        primaryStage.getIcons().add(new Image("file:recursos/KahloIcono.png"));
        primaryStage.show();

    }

    public static void main(String [] args){
        System.setProperty("java.net.useSystemProxies", "true");
        launch(args);
    }

    @Override
    public void onGetConfiguracion(Configuracion configuracion) {
        primaryStage.setScene(new Scene(consola));
        primaryStage.setMaximized(true);
    }
}
