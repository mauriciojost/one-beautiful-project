package main;

import de.uniba.wiai.lspi.chord.service.*;
/* Implementation of Key. */
public class MyKey implements Key{
    private String key;
    
    public MyKey(String str_key){
        key = str_key;
    }
    
    public byte[] getBytes() {
        return key.getBytes();
    }

    @Override
    public String toString(){
        return key;
    }
}
