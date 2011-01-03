package main;
import de.uniba.wiai.lspi.chord.service.*;

public class StringKey implements Key{
    private String key;
    public StringKey(String key){
        this.key = key;
    }
    public byte[] getBytes() {
        return key.getBytes();
    }

}
