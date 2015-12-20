package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

/* Class that wraps all the necessary things to run a subjob in a remote machine.
   As most important things it contains the zipfile inside, the name of the job,
   the command to be executed for this particular subjob, all the output files of the
   previous steps, once executed it will contain the output of the execution, the time, etc.
 */
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

    public static final String STD_LOCATION_ZIPS = "./jobs/";
    public static final String STATUS_WAITING =   "waiting"; /* Set of possible status. */
    public static final String STATUS_DONE =      "done";
    public static final int GENERAL_JOB_STEP = -1;

    private String status = STATUS_WAITING;

    private int jobStep = 0; /* Number of step in the workflow. */
    private int jobInstance = 0; /* Number of sibling in the same step. */
    
    private String zipFileName; /* Name of the zip file. */
    private byte[] zipFileContent; /* CONTENT of the zip file til this step. */

    private String generalJobName; /* Name of the general job. */
    private String subJobName; /* Name of this particular subjob. */
    
    private String jobDescriptorContent;

    private String output; /* Output obtained once executed. */
    private String realFinishedTime; /* Once executed, the real finished time. */

    private String fileToExecute; /* File to execute. */
    private String arguments; /* Arguments for the execution. */
    private String specificJobFolder; /* Folders. */
    private String generalJobFolder;
    
    private String auxiliaryData;
    private String ifCondition = "";

    public String getIfCondition() {
        return ifCondition;
    }

    public void setIfCondition(String ifCondition) {
        this.ifCondition = ifCondition;
    }

    public boolean satisfiesIfCondition(){
        if (ifCondition.equals("") || (this.jobStep==0)){
            System.out.println("IF: No condition to check or step=0.");
            return true;
        }else{
            File file = new File(this.generalJobFolder + File.separator + this.ifCondition);
            System.out.println("IF: Checking whether " + file.getPath() + " exists or not: " + file.exists());
            return file.exists();
        
            
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuxiliaryData() {
        return auxiliaryData;
    }

    public void setAuxiliaryData(String auxiliaryData) {
        this.auxiliaryData = auxiliaryData;
    }

    public void setRealFinishedTime(String realFinishedTime) {
        this.realFinishedTime = realFinishedTime;
    }

    public String getRealFinishedTime() {
        return realFinishedTime;
    }

    public void setZipFileContent(byte[] cont){
            zipFileContent = cont;
    }
    
    public void setFileToExecute(String command){
        this.fileToExecute = command;
    }

    public String getFileToExecute(){
        return this.fileToExecute;
    }

    public void setArguments(String arg){
        this.arguments = arg;
    }

    public String getArguments(){
        return this.arguments;
    }

    private boolean fileHasNotRightFormat(String zipfilename){
        return (!zipfilename.endsWith(".zip"));
    }

    /*  Creates a job package based on the input parameters, including the zip file content. */
    public JobPackage(String jobname, String subjobname, String zipfilename, int instance, int jobstep) throws Exception{
        this.generalJobName = jobname;
        this.subJobName = subjobname;
        this.jobInstance = instance;
        this.jobStep = jobstep;

        String specfolder;
        /* The first time, an auxiliary GENERAL_JOB_STEP job will be created. It will not
           be executed, it is only necessary to get the content of the xml file from here and
           let the other subjob packages to be initialized (getting the xml file content for instance).
         */
        if (jobstep == GENERAL_JOB_STEP){
            /* Particular case for the first time a job zip file is opened to create the tree of subjobs. */
            specfolder = null;
            if (fileHasNotRightFormat(zipfilename)){
                throw new Exception("File " + zipfilename + " is not a right job file.");
            }
            this.zipFileName = this.moveJobPacketToStandardLocation(zipfilename);
            String standard_folder = zipFileName+"-folder";
            
            Unzip uz = new Unzip(zipFileName, standard_folder + File.separator /*generalJobFolder*/);
            uz.unzipIt();
            this.jobDescriptorContent = this.getJobDescriptorFromFile(standard_folder + File.separator + "job_descriptor.xml");
        }else{
            /* More frequent case where a subjob is created from a line of the xml file. */
            this.zipFileName = zipfilename;
            specfolder = zipFileName + "-" + this.getName();
            this.jobDescriptorContent = null;
        }

        generalJobFolder = zipFileName + "-folder";

        this.zipFileContent = JobPackage.readFile(zipFileName);
        this.specificJobFolder = specfolder;

    }

    /* Download the zip content in the right folder and have it ready for execution. */
    public void downloadToExecute() throws Exception{
        Unzip uz = new Unzip(zipFileName, specificJobFolder + File.separator);
        uz.unzipIt();
    }

    public String getGeneralJobFolder(){
        return generalJobFolder; 
    }
    public String getSpecificJobFolder(){
        return specificJobFolder;
    }

    public String getZipFileName(){
        return zipFileName;
    }

    /* Read the content of a file and return it. */
    public static byte[] readFile(String filename) throws Exception{
        byte buff[] = null;
        try {
            FileInputStream i = new FileInputStream(filename);
            buff = new byte[i.available()];
            i.read(buff);

        }catch (Exception e){
            throw new Exception("Cannot open file: " + filename +". - " + e.toString());
        }

        return buff;
    }


    public String getJobDescriptorFromFile(String filename) throws Exception{
        byte[] file = JobPackage.readFile(filename);
        String ret = new String(file);
        return ret; 
        
    }


    /* Put the zip file in the correct place for job zip files and renames it
     to give to the execution a particular ID depending on the time it was executed. */
    public String moveJobPacketToStandardLocation(String zipfilenm) throws Exception{
        boolean existing;
        File src = new File(zipfilenm);
         existing = src.exists();
        File newlocation = new File(STD_LOCATION_ZIPS);
        existing = newlocation.exists();
        if (existing==false){
            if (!newlocation.mkdirs()){
                throw new Exception("Cannot create the standard jobs path.");
            }
        }

        String name = "";
        name = name + Calendar.getInstance().getTime().getHours();
        name = name + Calendar.getInstance().getTime().getMinutes();
        name = name + Calendar.getInstance().getTime().getSeconds();
        newlocation = new File(STD_LOCATION_ZIPS + name + src.getName());

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
        return zipFileName + "-" +  generalJobName + "-" + subJobName + "-" + jobStep + "-" + jobInstance;
    }

    /* Return the ID that should be used to get the DATA of this subjob from the chord. */
    public String getDataIdentifier(String status){
        return this.getName() + "-" + status;
    }

    /* Return the ID that should be used to get the STATUS of this subjob from the chord. */
    /*public String getStatusIdentifier(){
        return this.getDataIdentifier() + "-status";
    }*/

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


    /* Executes the process given by arguments and returns the output. */
    public static String execute(String workingdirectory, String command, String arguments) throws Exception{
        String output = "";

        String line;

        Runtime rt = Runtime.getRuntime();
        Process pr = null;
        File myWorking = new File(workingdirectory);

        pr = rt.exec(new File(myWorking, command).getAbsolutePath() + " " + arguments, null, myWorking);
        BufferedReader input =
            new BufferedReader (new InputStreamReader(pr.getInputStream()));

        while ((line = input.readLine()) != null){
            output = output + line + "\n";
        }

        pr.waitFor();
        input.close();

        pr.destroy();

        return output;
    }

    /* Execute a process with basic behaviour. */
    public static void executeBasic(String command) throws Exception{
        Runtime rt = Runtime.getRuntime();
        rt.exec(command);

    }

    public void downloadZipFile() throws FileNotFoundException, IOException{

        File newFile = new File(this.getZipFileName());
        System.out.println("File to download : " + newFile.getPath());
        //create all non exists folders
        //else you will hit FileNotFoundException for compressed folder
        new File(newFile.getParent()).mkdirs();
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(this.getFileContent(), 0, this.getFileContent().length);
        fos.close();
    }


}
