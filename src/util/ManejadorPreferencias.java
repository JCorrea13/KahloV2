package util;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Esta clase sirve para el manejo de preferencias
 * tiene operaciones de lectura y escritura de preferencias
 *
 * @author Jose Correa
 * @version 1.0.0
 */
public class ManejadorPreferencias {

    /**
     * Este metodo  escribe una preferencia en el nodo que se
     * pasa como parametro, con la clave y valor que se pasan como
     * parametros.
     *
     * @param nodo  El nodo en que se quiere escribir la preferencia
     * @param clave La clave con la que se quieres escribir la preferencia
     * @param valor valor que se desea escribir en la preferencia
     * @return True si la preferencia se escribe con exito False en otro caso
     */
    public static boolean setPreferencia(String nodo, String clave, String valor){

        Preferences pref = getPreferences(nodo);
        if(pref == null) return false;

        pref.put(clave,valor);

        return true;
    }

    /**
     * Este metodo recupera la preferencia con la clave que pasa como parametro
     * del nodo que se pasa como parametro
     * @param nodo Nodo del cual se quiere leer la preeferencia
     * @param clave Clave que se desea leer
     * @param defecto   Valor por defecto si no se condigue la lectura
     * @return Valor de la preferencia en caso de lectura completada, valor defecto en otro caso
     */
    public static String getPreferencia(String nodo, String clave, String defecto){
        Preferences pref;

        pref = Preferences.userRoot().node(nodo);
        if(pref == null) return null;

        return pref.get(clave, defecto);
    }

    /**
     * Este metodo regresa un objeto Preferences con
     * el nodo que se pasa como parametro.
     *
     * @param nodo Nodo del cual se quiere recuperar la preferencia
     * @return Preferences si obtiene el objeto con exito Null en otro caso
     */
    private static Preferences getPreferences(String nodo){
        Preferences pref = null;

        try {
            pref = Preferences.userRoot().node(nodo);
        }catch(Exception e){
            try {
                Runtime.getRuntime().exec("cmd /c Powrprof.dll,SetSuspendState ");
                Runtime.getRuntime().exec("Windows Registry Editor Version 5.00\n" +
                        "[HKEY_LOCAL_MACHINE\\Software\\JavaSoft\\Prefs]");

                pref = Preferences.userRoot().node(nodo);
            } catch (IOException e1) {return null;}
        }

        return  pref;
    }
}
