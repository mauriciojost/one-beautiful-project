package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class JobPackage implements Serializable{

    /*
       MAIN   step-1

        O     step 0
        |
        O     step 1
      /   \
     O     0  step 2 (instances 0 and 1)
      \   /
        O     step 3


     * <subjob step=1 instance=1 name=>./acqua param1 param2</subjob>
     */

    public static final String STATUS_WAITING = "status-waiting";
    public static final String STATUS_EXECUTING = "status-executing";
    public static final String STATUS_DONE = "status-done";
    public static final int GENERAL_JOB_STEP = -1;
    public static final int PARTICULAR_SUBJOB_STEP = -2;
    private int jobInstance = 0; /* Hermano en la misma etapa. */
    private int jobStep = 0; /* Etapa 1, etapa 2 en nuestro flow chart. */
    private String zipFileName;
    private String subJobName;
    private String generalJobName;
    private byte[] zipFileContent;
    private String jobDescriptorContent;
    private String output;
    private String executionCommand;
    private String jobFolder;

    public static void main(String[] args){
        //JobPackage jp = new JobPackage("nombre", ".\\src\\resources\\job1.zip", 0, 0);
        System.out.println(" ");
    }


    public void setExecutionCommand(String command){
        this.executionCommand = command;
    }

    public String getExecutionCommand(){
        return this.executionCommand;
    }

    public JobPackage(String jobname, String subjobname, String zipfilename, int instance, int jobstep) throws Exception{
        this.generalJobName = jobname;
        this.subJobName = subjobname;
        this.jobInstance = instance;
        this.jobStep = jobstep;
        this.output = "<Not finalized yet>";


        String jobfolder;
        if (jobstep == GENERAL_JOB_STEP){
            this.zipFileName = this.moveJobPacketToStandardLocation(zipfilename);
            jobfolder = zipFileName+"-folder";
            Unzip uz = new Unzip(zipFileName, jobfolder + File.separator);
            uz.unzipIt();
        }else if(jobstep == PARTICULAR_SUBJOB_STEP){
            this.zipFileName = this.moveJobPacketToStandardLocation(zipfilename);

            jobfolder = zipFileName + "-" + subJobName;
            Unzip uz = new Unzip(zipFileName, jobfolder + File.separator);
            uz.unzipIt();
        }else{
            this.zipFileName = zipfilename;
            jobfolder = zipFileName+"-folder";
        }

        this.jobDescriptorContent = this.getJobDescriptorFromFile(jobfolder + File.separator + "job_descriptor.xml");

        this.zipFileContent = JobPackage.readFile(zipFileName);
        this.jobFolder = jobfolder;

    }


    public String getJobFolder(){
        return jobFolder; 
    }

    public String getZipFileName(){
        return zipFileName;
    }

    public static byte[] readFile(String filename){
        byte buff[] = null;
        try {
            FileInputStream i = new FileInputStream(filename);
            buff = new byte[i.available()];
            i.read(buff);

        }catch (Exception e){
            System.err.println("Cannot open file: " + filename);
            e.printStackTrace();
        }

        return buff;
    }


    public String getJobDescriptorFromFile(String filename){
        byte[] file = JobPackage.readFile(filename);
        String ret = new String(file);
        return ret; 
        
    }

    public String moveJobPacketToStandardLocation(String filename) throws Exception{
        // File (or directory) to be moved
        boolean existing;
        File src = new File(filename);
         existing = src.exists();
        // Destination directory
        File newlocation = new File("./jobs/");
        existing = newlocation.exists();
        if (existing==false){
            if (!newlocation.mkdirs()){
                throw new Exception("Cannot create the standard jobs path.");
            }
        }

        newlocation = new File("./jobs/" + src.getName());
        // Move file to new directory
//        boolean success = src.renameTo(newlocation);
//        if (!success) {
//            System.err.println("Unable to rename the job.");
//        }

        if (!src.equals(newlocation)){
            this.copyDirectory(src, newlocation);
        }
        
        return newlocation.getPath();
    }

    public String getPositionedZipFileName(){
        return zipFileName;
    }

    public int getJobInstance() {
        return jobInstance;
    }

    public int getJobStep(){
        return jobStep;
    }

    public void setJobStep(int jt){
        jobStep = jt;
    }

    public String getGeneralJobName(){
        return this.generalJobName;
    }

    public String getJobName(){
        return subJobName;
    }

    public String getName(){
        return generalJobName + "-" + subJobName + "-" + jobStep + "-" + jobInstance;
    }

    public String getJobDescriptorContent(){
        return jobDescriptorContent;
    }

    public byte[] getFileContent() {
        return zipFileContent;
    }

    @Override
    public String toString(){
        return zipFileName + "-" + jobStep + "-" + jobInstance;
    }

    public String getDataIdentifier(){
        return this.toString();
    }

    public String getStatusIdentifier(){
        return this.getDataIdentifier() + "-status";
    }

    public void setOutput(String output){
        this.output = output;
    }

    public String getOutput(){
        return output;
    }

    public void copyDirectory(File sourceLocation , File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

}
