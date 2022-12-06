package ch.unizh.ini.friend.record;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/*
 * $Id: SpikePlotter.java,v 1.2 2003/06/15 19:17:31 tobi Exp $
 *
 * Created on June 8, 2003, 8:38 AM
 */


/**
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.2 $
 */
public class SpikePlotter extends javax.swing.JFrame implements SpikeListener {
    
    SpikeReporter reporter;
    java.awt.Point p;
    javax.swing.JPanel panel;
    java.awt.image.BufferedImage image;
    java.awt.Dimension dim=new java.awt.Dimension(400,400);
    java.awt.Graphics2D g;
    int stimSize=10, stimAspectRatio=30;
    int spikeSize=4;
    Font font;
    int spikePositionJitter=10;
    Random rand=new Random();
    SpikeLogger spikeLogger;
    
    public SpikePlotter(){
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        panel=new javax.swing.JPanel() {
            public void paintComponent(java.awt.Graphics g1){
                super.paintComponent(g1);
                java.awt.Graphics2D g2=(java.awt.Graphics2D)g1;
                if(p!=null){
                    g2.drawImage(image,0,0,this);
                    g2.setPaint(java.awt.Color.WHITE);
                    g2.fillRect(p.x-stimSize/2,p.y-stimAspectRatio*stimSize/2, stimSize, stimAspectRatio*stimSize);
                    //                    g2.fillOval(p.x-stimSize/2, p.y-stimSize/2, 30,30);
                }
            }
        };
        panel.setPreferredSize(dim);
        panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter(){
            public void mouseMoved(java.awt.event.MouseEvent e){
                p=e.getPoint();
                repaint();
            }
        });
        panel.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                if(e.getKeyChar()==' '){
                    image.getGraphics().clearRect(0,0,dim.width,dim.height);
                }
            }
        });
        panel.setFocusable(true);
        
        getContentPane().add(panel);
        //image=(BufferedImage)panel.createImage(getSize().width, getSize().height);
        image=new java.awt.image.BufferedImage(dim.width,dim.height,java.awt.image.BufferedImage.TYPE_INT_RGB);
        g=image.createGraphics();
        g.clearRect(0,0, dim.width,dim.height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        //        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        //        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        font=g.getFont();
        font=font.deriveFont(15f);
        try{
            reporter=new MicrophoneReporter();
        }catch(javax.sound.sampled.LineUnavailableException e){
            javax.swing.JOptionPane.showMessageDialog(this,e,"LineUnavailableException",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        reporter.addSpikeListener(this);
        
        try{
            File f=new File("spikes.txt");
            spikeLogger=new SpikeLogger(f, new SpikePlotter.MousePositionPrinter());
            spikeLogger.open();
            reporter.addSpikeListener(spikeLogger);
        }catch(IOException e){
            System.err.println("couldn't log spikes");
            e.printStackTrace();
        }
        
        pack();
        reporter.startReporting();
        new DimmerThread(image).start();
        show();
    }
    
    class DimmerThread extends Thread{
        BufferedImage image;
        public DimmerThread(BufferedImage image){ this.image=image;}
        public void run(){
            while(true){
                dimOp.filter(image,image);
                try{Thread.currentThread().sleep(200);}catch(InterruptedException e){}
            }
        }
    }
    
    class MousePositionPrinter {
        /** @return e.g., "35,43" mouse position x,y */
        public String toString(){
            return p.x+","+p.y;
        }
    }
    
    void exitForm(java.awt.event.WindowEvent evt){
        if(reporter!=null) reporter.stopReporting();
        try{
            if(spikeLogger!=null) spikeLogger.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.exit(0);
    }
    
    public static void main(String[] args){
        new SpikePlotter();
    }
    
    long startTime=System.currentTimeMillis();
    //    String[] familyNames=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    //    {
    //        for(int i=0;i<familyNames.length;i++){
    //            System.out.println(familyNames[i]);
    //        }
    //    }
    
    
    RescaleOp dimOp=new RescaleOp(.9f, 0, null);
    
    public int jitter(){
        return (int)((rand.nextFloat()-.5)*spikePositionJitter);
    }
    
    /** called by spike source ({@link SpikeReporter}) when a spike is detected
     *      @param e the spike event
     *
     */
    public void spikeOccurred(SpikeEvent e) {
        int bright=64,alpha=128;
        if(p!=null) {
            g.setPaint(new Color(bright,bright,bright));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
            g.setStroke(new java.awt.BasicStroke(1f));
            g.drawRect(p.x-spikeSize/2+jitter(),p.y-spikeSize/2+jitter(), spikeSize,spikeSize);
            //g.fillOval(p.x-stimSize/2+((int)(jitter*rand.nextFloat()-.5)),p.y-stimSize/2+((int)(jitter*rand.nextFloat()-.5)),stimSize,stimSize);
            //            g.setFont(font);
            //            g.drawString(Long.toString(e.getTime()),p.x,p.y);
            repaint();
        }
    }
    
}

/*
 *$Log: SpikePlotter.java,v $
 *Revision 1.2  2003/06/15 19:17:31  tobi
 *added capability of recording spikes from simulation or from microphone and plotting the
 *corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 *kind of a spike-tirggered average is possible.
 *
 *Revision 1.1  2003/06/12 06:28:47  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
