
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
        if (unique.size()>1){
            System.out.println("Element supposed to have one element had more: " + key.toString());
        }
        Iterator<Serializable> i = unique.iterator();
        while(i.hasNext()){
            return i.next();
        }
        return null;
    }


    /* Retrieve one random element from the given ID. */
    public Serializable retrieveOneRandom(ID id){
        Set<Serializable> unique = retrieve(id);
        
        if (unique.size()>1){
            System.out.println("Element supposed to have one element had more: " + id.toString());
        }
        Iterator<Serializable> i = unique.iterator();
        while(i.hasNext()){
            return i.next();
        }
        return null;
    }

    /* Retrieve the whole set for the given key. */
    public Set<Serializable> retrieveSet(MyKey key){
        Set<Serializable> unique = retrieve(key);
        return unique;
    }

    /* Retrieve the whole set for the given ID. */
    public Set<Serializable> retrieveSet(ID id){
        Set<Serializable> unique = retrieve(id);

        return unique;
    }

    /* Insert a job package and its status in the chord. */
    public void insertJobPackage(JobPackage jp, String status){
        this.insert(new MyKey(jp.getDataIdentifier()), jp);
        this.insert(new MyKey(jp.getStatusIdentifier()), status);
    }

    /* Removes a job package from the chord, and its status. */
    public void removeJobPackage(JobPackage jp){
        this.remove(new MyKey(jp.getDataIdentifier()), jp);
        this.remove(new MyKey(jp.getStatusIdentifier()), JobPackage.STATUS_DONE);
        this.remove(new MyKey(jp.getStatusIdentifier()), JobPackage.STATUS_EXECUTING);
        this.remove(new MyKey(jp.getStatusIdentifier()), JobPackage.STATUS_WAITING);
    }
}
