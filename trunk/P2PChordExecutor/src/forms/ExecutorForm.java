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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JTable;

import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import main.ChordImplExtended;
import main.JobDependencesTree;
import main.JobEvent;
import main.JobPackage;
import main.JobsEventsListener;
import main.MyKey;
import main.Parser;
import main.Zip;

public class ExecutorForm extends javax.swing.JFrame implements JobsEventsListener, MouseListener{
    private ChordImplExtended chord;

    private final int PROCESS_NAME = 0;
    private final int PROCESS_STATUS = 1;

    private final int UPGRADE_TIME_MS = 4000; 
    private ArrayList<JobEvent> jobsRequestedHereEvents;
    private ArrayList<JobEvent> foreignJobssExecutedHere;
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


    private void initMyComponents(){
        setLocallyRequestedJobsTableCell("Name", -1, this.PROCESS_NAME);
        //setLocallyRequestedJobsTableCell("Status", -1, this.PROCESS_STATUS);
        jobs = new ArrayList<JobDependencesTree>();
        foreignJobssExecutedHere = new ArrayList<JobEvent>();
        jobsRequestedHereEvents = new ArrayList<JobEvent>();

        String[] columnNames = {"Job", "Event", "Time"};
        locallyExecutedJobsTable.setModel(
                new javax.swing.table.DefaultTableModel((Object[])columnNames, 200));


        DefaultTableModel model = new DefaultTableModel((Object[])columnNames, 200){
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };

        requestedJobsTable.setModel(model);
        //listMod.addListSelectionListener(this);

        requestedJobsTable.addMouseListener(this);

        

    }


    private void addEventToForeignJobsExecutedHere(JobEvent je){
        foreignJobssExecutedHere.add(je);
        this.refreshForeignJobsExecutedHereTable();
    }
    private void initSystem(){
        
        Thread t = new Thread(new Runnable(){
            public void run() {
                while(true){
                    try{
                        Thread.sleep(UPGRADE_TIME_MS);
                        ArrayList<ID> ids = Parser.getParser().getAllIDs(chord.printEntries());
                        checkForPendingTasks(ids);
                        refreshForeignJobsExecutedHereTable();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
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
            Serializable value = chord.retrieveOneRandom(currentID);
            if (this.isExecutable(value)){
                JobPackage job = (JobPackage)value;
                if (chord.itBelongsToMe(currentID)){
                    System.out.println("\tI have found one job that is mine! (a non status): " + job.getDataIdentifier() + "-" + job.getName());
                    MyKey status_key = new MyKey((String)job.getStatusIdentifier());
                    Serializable status = JobPackage.getLastStatus(chord.retrieveSet(status_key));
                    System.out.println("\tIts status of execution is: " + (String)status);
                    if (JobPackage.STATUS_WAITING.equals(status)){
                        System.out.println("\tChanging status...\n");
                        chord.remove(status_key, status);
                        chord.insert(status_key, JobPackage.STATUS_EXECUTING);

                        chord.remove(new MyKey(job.getDataIdentifier()), job);

                        this.addEventToForeignJobsExecutedHere(new JobEvent(job, "started", Calendar.getInstance().getTime()));
                        
                        job = executeTask(job);
                        System.out.println("Output of the job: \n" + job.getOutput());
                        this.addEventToForeignJobsExecutedHere(
                                new JobEvent(job, "finished", Calendar.getInstance().getTime()));
                        

                        chord.insert(new MyKey(job.getDataIdentifier()), job);
                        chord.remove(status_key, status);
                        chord.insert(status_key, JobPackage.STATUS_DONE);
                    }
                }
            }
         }
        
    }

    private JobPackage executeTask(JobPackage jpvirtual){

        JobPackage processed=null;
        try{
            jpvirtual.downloadZipFile();
            JobPackage jpmaterial = new JobPackage(jpvirtual.getGeneralJobName(),
                    jpvirtual.getName(),
                    jpvirtual.getZipFileName(),
                    0,
                    JobPackage.PARTICULAR_SUBJOB_STEP);
                   
            String output = JobPackage.execute(jpmaterial.getJobFolder(), 
                    jpvirtual.getFileToExecute(),
                    jpvirtual.getArguments());


            Zip comp = new Zip(jpmaterial.getJobFolder(), jpmaterial.getZipFileName());
            comp.zip();

            jpvirtual.setZipFileContent(JobPackage.readFile(jpmaterial.getZipFileName()));
            jpvirtual.setOutput(output);
            jpvirtual.setRealFinishedTime(Calendar.getInstance().getTime().toString());

            processed = jpvirtual; 
            //put_content_in_jpvirtual_again;
        }catch(Exception e){
            e.printStackTrace();
        }

        return processed;
        
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
        jScrollPane1 = new javax.swing.JScrollPane();
        outputText = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChordExecutor");
        setResizable(false);

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
        requestedJobsTable.setToolTipText("List of locally requested jobs. Double click to see outputs and details.");
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

        outputText.setBackground(new java.awt.Color(0, 0, 0));
        outputText.setColumns(20);
        outputText.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        outputText.setForeground(new java.awt.Color(204, 204, 204));
        outputText.setLineWrap(true);
        outputText.setRows(5);
        outputText.setWrapStyleWord(true);
        jScrollPane1.setViewportView(outputText);

        jLabel3.setText("Details (locally requested jobs)");

        jLabel4.setText("M. Jost - S. Mazumdar - Prof. L. Liquori - P2P - Ubinet - Polytech'Nice - 2011");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addJobButton))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 369, Short.MAX_VALUE)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(addJobButton)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addJobButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJobButtonActionPerformed


        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File zip = fc.getSelectedFile();
            //String zip = ".\\src\\resources\\job1.zip";
            addJobRequestedHere(zip.getPath());

        } else {
            outputText.setText("Action cancelled by the user.");
        }

    }//GEN-LAST:event_addJobButtonActionPerformed

