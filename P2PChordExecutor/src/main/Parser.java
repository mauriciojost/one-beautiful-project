/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/*


Entries:
  key = 71 06 95 17 , value = [( key = 71 06 95 17 , value = value12)]
  key = A6 2F 22 25 , value = [( key = A6 2F 22 25 , value = value1213), ( key = A6 2F 22 25 , value = value)]
 */
public class Parser {
    public static final int ID_SIZE = 20;
    private static Parser instance = null;

    public static Parser getParser(){
        if (instance == null){
            instance = new Parser();
        }
        return instance; 
    }
    
    private Parser(){}
    
    public ArrayList<ID> getAllIDs(String entries){
        final int ID_SIZE = 20;
        byte b[] = new byte[ID_SIZE];
        ID id;
        ArrayList<ID> values = new ArrayList<ID>();
        String strmatch = "key = ";
        for (int i=0;i<ID_SIZE; i++){
            strmatch = strmatch + "(\\S{2}) ";
        }
        strmatch = strmatch + ", value = \\["; 
        System.out.println("Getting all keys... + "  + strmatch);
        Pattern strMatch = Pattern.compile(strmatch);
        Matcher m = strMatch.matcher(entries);
        System.out.println("Getting all keys... + "  + strmatch);
        while(m.find()){

            System.out.print("One new key found ");
            for (int i = 0; i<ID_SIZE; i++){
                String currentbyte = m.group(i+1);
                System.out.print("" + currentbyte);
                int s = Integer.valueOf(currentbyte, 16);
                b[i] = (byte)s;
            }

            id = new ID(b);
            System.out.print("The new ID is " + id.toString());

            values.add(id);
            //System.out.println("Key: " + key);
        }

        return values;
    }

    public static void main (String[] args){
        String str = "Entries:\n  key = 71 06 95 17 , value = [( key = 71 06 95 17 , value = value12)]\n  key = A6 2F 22 25 , value = [( key = A6 2F 22 25 , value = value1213), ( key = A6 2F 22 25 , value = value)]";
        Parser p = new Parser();
        p.getAllIDs(str);

    }
}


