package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kahlo_configuraciones.ConfiguracionSensor;
import vista.TablaConfiguracionTelemetria;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        AnchorPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        TablaConfiguracionTelemetria tablaConfiguracionTelemetria = new TablaConfiguracionTelemetria();

        Button btn = new Button("imprime");

        tablaConfiguracionTelemetria.setEditable(true);
        root.getChildren().addAll(tablaConfiguracionTelemetria);
        tablaConfiguracionTelemetria.setPrefWidth(650);
        tablaConfiguracionTelemetria.setPrefHeight(100);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        ConfiguracionSensor cs = new ConfiguracionSensor("id");
        cs.setEtiqueta_sensor_property("Etiqueta Prueba");
        cs.setHabilitado(true);
        cs.setBytes_property(new int[]{1,2});
        cs.setValor_max_property(200);
        cs.setValor_min_property(0);
        cs.setGrafica(true);
        tablaConfiguracionTelemetria.getItems().addAll(cs);

        cs = new ConfiguracionSensor("id1");
        tablaConfiguracionTelemetria.getItems().addAll(cs);
        cs = new ConfiguracionSensor("id2");
        tablaConfiguracionTelemetria.getItems().addAll(cs);
        cs = new ConfiguracionSensor("id3");
        tablaConfiguracionTelemetria.getItems().addAll(cs);
        cs = new ConfiguracionSensor("id4");
        tablaConfiguracionTelemetria.getItems().addAll(cs);

        root.getChildren().addAll(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(((ConfiguracionSensor)(tablaConfiguracionTelemetria.getItems().get(0))).getEtiqueta_sensor_property());
                System.out.println(((ConfiguracionSensor)(tablaConfiguracionTelemetria.getItems().get(0))).getValor_min_property());
                System.out.println(((ConfiguracionSensor)(tablaConfiguracionTelemetria.getItems().get(0))).getValor_max_property());
            }
        });

    }


    public static void main(String[] args) throws Exception {


        byte num [] = new byte[] {0 , (byte)255};

        int n = 0;
        for (int i = 0; i < num.length; i++)
            n = (n<<8)| Byte.toUnsignedInt(num[i]);

        System.out.println(n);
        System.out.println(Byte.toUnsignedInt((byte)255));
        //launch(args);
    }
}
