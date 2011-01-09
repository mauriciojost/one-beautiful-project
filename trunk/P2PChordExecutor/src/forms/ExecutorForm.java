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
import java.util.Iterator;
import java.util.Set;

import javax.swing.UIManager;
import javax.swing.table.TableColumnModel;
import main.ChordImplExtended;
import main.JobPackage;
import main.MyKey;
import main.Parser;

public class ExecutorForm extends javax.swing.JFrame{
    private ChordImplExtended chord;
    private final String STATUS_WAITING = "status-waiting";
    private final String STATUS_EXECUTING = "status-executing";
    private final String STATUS_DONE = "status-done";

    private final int PROCESS_NAME = 0;
    private final int PROCESS_STATUS = 1;

    private final int UPGRADE_TIME_MS = 4000; 
    private ArrayList<JobPackage> jobsRequestedHere;

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
        jobsRequestedHere = new ArrayList<JobPackage>();
        Thread t = new Thread(new Runnable(){
            public void run() {
                while(true){
                    try{
                        Thread.sleep(UPGRADE_TIME_MS);
                        ArrayList<ID> ids = Parser.getParser().getAllIDs(chord.printEntries());
                        checkForPendingTasks(ids);
                        refreshForeignJobsExecutedHereTable(ids);
                        refreshLocallyRequestedJobsTable();
                    }catch(Exception e){}
                }
            }
        });
        t.start();

    }

    private void checkForPendingTasks(ArrayList<ID> localIDs){
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
            if (!this.isStatus(value)){
                System.out.println("\tWe have found one non status: " + value);

                JobPackage job = (JobPackage)value;
                
                MyKey status_key = new MyKey((String)job.getStatusIdentifier());
                Serializable status = chord.retrieveUnique(status_key);

                if (((String)status).equals(this.STATUS_WAITING)){
                    
                    chord.remove(status_key, status);
                    chord.insert(status_key, this.STATUS_EXECUTING);
                    System.out.println("For now we will just change its status.");
                    executeTask(job);
                }
            }
         }
        
    }

    private void executeTask(JobPackage jp){
        System.out.println("This task must be executed!!!.");
    }

    private void initMyComponents(){
        setLocallyRequestedJobsTableCell("Name", -1, this.PROCESS_NAME);
        setLocallyRequestedJobsTableCell("Status", -1, this.PROCESS_STATUS);

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
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane2.setViewportView(requestedJobsTable);

        locallyExecutedJobsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
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

        String filename = "c:\\job.zip";
        JobPackage jp = new JobPackage(filename, 0);
        addJobRequestedHere(jp);
        
    }//GEN-LAST:event_addJobButtonActionPerformed

    private void addJobRequestedHere(JobPackage jp){
        jobsRequestedHere.add(jp);
        chord.insert(new MyKey(jp.getDataIdentifier()), jp);
        chord.insert(new MyKey(jp.getStatusIdentifier()), this.STATUS_WAITING);
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


    private void refreshLocallyRequestedJobsTable(){

        int i;
        for (i=0;i<jobsRequestedHere.size();i++){
            JobPackage currentJ = jobsRequestedHere.get(i);

            String name = currentJ.getDataIdentifier();
            String status = (String)chord.retrieveUnique(
                    new MyKey(currentJ.getStatusIdentifier())
                    );            
            this.setLocallyRequestedJobsTableCell(name, i, 0);
            this.setLocallyRequestedJobsTableCell(status, i, 1);
        }
    }

    private void setLocallyRequestedJobsTableCell(String text, int row, int column){
        if (row>=0){
            this.requestedJobsTable.setValueAt(text, row, column);
        }else{
            TableColumnModel tcm = requestedJobsTable.getColumnModel();
            tcm.getColumn(column).setHeaderValue(text);
        }
    }

    private void refreshForeignJobsExecutedHereTable(ArrayList<ID> ids){
        int i;
        for (i=0;i<ids.size();i++){
            Set<Serializable> values = chord.retrieve(ids.get(i));
            //System.out.println("Entrie " + ent);
            Iterator j = values.iterator();
            while(j.hasNext()){
                String value = ((Object)j.next()).toString();
                if (chord.itBelongsToMe(ids.get(i))){
                    if (!isStatus(value)){
                        this.setForeignJobsExecutedHereTableCell(value, i, 0);
                    }
                }else{
                    this.setForeignJobsExecutedHereTableCell("-", i, 0);
                }
            }
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

    private boolean isStatus(Serializable value){
        String val;
        try{
            val = (String)value;
            if (val.startsWith("status-")){
                return true;
            }
        }catch(Exception e){

        }
        return false; 
    }
}
