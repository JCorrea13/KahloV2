package kahlo_mision;

import gnu.io.CommPortIdentifier;

/**
 * Esta clase modela la configuracion de
 * una mision en el proyecto kahlo
 */
public class Configuracion {

    private Tipo tipo;
    private CommPortIdentifier commPortIdentifier;
    private String nombre;
    private String descripcion;
    private int altura_maxima_esperada;
    private String ruta_archivo;
    private String inicioMision;

    public Configuracion(Tipo tipo) {
        this.tipo = tipo;
    }

    public Tipo getTipo() { return tipo; }

    public CommPortIdentifier getCommPortIdentifier() {
        return commPortIdentifier;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getAltura_maxima_esperada() {
        return altura_maxima_esperada;
    }


    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setCommPortIdentifier(CommPortIdentifier commPortIdentifier) {
        this.commPortIdentifier = commPortIdentifier;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAltura_maxima_esperada(int altura_maxima_esperada) {
        this.altura_maxima_esperada = altura_maxima_esperada;
    }

    public String getRutaArchivo() {
        return ruta_archivo;
    }

    public void setRutaArchivo(String ruta_archivo) {
        this.ruta_archivo = ruta_archivo;
    }

    public String getInicioMision() {
        return inicioMision;
    }

    public void setInicioMision(String inicioMision) {
        this.inicioMision = inicioMision;
    }

    public enum Tipo{
        MISION,
        PRUEBA,
        ARCHIVO
    }
}
