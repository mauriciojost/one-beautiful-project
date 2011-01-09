package main;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.*;

public class MyKey implements Key{
    public final static int KEY_SIZE = 20;
    private byte[] key;

   
    public MyKey(byte[] rawkey){
        key = rawkey; 
    }
    
    public MyKey(String str_key){
        this.key = new byte[KEY_SIZE];
        if (str_key.length() >= KEY_SIZE){
            System.out.println("Key string big enough.");
            for (int i=0; i<KEY_SIZE;i++){
                this.key[i] = (byte)str_key.charAt(i);
            }
        }else{
            System.out.println("Key strign not big enough. Completing with zeros...");
            for (int i=0; i<KEY_SIZE;i++){
                this.key[i] = (i<str_key.length()?(byte)str_key.charAt(i):0);
            }
        }
        
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
