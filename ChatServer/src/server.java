/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SOUMIK
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import java.text.MessageFormat;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
public class server extends javax.swing.JFrame {

    /**
     * Creates new form server
     */
    static boolean serv_state=false;
    static Socket sock[]=new Socket[100];                       
    static servercomm obj[]=new servercomm[100];                
    static Thread threads[]=new Thread[100];                    
    static boolean conn_state[]=new boolean[100];
    static DataInputStream dis[]=new DataInputStream[100];      
    static DataOutputStream dos[]=new DataOutputStream[100];    
    static InetAddress ipaddr[]=new InetAddress[100];           
    static String names[]=new String[100];
    static int port[]=new int[100];                             
    static int i=0;                                             
    static int c=0;
    static int a;
    static boolean stoppress=false;
    static int except=0;                                        
    static int count=0;                                         
    static boolean flag=false;                                  
    static String str;      
    public server() {
        initComponents();
        setLocationRelativeTo(null);
    }
    public class closure implements Runnable
    {
        public void run()
        {
            try
            {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        jTextArea1.append("Server Stopping.....\n");
                    }
                });
                for(a=0;a<i;a++)
                {
                    if(conn_state[a]==true)
                    {
                        dis[a].close();
                        dos[a].close();
                        sock[a].close();
                    }
                }
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        jTextArea1.append("Successfully closed all open sockets.\n");
                    }
                });
                for(a=3;a>=1;a--)
                {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            jTextArea1.append("Terminating server in "+a+" seconds.....\n");
                        }
                    });
                    Thread.sleep(1000);
                }
                System.exit(0);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public class servercomm implements Runnable
    {
        int d;
        servercomm()
        {
            d=c;
            c++;
        }
        public void run()
        {
            if(d==0)
                serve();
            else
                chat();
        }
        public void chat()
        {
            int k=i-1;
            try
            {
                while(true)
                {
                    String strt;
                    strt=dis[k].readUTF();
                    if(strt.equalsIgnoreCase("exit"))
                    {
                        dis[k].close();
                        dos[k].close();
                        conn_state[k]=false;
                        sock[k].close();
                        count--;
                        String str2;
                        str2="Client with IP Address :- "+(ipaddr[k].getHostAddress())+":"+port[k]+" and username = \""+names[k]+"\" has left the chat.";
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()   {
                                jTextArea1.append(str2+"\n");
                            }
                        }); 
                        str=names[k]+" has left the chat.";
                        flag=true;
                        write();
                        /*if(count!=0)       //When open connections = 0, the server shuts down
                            return;
                        else
                          System.exit(0);*/
                        return;
                    }
                    except=k;
                    String str2;
                    str=names[k]+" : "+strt;
                    str2=ipaddr[k].getHostAddress()+":"+port[k]+"("+names[k]+") :- "+strt;
                    System.out.println(str);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run()   {
                        jTextArea1.append(str2+"\n");
                        }
                    });
                    write();    
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                conn_state[k]=false;
                count--;
                str="Client with IP Address :- "+(ipaddr[k].getHostAddress())+":"+port[k]+" and username = \""+names[k]+"\" has left the chat.";        
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run()   {
                        jTextArea1.append(str+"\n");
                    }
                });
                flag=true;
                write();        
            }
        }
        public void write()
        {
            int c;
            for(c=0;c<i;c++)
            {
                
                if(c==except||conn_state[c]==false)     
                    continue;
                try
                {
                    dos[c].writeUTF(str);         
                    dos[c].flush();                    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        public void serve()
        {
            try
            {
                ServerSocket ss = new ServerSocket(50000);
                serv_state=true;
                while(true)
                {
                    System.out.println("Waiting");
                    sock[i]=ss.accept();
                    obj[i]=new servercomm();
                    threads[i]=new Thread(obj[i]);
                    conn_state[i]=true;
                    dis[i]=new DataInputStream(sock[i].getInputStream());
                    dos[i]=new DataOutputStream(sock[i].getOutputStream());
                    ipaddr[i]=sock[i].getInetAddress();
                    port[i]=sock[i].getPort();
                    names[i]=dis[i].readUTF();
                    System.out.println(names[i]);
                    System.out.println("Client with IP Address :- "+ipaddr[i].getHostAddress()+":"+port[i]+" connected.\n");
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run()   {
                        jTextArea1.append("Client with IP Address :- "+ipaddr[i].getHostAddress()+":"+port[i]+" and username = \""+names[i]+"\" has connected.\n");
                        }
                    });
                    count++;
                    i++;
                    threads[i-1].start();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        start = new javax.swing.JButton();
        stop = new javax.swing.JButton();
        print = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Chat Server");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onexit(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Chat Log :");

        start.setBackground(new java.awt.Color(205, 205, 255));
        start.setText("Start Server");
        start.setToolTipText("Start the server\n");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });

        stop.setBackground(new java.awt.Color(205, 205, 255));
        stop.setText("Stop Server");
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });

        print.setBackground(new java.awt.Color(205, 205, 255));
        print.setText("Print Chat Log");
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(stop, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stop)
                    .addComponent(print))
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        // TODO add your handling code here:
        if(stoppress==true)
            return;
        if(serv_state==true)
        {
            JOptionPane.showMessageDialog(this,"Server is alreay running.","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            jTextArea1.append("Server Running.....\n");
            }
        });
        servercomm obj1=new servercomm();
        Thread t1=new Thread(obj1);
        t1.start();
    }//GEN-LAST:event_startActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        // TODO add your handling code here:
        if(stoppress==true)
            return;
        MessageFormat header,footer; 
        try 
        {
            header = new MessageFormat("Chat Log :");
            footer = new MessageFormat("Page {0,number}");
            jTextArea1.print(header, footer);
        } 
        catch (java.awt.print.PrinterException e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_printActionPerformed

    private void stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopActionPerformed
        // TODO add your handling code here:
        try
        {
            stoppress=true;
            int confirm = JOptionPane.showOptionDialog(null, "Are you sure you want to stop the server?","Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0)
            {
                closure objt = new closure();
                Thread c = new Thread(objt);
                c.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_stopActionPerformed

    private void onexit(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onexit
        // TODO add your handling code here:
        try
        {
            stoppress=true;
            int confirm = JOptionPane.showOptionDialog(null, "Are you sure you want to stop the server?","Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(confirm == 0)
            {
                closure objt = new closure();
                Thread c = new Thread(objt);
                c.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_onexit
       
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton print;
    private javax.swing.JButton start;
    private javax.swing.JButton stop;
    // End of variables declaration//GEN-END:variables
}
