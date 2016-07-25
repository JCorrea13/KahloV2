package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import util.ManejadorArchivos;
import util.ManejadorPreferencias;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;


/**
 * Esta clase es el controlador del
 * dialogo de la ubicacion del jdk
 * @author Jose Correa
 * @version 1.0.0
 */
public class Instalacion_kahlov2_controller {

    public static final String CLAVE_JDK = "K_JDK";

    @FXML
    private TextField txt_ruta;

    @FXML
    private void initialize(){
        txt_ruta.setText(ManejadorPreferencias.getPreferencia(this.getClass().getName(),CLAVE_JDK, ""));
    }

    @FXML
    private void onChooserClick(){
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona ruta");
        File f = fileChooser.showDialog(null);
        if(f != null) txt_ruta.setText(f.getAbsoluteFile().getAbsolutePath().toString());
    }

    public void guardaPreferencia(){
        ManejadorPreferencias.setPreferencia(this.getClass().getName(),CLAVE_JDK, txt_ruta.getText().trim());
        instala();
    }

    /**
     * Este metodo instala las librerias en el directorio correspondiente
     * dependiendo el sistema operativo y el sistema de archivos
     *
     * @return True si se instala con exito False en otro caso
     */
    private boolean instala(){

        Alert alert;
        ManejadorArchivos ma = new ManejadorArchivos();
        String s = File.separator;

        File jar = new File("recursos" + s + "rxtxserial" + s + "RXTXcomm.jar");
        File win1 = new File("recursos" + s + "rxtxserial" + s + "Windows" + s + "i368-mingw32" + s + "rxtxParallel.dll");
        File win2 = new File("recursos" + s + "rxtxserial" + s + "Windows" + s + "i368-mingw32" + s + "rxtxSerial.dll");
        File linux64 = new File("recursos" + s + "rxtxserial" + s + "Linux" + s + "ia64-unkown-linux-gnu" + s + "librxtxSerial.so");
        File linuxx86 = new File("recursos" + s + "rxtxserial" + s + "Linux" + s + "x86_64-unknown-linux-gnu" + s + "librxtxSerial.so");

        String OS = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        if(OS.indexOf("win") >= 0){
            //copiamos los archivos para windows
            File j = new File(txt_ruta.getText().trim() + s + "jre" + s + "lib" + s + "ext");
            File w = new File(txt_ruta.getText().trim() + s + "jre" + s + "bin");
            if(j.exists() && w.exists()) {
                if (ma.copiar(jar.getAbsolutePath(), j + s + "RXTXcomm.jar")
                        && ma.copiar(win1.getAbsolutePath(), w + s + "rxtxParallel.dll")
                        && ma.copiar(win2.getAbsolutePath(), w + s + "rxtxSerial.dll")) {

                    //enviamos un mensaje al usuario de que la operacion tuvo exito
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("La instalación termino con exito");
                    alert.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error en la instalación");
                    alert.setContentText("Verifica la ruta especificada, es necesario ejecutar la aplicación como " +
                            "administrador para la instalación");
                    alert.show();
                    return false;
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error en la instalación");
                alert.setContentText("Verifica la ruta especificada, es necesario ejecutar la aplicación como " +
                        "administrador para la instalación");
                alert.show();
                return false;
            }

        }else if(OS.indexOf("nux") >= 0){
            //copiamos los archivos para linux
            File j = new File(txt_ruta.getText().trim() + s + "jre" + s + "lib" + s + "ext");
            File l = new File(txt_ruta.getText().trim() + s + "jre" + s + "lib");
            if(j.exists() && l.exists()) {
                if (ma.copiar(jar.getAbsolutePath(), j + s + "RXTXcomm.jar")
                        && ma.copiar((arch.equals("x86"))? linuxx86.getAbsolutePath() : linux64.getAbsolutePath()
                                                            , l + s + "librxtxSerial.so")) {

                    //enviamos un mensaje al usuario de que la operacion tuvo exito
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("La instalación termino con exito");
                    alert.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error en la instalación");
                    alert.setContentText("Verifica la ruta especificada, es necesario ejecutar la aplicación como " +
                            "root para la instalación");
                    alert.show();
                    return  false;
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error en la instalación");
                alert.setContentText("Verifica la ruta especificada, es necesario ejecutar la aplicación como " +
                        "root para la instalación");
                alert.show();
                return  false;
            }

        }else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error en la instalación");
            alert.setContentText("Tienes que instalar las librerias manualmente en este sistema operativo");
            alert.show();
            return false;
        }

        return true;
    }
}
