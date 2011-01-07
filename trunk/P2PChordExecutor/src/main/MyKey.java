package main;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.*;

public class MyKey implements Key{
    private byte[] key;

   
    public MyKey(byte[] rawkey){
        key = rawkey; 
    }
    public MyKey(String key){
        this.key = key.getBytes();
    }
    public byte[] getBytes() {
        return key;
    }

    public ID getEquivalentID(){
        return new ID(this.getBytes());
    }

    @Override
    public String toString(){
        String str = "";
        for(int i=0; i<key.length; i++){
            str = str + String.format("%02X ", key[i]);
        }
        return str; 
    }
}
