package kahlo_configuraciones;

import util.ManejadorArchivos;
import util.ManejadorCadenas;

import java.util.ArrayList;

/**
 * Esta clase funciona como manejador de configuraciones
 * se encarga de leer e interpretar los archivos de configuraciones
 * asi como de escribirlos.
 *
 * @author Jose Correa
 * @version 1.0.0
 */
public class ManejadorConfiguraciones {

    public static ArrayList<ConfiguracionSensor> getConguracoinSensores(String ruta) throws Exception{
        ManejadorArchivos ma = new ManejadorArchivos();
        ArrayList<String> archivo = ma.getContenidoArchivoToArray(ruta);
        ArrayList<ConfiguracionSensor> configuraciones = new ArrayList<>(archivo.size()-1);

        ConfiguracionSensor c;
        for(int i = 1;  i < archivo.size(); i++) {
            c = new ConfiguracionSensor(archivo.get(i).substring(1, ManejadorCadenas.getIndexAt(archivo.get(i), ",", 1)));
            c.setEtiqueta_sensor_property(archivo.get(i).substring(ManejadorCadenas.getIndexAt(archivo.get(i), ",", 1)+1, ManejadorCadenas.getIndexAt(archivo.get(i), ",", 2)));
            String habilitado = archivo.get(i).substring(ManejadorCadenas.getIndexAt(archivo.get(i), ",", 2)+1, ManejadorCadenas.getIndexAt(archivo.get(i), ",", 3)).trim();
            c.setHabilitado(habilitado.equals("1"));
            String bytes = archivo.get(i).substring(ManejadorCadenas.getIndexAt(archivo.get(i), ",", 3)+1, ManejadorCadenas.getIndexAt(archivo.get(i), ",", 4));
            c.setBytes_property(getBytesFromString(bytes));
            c.setValor_max_property(Integer.valueOf(archivo.get(i).substring(ManejadorCadenas.getIndexAt(archivo.get(i), ",", 4)+1, ManejadorCadenas.getIndexAt(archivo.get(i), ",", 5)).trim()));
            c.setValor_min_property(Integer.valueOf(archivo.get(i).substring(ManejadorCadenas.getIndexAt(archivo.get(i), ",", 5)+1, ManejadorCadenas.getIndexAt(archivo.get(i), ",", 6)).trim()));
            String grafica =  archivo.get(i).substring(ManejadorCadenas.getIndexAt(archivo.get(i), ",", 6)+1, ManejadorCadenas.getIndexAt(archivo.get(i), ">", 1)).trim();
            c.setGrafica(grafica.equals("1"));
            configuraciones.add(c);
        }

        return configuraciones;
    }

    private static int [] getBytesFromString(String bytes){
        String [] indices = bytes.split(" ");
        int [] result = new int[indices.length];

        for(int i = 0; i < result.length; i++)
            result[i] = Integer.valueOf(indices[i].trim());

        return result;
    }
}
