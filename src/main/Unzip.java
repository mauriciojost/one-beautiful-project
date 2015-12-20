package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip
{
    List<String> fileList;
    private  String INPUT_ZIP_FOLDER;
    private String OUTPUT_FOLDER;

    public Unzip(String in, String out){
        this.INPUT_ZIP_FOLDER = in;
        this.OUTPUT_FOLDER = out;
    }


    /**
    * Unzip it.
    * @param zipFile input zip file
    * @param output zip file output folder
    */
    public void unzipIt() throws Exception{
        byte[] buffer = new byte[1024];

        /* Create output directory if doesnt exist. */
        File folder = new File(OUTPUT_FOLDER);
        if(!folder.exists()){
            folder.mkdir();
        }

        /* Get the zip file content. */
        ZipInputStream zis = new ZipInputStream(new FileInputStream(INPUT_ZIP_FOLDER));
        /* Get the zipped file list entry. */
        ZipEntry ze = zis.getNextEntry();
        while(ze!=null){
            String fileName = ze.getName();
            File newFile = new File(OUTPUT_FOLDER + File.separator + fileName);
            System.out.println("file unzip : "+ newFile.getAbsoluteFile());
            /* Create all non exists folders.
               else you will hit FileNotFoundException for compressed folder.
             */
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        System.out.println("unzip is complete");
    }
}