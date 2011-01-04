
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
                    ret = ret +"\n  ("+ e.toString() + ")";
                }
            }
            
            
        }
        System.out.println(ret + "\nPrinting done.");
        return ret; 
    }

    public boolean itBelongsToMe(ID key){
        return (!itBelongsToMyPredecessor(key));
    }

    public boolean itBelongsToMyPredecessor(ID key){
        return ((this.getPredecessorID().compareTo(key)>=0) /* Npred > k */
        );
    }
}
