package main;

import java.io.*;
import java.util.zip.*;

/* Class to compres a given folder into a zip file. */
public class Zip
{
    private File inFolder;
    private File outFolder;

    public Zip(String in, String out)
    {
        inFolder = new File(in);
        outFolder = new File(out);
    }
    
    public void zip()
    {
        try
        {

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
            BufferedInputStream in = null;

            byte[] data = new byte[1000];
            String files[] = inFolder.list();

            for (int i=0; i < files.length; i++)
            {
                in = new BufferedInputStream(new FileInputStream(inFolder.getPath() + "\\" + files[i]), 1000);
                out.putNextEntry(new ZipEntry(files[i]));
                int count;
                while((count = in.read(data,0,1000)) != -1)
                {
                    out.write(data, 0, count);
                }
                out.closeEntry();
            }
            out.flush();
            out.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
