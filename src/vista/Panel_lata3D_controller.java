package vista;

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

    /*private double roll = 12.37722;
    private double yaw = 3.741706;
    private double pitch = -67.89284;
    private double roll = -20.599962;
    private double yaw = 6.4251714;
    private double pitch = -16.632632;*/
    private double roll = 0;
    private double yaw = 0;
    private double pitch = 0;

    @FXML
    private void initialize(){
        actualizaRotacion(roll,pitch,yaw);
    }

    /**
     * Este metodo rota la lata a las condiciones
     * actuales de los ejes
     */
     private void actualizaRotacion(double alf, double bet, double gam){
            double A11=Math.cos(alf)*Math.cos(gam);
            double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
            double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
            double A21=-Math.cos(gam)*Math.sin(alf);
            double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
            double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
            double A31=Math.sin(gam);
            double A32=-Math.cos(gam)*Math.sin(bet);
            double A33=Math.cos(bet)*Math.cos(gam);

            double d = Math.acos((A11+A22+A33-1d)/2d);
            if(d!=0d){
                double den=2d*Math.sin(d);
                Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
                lata.setRotationAxis(p);
                lata.setRotate(Math.toDegrees(d));
            }
    }


    public void rota(double roll, double yaw, double pitch){
        this.roll = roll;
        this.yaw = yaw;
        this.pitch = pitch;

        actualizaRotacion(yaw, pitch, roll);
    }
}
