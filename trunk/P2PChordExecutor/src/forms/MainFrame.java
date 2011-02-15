
package forms;


import de.uniba.wiai.lspi.chord.data.*;
import de.uniba.wiai.lspi.chord.service.*;
import de.uniba.wiai.lspi.chord.service.impl.*;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.UIManager;

import main.*;

public class MainFrame extends javax.swing.JFrame{
    private static String DEFAULT_PORT = "8080";
    private ChordImplExtended chord = null;

    static{
        try  {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch  (Exception e) {
            e.printStackTrace();
        }
   }


    private void initMyComponents(){
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            this.localPortTextField.setText(thisIp.getHostAddress() + ":" + DEFAULT_PORT);
        }catch(Exception e){
            System.err.println("Error getting the local IP address: \n" + e.getMessage());
        }

    }
    /** Creates new form MainFrame */
    public MainFrame() {

    
        initComponents();
        PropertiesLoader.loadPropertyFile();

        initMyComponents();
        /* Look & Feels. */
        setDebugButtonsVisibility(false);
        this.setLocation((                      /* Put the window in the center of the screeen. */
            Toolkit.getDefaultToolkit().getScreenSize().
            width-this.getSize().width)/2,
            (Toolkit.getDefaultToolkit().getScreenSize().
            height-this.getSize().height)/2);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
    }


