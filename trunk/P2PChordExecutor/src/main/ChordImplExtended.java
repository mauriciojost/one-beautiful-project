
package main;

import de.uniba.wiai.lspi.chord.service.impl.*;
import de.uniba.wiai.lspi.chord.com.Entry;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.Key;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ChordImplExtended extends ChordImpl {

    public String printMyEntries(){
        String ret = "";

        Parser p = new Parser();
        ArrayList<MyKey> keys = p.getAllKeys(this.printEntries());
        for(int i=0; i<keys.size(); i++){
            MyKey key = keys.get(i);
            if (itBelongsToMe(key.getEquivalentID())){
                ret = ret +"\n"+ key;
            }else{
                ret = ret +"\n  *Replica of "+ key + "";
            }
        }

        return ret;
    }

    public boolean itBelongsToMe(ID key){
        if (this.getPredecessorID().compareTo(this.getID())>0){ /* pred > me */
            /* I am the first in the cord. Mi predecesor is the last one. */
            if ((this.getPredecessorID().compareTo(key)>=0) && (this.getID().compareTo(key)>=0)){
                return true; /* Si el es mayor al id, y si yo soy mayor al numero -> es mio (yo soy menor). */
            }else{
                return false; /* Si yo soy menor al numero y el es mayor -> es de el (yo soy menor). */
            }
        }else{
            /* I am in a normal case, my predecesor's ID is less than mine. */
            if (this.getPredecessorID().compareTo(key)>=0){ /*my predecesor is still bigger than the  numer then it is not mine. */
                return false;
            }else{
                return true;
            }
            
        }
        
    }

}
