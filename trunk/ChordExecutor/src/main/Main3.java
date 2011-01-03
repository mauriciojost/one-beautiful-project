
package main;
import de.uniba.wiai.lspi.chord.data.*;
import de.uniba.wiai.lspi.chord.service.*;
import de.uniba.wiai.lspi.chord.service.impl.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main3 {

    public static void main ( String [] args ) {
        PropertiesLoader.loadPropertyFile();

        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);

        URL localURL = null;
        try {
            localURL = new URL( protocol + "://localhost:8082/");
        } catch ( MalformedURLException e){ throw new RuntimeException (e);}

        Chord chord = new ChordImpl();
        chord.setURL(localURL);

        URL bootstrapURL = null;
        try {
            bootstrapURL = new URL( protocol + "://localhost:8080/");
        } catch ( MalformedURLException e){ throw new RuntimeException (e);}

        try {
            chord.join(bootstrapURL);
        } catch (ServiceException ex) {
            Logger.getLogger(Main3.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            chord.insert(new StringKey("two key"), "two value");
        } catch (ServiceException ex) {
            Logger.getLogger(Main3.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Waiting for key one to be set...");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main3.class.getName()).log(Level.SEVERE, null, ex);
        }

        Set<Serializable> set = null;
        try {
            set = chord.retrieve(new StringKey("one key"));
        } catch (ServiceException ex) {
            Logger.getLogger(Main3.class.getName()).log(Level.SEVERE, null, ex);
        }

        Iterator i = set.iterator();
        while(i.hasNext()){
            System.out.println(i.next());
        }

    }

}



