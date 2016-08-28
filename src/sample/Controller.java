package sample;

import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.chart.Axis;
import javafx.scene.control.Button;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class Controller {

    @FXML
    Cylinder c;

    @FXML
    public void initialize(){

        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(45);
        ryBox.setAngle(0);
        rzBox.setAngle(90);
        c.getTransforms().addAll(rxBox, ryBox, rzBox);
    }

}
