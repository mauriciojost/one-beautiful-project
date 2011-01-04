package main;

public class Timer extends Thread{
    private PeriodicTask task;
    private int intervalMS = 0;
    

    public Timer(PeriodicTask task, int interval_ms){
        this.intervalMS = interval_ms;
        this.task = task;
    }

    @Override
    public void run() {
        while(true){
            task.periodicTask();
            try {
                Thread.sleep(intervalMS);
            } catch (InterruptedException ex) {ex.printStackTrace();}
        }
    }
}
