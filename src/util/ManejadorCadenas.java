package util;

/**
 * @author Jose Correa
 */
public class ManejadorCadenas {
    
    public static int buscaConcurrencia(String cadena, String cadena_a_buscar, int concurrencia){
        if(concurrencia == 1)
            return cadena.indexOf(cadena_a_buscar);
        
        if(buscaConcurrencia(cadena.substring(cadena.indexOf(cadena_a_buscar)+1), cadena_a_buscar, concurrencia-1) == -1)
            return -1;
        
        return (cadena.indexOf(cadena_a_buscar) + 1) + buscaConcurrencia(cadena.substring(cadena.indexOf(cadena_a_buscar)+1), cadena_a_buscar, concurrencia-1);
    }
}
