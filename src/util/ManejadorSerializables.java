package util;

import java.io.*;

/**
 * Este clase sirve como manejador de Objetos
 * serializables, facilita tareas como leer y
 * escribir objetos.
 *
 * @author Jose Correa
 * @version 1.0.0
 */
public class ManejadorSerializables {

    /**
     * Este metodo guarda el Serializable que pasa como parametro
     * en la ruta que pasa como parametro
     * @param ruta Ruta donde se desea guardar el objeto
     * @param objeto  Serializable que se desea guardar
     * @return True si se guarda con exito False en otro caso
     */
    public static boolean escribeObjeto(String ruta, Serializable objeto){

        if(!(objeto instanceof  Serializable)) return false;

        try {
            FileOutputStream archivo = new FileOutputStream(ruta);
            //se guarda el objeto
            ObjectOutputStream o = new ObjectOutputStream(archivo);
            o.writeObject(objeto);

            o.flush();
            o.close();
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo lee un Serializable desde la ruta que
     * pasa como parametro
     *
     * @param ruta Ruta de la que se desea leer el objeto
     * @return Serializable si se consigue leer con exito false en otro caso
     */
    public static Serializable leeObjeto(String ruta, Class<?> clase){

        Object o;

        try {
            o =  new ObjectInputStream(new FileInputStream(ruta)).readObject();
        } catch (IOException|ClassNotFoundException e) {
            return null;
        }

        if(o.getClass() == clase) return (Serializable) o;
        return null;
    }
}
