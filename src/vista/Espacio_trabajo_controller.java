package vista;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import util.ManejadorPreferencias;

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

    @FXML
    private void initialize(){
        txt_ruta.setText(ManejadorPreferencias.getPreferencia(this.getClass().getName(),CLAVE_ESPACIO_TRABAJO, ""));
    }

    @FXML
    private void onChooserClick(){
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona ruta");
        File f = fileChooser.showDialog(null);
        if(f != null) txt_ruta.setText(f.getAbsolutePath().toString());
    }

    public void guardaPreferencia(){
        ManejadorPreferencias.setPreferencia(this.getClass().getName(),CLAVE_ESPACIO_TRABAJO, txt_ruta.getText().trim());
    }
}
