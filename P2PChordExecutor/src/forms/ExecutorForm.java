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

import de.uniba.wiai.lspi.chord.com.Entry;
import de.uniba.wiai.lspi.chord.service.impl.Entries;
import de.uniba.wiai.lspi.chord.service.impl.EntriesEventListener;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.UIManager;
import javax.swing.table.TableColumnModel;
import main.ChordImplExtended;

/**
 *
 * @author Mauricio
 */
public class ExecutorForm extends javax.swing.JFrame implements EntriesEventListener {
    private ChordImplExtended chord;
    private final int PROCESS_NAME = 0;
    private final int PROCESS_STATUS = 1;
    private ArrayList<Entry> entries = new ArrayList<Entry>();


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
        chord.getEntries().setEventListener(this);
        initComponents();
        this.setLocation((                      /* Put the window in the center of the screeen. */
            Toolkit.getDefaultToolkit().getScreenSize().
            width-this.getSize().width)/2,
            (Toolkit.getDefaultToolkit().getScreenSize().
            height-this.getSize().height)/2);
        initMyComponents();
    }


    private void initMyComponents(){
        setInfoTableCell("Name", -1, this.PROCESS_NAME);
        setInfoTableCell("Status", -1, this.PROCESS_STATUS);

    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        infoTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        infoTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(infoTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(206, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable infoTable;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    public synchronized void newEvent(int i, Object o) {
        switch(i){
            case EntriesEventListener.ENTRY_ADDED:
                this.entries.add((Entry)o);
                break;
            case EntriesEventListener.ENTRY_REMOVED:
                this.entries.remove((Entry)o);
                break;
            default:
                System.out.println("Nothing to do in this event.");
                break; 
        }
       this.formatEntries(entries);
    }

    private void formatEntries(ArrayList entries){
        Serializable a;
        Object[] ar = entries.toArray();
        
        int i;

        for (i=0;i<ar.length;i++){
            Entry ent = ((Entry)(ar[i]));
            //System.out.println("Entrie " + ent);
            if (chord.itBelongsToMe(ent.getId())){
                this.setInfoTableCell(ent.getValue().toString(), i, 0);
            }else{
                this.setInfoTableCell(" *" + ent.getValue().toString(), i, 0);
            }
            
        }
        

        
            
            
        
    }

    private void setInfoTableCell(String text, int row, int column){
        if (row>=0){
            this.infoTable.setValueAt(text, row, column);
        }else{
            TableColumnModel tcm = infoTable.getColumnModel();
            tcm.getColumn(column).setHeaderValue(text);
        }



    }

}
