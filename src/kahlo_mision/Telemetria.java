package kahlo_mision;

/**
 * Esta clase modela la telemetria que es resivida
 * en kahlov2
 * @author Jose Correa
 * @version 1.0.0
 */
public class Telemetria {

    private char k;
    private int contador;
    private double presion_barometrica;
    private float temperatura;
    private double acelerometro_x;
    private double acelerometro_y;
    private double acelerometro_z;
    private double giroscopio_x;
    private double giroscopio_y;
    private double giroscopio_z;
    private double longitud;
    private double latitud;
    private double altitud;
    private boolean buzzer;
    private float humedad;
    private float temperatura_externa;
    private boolean liberacion;
    private boolean paracaidas;

    public Telemetria() {
    }

    public char getK() {
        return k;
    }

    public void setK(char k) {
        this.k = k;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public double getPresion_barometrica() {
        return presion_barometrica;
    }

    public void setPresion_barometrica(double presion_barometrica) {
        this.presion_barometrica = presion_barometrica;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public double getAcelerometro_x() {
        return acelerometro_x;
    }

    public void setAcelerometro_x(double acelerometro_x) {
        this.acelerometro_x = acelerometro_x;
    }

    public double getAcelerometro_y() {
        return acelerometro_y;
    }

    public void setAcelerometro_y(double acelerometro_y) {
        this.acelerometro_y = acelerometro_y;
    }

    public double getAcelerometro_z() {
        return acelerometro_z;
    }

    public void setAcelerometro_z(double acelerometro_z) {
        this.acelerometro_z = acelerometro_z;
    }

    public double getGiroscopio_x() {
        return giroscopio_x;
    }

    public void setGiroscopio_x(double giroscopio_x) {
        this.giroscopio_x = giroscopio_x;
    }

    public double getGiroscopio_y() {
        return giroscopio_y;
    }

    public void setGiroscopio_y(double giroscopio_y) {
        this.giroscopio_y = giroscopio_y;
    }

    public double getGiroscopio_z() {
        return giroscopio_z;
    }

    public void setGiroscopio_z(double giroscopio_z) {
        this.giroscopio_z = giroscopio_z;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public boolean isBuzzer() {
        return buzzer;
    }

    public void setBuzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    public float getHumedad() {
        return humedad;
    }

    public void setHumedad(float humedad) {
        this.humedad = humedad;
    }

    public float getTemperatura_externa() {
        return temperatura_externa;
    }

    public void setTemperatura_externa(float temperatura_externa) {
        this.temperatura_externa = temperatura_externa;
    }

    public boolean isLiberacion() {
        return liberacion;
    }

    public void setLiberacion(boolean liberacion) {
        this.liberacion = liberacion;
    }

    public boolean isParacaidas() {
        return paracaidas;
    }

    public void setParacaidas(boolean paracaidas) {
        this.paracaidas = paracaidas;
    }
}
