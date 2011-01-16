package main;

import java.io.FileInputStream;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JobDescriptorParser {

    /*
     *
     *
     *
     */

    
    public static String getJobNameFromXML(String rawFileContent){
        String path = null;

        Pattern strMatch = Pattern.compile( "<job_name>(.*)</job_name>");
        Matcher m = strMatch.matcher(rawFileContent);

        while ( m.find() ){
            path = m.group(1).trim();
        }

        return path;
    }

    
    
    public static float getSignificanceLevelFromXML(String rawFileContent){

        float aux = (float)0.95;
        Pattern strMatch = Pattern.compile( "<significance_level value=(.*)>");
        Matcher m = strMatch.matcher(rawFileContent);
        if(m.find()){
            String str = m.group(1);
            aux = Float.valueOf(str);
        }
        else{
            System.out.println("Significance level not fount. Using default: " + aux);
        }
        return aux;
    }

    public static ArrayList<String> getLandmarksFromXML(String rawFileContent){
        String landmarksTemp = null;
        Pattern strMatch = Pattern.compile( "<landmarks> (.*) </landmarks>");
        Matcher m = strMatch.matcher(rawFileContent);

        while ( m.find() ){
            landmarksTemp = m.group(1).trim();
        }
        return getLandmarksFromText(landmarksTemp);
    }

    public static ArrayList<String> getLandmarksFromText(String landmarksTemp){
        int indice=0;
        String oneM;
        ArrayList<String> lm = new ArrayList<String>();
        while (indice!=-1){
            indice = landmarksTemp.indexOf(" ");
            if (indice!=-1){
                oneM = landmarksTemp.substring(0,indice).trim();
                landmarksTemp = landmarksTemp.substring(indice).trim();
            }else{
                oneM = landmarksTemp.trim();
            }
            lm.add(oneM);
        }
        return lm;

    }


}







