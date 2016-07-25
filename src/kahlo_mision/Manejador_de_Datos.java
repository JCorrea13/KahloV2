package kahlo_mision;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import kahlo_configuraciones.ConfiguracionPuertoSerial;
import kahlo_configuraciones.ConfiguracionSensor;
import kahlo_configuraciones.ConfiguracionTelemetria;
import serialport.PuertoSerial;
import util.ManejadorArchivos;
import util.ManejadorPreferencias;
import vista.Configuracion_telemetria_controller;
import vista.Espacio_trabajo_controller;

import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import static vista.Configuracion_telemetria_controller.CLAVE_ARCHIVO_TELEMETRIA;

/**
 * Esta clase se encarga de la lectura e interpretacion
 * de los datos que recibe la consola, es decir
 * es aqui donde se reciben los datos leeidos desde
 * el puerto serial y se interpretan con respecto al
 * protocolo.
 *
 *@author Jose Correa
 *@version 1.0.0
 */
public class Manejador_de_Datos {

    private PuertoSerial puertoSerial;
    private ByteArrayBuffer datos_bruto;
    private ByteArrayBuffer datos;
    private Configuracion configuracion;
    private byte [] archivo;
    private int indice_archivo = 0;
    private ConfiguracionTelemetria configuracionTelemetria;
    private ConfiguracionPuertoSerial configuracionPuertoSerial;
    private int cadenas_recibidas = 0;

    public Manejador_de_Datos(@NotNull Configuracion configuracion,@NotNull ConfiguracionTelemetria configuracionTelemetria
            , @NotNull ConfiguracionPuertoSerial configuracionPuertoSerial) throws PortInUseException, IOException {

        this.configuracion = configuracion;
        this.configuracionTelemetria = configuracionTelemetria;
        this.configuracionPuertoSerial = configuracionPuertoSerial;

        if(configuracion.getTipo() == Configuracion.Tipo.ARCHIVO)
            initArchivo(configuracion.getRutaArchivo());
        else
            initMision_Prueba(configuracion.getCommPortIdentifier());

    }

    private void initMision_Prueba(CommPortIdentifier commPortIdentifier) throws PortInUseException, IOException {
        //Configuracion default estandar uart (velocidad = 9600, bits datos = 8, bist parada = 2, paridad = NONE)
        puertoSerial = new PuertoSerial(commPortIdentifier
                , configuracionPuertoSerial.getVelocidad()
                , configuracionPuertoSerial.getBits_datos()
                , configuracionPuertoSerial.getBits_parada()
                , configuracionPuertoSerial.getParidad());

        datos_bruto = new ByteArrayBuffer();
        datos = new ByteArrayBuffer();
    }

    private void initArchivo(String ruta_archivo) throws IOException {
        //codigo para iniciar la lectura del archivo
        ManejadorArchivos ma = new ManejadorArchivos();
        archivo = ma.getContenidoArchivoBytes(ruta_archivo);
    }

    public HashMap<String, String> getTelemetria() throws IOException {
        if(configuracion.getTipo() == Configuracion.Tipo.ARCHIVO)
            return getTelemetriaArchivo();

        return getTelemetriaSerial();
    }

    private HashMap<String, String> getTelemetriaSerial() throws IOException {
        int datos_leidos;

        byte [] b = new byte[configuracionTelemetria.getTamanio_telemetria()];


        //primero leemos los datos desde el puerto seial
        datos_leidos = puertoSerial.read(b);
        System.out.println("Datos leidos: " + datos_leidos);

        //interpretamos los datos leeidos
        return interpretaDatos(b, datos_leidos);
    }

    private HashMap<String, String> getTelemetriaArchivo() throws IOException {
        int nuevo_indice = indice_archivo + configuracionTelemetria.getTamanio_telemetria();

        //validamos que seguimos leyendo dentro del tamanio del archivo
        if(nuevo_indice > archivo.length-1) {
            System.out.println("Fin del archivo");
            return null;
        }

        //Leemos los siguientes bytes del archivo
        byte [] b = Arrays.copyOfRange(archivo, indice_archivo, nuevo_indice);
        indice_archivo += configuracionTelemetria.getTamanio_telemetria();

        return interpretaDatos(b, configuracionTelemetria.getTamanio_telemetria());
    }

