import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Esta clase es la clase principal de la aplicacion
 * vista.KahloV2
 * @author Jose Correa
 * @version 1.0.0
 */
public class KahloV2 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("vista/kahlo_gui.fxml"));

        primaryStage.setTitle("Kahlo V2");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.getIcons().add(new Image("file:recursos/KahloIcono.png"));
        primaryStage.show();
    }

    public static void main(String [] args){
        launch(args);
    }
}
