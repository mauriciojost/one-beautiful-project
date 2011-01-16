/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Date;

/**
 *
 * @author Mauricio
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
