package vista;

import gnu.io.CommPortIdentifier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import kahlo_mision.Configuracion;
import serialport.Puerto;
import serialport.PuertoSerial;
import java.io.IOException;
import java.util.Stack;

/**
 * Esta clase es el controlador de la interfaz
 * de inicio para la aplicacion de kahlo
 * @author Jose Correa
 * @version 1.0.0
 */
public class kahlo_inicio_controller {

    @FXML
    private ImageView imagen;
    @FXML
    private RadioButton rbtn_mision;
    @FXML
    private RadioButton rbtn_prueba;
    @FXML
    private Pane pnl_mision;
    @FXML
    private ComboBox cbo_puertos;
    @FXML
    private TextField txt_nombremision;
    @FXML
    private TextField txt_alturamaxima;
    @FXML
    private TextArea txt_descripcion;
    @FXML
    private Button btn_aceptar;

    private static CallBack_Configuracion callBack_configuracion;
    private boolean mision;

    final ToggleGroup tipos_ejecucion = new ToggleGroup();
    @FXML
    private void initialize() throws IOException {
        imagen.setImage(new Image("file:recursos/KahloIcono.png"));
        imagen.setVisible(true);

        txt_alturamaxima.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txt_alturamaxima.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        rbtn_mision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(rbtn_mision.isSelected())
                cambiaTipo(true);
            }
        });
        rbtn_prueba.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(rbtn_prueba.isSelected())
                    cambiaTipo(false);
            }
        });
        cargaPuertos();
    }

    @FXML
    private void cambiaTipo(boolean mision){
        this.mision = mision;
        if(mision){
            rbtn_prueba.setSelected(false);
            pnl_mision.setDisable(false);
        }else{
            rbtn_mision.setSelected(false);
            pnl_mision.setDisable(true);
        }
    }

    @FXML
    private void cargaPuertos(){
        Stack<Puerto> puertos = PuertoSerial.getListaPuertos();
        ObservableList p = FXCollections.observableArrayList();
        for (Puerto puerto: puertos) {
            p.add(puerto);
        }
        cbo_puertos.setItems(p);
    }

    @FXML
    private void onTerminarConfiguracion(){
        if(validaCamposConfiguracion())
            if(callBack_configuracion != null)
                callBack_configuracion.onGetConfiguracion(getConfiguracion());
    }

    public interface CallBack_Configuracion {
        void onGetConfiguracion(Configuracion configuracion);
    }

    public static void setCallBack_configuracion(CallBack_Configuracion callBack_configuracion){
        kahlo_inicio_controller.callBack_configuracion = callBack_configuracion;
    }

    private Configuracion getConfiguracion(){
        if(mision)
            return new Configuracion(
                    ((Puerto) cbo_puertos.getSelectionModel().getSelectedItem()).getCommPortIdentifier(),
                    txt_nombremision.getText(),
                    txt_descripcion.getText(),
                    Double.valueOf(txt_alturamaxima.getText())
            );

        return null;
    }
    private boolean validaCamposConfiguracion() {
        if(mision){ //validamos el caso de que sea mision
            if (txt_nombremision.getText().isEmpty())
                return false;
            if (txt_alturamaxima.getText().isEmpty())
                return false;
            if (txt_descripcion.getText().isEmpty())
                return false;
        }

        //se hacen la validaciones generales
        if(cbo_puertos.getSelectionModel().getSelectedItem() == null)
            return false;

        return true;
    }
}
