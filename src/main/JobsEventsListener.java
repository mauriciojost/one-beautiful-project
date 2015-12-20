package main;

/* Interface a class should implement to listen to events from a SubJob execution. */
public interface JobsEventsListener {
    public void addJobRequestedHereEvent(JobEvent je);
}
