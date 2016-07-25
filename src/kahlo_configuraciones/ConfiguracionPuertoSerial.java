package kahlo_configuraciones;

import java.io.Serializable;

/**
 * Esta clase modela la configuracion del puerto serial
 * (velocidad, bits de parada, bits de datos, paridad)
 *
 * Esta clase es serializable ya que la configuracion
 * se puede guardar.
 *
 * @author Jose Correa
 * @version 1.0.0
 */

public class ConfiguracionPuertoSerial implements Serializable {

    private int velocidad;
    private int bits_parada;
    private int bits_datos;
    private int paridad;

    public ConfiguracionPuertoSerial(int velocidad, int bits_parada, int bits_datos, int paridad) {
        this.velocidad = velocidad;
        this.bits_parada = bits_parada;
        this.bits_datos = bits_datos;
        this.paridad = paridad;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getBits_parada() {
        return bits_parada;
    }

    public void setBits_parada(int bits_parada) {
        this.bits_parada = bits_parada;
    }

    public int getBits_datos() {
        return bits_datos;
    }

    public void setBits_datos(int bits_datos) {
        this.bits_datos = bits_datos;
    }

    public int getParidad() {
        return paridad;
    }

    public void setParidad(int paridad) {
        this.paridad = paridad;
    }
}
