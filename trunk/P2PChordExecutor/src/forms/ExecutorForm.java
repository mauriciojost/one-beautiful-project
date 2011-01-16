/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ExecutorForm.java
 *
 * Created on 06/01/2011, 21:14:32
 */

package forms;

import de.uniba.wiai.lspi.chord.data.ID;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.UIManager;
import javax.swing.table.TableColumnModel;
import main.ChordImplExtended;
import main.JobDependencesTree;
import main.JobEvent;
import main.JobPackage;
import main.JobsEventsListener;
import main.MyKey;
import main.Parser;

public class ExecutorForm extends javax.swing.JFrame implements JobsEventsListener{
    private ChordImplExtended chord;

    private final int PROCESS_NAME = 0;
    private final int PROCESS_STATUS = 1;

    private final int UPGRADE_TIME_MS = 4000; 
    private ArrayList<JobEvent> jobsRequestedHereEvents;
    private ArrayList<String> foreignJobsExecutedHere;
    private ArrayList<JobDependencesTree> jobs;

    static{
        try  {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch  (Exception e) {
            e.printStackTrace();
        }
   }

    /** Creates new form ExecutorForm */
    public ExecutorForm(ChordImplExtended ch) {
        chord = ch;
        //chord.getEntries().setEventListener(this);
        initComponents();
        this.setLocation((                      /* Put the window in the center of the screeen. */
            Toolkit.getDefaultToolkit().getScreenSize().
            width-this.getSize().width)/2,
            (Toolkit.getDefaultToolkit().getScreenSize().
            height-this.getSize().height)/2);
        initMyComponents();
        initSystem();
    }

    private void initSystem(){
        
        Thread t = new Thread(new Runnable(){
            public void run() {
                while(true){
                    try{
                        Thread.sleep(UPGRADE_TIME_MS);
                        ArrayList<ID> ids = Parser.getParser().getAllIDs(chord.printEntries());
                        checkForPendingTasks(ids, foreignJobsExecutedHere);
                        refreshForeignJobsExecutedHereTable(foreignJobsExecutedHere);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

    }

    private void checkForPendingTasks(ArrayList<ID> localIDs, ArrayList<String> executedHere){
        /* Here we check if there are pending tasks to execute.
         * If there is key whose value is not one status string, it is a
         * task to execute.
         * We get this key, then we get the JobPackage, we get the name, and
         * we get the status.
         * If the status is waiting then we execute it, and put its status (by
         * removing-adding) as executing.
         */
         for (int i=0;i<localIDs.size();i++){
            ID currentID = localIDs.get(i); 
            System.out.println("\tThere is something in my list... To execute?");
            Serializable value = chord.retrieveUnique(currentID);
            if (this.isExecutable(value)){
                JobPackage job = (JobPackage)value;
                if (chord.itBelongsToMe(currentID)){
                    System.out.println("\tI have found one job that is mine! (a non status): " + job.getDataIdentifier() + "-" + job.getName());
                    MyKey status_key = new MyKey((String)job.getStatusIdentifier());
                    Serializable status = chord.retrieveUnique(status_key);
                    System.out.println("\tIts status of execution is: " + (String)status);
                    if (JobPackage.STATUS_WAITING.equals(status)){
                        System.out.println("\tChanging status...\n");
                        chord.remove(status_key, status);
                        chord.insert(status_key, JobPackage.STATUS_DONE);
                        executedHere.add("Job " + job.getName() + " started at " + Calendar.getInstance().getTime().toString());
                        executeTask(job);
                        System.out.println("\tJob done.\n");
                        executedHere.add("Job " + job.getName() + " finished at " + Calendar.getInstance().getTime().toString());
                    }
                }
            }
         }
        
    }

    private void executeTask(JobPackage jp){
        jp.getFileContent();
        System.out.println("Executing (simulation)...\n");
    }

    private void initMyComponents(){
        setLocallyRequestedJobsTableCell("Name", -1, this.PROCESS_NAME);
        //setLocallyRequestedJobsTableCell("Status", -1, this.PROCESS_STATUS);
        jobs = new ArrayList<JobDependencesTree>();
        foreignJobsExecutedHere = new ArrayList<String>();
        jobsRequestedHereEvents = new ArrayList<JobEvent>();
        String[] columnNames = {"Events"};
        locallyExecutedJobsTable.setModel(
                new javax.swing.table.DefaultTableModel((Object[])columnNames, 200));

                
        requestedJobsTable.setModel(
                new javax.swing.table.DefaultTableModel((Object[])columnNames, 200));

    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        requestedJobsTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        locallyExecutedJobsTable = new javax.swing.JTable();
        addJobButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        requestedJobsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        jScrollPane2.setViewportView(requestedJobsTable);

        locallyExecutedJobsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        jScrollPane3.setViewportView(locallyExecutedJobsTable);

        addJobButton.setText("Add Job");
        addJobButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJobButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Locally Requested Jobs");

        jLabel2.setText("Foreign Jobs Executed Here");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                        .addComponent(addJobButton))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addJobButton)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(181, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addJobButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJobButtonActionPerformed

        String zip = ".\\src\\resources\\job1.zip";
        //JobPackage jp = new JobPackage(filename, 0);
        addJobRequestedHere(zip);
        
    }//GEN-LAST:event_addJobButtonActionPerformed

    private void addJobRequestedHere(String zipFileName){
        //jobsRequestedHere.add(jp);
//        chord.insert(new MyKey(jp.getDataIdentifier()), jp);
//        chord.insert(new MyKey(jp.getStatusIdentifier()), this.STATUS_WAITING);
        try{
            JobDependencesTree jdt = new JobDependencesTree(zipFileName);
            jdt.startJob(this, chord);
            jobs.add(jdt);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void addJobRequestedHereEvent(JobEvent je){
        int i;
        jobsRequestedHereEvents.add(je);
        for (i=0;i<jobsRequestedHereEvents.size();i++){
            JobEvent current = jobsRequestedHereEvents.get(i);
            this.setLocallyRequestedJobsTableCell(current.getJob().getName() + current.getEvent() + " at " + current.getWhen(), i, 0);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJobButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable locallyExecutedJobsTable;
    private javax.swing.JTable requestedJobsTable;
    // End of variables declaration//GEN-END:variables



    private void setLocallyRequestedJobsTableCell(String text, int row, int column){
        if (row>=0){
            this.requestedJobsTable.setValueAt(text, row, column);
        }else{
            TableColumnModel tcm = requestedJobsTable.getColumnModel();
            tcm.getColumn(column).setHeaderValue(text);
        }
    }

    private void refreshForeignJobsExecutedHereTable(ArrayList<String> events){
        int i;

        for (i=0;i<events.size();i++){
            this.setForeignJobsExecutedHereTableCell(events.get(i), i, 0);
        }
    }

    private void setForeignJobsExecutedHereTableCell(String text, int row, int column){
        if (row>=0){
            this.locallyExecutedJobsTable.setValueAt(text, row, column);
        }else{
            TableColumnModel tcm = locallyExecutedJobsTable.getColumnModel();
            tcm.getColumn(column).setHeaderValue(text);
        }
    }

    private boolean isExecutable(Serializable value){
        String val;
        if (value==null){
            return false;
        }
        try{
            val = (String)value;
            if (val.startsWith("status-")){
                return false;
            }
        }catch(Exception e){
        }
        return true;
    }
}