    public void closeApplication(){
        if (chord!=null)
            chord.leave();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bootstrapTextField = new javax.swing.JTextField();
        localPortTextField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        initButton = new javax.swing.JButton();
        debuGinsertButton = new javax.swing.JButton();
        debuGremoveButton = new javax.swing.JButton();
        debuGkeyTextField = new javax.swing.JTextField();
        debugKeyLabel = new javax.swing.JLabel();
        debuGvalueLabel = new javax.swing.JLabel();
        debuGvalueTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        debuGreportTextArea = new javax.swing.JTextArea();
        showDebugButton = new javax.swing.JToggleButton();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chord's Job Executor");
        setResizable(false);

        bootstrapTextField.setText("localhost:8080");
        bootstrapTextField.setToolTipText("Bootstrap Chord Executor to use to connect to an existing Chord of Executors. ");
        bootstrapTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bootstrapTextFieldActionPerformed(evt);
            }
        });

        localPortTextField.setText("localhost:8080");
        localPortTextField.setToolTipText("Local port which other Chord Executors will connect to. ");
        localPortTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                localPortTextFieldActionPerformed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.setToolTipText("Connect to an existing Chord of Executors.");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Bootstrap IP:");

        jLabel2.setText("Local port:");

        initButton.setText("Init chord");
        initButton.setToolTipText("Init a new Chord of Executors. ");
        initButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initButtonActionPerformed(evt);
            }
        });

        debuGinsertButton.setText("Insert");
        debuGinsertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debuGinsertButtonActionPerformed(evt);
            }
        });

        debuGremoveButton.setText("Remove");
        debuGremoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debuGremoveButtonActionPerformed(evt);
            }
        });

        debuGkeyTextField.setText("key");
        debuGkeyTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debuGkeyTextFieldActionPerformed(evt);
            }
        });

        debugKeyLabel.setText("Key:");

        debuGvalueLabel.setText("Value:");

        debuGvalueTextField.setText("value");

        debuGreportTextArea.setBackground(new java.awt.Color(0, 0, 0));
        debuGreportTextArea.setColumns(20);
        debuGreportTextArea.setForeground(new java.awt.Color(255, 255, 255));
        debuGreportTextArea.setLineWrap(true);
        debuGreportTextArea.setRows(5);
        debuGreportTextArea.setText("Report...");
        debuGreportTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(debuGreportTextArea);

        showDebugButton.setText("Debug");
        showDebugButton.setToolTipText("Debugging.");
        showDebugButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDebugButtonActionPerformed(evt);
            }
        });

        statusLabel.setText("M. Jost - S. Mazumdar - Prof. L. Liquori - P2P - Ubinet - Polytech'Nice - 2011");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(bootstrapTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(localPortTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)))
                            .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(initButton, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(showDebugButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(debuGvalueLabel)
                            .addComponent(debugKeyLabel))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(debuGvalueTextField)
                            .addComponent(debuGkeyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(debuGremoveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(debuGinsertButton, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(initButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(localPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(bootstrapTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showDebugButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(statusLabel)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(debuGkeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(debugKeyLabel))
                    .addComponent(debuGinsertButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(debuGvalueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(debuGvalueLabel)
                    .addComponent(debuGremoveButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bootstrapTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bootstrapTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bootstrapTextFieldActionPerformed

    private void initButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initButtonActionPerformed
        /* To create a cord. Only one node should do it, and at the beginning. */
        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);

        URL localURL = null;
        
        try {
            String port = formatIPAddress(this.localPortTextField.getText());
            System.out.println(port);
            localURL = new URL( protocol + "://"+port+"/");
        
            chord = new ChordImplExtended();
            chord.create ( localURL );
            this.setStatusLabel("Chord created successfully.");
            this.connectionToChordInstanceDone();
            continueToNextScreen();
        } catch ( Exception e) {
            this.setStatusLabel(e.getMessage());
            e.printStackTrace();
            chord.leave();
        }

        
    }//GEN-LAST:event_initButtonActionPerformed


    private void continueToNextScreen(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExecutorForm(chord).setVisible(true);
            }
        });
        this.dispose();
    }

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed

        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);

        
        URL localURL = null;
        try {
            String port = formatIPAddress(this.localPortTextField.getText());
            localURL = new URL( protocol + "://"+port+"/");
        
            chord = new ChordImplExtended();
            chord.setURL(localURL);

            URL bootstrapURL = null;
            String bootstrapString = formatIPAddress(this.bootstrapTextField.getText());
        
            bootstrapURL = new URL( protocol + "://" + bootstrapString + "/");
        
            chord.join(bootstrapURL);
            this.setStatusLabel("Join done.");
            this.connectionToChordInstanceDone();
            this.continueToNextScreen();
        } catch (Exception e) {
            this.setStatusLabel(e.getMessage());
            e.printStackTrace();
            chord.leave();
        }

    }//GEN-LAST:event_connectButtonActionPerformed


    public void setStatusLabel(String status){
        this.statusLabel.setText(status);
    }

    public void connectionToChordInstanceDone(){

        this.initButton.setEnabled(false);
        this.connectButton.setEnabled(false);
        this.bootstrapTextField.setEnabled(false);
        this.localPortTextField.setEnabled(false);
        //this.chord.getEntries().setEventListener(this);
        this.setTitle(this.getTitle() + " - " + chord.getID());

        Thread t = new Thread(new Runnable(){

            public void run() {
                while(true){
                    try{
                        Thread.sleep(2000);
                        debuGreportTextArea.setText(chord.printMyEntries());
                    }catch(Exception e){}
                }
            }

        });
        t.start();

    }

    private void debuGkeyTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debuGkeyTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_debuGkeyTextFieldActionPerformed

    private void debuGinsertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debuGinsertButtonActionPerformed
        String key = this.debuGkeyTextField.getText();
        String value = this.debuGvalueTextField.getText();
        chord.insert(new MyKey(key), value);
    }//GEN-LAST:event_debuGinsertButtonActionPerformed

    private void debuGremoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debuGremoveButtonActionPerformed
        String key = this.debuGkeyTextField.getText();
        Set<Serializable> set = null;

        set = chord.retrieve(new MyKey(key));
        
        Serializable value = "";
        Iterator<Serializable> i = set.iterator();
        while(i.hasNext()){
            value = i.next();
            chord.remove(new MyKey(key), value);
        }
    }//GEN-LAST:event_debuGremoveButtonActionPerformed

    private void localPortTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_localPortTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_localPortTextFieldActionPerformed

    private void showDebugButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showDebugButtonActionPerformed

        boolean b = false;
        b = this.showDebugButton.isSelected();
        setDebugButtonsVisibility(b);
        
        
    }//GEN-LAST:event_showDebugButtonActionPerformed

    private String formatIPAddress(String ipaddress) throws Exception{

        String ip = "127.0.0.1";
        byte[] ipbytes = new byte[4];
        String port = DEFAULT_PORT;

        Pattern strMatch;
        Matcher m;



        strMatch = Pattern.compile("(.*):(.*)");
        m = strMatch.matcher(ipaddress);
        if(m.find()){
            ip = m.group(1);
            port = m.group(2); 
        }else{
            ip = ipaddress; 
        }


        try{
            strMatch = Pattern.compile("(.*)\\.(.*)\\.(.*)\\.(.*)");
            m = strMatch.matcher(ip);
            if(m.find()){
                for(int i=0; i<4; i++){
                    ipbytes[i] = (byte)Integer.parseInt(m.group(i+1));
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Cannot interpret IP address ('" + ipaddress + "'). It must have the form xxx.xxx.xxx.xxx[:port].");
        }

        System.out.println("Using IP: " + ip + ":" + port);
        return ip + ":" + port;
    }

    private void setDebugButtonsVisibility(boolean b){
        this.debuGinsertButton.setVisible(b);
        this.debuGkeyTextField.setVisible(b);
        this.debuGreportTextArea.setVisible(b);
        this.debuGremoveButton.setVisible(b);
        this.debuGvalueTextField.setVisible(b);
        this.debugKeyLabel.setVisible(b);
        this.debuGvalueLabel.setVisible(b);
        int c = (b?+220:-220);
        this.setSize(this.getWidth(), this.getHeight() + c);
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bootstrapTextField;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton debuGinsertButton;
    private javax.swing.JTextField debuGkeyTextField;
    private javax.swing.JButton debuGremoveButton;
    private javax.swing.JTextArea debuGreportTextArea;
    private javax.swing.JLabel debuGvalueLabel;
    private javax.swing.JTextField debuGvalueTextField;
    private javax.swing.JLabel debugKeyLabel;
    private javax.swing.JButton initButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField localPortTextField;
    private javax.swing.JToggleButton showDebugButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

    public void newEvent(int i, Object o) {
        this.debuGreportTextArea.setText(this.chord.printMyEntries());
    }


}
