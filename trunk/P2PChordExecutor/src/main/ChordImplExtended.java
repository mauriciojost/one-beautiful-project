
package main;

import de.uniba.wiai.lspi.chord.service.impl.*;
import de.uniba.wiai.lspi.chord.com.Entry;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ChordImplExtended extends ChordImpl {

    public String printMyEntries(){
        String ret = "";
        Collection set = this.entries.getValues();

        Iterator<Entry> i = set.iterator();
        System.out.println("Printing " + set.size() + " elements.");
        while(i.hasNext()){
            ret = ret + i.next();
        }
        System.out.println(ret + "\nPrinting done.");
        return ret; 
    }
}
