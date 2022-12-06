package ch.unizh.ini.friend.gui;

/*
 * SplashScreen.java
 *
 * Created on October 25, 2002, 9:10 AM
 * $Id: SplashScreen.java,v 1.2 2003/05/10 17:27:43 jgyger Exp $
 *
 * <hr>
 * <pre>
 * $Log: SplashScreen.java,v $
 * Revision 1.2  2003/05/10 17:27:43  jgyger
 * Merge from color-branch
 *
 * Revision 1.1.2.1  2003/05/05 10:48:45  tobi
 * initial version
 *
 * </pre>
 * @author $Author: jgyger $
 */

import java.awt.event.*;
import java.awt.*;

/**
 Shows a splash screen image.
 From java developer connection <a href="http://developer.java.sun.com/developer/qow/archive/24/index.html">http://developer.java.sun.com/developer/qow/archive/24/index.html</a>
 
 
 @author paternostro, JDC member.
 */

public class SplashScreen {
    
    public static void main(String args[]) {
        new SplashWindowFrame();
    }
}

class SplashWindowFrame extends Frame {
    SplashWindow sw;
    Image splashIm;
    
    SplashWindowFrame() {
        super();
        
        /* Add the window listener */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                dispose();
                System.exit(0);
            }});
            
            /* Size the frame */
            setSize(200,200);
            
            /* Center the frame */
            Dimension screenDim =
            Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle frameDim = getBounds();
            setLocation((screenDim.width - frameDim.width) / 2,
            (screenDim.height - frameDim.height) / 2);
            
            MediaTracker mt = new MediaTracker(this);
            splashIm = Toolkit.getDefaultToolkit(
            ).getImage("splash.gif");
            mt.addImage(splashIm,0);
            try {
                mt.waitForID(0);
            } catch(InterruptedException ie){}
            
            sw = new SplashWindow(this,splashIm);
            
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ie){}
            
            sw.dispose();
            
            /* Show the frame */
            setVisible(true);
    }
}

class SplashWindow extends Window {
    Image splashIm;
    
    SplashWindow(Frame parent, Image splashIm) {
        super(parent);
        this.splashIm = splashIm;
        setSize(200,200);
        
        /* Center the window */
        Dimension screenDim =
        Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winDim = getBounds();
        setLocation((screenDim.width - winDim.width) / 2,
        (screenDim.height - winDim.height) / 2);
        setVisible(true);
    }
    
    public void paint(Graphics g) {
        if (splashIm != null) {
            g.drawImage(splashIm,0,0,this);
        }
    }
}


