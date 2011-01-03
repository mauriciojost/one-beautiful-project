
package main;
import de.uniba.wiai.lspi.chord.data.*;
import de.uniba.wiai.lspi.chord.service.*;
import de.uniba.wiai.lspi.chord.service.impl.*;
import java.net.MalformedURLException;

public class Main {

    public static void main ( String [] args ) {
        PropertiesLoader.loadPropertyFile();

        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
        URL localURL = null;
        String port = "8080";
        try {
            localURL = new URL( protocol + "://localhost:"+port+"/");
        } catch ( MalformedURLException e){
            throw new RuntimeException (e);
        }

        Chord chord = new ChordImpl();
        try {
            chord.create ( localURL );
        } catch ( ServiceException e) {
            throw new RuntimeException (" Could not create DHT !", e);
        }


        
    }

}


