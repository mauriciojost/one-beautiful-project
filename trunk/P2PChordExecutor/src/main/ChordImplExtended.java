
package main;

import de.uniba.wiai.lspi.chord.service.impl.*;
import de.uniba.wiai.lspi.chord.com.Entry;
import de.uniba.wiai.lspi.chord.data.ID;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ChordImplExtended extends ChordImpl {

    public String printMyEntries(){
        String ret = "";
        Collection<Set> set = this.entries.getValues();

        Iterator<Set> i = set.iterator();
        System.out.println("Printing " + set.size() + " elements.");
        while(i.hasNext()){
            Set s = i.next();
            Iterator <Entry> j = s.iterator();
            while(j.hasNext()){
                Entry e = j.next();
                if (itBelongsToMe(e.getId())){
                    ret = ret +"\n"+ e.toString();
                }else{
                    ret = ret +"\n  ----("+ e.toString() + ")";
                }
            }
            
            
        }
        System.out.println(ret + "\nPrinting done.");
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
