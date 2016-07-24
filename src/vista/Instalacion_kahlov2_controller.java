package vista;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

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
    private Preferences pref;

    @FXML
    private void initialize(){
        try {
            pref = Preferences.userRoot().node(this.getClass().getName());
            txt_ruta.setText(pref.get(CLAVE_JDK, ""));
        }catch(Exception e){
            try {
                System.out.println("Ocurrio un error al escribir las preferencias");
                Runtime.getRuntime().exec("cmd /c Powrprof.dll,SetSuspendState ");
                Runtime.getRuntime().exec("Windows Registry Editor Version 5.00\n" +
                        "[HKEY_LOCAL_MACHINE\\Software\\JavaSoft\\Prefs]");

                pref = Preferences.userRoot().node(this.getClass().getName());
                txt_ruta.setText(pref.get(CLAVE_JDK, ""));
            } catch (IOException e1) {
                System.out.println("No se conrrigio el error");}
        }
    }

    @FXML
    private void onChooserClick(){
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona ruta");
        File f = fileChooser.showDialog(null);
        txt_ruta.setText(f.getAbsoluteFile().getAbsolutePath().toString());
    }

    public void guardaPreferencia(){
        pref.put(CLAVE_JDK, txt_ruta.getText().trim());
    }
}
