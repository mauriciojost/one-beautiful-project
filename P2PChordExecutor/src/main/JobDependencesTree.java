
package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 Class that represents a general job.
 A general job has a workflow, a xml descriptor file.
 Once it is executed, all its subjobs will be executed in order. Once they are
 executed they will emit events. 
 */
public class JobDependencesTree extends Thread{

    private String jobDescriptorContent; /* Content of the XML job descriptor file. */
    private String zipFileName;
    private ArrayList<JobPackage> subJobsList;  /* Set of subjobs.*/
    private HashMap<String, JobPackage> finishedJobs;
    private ChordImplExtended chord;
    private JobsEventsListener jobsEventsListener;  /* Listener of the events of this general job. */

    public JobDependencesTree (String zipFileName) throws Exception{
        /* Creates a special job package to get the content of the xml file. */
        JobPackage jp = new JobPackage("main", "main", zipFileName, 0, JobPackage.GENERAL_JOB_STEP);
        this.jobDescriptorContent = jp.getJobDescriptorContent();
        this.zipFileName = jp.getPositionedZipFileName();
        /* Creates the tree of the job packages respecting the workflow. */
        subJobsList = this.generateSubJobsList(jobDescriptorContent);
        finishedJobs = new HashMap<String, JobPackage>();
    }

    public JobPackage lookForFinishedJobByName(String name){
        return finishedJobs.get(name);
    }

     /* It starts executing the job (all its subjobs step by step) on the chord. */
    public void startJob(JobsEventsListener jobsEventsListener, ChordImplExtended chord){
        this.chord = chord;
        this.jobsEventsListener = jobsEventsListener;
        this.start();
    }