    private void addJobRequestedHere(String zipFileName){
        //jobsRequestedHere.add(jp);
//        chord.insert(new MyKey(jp.getDataIdentifier()), jp);
//        chord.insert(new MyKey(jp.getStatusIdentifier()), this.STATUS_WAITING);
        try{
            JobDependencesTree jdt = new JobDependencesTree(zipFileName);
            jdt.startJob(this, chord);
            addJobDescriptorTree(jdt);
            outputText.setText("File loaded successfully.");
        }catch(Exception e){
            this.outputText.setText("Error while processing " + zipFileName + ".");
            e.printStackTrace();
        }
    }


    private void addJobDescriptorTree(JobDependencesTree jd){
        jobs.add(jd);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJobButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable locallyExecutedJobsTable;
    private javax.swing.JTextArea outputText;
    private javax.swing.JTable requestedJobsTable;
    // End of variables declaration//GEN-END:variables




    public void addJobRequestedHereEvent(JobEvent je){
        int i;
        jobsRequestedHereEvents.add(je);
        for (i=0;i<jobsRequestedHereEvents.size();i++){
            JobEvent current = jobsRequestedHereEvents.get(i);
            this.setLocallyRequestedJobsTableCell(current.getJob().getName(), i, 0);
            this.setLocallyRequestedJobsTableCell(current.getEvent(), i, 1);
            this.setLocallyRequestedJobsTableCell(
                    current.getWhen().getHours() +":"+ current.getWhen().getMinutes() +":"+ current.getWhen().getSeconds(), i, 2);
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



    private void refreshForeignJobsExecutedHereTable(){
        int i;

        for (i=0;i<foreignJobssExecutedHere.size();i++){
            this.setForeignJobsExecutedHereTableCell(foreignJobssExecutedHere.get(i).getJob().getName(), i, 0);
            this.setForeignJobsExecutedHereTableCell(foreignJobssExecutedHere.get(i).getEvent(), i, 1);
            this.setForeignJobsExecutedHereTableCell(
                    foreignJobssExecutedHere.get(i).getWhen().getHours() + ":" +
                    foreignJobssExecutedHere.get(i).getWhen().getMinutes() + ":" +
                    foreignJobssExecutedHere.get(i).getWhen().getSeconds(), i, 2);
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



    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getClickCount() == 2) {
            JTable target = (JTable)e.getSource();
            int row = target.getSelectedRow();
            int column = target.getSelectedColumn();
            System.out.println();
            String name = (String)this.requestedJobsTable.getValueAt(row, 0);
            
            JobPackage jp = getRequestedJobFromName(name);
            if (jp!=null){
                System.out.println("SubJob asociado!!" + jp.getName());
                outputText.setText(
                        "\nGeneral job name: " + jp.getGeneralJobName() +
                        "\nJob name: " + jp.getName() +
                        "\nGeneral job folder: " + jp.getGeneralJobFolder() +
                        "\nSpecific job folder: " + jp.getJobFolder() +
                        "\nReal finished time: " + jp.getRealFinishedTime() +
                        "\nCommand: " + jp.getFileToExecute() + " " + jp.getArguments() +
                        "\nJob ID in chord (before sha-1): " + jp.getDataIdentifier() +
                        "\nStatus ID in chord (before sha-1): " + jp.getStatusIdentifier() +
                        "\nOutput:\n" + jp.getOutput()
                        
                        );
            }

        }
    }

    private JobPackage getRequestedJobFromName(String name){
        Iterator<JobDependencesTree> i = this.jobs.iterator();
        while(i.hasNext()){
            JobDependencesTree jd = i.next();
            JobPackage jp = jd.lookForFinishedJobByName(name);
            if (jp!=null){
                return jp; 
            }
        }
        return null;
    }

    public void mousePressed(MouseEvent e) {
        
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }


}

