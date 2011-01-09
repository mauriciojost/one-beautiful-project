package main;

import java.io.Serializable;

public class JobPackage implements Serializable{

    /*

        O     step 1
        |
        O     step 2
      /   \
     O     0  step 3 (instances 1 and 2)
      \   /
        O     step 4


     */
    private int jobInstance = 0; /* Hermano en la misma etapa. */
    private int jobStep = 0; /* Etapa 1, etapa 2 en nuestro flow chart. */
    private String fileName;
    private byte[] fileContent;

    public JobPackage(String filename, int instance){
        this.fileName = filename;
        this.jobInstance = instance;
        this.fileContent = JobDescriptorParser.readFile(filename);
    }

    public JobPackage cloneJobPackage(){
        return new JobPackage(fileName, jobInstance++);
    }

    public String getFileName(){
        return fileName;
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

    public byte[] getFile() {
        return fileContent;
    }

    @Override
    public String toString(){
        return fileName + "-" + jobInstance;
    }

    public String getDataIdentifier(){
        return this.toString();
    }

    public String getStatusIdentifier(){
        return this.getDataIdentifier() + "-status";
    }
}
