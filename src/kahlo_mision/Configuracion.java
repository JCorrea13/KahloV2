package kahlo_mision;

import gnu.io.CommPortIdentifier;

/**
 * Esta clase modela la configuracion de
 * una mision en el proyecto kahlo
 */
public class Configuracion {

    private CommPortIdentifier commPortIdentifier;
    private String nombre;
    private String descripcion;
    private double altura_maxima_esperada;

    public Configuracion(CommPortIdentifier commPortIdentifier, String nombre, String descripcion, double altura_maxima_esperada) {
        this.commPortIdentifier = commPortIdentifier;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.altura_maxima_esperada = altura_maxima_esperada;
    }

    public CommPortIdentifier getCommPortIdentifier() {
        return commPortIdentifier;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getAltura_maxima_esperada() {
        return altura_maxima_esperada;
    }
}
