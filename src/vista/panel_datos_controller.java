package vista;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Esta clase es el controlador del panel
 * de datos en la interfaz grafica de usuario
 * @author Jose Correa
 * @version 1.0.0
 */

public class panel_datos_controller{

    @FXML
    private TextField txt_paquetesperdidos;
    int paquetes_perdidos;

    @FXML
    private TextField txt_presionbarometrica;
    double presion_barometrica;
    @FXML
    private Label lbl_presionbarometrica;

    @FXML
    private TextField txt_temperatura;
    double temperatura;
    @FXML
    private Label lbl_temperatura;

    @FXML
    private TextField txt_latitud;
    double latitud;

    @FXML
    private TextField txt_longitud;
    double longitud;

    @FXML
    private TextField txt_altitud;
    double altitud;

    @FXML
    private TextField txt_altura;
    double altura;

    @FXML
    private TextField txt_humedad;
    double humedad;
    @FXML
    private Label lbl_humedad;

    @FXML
    private TextField txt_tempexterna;
    double temperatura_externa;
    @FXML
    private Label lbl_temperaturaexterna;

    @FXML
    private TextField txt_estadoliberacion;
    boolean estado_liberacion = false;

    @FXML
    private TextField txt_estadoparacaidas;
    boolean estado_paracaidas = false;

    @FXML
    private void initialize(){
        actualizaDatos();

        lbl_presionbarometrica.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(callBack_clickLabel != null)
                    callBack_clickLabel.action_clickLabel_datos(kahlo_gui_controller.Graficas_e.PRESION_BAROMETRICA);
            }
        });
        lbl_temperatura.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(callBack_clickLabel != null)
                    callBack_clickLabel.action_clickLabel_datos(kahlo_gui_controller.Graficas_e.TEMPERATURA);
            }
        });
        lbl_humedad.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(callBack_clickLabel != null)
                    callBack_clickLabel.action_clickLabel_datos(kahlo_gui_controller.Graficas_e.HUMEDAD);
            }
        });
        lbl_temperaturaexterna.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(callBack_clickLabel != null)
                    callBack_clickLabel.action_clickLabel_datos(kahlo_gui_controller.Graficas_e.TEMPERATURA_EXTERNA);
            }
        });
    }

    private enum Estados_Liberacion{
        NO_LIBERADO,
        LIBERADO
    }

    private enum Estados_Paracaidas{
        GUARDADO,
        EXTENDIDO
    }

    /**
     * Este metodo acutaliza los componentes de la interfaz grafica
     * con los valores actuales en las variables que modelan el
     * estado de los sensores
     */
    private void actualizaDatos(){
        txt_paquetesperdidos.setText(String.valueOf(paquetes_perdidos));
        txt_presionbarometrica.setText(String.valueOf(presion_barometrica));
        txt_temperatura.setText(String.valueOf(temperatura));
        txt_latitud.setText(String.valueOf(latitud));
        txt_longitud.setText(String.valueOf(longitud));
        txt_altitud.setText(String.valueOf(altitud));
        txt_altura.setText(String.valueOf(altura));
        txt_humedad.setText(String.valueOf(humedad));
        txt_tempexterna.setText(String.valueOf(temperatura_externa));
        txt_estadoliberacion.setText(estado_liberacion?
                                        Estados_Liberacion.LIBERADO.toString():
                                        Estados_Liberacion.NO_LIBERADO.toString());

        txt_estadoparacaidas.setText(estado_paracaidas?
                                        Estados_Paracaidas.EXTENDIDO.toString():
                                        Estados_Paracaidas.GUARDADO.toString());
    }

    private static CallBack_ClickLabel callBack_clickLabel;
    public static void setCallBack(CallBack_ClickLabel callBack){
        callBack_clickLabel = callBack;
    }

    public interface CallBack_ClickLabel{
        void action_clickLabel_datos(kahlo_gui_controller.Graficas_e grafica);
    }

}
