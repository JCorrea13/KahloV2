package kahlo_configuraciones;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Esta clase modela la configuracion de una
 * cadena de telelmetria y es serializable
 *
 * @author Jose Correa
 * @version 1.0.0
 */
public class ConfiguracionTelemetria implements Serializable{

    private ArrayList<ConfiguracionSensor> sensores;
    private int tamanio_telemetria;
    private int periodo;

    public ConfiguracionTelemetria(ArrayList<ConfiguracionSensor> configuraciones, int tamanio_telemetria, int periodo){
        this.tamanio_telemetria = tamanio_telemetria;
        this.sensores = configuraciones;
        this.periodo = periodo;
    }

    public ArrayList<ConfiguracionSensor> getSensores() {
        return sensores;
    }

    public void setSensores(ArrayList<ConfiguracionSensor> sensores) {
        this.sensores = sensores;
    }

    public int getTamanio_telemetria() {
        return tamanio_telemetria;
    }

    public void setTamanio_telemetria(int tamanio_telemetria) {
        this.tamanio_telemetria = tamanio_telemetria;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public boolean contiene(String id) {
        for(ConfiguracionSensor c: sensores)
            if(c.getId_sensor_property().equals(id))
                return true;

        return false;
    }

    /**
     * Este metodo regresa la configuracion con el id que
     * pasa como parametro
     * @param id Id de la configuracion que se desea obtener
     * @return ConfiguracionSensor si la configuracion existe y esta habilitada Null en otro caso
     */
    public ConfiguracionSensor getConfiguracion(String id){
        for (ConfiguracionSensor c: sensores)
            return (c.isHabilitado())?c : null;

        return null;
    }

    public void init(){
        for (ConfiguracionSensor c: sensores) {
            c.initConfiguracionSensor();
        }
    }

    public ArrayList<ConfiguracionSensor> getSensoresHabilitados() {
        ArrayList a = new ArrayList();

        for(ConfiguracionSensor c: getSensores())
            if(c.isHabilitado())
                a.add(c);

        return a;
    }
}
