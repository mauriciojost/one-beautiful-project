
package main;
import de.uniba.wiai.lspi.chord.data.*;
import de.uniba.wiai.lspi.chord.service.*;
import de.uniba.wiai.lspi.chord.service.impl.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main2 {

    public static void main ( String [] args ) {
        PropertiesLoader.loadPropertyFile();

        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);

        URL localURL = null;
        try {
            localURL = new URL( protocol + "://localhost:8081/");
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
            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            chord.insert(new StringKey("one key"), "one value");
        } catch (ServiceException ex) {
            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
        }

        Set<Serializable> set = null;
        try {
            set = chord.retrieve(new StringKey("one key"));
        } catch (ServiceException ex) {
            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
        }

        Iterator i = set.iterator();
        while(i.hasNext()){
            System.out.println(i.next());
        }

    }

}



class StringKey implements Key{
    private String key;
    public StringKey(String key){
        this.key = key;
    }
    public byte[] getBytes() {
        return key.getBytes();
    }

}