    /* Execute the job (all the subjobs). */
    @Override
    public void run(){
        int i;
        byte[] newzip = null;
        for (i=0;i<this.getAmountOfSteps();i++){
            try{
                ArrayList<JobPackage> subjobs_of_this_step = this.getSubJobsOfStep(i);
                ArrayList<JobPackage> subjobs_of_this_step_finished;
                addAllTheseSubjobsToTheChord(subjobs_of_this_step, chord, newzip);
                checkThatThisSubjobsAreDone(i, subjobs_of_this_step, chord);
                subjobs_of_this_step_finished = getAndDeleteAllTheseSubjobsFromTheChord(subjobs_of_this_step, chord);
                newzip = mergeAllResultsOfTheseSubjobs(subjobs_of_this_step_finished);
                System.out.println("Finished all the procedures for " + i + "th  step's jobs. Next one...");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Finished all the procedures for this job!! ");
    }

    /* Put the results of all the zip files from parallel jobs in the same folder. */
    private byte[] mergeAllResultsOfTheseSubjobs(ArrayList<JobPackage> subjobs_of_this_step_finished) throws Exception{
        Iterator<JobPackage> i = subjobs_of_this_step_finished.iterator();
        JobPackage jp = null;
        while(i.hasNext()){
            jp = i.next();
            try {
                /* Decompress each instance in <place><instance>\... and then put them all together. Then delete those created directories. */
                jp.downloadZipFile();
                Unzip u = new Unzip(jp.getZipFileName(), jp.getGeneralJobFolder());
                u.unzipIt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Zip z = new Zip(jp.getGeneralJobFolder(), jp.getZipFileName());
        z.zip();
        byte[] newzip = JobPackage.readFile(jp.getZipFileName());
        return newzip; 
    }

    /* Put all the given subjobs in the cord to execute them parallely. */
    private void addAllTheseSubjobsToTheChord(ArrayList<JobPackage> subjobs_of_this_step, ChordImplExtended chord, byte[] lastzip){
        Iterator<JobPackage> i = subjobs_of_this_step.iterator();
        while(i.hasNext()){
            JobPackage jp = i.next();
            if (lastzip!=null){
                jp.setZipFileContent(lastzip);
            }
            jp.setStatus(JobPackage.STATUS_WAITING);
            chord.insertJobPackage(jp);
            jobsEventsListener.addJobRequestedHereEvent(new JobEvent(jp, "sent to chord", Calendar.getInstance().getTime()));
        }
    }

    /* Check that are the given subjobs have a 'done' status. */
    private void checkThatThisSubjobsAreDone(int step, ArrayList<JobPackage> subjobs_of_this_step, ChordImplExtended chord){

        while(true){ /* Repeat checking if last one went bad. */
            int not_finished_jobs=0;
            System.out.println("Checking for jobs of the same substep...");
            Iterator<JobPackage> i = subjobs_of_this_step.iterator(); /* Check for every subjob of this step. */
            while(i.hasNext()){
                JobPackage jp = i.next();
                System.out.print("Checking for job '" + jp.getName() + "' of substep '" + jp.getJobStep() + "'...");
                //String status = (String)JobPackage.getLastStatus(chord.retrieveSet(new MyKey(jp.getStatusIdentifier())));
                JobPackage retrieved = (JobPackage)chord.retrieveOneRandom(new MyKey(jp.getDataIdentifier(JobPackage.STATUS_DONE)));
                
                if (retrieved==null){
                    not_finished_jobs++; /* At least one has not finished yet. */
                    System.out.println("Not ready...");
                }else{
                    System.out.println("Ready!!! Status of the job (should be DONE): " + retrieved.getStatus());
                    if (jp.getAuxiliaryData()==null){
                        this.jobsEventsListener.addJobRequestedHereEvent(new JobEvent(jp, "notified finished", Calendar.getInstance().getTime()));
                        jp.setAuxiliaryData("Already notified change");
                    }
                }
            }
            if(not_finished_jobs>0){
                
                try {
                    System.out.println("Waiting for some jobs to finish...");
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {}
            }else{
                System.out.println("Done this step's jobs!!");
                return;
            }
        }
    }

    /* Once all subjobs of the step are 'done' get them from the chord and remove all. */
    private ArrayList<JobPackage> getAndDeleteAllTheseSubjobsFromTheChord(ArrayList<JobPackage> subjobs_of_this_step, ChordImplExtended chord){
        ArrayList<JobPackage> procesed_jobs = new ArrayList<JobPackage>();
        Iterator<JobPackage> i = subjobs_of_this_step.iterator(); /* Check for every subjob of this step. */
        System.out.println("Once done these step's jobs, they will be deleted...");
        while(i.hasNext()){
            JobPackage oldjp = i.next();
            JobPackage newjp = (JobPackage) chord.retrieveOneRandom(new MyKey(oldjp.getDataIdentifier(JobPackage.STATUS_DONE)));
            procesed_jobs.add(newjp);
            
            finishedJobs.put(newjp.getName(), newjp);
            //chord.removeJobPackage(oldjp);
            chord.removeJobPackage(newjp);
            System.out.println("Removed job '" + oldjp.getName() + "' from the chord.");
        }
        return procesed_jobs;
    }

    /* Generate a list of subjobs (jobpackages) from the xml job descriptor file content. */
    private ArrayList<JobPackage> generateSubJobsList(String descriptorContent) throws Exception{
        ArrayList<JobPackage> subjobs = new ArrayList<JobPackage>();
        String jobname = "defaultjobname";

        Pattern strMatch = Pattern.compile( "<job_descriptor job_name=\"(.*?)\">");
        Matcher m = strMatch.matcher(descriptorContent);
        while(m.find()){
            jobname = m.group(1);
        }

        jobname = jobname + "_" +Calendar.getInstance().getTime().getSeconds()+
                Calendar.getInstance().getTime().getMinutes(); 

        strMatch = Pattern.compile("(<subjob step=\"\\d*?\" instance=\"\\d*?\" name=\".*?\" filetoexecute=\".*?\" arguments=\".*?\">)");
        m = strMatch.matcher(descriptorContent);
        while(m.find()){
            String subjob = m.group(1);
            JobPackage sj = this.generateSubJob(jobname, subjob, zipFileName);
            subjobs.add(sj);
        }
        return subjobs; 
        
    }

    /* Return all the subjobs in the given substep of the workflow. */
    public ArrayList<JobPackage> getSubJobsOfStep(int step){
        ArrayList<JobPackage> instep = new ArrayList<JobPackage>(); 
        Iterator<JobPackage> i = subJobsList.iterator();
        while(i.hasNext()){
            JobPackage sj = i.next();
            if (sj.getJobStep()==step){
                instep.add(sj);
            }
        }
        return instep; 
    }

    /* Return the number of steps in the workflow. */
    public int getAmountOfSteps(){
        /* If there is 0,1,2,3 it will return 4.*/
        int max = -1;
        Iterator<JobPackage> i = subJobsList.iterator();
        while(i.hasNext()){
            JobPackage sj = i.next();
            if (sj.getJobStep()>max){
                max = sj.getJobStep();
            }
        }
        return max+1;
    }

    /* Generate a subjob from the given parameters. Will include the zip file content. */
    private JobPackage generateSubJob(String general_job_name, String subjobstr, String zipfilename) throws Exception{
        /*
        <subjob step=(\d*?) instance=(\d*?) name="(.*?)">(.*?)</subjob>
         */
        JobPackage sj = null;

        Pattern strMatch = Pattern.compile( "<subjob step=\"(\\d*?)\" instance=\"(\\d*?)\" name=\"(.*?)\" filetoexecute=\"(.*?)\" arguments=\"(.*?)\">");
        Matcher m = strMatch.matcher(subjobstr);
        if(m.find()){
            int step = Integer.valueOf(m.group(1));
            int instance = Integer.valueOf(m.group(2));
            String name = m.group(3);
            String filetoexecute = m.group(4);
            String arguments = m.group(5);
            sj = new JobPackage(general_job_name, name, zipfilename, instance, step);
            sj.setFileToExecute(filetoexecute);
            sj.setArguments(arguments);
        }
        else{
            System.err.println("Cannot parse as a sub job: " + subjobstr);
        }
        return sj; 
    }

}
