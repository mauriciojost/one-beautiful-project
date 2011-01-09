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
        
        byte b[] = new byte[MyKey.KEY_SIZE];
        MyKey key;
        ArrayList<MyKey> values = new ArrayList<MyKey>();
        String strmatch = "key = ";
        for (int i=0;i<MyKey.KEY_SIZE; i++){
            strmatch = strmatch + "(\\S{2}) ";
        }
        strmatch = strmatch + ", value = \\["; 
        System.out.println("Getting all keys... + "  + strmatch);
        Pattern strMatch = Pattern.compile(strmatch);
        Matcher m = strMatch.matcher(entries);
        System.out.println("Getting all keys... + "  + strmatch);
        while(m.find()){

            System.out.print("One new key found ");
            for (int i = 0; i<MyKey.KEY_SIZE; i++){
                String currentbyte = m.group(i+1);
                System.out.print("" + currentbyte);
                int s = Integer.valueOf(currentbyte, 16);
                b[i] = (byte)s;
            }

            key = new MyKey(b);
            System.out.print("The new key is " + key.toString());

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


