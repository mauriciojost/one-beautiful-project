/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

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
    
    public ArrayList<MyKey> getAllKeys(String entries){
        byte b[] = new byte[4];
        MyKey key;
        ArrayList<MyKey> values = new ArrayList<MyKey>();
        
        Pattern strMatch = Pattern.compile( "key = (.*) (.*) (.*) (.*) , value = \\[");
        Matcher m = strMatch.matcher(entries);
        while(m.find()){
            for (int i = 0; i<4; i++){
                int s = Integer.valueOf(m.group(i+1), 16);
                b[i] = (byte)s;
            }
            key = new MyKey(b);
            values.add(key);
            //System.out.println("Key: " + key);
        }

        return values;
    }

    public static void main (String[] args){
        String str = "Entries:\n  key = 71 06 95 17 , value = [( key = 71 06 95 17 , value = value12)]\n  key = A6 2F 22 25 , value = [( key = A6 2F 22 25 , value = value1213), ( key = A6 2F 22 25 , value = value)]";
        Parser p = new Parser();
        p.getAllKeys(str);


    }
}


