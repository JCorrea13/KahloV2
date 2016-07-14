package serialport;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Stack;


/**
 * Esta clase modela el Puerto Serial se encarga
 * de la configuracion del puerto para su arranque
 * de la escritura y lectura en el.
 *
 * Cuando se configura el puerto se configura una tiempo de pullin
 * al que se desae leer el puerto.
 * @author Jose Correa
 * @version 1.0.0
 */

public class PuertoSerial {

    private static SerialPort puertoSerie = null;
    private static InputStream entrada = null;
    private static OutputStream salida = null;

    /**
     * Este metodo verifica y pone en una pila todos los puertos disponibles
     * @return Stack con los puertos disponibles
    */
    public static Stack getListaPuertos() {
        Stack<CommPortIdentifier> puertos = new Stack<CommPortIdentifier>();
        CommPortIdentifier puerto = null;
        Enumeration listaPuertos = CommPortIdentifier.getPortIdentifiers();

        while (listaPuertos.hasMoreElements()) {
            puerto = (CommPortIdentifier) listaPuertos.nextElement();
            puertos.push(puerto);
        }
        return puertos;
    }
    
   /**
    * Este metodo abre el puerto seria especificado
    * @param puerto se refiere al puerto que se desea abrir
    * @return True si se abrio el puerto exitosamente
    */
    private boolean abrirPuerto(CommPortIdentifier puerto, int baudrate, int databits, int stopbits, boolean parity) throws PortInUseException, IOException {

        if (puerto == null) {
            return false;
        }

            puertoSerie = (SerialPort) puerto.open(puerto.getName(), 2000); //se abre el puerto con un delay de 2000ms
            entrada = puertoSerie.getInputStream();
            salida = puertoSerie.getOutputStream();

            // configuramos el puerto
            configurarPuertoEscritura(baudrate, databits, stopbits, parity);
            return true;
    }

    /**
     * Este metodo configura el puerto para escribir y hacer la comunicacion con
     * Google Earth con el protocolo NMEA
     *
     * @return True si la configuracion ah tenido exito
     */
    private boolean configurarPuertoEscritura(int baudrate, int databits, int stopbits, boolean parity) {
        try {
            puertoSerie.setSerialPortParams(baudrate, databits, stopbits, (parity)?SerialPort.PARITY_EVEN:puertoSerie.PARITY_NONE );
            puertoSerie.notifyOnDataAvailable(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *Este metodo se encarga de cerrar el puerto serial
     */
    public void cerrarPuerto() {
        try {
            puertoSerie.close();
        } catch (Exception e) {

        }
    }

    /**
     * Este metodo escribe los bytes que se pasan como parametro
     * en el puerto serial
     * @param b - arreglo de bytes que se desea escribir
     * @throws IOException
     */
    public void write(byte [] b) throws IOException {
        salida.write(b);
    }

    /**
     * Este metodo lee el puerto seria y acomada los bytes leeidos
     * en el arreglo que pasa como parametro
     * @param b - arreglo en donde se desean almacenar los bytes leidos
     * @return numero de byetes leidos
     * @throws IOException
     */
    public int read(byte [] b) throws IOException {
        return entrada.read(b);
    }
    
    
}
