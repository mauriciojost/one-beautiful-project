
package main;

import de.uniba.wiai.lspi.chord.service.impl.*;
import de.uniba.wiai.lspi.chord.data.ID;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


/* Extension of the real Chord Implementation of Open Chord. */
public class ChordImplExtended extends ChordImpl {

    public String printMyEntries(){
        String ret = "";
        Parser p = Parser.getParser();
        ArrayList<ID> ids = p.getAllIDs(this.printEntries());
        for(int i=0; i<ids.size(); i++){
            ID id = ids.get(i);
            if (itBelongsToMe(id)){
                ret = ret +"\n"+ id;
            }else{
                ret = ret +"\n  *Replica of "+ id + "";
            }
        }

        return ret;
    }

    /* Check whether an ID belongs to the current node or not. */
    public boolean itBelongsToMe(ID id){
        //System.out.println("Trying to compare my key " + getID().toHexString() + " with pred key " + getPredecessorID().toHexString() + " with key " + key.toHexString());
        if (this.getPredecessorID().compareTo(this.getID())>0){ /* pred > me */
            /* I am the first in the cord. Mi predecesor is the last one. */
            if ((this.getPredecessorID().compareTo(id)>=0) && (this.getID().compareTo(id)>=0)){
                return true; /* Si el es mayor al id, y si yo soy mayor al numero -> es mio (yo soy menor). */
            }else{
                return false; /* Si yo soy menor al numero y el es mayor -> es de el (yo soy menor). */
            }
        }else{
            /* I am in a normal case, my predecesor's ID is less than mine. */
            if (this.getPredecessorID().compareTo(id)>=0){ /*my predecesor is still bigger than the  numer then it is not mine. */
                return false;
            }else{
                return true;
            }   
        }   
    }

    /* Retrieve one random element from the given keys. */
    public Serializable retrieveOneRandom(MyKey key){
        Set<Serializable> unique = retrieve(key);
        return checkSetRetrieved(unique);
        
    }


    /* Retrieve one random element from the given ID. */
    public Serializable retrieveOneRandom(ID id){
        Set<Serializable> unique = retrieve(id);
        return checkSetRetrieved(unique);
    }

    private Serializable checkSetRetrieved(Set<Serializable> set){
        System.err.println("Checking size of set retrieved...");
        if (set.size()>1){
            System.err.println("Set with more than one element.");
            System.err.println("Size: " + set.size());
            System.err.println("Elements: ");

            Iterator<Serializable> it = set.iterator();
            while(it.hasNext()){
                JobPackage jp = (JobPackage)it.next();
                System.err.println("\t" + jp.getDataIdentifier(jp.getStatus()) + " " + jp.getStatus());
            }
            System.exit(-1);
            return null;
        }else{
            if (set.size()==1){
                Object[] oba = set.toArray();
                return (Serializable)oba[0];
            }else{
                return null;
            }
        }
    }

    /* Insert a job package and its status in the chord. */
    public void insertJobPackage(JobPackage jp){
        System.err.println("Inserting " + jp.getDataIdentifier(jp.getStatus()) + " " + jp.getStatus());
        this.insert(new MyKey(jp.getDataIdentifier(jp.getStatus())), jp);
        
    }

    /* Removes a job package from the chord, and its status. */
    public void removeJobPackage(JobPackage jp){
        System.err.println("Removing " + jp.getDataIdentifier(jp.getStatus()) + " " + jp.getStatus());
        //this.remove(new MyKey(jp.getDataIdentifier(jp.getStatus())), jp);
    }
}
