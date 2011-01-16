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
public interface JobsEventsListener {
    public void addJobRequestedHereEvent(JobEvent je);
}
