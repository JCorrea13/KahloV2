package vista;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

/**
 * Esta clase es el controlador del la lata 3D
 * que se muestra en la interfaz de usuario
 * @author Jose Correa
 * @version 1.0.0
 */
public class Panel_lata3D_controller {

    @FXML
    private Cylinder lata;

    private double roll = 0;
    private double yaw = 0;
    private double pitch = 0;

    private Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
    private Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    private Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    @FXML
    private void initialize(){
        rxBox.setAngle(0);
        ryBox.setAngle(0);
        rzBox.setAngle(0);
        lata.getTransforms().addAll(rxBox, ryBox, rzBox);
    }

    /**
     * Este metodo rota la lata a las condiciones
     * actuales de los ejes
     */
     private void actualizaRotacion(double alf, double bet, double gam){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    rxBox.setAngle(-alf);
                    ryBox.setAngle(bet);
                    rzBox.setAngle(gam);
                }
            });
    }


    public void rota(double roll, double yaw, double pitch){
        this.roll = roll;
        this.yaw = yaw;
        this.pitch = pitch;
        actualizaRotacion(yaw, pitch, roll);
    }
}
