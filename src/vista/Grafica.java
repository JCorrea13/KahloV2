package vista;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import sun.java2d.pipe.AATextRenderer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Esta clase modela las graficas de los datos
 * de senso, se grafica tiempo-altura y tiempo-valor
 * @author Jose Correa
 * @version 1.0.0
 */
public class Grafica extends AnchorPane {

    private AreaChart grafica;
    private final NumberAxis ejeX;
    private final NumberAxis ejeY;

    private XYChart.Series serieValor;
    private XYChart.Series serieAltura;

    private ConcurrentLinkedQueue<Dato> datos = new ConcurrentLinkedQueue<Dato>();
    private final int MAX_DATA_POINTS = 50;

    public  Grafica(String titulo, int valor_maximo, int valor_minimo, int altura_maxima){
        ejeY = new NumberAxis(valor_minimo
                ,(altura_maxima < valor_maximo)? valor_maximo : altura_maxima
                ,calcula_intervalo(valor_maximo,altura_maxima, valor_minimo));

        ejeX = new NumberAxis(valor_minimo
                ,MAX_DATA_POINTS
                ,1);

        grafica = new AreaChart(ejeX, ejeY);
        grafica.setTitle(titulo);

        serieValor = new XYChart.Series();
        serieValor.setName(titulo);

        serieAltura = new XYChart.Series();
        serieAltura.setName("Altura");


        grafica.getData().addAll(serieValor, serieAltura);

        setTopAnchor(grafica, 0d);
        setLeftAnchor(grafica, 0d);
        setRightAnchor(grafica, 0d);
        setBottomAnchor(grafica, 0d);
        getChildren().add(grafica);

        // Iniciamos la animacion de la grafica de tiempo real
        new Actulizador().start();
    }

    /**
     * Este metodo calcula el intervalo del el eje Y de la grafica
     * @return intervalo
     */
    private int calcula_intervalo(int valor_maximo, int altura_maxima, int valor_minimo){
        if(altura_maxima < valor_maximo)
            return ((valor_maximo - valor_minimo) > 15)? 15: (valor_maximo - valor_minimo)/15;
        return ((altura_maxima- valor_minimo) > 15)? 15: (altura_maxima- valor_minimo)/15;
    }

    private int tiempo = 0;
    private void agrega_punto(double valor, double altura){
        serieValor.getData().add(new XYChart.Data(tiempo, valor));
        serieAltura.getData().add(new XYChart.Data(tiempo, altura));
        tiempo ++;
    }

    /**
     * Este metodo sirve para agregar valores a la grafica
     * @param valor valor de sensor
     * @param altura altura
     */
    public void agrega_datos(double valor, double altura){
        datos.add(new Dato(valor,altura));
    }

    /**
     * Esta clase es ejecutada por la el Thread de JavaFx
     * y permite la simnualcion de Streaming en tiempo real
     * en la grafica
     */
    private class Actulizador extends AnimationTimer{
        private long lastUpdate = 0 ;

        @Override
        public void handle(long now) {
            if (now - lastUpdate >= 200_000_000) {
                run();
                lastUpdate = now;
            }
        }

        private Dato dato;
        public void run() {

            if (!datos.isEmpty()) {
                dato = datos.remove();
                agrega_punto(dato.getValor(), dato.getAltura());
            }else{
                agrega_punto(0,0);
            }
            // remove points to keep us at no more than MAX_DATA_POINTS
            if (serieValor.getData().size() > MAX_DATA_POINTS) {
                serieValor.getData().remove(0, serieValor.getData().size() - MAX_DATA_POINTS);
                serieAltura.getData().remove(0, serieAltura.getData().size() - MAX_DATA_POINTS);
            }
            // update
            ejeX.setLowerBound(tiempo-MAX_DATA_POINTS);
            ejeX.setUpperBound(tiempo-1);
        }
    }

    /**
     * Esta clase modela al conjunto de datos que
     * se grafican en este tipo de grafica es decir,
     * Valor, Altura
     */
    private class Dato{
        private double valor;
        private double altura;

        Dato(double valor, double altura){
            this.valor = valor;
            this.altura = altura;
        }

        public double getValor() {
            return valor;
        }

        public double getAltura() {
            return altura;
        }
    }
}
