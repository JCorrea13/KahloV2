package util;

/**
 * @author Jose Correa
 */
public class ManejadorCadenas {
    
    public static int getIndexAt(String cadena, String cadena_a_buscar, int concurrencia){
        if(concurrencia == 1)
            return cadena.indexOf(cadena_a_buscar);
        
        if(getIndexAt(cadena.substring(cadena.indexOf(cadena_a_buscar)+1), cadena_a_buscar, concurrencia-1) == -1)
            return -1;
        
        return (cadena.indexOf(cadena_a_buscar) + 1) + getIndexAt(cadena.substring(cadena.indexOf(cadena_a_buscar)+1), cadena_a_buscar, concurrencia-1);
    }
}
