package ch.unizh.ini.friend.browser;


import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

/** from http://forum.java.sun.com/thread.jsp?forum=57&thread=215682 */
public class Browser extends JFrame {
    
    private JTextField enter;
    private JEditorPane contents;
    
    public Browser() {
        super("Simple Web Browser");
        Container c= getContentPane();
        
        enter=new JTextField("Enter file URL here");
        enter.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getThePage(e.getActionCommand());
            }
        } );
        c.add(enter,BorderLayout.NORTH);
        
        contents=new JEditorPane();
        contents.setEditable(false);
        contents.addHyperlinkListener(
        new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType()==
                HyperlinkEvent.EventType.ACTIVATED)
                    getThePage(e.getURL().toString());
            }
            
        });
        
        c.add(new JScrollPane(contents),BorderLayout.CENTER);
        
        setSize(400,300);
        show();
    }//end Browser
    
    private void getThePage(String location) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            contents.setPage(location);
            enter.setText(location);
        }catch (IOException io) {
            JOptionPane.showMessageDialog(this,"Error","Bad URL",
            JOptionPane.ERROR_MESSAGE);
            System.out.println("hello");
        }
        
        
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
    }//end getThePage
    
    
    public static void main(String args[]) {
        Browser b=new Browser();
        b.addWindowListener(
        new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
    }//end main
    
    
}//end public class