    private HashMap<String, String> interpretaDatos(byte [] b, int datos_leidos) throws IOException {
        HashMap<String,String> t = new HashMap<>();
        ConfiguracionSensor c_sensor;

        if(datos_leidos == configuracionTelemetria.getTamanio_telemetria()){

            //Leemos el byte de inicio de telemetria en caso de estar habilitado y validamos los bytes leidos
            c_sensor = configuracionTelemetria.getConfiguracion("K");
            if(c_sensor != null)
                if(!getValorString(c_sensor.getBytes_property(), b).equals(c_sensor.getEtiqueta_sensor_property())) {
                    if (configuracion.getTipo() == Configuracion.Tipo.ARCHIVO) indice_archivo++;
                    else puertoSerial.corrige();

                    return t;
                }

            //Leemos todos los sensores de la configuracion de sensores
            for(int i = 0; i < configuracionTelemetria.getSensoresHabilitados().size(); i++){
                c_sensor = configuracionTelemetria.getSensoresHabilitados().get(i);
                t.put(c_sensor.getId_sensor_property(), String.valueOf(getValDecimal(c_sensor.getBytes_property()
                        , b, c_sensor.getDecimales_property())));
            }

             if (configuracion.getTipo() == Configuracion.Tipo.MISION) datos.write(b);
             cadenas_recibidas ++;
        }
        if(configuracion.getTipo() == Configuracion.Tipo.MISION) datos_bruto.write(b);

        return t;
    }

    private String getValorString(int [] indice, byte [] bytes){
        StringBuilder s = new StringBuilder("");

        for(int i = 0; i < indice.length; i ++)
            s.append((char)bytes[indice[i]]);

        return s.toString();
    }

    private double getValDecimal(int [] indice, byte [] bytes, int decimales){
        return getValorLong(indice, bytes) * getCeros(decimales);
    }

    private long getValorLong(int [] indice, byte [] bytes){
        int valor = 0;

        for (int i = 0; i < indice.length; i++)
            valor = (valor<<8)|Byte.toUnsignedInt(bytes[indice[i]]);

        return valor;
    }

    private double getCeros(int decimales){
        StringBuilder s = new StringBuilder("0.");
        if(decimales < 1) return 1;

        for(int i = 1; i < decimales; i++)
            s.append("0");

        s.append("1");
        return Double.valueOf(s.toString());
    }

    public byte [] getDatosBrutoMision(){
        return datos_bruto.getRawData();
    }

    public byte [] getDatosMision(){
        return datos.getRawData();
    }

    /**
     * Este metdo guarda la mision de la
     * sesion actual cuando es invocado
    */
    public void guardaMision() throws IOException {
        ManejadorArchivos ma = new ManejadorArchivos();
        Preferences pref = Preferences.userRoot().node(Espacio_trabajo_controller.class.getName());
        String espacio_trabajo = pref.get(Espacio_trabajo_controller.CLAVE_ESPACIO_TRABAJO, null);
        espacio_trabajo = (espacio_trabajo != null )? espacio_trabajo.substring(0, espacio_trabajo.length()):null;

        //Creamos el archivo de descripcion de la mision
        ArrayList ka = new ArrayList(6);
        ka.add("-Kahlo_V2-");
        ka.add(configuracion.getNombre());
        ka.add(String.valueOf(configuracion.getAltura_maxima_esperada()));
        ka.add(configuracion.getInicioMision());
        ka.add(configuracion.getDescripcion());
        ka.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()).toString());

        String  dir = getDir(espacio_trabajo + File.separator + configuracion.getNombre(), 0) + File.separator;
        ma.agregaContenidoArchivoInArray(getDir(dir + configuracion.getNombre() + ".ka",0), ka);

        //guardamos los datos brutos
        ma.agregaContenidoArchivoByte(getDir(dir + configuracion.getNombre() + ".kab",0), datos_bruto.getRawData());

        //guardamos los datos
        ma.agregaContenidoArchivoByte(getDir(dir + configuracion.getNombre() + ".kar",0), datos.getRawData());

        //copiamos el archivo de configuracion de telemetria si es que existe
        File f = new File(ManejadorPreferencias.getPreferencia(Configuracion_telemetria_controller.class.getName()
                                                                ,CLAVE_ARCHIVO_TELEMETRIA, ""));
        if(f.exists())
            new ManejadorArchivos().copiar(f.getAbsolutePath(), getDir(dir + configuracion.getNombre() + ".kT",0));
    }

    /**
     * Este metodo cierra el puerto serial
     * si esta abierto
     */
    public void cierraPuertoSerial(){
        if(puertoSerial != null)
            puertoSerial.cerrarPuerto();
    }

    private String getDir(String ruta, int cont){
        if((cont == 0)?!(new File(ruta).exists()):!(new File(ruta + "(" + cont + ")").exists()))
            return (cont == 0)? ruta: ruta + "(" + cont + ")";

        return getDir(ruta, cont+1);
    }

    public int getCadenas_recibidas(){
        return cadenas_recibidas;
    }
}
