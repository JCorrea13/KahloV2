package vista;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import kahlo_configuraciones.ConfiguracionSensor;

/**
 * Esta clase es el oontrolador del panel
 * de datos
 *
 * @author  Jose Correa
 * @@version 1.0.0
 */
public class Panel_datos_controller {

    @FXML
    private Label lbl_etiqueta;
    @FXML
    private TextField txt_valor;

    private CallBackClick callBackClick;
    private ConfiguracionSensor configuracion;

    public void setConfiguracion(ConfiguracionSensor configuracion) {
        this.configuracion = configuracion;
        lbl_etiqueta.setText((configuracion.getEtiqueta_sensor_property()!= null?
                                configuracion.getEtiqueta_sensor_property():
                                configuracion.getId_sensor_property()) + ":");
    }

    @FXML
    private void onClickEtiqueta(){
        if(callBackClick != null) callBackClick.onClickEtiqueta(configuracion.getId_sensor_property());
    }

    public void setValor(String valor){
        txt_valor.setText(valor);
    }

    public void setCallBackClick(CallBackClick callBackClick){
        this.callBackClick = callBackClick;

        lbl_etiqueta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onClickEtiqueta();
                System.out.println("se detecta el click");
            }
        });
    }

    public interface CallBackClick{
        void onClickEtiqueta(String grafica);
    }
}
