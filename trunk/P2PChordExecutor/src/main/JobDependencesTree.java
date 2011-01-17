
package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobDependencesTree extends Thread{


/*
 * Es el unico de step 1
 * Cuantos hay de step 2?
 * Dame todos los JobPackages de step 2
 */
    private String jobDescriptorContent;
    private String zipFileName;
    private ArrayList<JobPackage> subJobsList;
    private HashMap<String, JobPackage> finishedJobs;
    private ChordImplExtended chord;
    private JobsEventsListener jobsEventsListener;



    public JobDependencesTree (String zipFileName) throws Exception{
        JobPackage jp = new JobPackage("main", "main", zipFileName, 0, JobPackage.GENERAL_JOB_STEP);
        this.jobDescriptorContent = jp.getJobDescriptorContent();
        this.zipFileName = jp.getPositionedZipFileName();
        subJobsList = this.generateSubJobsList();
        finishedJobs = new HashMap<String, JobPackage>();
    }

    public JobPackage lookForFinishedJobByName(String name){
        return finishedJobs.get(name);
    }

    public void startJob(JobsEventsListener jobsEventsListener, ChordImplExtended chord){
        this.chord = chord;
        this.jobsEventsListener = jobsEventsListener;
        this.start();
    }
    
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

    private byte[] mergeAllResultsOfTheseSubjobs(ArrayList<JobPackage> subjobs_of_this_step_finished) throws Exception{
        Iterator<JobPackage> i = subjobs_of_this_step_finished.iterator();
        JobPackage jp = null;
        while(i.hasNext()){
            jp = i.next();
            try {
                /* Decompres each instance in <place><instance>\... and then put them all together. Then delete those created directories. */
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

    private void addAllTheseSubjobsToTheChord(ArrayList<JobPackage> subjobs_of_this_step, ChordImplExtended chord, byte[] lastzip){
        Iterator<JobPackage> i = subjobs_of_this_step.iterator();
        while(i.hasNext()){
            JobPackage jp = i.next();
            if (lastzip!=null){
                jp.setZipFileContent(lastzip);
            }
            chord.insertJobPackage(jp, JobPackage.STATUS_WAITING);
            jobsEventsListener.addJobRequestedHereEvent(new JobEvent(jp, "sent to chord", Calendar.getInstance().getTime()));
        }
    }
    
    private void checkThatThisSubjobsAreDone(int step, ArrayList<JobPackage> subjobs_of_this_step, ChordImplExtended chord){

        while(true){ /* Repeat checking if last one went bad. */
            int not_finished_jobs=0;
            System.out.println("Checking for jobs of the same substep...");
            Iterator<JobPackage> i = subjobs_of_this_step.iterator(); /* Check for every subjob of this step. */
            while(i.hasNext()){
                JobPackage jp = i.next();
                System.out.print("Checking for job '" + jp.getName() + "' of substep '" + jp.getJobStep() + "'...");
                String status = (String)JobPackage.getLastStatus(chord.retrieveSet(new MyKey(jp.getStatusIdentifier())));
                if (!status.equals(JobPackage.STATUS_DONE)){
                    not_finished_jobs++; /* At least one has not finished yet. */
                    System.out.println("Not ready...");
                }else{
                    System.out.println("Ready!!!");
                    this.jobsEventsListener.addJobRequestedHereEvent(new JobEvent(jp, "notified finished", Calendar.getInstance().getTime()));
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

    private ArrayList<JobPackage> getAndDeleteAllTheseSubjobsFromTheChord(ArrayList<JobPackage> subjobs_of_this_step, ChordImplExtended chord){
        ArrayList<JobPackage> procesed_jobs = new ArrayList<JobPackage>();
        Iterator<JobPackage> i = subjobs_of_this_step.iterator(); /* Check for every subjob of this step. */
        System.out.println("Once done these step's jobs, they will be deleted...");
        while(i.hasNext()){
            JobPackage oldjp = i.next();
            JobPackage newjp = (JobPackage) JobPackage.getLastJobPackage(chord.retrieveSet(new MyKey(oldjp.getDataIdentifier())));
            procesed_jobs.add(newjp);
            
            finishedJobs.put(newjp.getName(), newjp);
            //chord.removeJobPackage(oldjp);
            chord.removeJobPackage(newjp);
            System.out.println("Removed job '" + oldjp.getName() + "' from the chord.");
        }
        return procesed_jobs;
    }

    private ArrayList<JobPackage> generateSubJobsList() throws Exception{
        ArrayList<JobPackage> subjobs = new ArrayList<JobPackage>();
        String jobname = "defaultjobname";

        Pattern strMatch = Pattern.compile( "<job_descriptor job_name=\"(.*?)\">");
        Matcher m = strMatch.matcher(jobDescriptorContent);
        while(m.find()){
            jobname = m.group(1);
        }

        jobname = jobname + "_" +Calendar.getInstance().getTime().getSeconds()+
                Calendar.getInstance().getTime().getMinutes(); 

        strMatch = Pattern.compile("(<subjob step=\"\\d*?\" instance=\"\\d*?\" name=\".*?\" filetoexecute=\".*?\" arguments=\".*?\">)");
        m = strMatch.matcher(jobDescriptorContent);
        while(m.find()){
            String subjob = m.group(1);
            JobPackage sj = this.generateSubJob(jobname, subjob, zipFileName);
            subjobs.add(sj);
        }
        return subjobs; 
        
    }

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

    private JobPackage generateSubJob(String general_job_name, String subjobstr, String filename) throws Exception{
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
            sj = new JobPackage(general_job_name, name, filename, instance, step);
            sj.setFileToExecute(filetoexecute);
            sj.setArguments(arguments);
        }
        else{
            System.err.println("Cannot parse as a sub job: " + subjobstr);
        }
        return sj; 
    }

}
