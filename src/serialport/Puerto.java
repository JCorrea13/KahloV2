package serialport;

import gnu.io.CommPortIdentifier;

/**
 * Esta clase modela al CommPortIdentifier
 * La unica razon de existencia de esta clase es facilitar
 * el manejo de los puertos en toda la aplicacion
 */
public class Puerto {

    CommPortIdentifier commPortIdentifier;
    Puerto(CommPortIdentifier commPortIdentifier) {
        this.commPortIdentifier = commPortIdentifier;
    }

    public CommPortIdentifier getCommPortIdentifier(){
        return commPortIdentifier;
    }

    @Override
    public String toString() {
        return commPortIdentifier.getName();
    }
}
