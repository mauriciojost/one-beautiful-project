package main;
import java.util.Date;

/* Class to pass information about the event related to a subjob:
   when it was executed, its name, etc.
 */
public class JobEvent {
    private JobPackage job;
    private String event;
    private Date when;

    public JobEvent(JobPackage jp, String event, Date when){
        this.job = jp;
        this.event = event;
        this.when = when;
    }

    public String getEvent() {
        return event;
    }

    public JobPackage getJob() {
        return job;
    }

    public Date getWhen() {
        return when;
    }
    
}
