package kahlo_configuraciones;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import kahlo_mision.Configuracion;

import java.io.Serializable;

/**
 * Esta clase es el modelo de la configuracion de los
 * sensores desde la telemetria para la parte grafica
 *
 * Aqui se define los bytes que le corresponden a cada
 * senial que se graficara.
 *
 * Se define si la senial tendra grafica o solo se
 * mostrara su valor de manera fija.
 *
 * @author Jose Correa
 * @version 1.0.0
 */
public class ConfiguracionSensor implements Serializable{

    private String id_sensor;
    private String etiqueta_sensor;
    private int [] bytes;
    private int decimales;
    private int valor_max;
    private int valor_min;
    private boolean hab;
    private boolean graf;


    transient private SimpleStringProperty id_sensor_property = new SimpleStringProperty(id_sensor);
    transient private SimpleStringProperty etiqueta_sensor_property = new SimpleStringProperty(etiqueta_sensor);
    transient private SimpleStringProperty bytes_property = new SimpleStringProperty(arrayToString(bytes));
    transient private SimpleStringProperty decimales_property = new SimpleStringProperty(String.valueOf(decimales));
    transient private SimpleStringProperty valor_max_property = new SimpleStringProperty(String.valueOf(valor_max));
    transient private SimpleStringProperty valor_min_property = new SimpleStringProperty(String.valueOf(valor_min));
    transient private SimpleBooleanProperty habilitado = new SimpleBooleanProperty(hab);
    transient private SimpleBooleanProperty grafica = new SimpleBooleanProperty(graf);

