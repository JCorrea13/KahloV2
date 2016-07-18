package vista;

import javafx.fxml.FXML;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

/**
 * Esta clase es el controlador del la lata 3D
 * que se muestra en la interfaz de usuario
 * @author Jose Correa
 * @version 1.0.0
 */
public class panel_lata3D_controller{

    @FXML
    private Cylinder lata;

    private int ejeX = 0;
    private int ejeY = 0;
    private int ejeZ = 45;

    @FXML
    private void initialize(){
        actualizaRotacion();
    }

    /**
     * Este metodo rota la lata a las condiciones
     * actuales de los ejes
     */
    private void actualizaRotacion(){
        rotaX(ejeX);
        rotaY(ejeY);
        rotaZ(ejeZ);
    }

    /**
     * Rota eje X
     * @param r algulo que se rotara
     */
    private void rotaX(int r){
        lata.setRotationAxis(Rotate.X_AXIS);
        lata.setRotate(r);
    }

    /**
     * Rota eje Y
     * @param r algulo que se rotara
     */
    private void rotaY(int r){
        lata.setRotationAxis(Rotate.Y_AXIS);
        lata.setRotate(r);
    }

    /**
     * Rota eje Z
     * @param r algulo que se rotara
     */
    private void rotaZ(int r){
        lata.setRotationAxis(Rotate.Z_AXIS);
        lata.setRotate(r);
    }
}
