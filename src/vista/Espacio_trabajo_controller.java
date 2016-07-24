package vista;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;


/**
 * Esta clase es el controlador del
 * dialogo de configuracion de el espacion de trabajo
 * @author Jose Correa
 * @version 1.0.0
 */
public class Espacio_trabajo_controller {
    public static final String CLAVE_ESPACIO_TRABAJO = "K_ES";


    @FXML
    private TextField txt_ruta;
    private Preferences pref;

    @FXML
    private void initialize(){
        try {
            pref = Preferences.userRoot().node(this.getClass().getName());
            txt_ruta.setText(pref.get(CLAVE_ESPACIO_TRABAJO, ""));
        }catch(Exception e){
            try {
                Runtime.getRuntime().exec("cmd /c Powrprof.dll,SetSuspendState ");
                Runtime.getRuntime().exec("Windows Registry Editor Version 5.00\n" +
                        "[HKEY_LOCAL_MACHINE\\Software\\JavaSoft\\Prefs]");

                pref = Preferences.userRoot().node(this.getClass().getName());
                txt_ruta.setText(pref.get(CLAVE_ESPACIO_TRABAJO, ""));
            } catch (IOException e1) {e1.printStackTrace();}
        }
    }

    @FXML
    private void onChooserClick(){
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona ruta");
        File f = fileChooser.showDialog(null);
        txt_ruta.setText(f.getAbsolutePath().toString());
    }

    public void guardaPreferencia(){
        pref.put(CLAVE_ESPACIO_TRABAJO, txt_ruta.getText().trim());
    }
}