    public void initConfiguracionSensor(){

        id_sensor_property = new SimpleStringProperty();
        etiqueta_sensor_property = new SimpleStringProperty();
        bytes_property = new SimpleStringProperty();
        decimales_property = new SimpleStringProperty();
        valor_max_property = new SimpleStringProperty();
        valor_min_property = new SimpleStringProperty();
        habilitado = new SimpleBooleanProperty();
        grafica = new SimpleBooleanProperty();

        setId_sensor_property(id_sensor);
        setEtiqueta_sensor_property(etiqueta_sensor);
        setBytes_property(bytes);
        setDecimales_property(decimales);
        setValor_max_property(valor_max);
        setValor_min_property(valor_min);
        setHabilitado(hab);
        setGrafica(graf);

        //Agregamos los listeners a la tabla para escuchar los cambios efecutados
        id_sensor_propertyProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setId_sensor_property(newValue);
            }
        });
        etiqueta_sensor_propertyProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setEtiqueta_sensor_property(newValue);
            }
        });
        habilitadoProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
                setHabilitado(isSelected);
            }
        });
        graficaProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
                setGrafica(isSelected);
            }
        });
        decimales_propertyProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    if(oldValue != null && !oldValue.isEmpty()) {
                        setDecimales_property(Integer.valueOf(oldValue));
                    }
                }else {
                    setDecimales_property(Integer.valueOf(newValue.replaceAll("[^\\d]", "")));
                }
            }
        });
        valor_max_propertyProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    if(oldValue != null && !oldValue.isEmpty()) {
                        setValor_max_property(Integer.valueOf(oldValue));
                    }
                }else {
                    setValor_max_property(Integer.valueOf(newValue.replaceAll("[^\\d]", "")));
                }
            }
        });
        valor_min_propertyProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    if(oldValue != null && !oldValue.isEmpty()) {
                        setValor_min_property(Integer.valueOf(oldValue));
                    }
                }else {
                    setValor_min_property(Integer.valueOf(newValue.replaceAll("[^\\d]", "")));
                }
            }
        });
        bytes_propertyProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.replace(" ", "")).matches("\\d*")){
                    if(oldValue != null && !oldValue.isEmpty()) {
                        setBytes_property(stringToArray(oldValue));
                    }
                }else{
                    try {
                        setBytes_property(stringToArray(newValue));
                    }catch(NumberFormatException e){
                        if(oldValue != null && !oldValue.isEmpty())
                            setBytes_property(stringToArray(oldValue));}
                }
            }
        });
    }

    public ConfiguracionSensor(String id_sensor_property) {
        this.id_sensor_property.set(id_sensor_property);
        this.id_sensor = id_sensor_property;

        initConfiguracionSensor();
    }

    public String getId_sensor_property() {
        return id_sensor_property.get();
    }

    public SimpleStringProperty id_sensor_propertyProperty() {
        return id_sensor_property;
    }

    public void setId_sensor_property(String id_sensor_property) {
        this.id_sensor_property.set(id_sensor_property);
        this.id_sensor = id_sensor_property;
    }

    public String getEtiqueta_sensor_property() {
        return etiqueta_sensor_property.get();
    }

    public SimpleStringProperty etiqueta_sensor_propertyProperty() {
        return etiqueta_sensor_property;
    }

    public void setEtiqueta_sensor_property(String etiqueta_sensor_property) {
        this.etiqueta_sensor_property.set(etiqueta_sensor_property);
        this.etiqueta_sensor = etiqueta_sensor_property;
    }

    public SimpleStringProperty bytes_propertyProperty() {
        return bytes_property;
    }

    public int getDecimales_property() {
        return Integer.valueOf(decimales_property.get());
    }

    public SimpleStringProperty decimales_propertyProperty() {
        return decimales_property;
    }

    public void setDecimales_property(int decimales_property) {
        this.decimales_property.set(String.valueOf(decimales_property));
        this.decimales = decimales_property;
    }

    public int getValor_max_property() {
        return Integer.valueOf(valor_max_property.get());
    }

    public SimpleStringProperty valor_max_propertyProperty() {
        return valor_max_property;
    }

    public void setValor_max_property(int valor_max_property) {
        this.valor_max_property.set(String.valueOf(valor_max_property));
        this.valor_max = valor_max_property;
    }

    public int getValor_min_property() {
        return Integer.valueOf(valor_min_property.get());
    }

    public SimpleStringProperty valor_min_propertyProperty() {
        return valor_min_property;
    }

    public void setValor_min_property(int valor_min_property) {
        this.valor_min_property.set(String.valueOf(valor_min_property));
        this.valor_min = valor_min_property;
    }

    public boolean isHabilitado() {
        return habilitado.get();
    }

    public SimpleBooleanProperty habilitadoProperty() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado.set(habilitado);
        this.hab = habilitado;
    }

    public boolean isGrafica() {
        return grafica.get();
    }

    public SimpleBooleanProperty graficaProperty() {
        return grafica;
    }

    public void setGrafica(boolean grafica) {
        this.grafica.set(grafica);
        this.graf = grafica;
    }

    public int [] getBytes_property() {
        return stringToArray(bytes_property.get());
    }

    public void setBytes_property(int [] bytes) {
        this.bytes_property.set(arrayToString(bytes));
        this.bytes = bytes;
    }

    private String arrayToString(int [] bytes){
        StringBuilder s = new StringBuilder();

        if(bytes != null)
            for(int i = 0; i < bytes.length; i++)
                s.append(bytes[i]).append(" ");

        return s.toString();
    }

    public static int [] stringToArray(String bytes){
        String [] s = bytes.split(" ");
        int [] b = new int[s.length];

        if(s.length == 1 && s[0].trim().isEmpty()) return new int[0];

        for(int i = 0; i < b.length; i++)
            b[i] = Integer.valueOf(s[i]);

        return b;
    }

    @Override
    public String toString() {
        return "ConfiguracionSensor{" +
                "id_sensor_property=" + id_sensor_property.get() +
                ", etiqueta_sensor_property=" + etiqueta_sensor_property.get() +
                ", bytes_property=" + bytes_property.get() +
                ", valor_max_property=" + valor_max_property.get() +
                ", valor_min_property=" + valor_min_property.get() +
                ", habilitado=" + habilitado.get() +
                ", grafica=" + grafica.get() +
                '}';
    }
}
