package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Class that parses the xml job descriptor file. */
public class JobDescriptorParser {

    /* Get the name of the general job from the descriptor. */
    public static String getJobNameFromXML(String rawFileContent){
        String path = null;

        Pattern strMatch = Pattern.compile( "<job_name>(.*)</job_name>");
        Matcher m = strMatch.matcher(rawFileContent);
        while ( m.find() ){
            path = m.group(1).trim();
        }
        return path;
    }
    
}
