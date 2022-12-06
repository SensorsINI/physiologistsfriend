/*
  $Id: TangentScreen.java,v 1.36 2004/11/12 13:37:12 tobi Exp $
 
 
 Copyright 2002 Institute of Neuroinformatics, University and ETH Zurich, Switzerland
 
 This file is part of The Physiologist's Friend.
 
 The Physiologist's Friend is free software; you can redistribute it
 and/or modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2 of
 the License, or (at your option) any later version.
 
 The Physiologist's Friend is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with The Physiologist's Friend; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Created on September 14, 2002, 3:50 AM
 */

package ch.unizh.ini.friend.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Iterator;


import ch.unizh.ini.friend.graphics.ConvexPolygon;
import ch.unizh.ini.friend.graphics.ShapeList;
import ch.unizh.ini.friend.record.SpikeEvent;
import ch.unizh.ini.friend.record.SpikeListener;
import ch.unizh.ini.friend.simulation.SimulationSetup;
import ch.unizh.ini.friend.stimulus.ColorStimulus;
import ch.unizh.ini.friend.stimulus.ColorStimulusImpl;
import ch.unizh.ini.friend.stimulus.ConcreteStimulus;
import ch.unizh.ini.friend.stimulus.Stimulus;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.LinkedList;
import javax.swing.JViewport;
import javax.swing.RepaintManager;


/**
 *
 * Represents the plotting "tangent screen" -- the screen on which  experimentalists display stimuli to characterize or measure cell responses.
 * <p>
 * The stimulus appears in this tangent screen.  You set the stimulus using {@link #setStimulus}. You set the simulation using {@link #setSimulationSetup}.
 *
 * <p>
 * The {@link ch.unizh.ini.friend.stimulus.Stimulus} that is shown in the TangentScreen is scaled to the size of the screen to maintain an aspect
 * ratio of 1:1.  The minimum dimension of the TangentScrren sets the scaling of both x and y coordinates of the stimulus.
 * <p>
 * This component manages the popup menu for the selection of stimulus color, rotation, etc. It also handles the mouse wheel actions on the stimulus.
 *
 *
 * @author  tobi
 * @version $Revision: 1.36 $
 *
 */
public class TangentScreen extends javax.swing.JPanel implements SpikeListener {
    
    /** Dimension of tangent screen in our user (stimulus) coordinates.
     * Coordinates start at 0,0 in center of
     * screen and increase upwards and to right.
     * Limits are -SCREEN_DIMENSION and +SCREEN_DIMENSION in both x and y.
     * The value of <code>SCREEN_DIMENSION</code> is {@value}.
     * <p>
     * Even if the Panel is resized to a non-square size, the stimulus aspect ratio 1:1 is maintained.
     */
    public static final float SCREEN_DIMENSION=10f;
    
    /**
     * Color-chooser to select the forground and background color of
     * the (color) stimulus.
     */
    protected ColorChooser colorChooser = new ColorChooser();
    
    
    /** Creates new form TangentScreen.
     * @param simulationSetup the simulationSetup passed to FriendGUI
     */
    public TangentScreen(SimulationSetup simulationSetup) {
        initComponents();
        setRequestFocusEnabled(true); // component can request focus
        requestFocus();
        setFocusable(true); // must set this to receive key presses. this is 1.4+ method.
        setOpaque(true);
        RepaintManager.currentManager(this).setDoubleBufferingEnabled(true);
        // only for 1.4+
        addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e){
                mouseWheelRotation(e);
            }
        });
        
        setSimulationSetup(simulationSetup);
        
    }
    
    /** sets the {@link ch.unizh.ini.friend.simulation.SimulationSetup}
     * @param setup the {@link ch.unizh.ini.friend.simulation.SimulationSetup}
     */
    public void setSimulationSetup(SimulationSetup setup){
        this.simulationSetup=setup;
        setStimulus(simulationSetup.getStimulus());
    }
    
    private SimulationSetup simulationSetup;
    
    private Stimulus stimulus;
    
    /** set the stimulus displayed. This method also disables and enables the foreground and background brightness menu items
     * when the {@link ch.unizh.ini.friend.stimulus.Stimulus} is and is not an instance of {@link ch.unizh.ini.friend.stimulus.ColorStimulus}.
     * @param s the Stimulus
     */
    public void setStimulus(Stimulus s){
        stimulus = s;
        if (!(stimulus instanceof ColorStimulus)) {
            foregroundMenuItem.setVisible(false);
            backgroundMenuItem.setVisible(false);
        }else{
            foregroundMenuItem.setVisible(true);
            backgroundMenuItem.setVisible(true);
        }
    }
    
    /** get the displayed stimulus
     * @return the Stimulus
     */
    public Stimulus getStimulus(){ return stimulus;}
    
    //    private SeparateTransforms stimulus.getTransforms();
    //    /** set the stimulus.getTransforms() to the stimulus shape */
    //    public void setStimulusTransform(SeparateTransforms t){
    //        stimulus.getTransforms()=t;
    //    }
    //    /** get the stimulus stimulus.getTransforms() */
    //    public SeparateTransforms getStimulusTransform(){
    //        return stimulus.getTransforms();
    //    }
    
    private boolean showPhotoreceptorsEnabled;
    /** enable showing the locations of the photoreceptors */
    public void setPhotoreceptorsShown(boolean flag){showPhotoreceptorsEnabled=flag;}
    /** are photoreceptor locations being shown? */
    public boolean isPhotoreceptorsShown(){ return showPhotoreceptorsEnabled; }
    
    
    // only for 1.4+
    private void mouseWheelRotation(MouseWheelEvent e){
        int mod=e.getModifiersEx();
        if((mod&MouseWheelEvent.CTRL_DOWN_MASK)==MouseWheelEvent.CTRL_DOWN_MASK){
            if(e.getWheelRotation()>0)
                stimulus.expand();
            else
                stimulus.shrink();
        }else if((mod&MouseWheelEvent.SHIFT_DOWN_MASK)==MouseWheelEvent.SHIFT_DOWN_MASK){
            if(e.getWheelRotation()>0)
                stimulus.lengthen();
            else
                stimulus.shorten();
        }else{
            stimulus.rotate(e.getWheelRotation());
        }
        repaint(100);
    }
    
    private boolean stimulusControlEnabled=false;
    /** set mouse/keyboard control of stimulus enabled */
    public void setStimulusControlEnabled(boolean f){
        stimulusControlEnabled=f;
        if(f){
            //            addMouseMotionListener();
            //            enableEvents( MouseEvent.MOUSE_MOTION_EVENT_MASK );
        }
    }
    /** is mouse/keyboard control of stimulus enabled? */
    public boolean isStimulusControlEnabled(){return stimulusControlEnabled;}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        stimulusControlPopupMenu = new javax.swing.JPopupMenu();
        rotateMenuItem = new javax.swing.JMenuItem();
        foregroundMenuItem = new javax.swing.JMenuItem();
        backgroundMenuItem = new javax.swing.JMenuItem();
        brightenMenuItem = new javax.swing.JMenuItem();
        darkenMenuItem = new javax.swing.JMenuItem();
        brightenBackgroundMenuItem = new javax.swing.JMenuItem();
        darkenBackgroundMenuItem = new javax.swing.JMenuItem();
        invertContrastMenuItem = new javax.swing.JMenuItem();

        stimulusControlPopupMenu.setFont(new java.awt.Font("Dialog", 0, 10));
        stimulusControlPopupMenu.setToolTipText("Controls the stimulus");
        stimulusControlPopupMenu.setLabel("Stimulus");
        rotateMenuItem.setMnemonic('r');
        rotateMenuItem.setText("Rotate (r)");
        rotateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(rotateMenuItem);

        foregroundMenuItem.setText("Foreground Color...");
        foregroundMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foregroundMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(foregroundMenuItem);

        backgroundMenuItem.setText("Background Color...");
        backgroundMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(backgroundMenuItem);

        brightenMenuItem.setMnemonic('b');
        brightenMenuItem.setText("Brighten stimulus (b)");
        brightenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brightenMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(brightenMenuItem);

        darkenMenuItem.setMnemonic('d');
        darkenMenuItem.setText("Darken stimulus (d)");
        darkenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                darkenMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(darkenMenuItem);

        brightenBackgroundMenuItem.setText("Brighten background (B)");
        brightenBackgroundMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brightenBackgroundMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(brightenBackgroundMenuItem);

        darkenBackgroundMenuItem.setText("Darken background (D)");
        darkenBackgroundMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                darkenBackgroundMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(darkenBackgroundMenuItem);

        invertContrastMenuItem.setMnemonic('i');
        invertContrastMenuItem.setText("Invert contrast (i)");
        invertContrastMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertContrastMenuItemActionPerformed(evt);
            }
        });

        stimulusControlPopupMenu.add(invertContrastMenuItem);

        setLayout(new java.awt.BorderLayout());

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

    }//GEN-END:initComponents
    
    private void backgroundMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backgroundMenuItemActionPerformed
        ColorStimulus cs = (ColorStimulus) stimulus;
        Color newColor =
        colorChooser.showDialog(this, "Choose background color",
        cs.getBackground());
        if (newColor != null)
            cs.setBackground(newColor);
        repaint();
    }//GEN-LAST:event_backgroundMenuItemActionPerformed
    
    private void foregroundMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foregroundMenuItemActionPerformed
        ColorStimulus cs = (ColorStimulus) stimulus;
        Color newColor =
        colorChooser.showDialog(this, "Choose foreground color",
        cs.getForeground());
        if (newColor != null)
            cs.setForeground(newColor);
        repaint();
    }//GEN-LAST:event_foregroundMenuItemActionPerformed
    
    private void invertContrastMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertContrastMenuItemActionPerformed
        stimulus.flipContrast();
        repaint();
    }//GEN-LAST:event_invertContrastMenuItemActionPerformed
    
    private void darkenBackgroundMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_darkenBackgroundMenuItemActionPerformed
        stimulus.darkenBackground();
        repaint();
    }//GEN-LAST:event_darkenBackgroundMenuItemActionPerformed
    
    private void brightenBackgroundMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brightenBackgroundMenuItemActionPerformed
        stimulus.brightenBackground();
        repaint();
    }//GEN-LAST:event_brightenBackgroundMenuItemActionPerformed
    
    private void darkenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_darkenMenuItemActionPerformed
        stimulus.darkenForeground();
        repaint();
    }//GEN-LAST:event_darkenMenuItemActionPerformed
    
    private void brightenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brightenMenuItemActionPerformed
        stimulus.brightenForeground();
        repaint();
    }//GEN-LAST:event_brightenMenuItemActionPerformed
    
    private void rotateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateMenuItemActionPerformed
        stimulus.getTransforms().rotate((float)Math.PI/6);
        repaint();
    }//GEN-LAST:event_rotateMenuItemActionPerformed
    
    // mouse presses.
    // we don't use getButton becaduse that is 1.4+ feature.
    // instead, to detect which button is pressed (so that button 1 can hide the stimulus)
    // we use InputEvent button masks.
    // button 1. if the stimulus was visible, make it invisible.
    // button 2/3 shows popup window for stimulus control
    
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if(evt.isPopupTrigger()){ // must check release as well as press for cross platform compatibility
            //System.err.println("relaes evt = " + evt );
            stimulusControlPopupMenu.show(this,evt.getX(), evt.getY());
            //            stimulusControlPopupMenu.setLocation(evt.getPoint());
            //            stimulusControlPopupMenu.setVisible(true);
        }else {
            stimulus.setVisible(true);
            repaint();
        }
    }//GEN-LAST:event_formMouseReleased
    
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if(evt.isPopupTrigger()){
            //System.err.println("press evt = " + evt );
            stimulusControlPopupMenu.show(this,evt.getX(), evt.getY());
            //            stimulusControlPopupMenu.setLocation(evt.getPoint());
            //            stimulusControlPopupMenu.setVisible(true);
        }else if(stimulus.isVisible() && ( (evt.getModifiers()&java.awt.event.InputEvent.BUTTON1_MASK)==InputEvent.BUTTON1_MASK) ) {
            stimulus.setVisible(false);
            repaint();
        }
        requestFocus();
    }//GEN-LAST:event_formMousePressed
    
    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }//GEN-LAST:event_formMouseEntered
    
    //    //        System.out.println("key typed");
    //        //        System.out.println("evt is "+evt);
    //        Stimulus s=stimulus;
    //        switch(evt.getKeyChar()){
    //            case 'b':
    //                s.brightenForeground();
    //                break;
    //            case 'd':
    //                s.darkenForeground();
    //                break;
    //
    //            case 'B':
    //                s.brightenBackground();
    //                break;
    //            case 'D':
    //                s.darkenBackground();
    //                break;
    //            case ' ':
    //                s.setVisible(!s.isVisible());
    //                break;
    //
    //        }
    //        repaint();
    
    
    /** compute transform from stimulus coordinates ((0,0) in center,
     * {@link #SCREEN_DIMENSION} units total in x and y)
     * to actual screen pixels in Java graphics coordinates.
     * Applying this transform takes you from stimulus coordinates to screen pixel coordinates.
     * <p>
     * This scaling maintains aspect ratio 1:1 by chosing the scaling
     * to scale to the miniumum screen dimension (min(wdith,height)).
     *
     * @see #transformFromScreenToStimulus
     */
    private AffineTransform transformFromStimulusToScreen(){
        float size = TangentScreen.SCREEN_DIMENSION;
        //float height = TangentScreen.SCREEN_HEIGHT;  // the units of stimulus coordinates.
        
        Dimension d = getSize();  // get size of tangent screen
        int minDim=(int)Math.min(d.width,d.height);
        
        // compute scaling to transform stimulus to min screen dimension
        double scaling=minDim/size; // scales 1:1 aspect ratio so that all of stimulus space fits on screen
        
        AffineTransform toScreen=new AffineTransform();
        
        // now compute offset x or y depending on whether screen is wider than high or vice versa
        // if screen is wider than high, then we must offset transform onto screen by difference
        if(d.width>d.height){
            toScreen.concatenate(AffineTransform.getTranslateInstance((d.width-d.height)/2,0));
        }else{
            toScreen.concatenate(AffineTransform.getTranslateInstance(0,(d.height-d.width)/2));
        }
        
        // compute stimulus.getTransforms() from stimulus coordinates to screen coordinates
        // note y flip
        toScreen.concatenate(AffineTransform.getScaleInstance(scaling, -scaling));
        // now reference 0,0 at center of screen
        toScreen.concatenate(AffineTransform.getTranslateInstance(size/2.0f, -size/2.0f));
        
        
        return toScreen;
    }
    
    /** compute stimulus.getTransforms() from screen (Java) coordinates to stimulus coordinates.
     * This stimulus.getTransforms() maintains aspect ratio 1:1 by chosing the minimum screen dimension for scaling.
     * @see #transformFromStimulusToScreen
     */
    private AffineTransform transformFromScreenToStimulus(){
        float width = TangentScreen.SCREEN_DIMENSION;
        //float height = TangentScreen.SCREEN_HEIGHT;  // the units of stimulus coordinates.
        
        Dimension d = getSize();  // get size of tangent screen
        double scaling=width/Math.min(d.width,d.height); // this assumes TangentScreen.WIDTH and HEIGHT are the same....
        
        // compute stimulus.getTransforms() from stimulus coordinates to screen coordinates
        AffineTransform toStimulus = AffineTransform.getScaleInstance(scaling, -scaling); // scales down and flips y
        toStimulus.preConcatenate(AffineTransform.getTranslateInstance(-width/2.0f, width/2.0f)); // translates so mouse 0,0 is stimulus -width/2, height/2
        // we need to preConcatenate because we want the translation performed after the scaling.  this is non-intuitive to me....
        //        System.out.println("toStimulus= "+toStimulus);
        return toStimulus;
    }
    
    // the last mouse transform.  this is computed by formMouseMoved and used in paintComponent
    private AffineTransform mouseTransform=new AffineTransform();
    
    // the last mouse position in Java2D coordinates (pixels from UL corner)
    private Point mousePosition=null;
    
    // comes here when mouse moves
    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if(getStimulus()==null) return;
        mousePosition=evt.getPoint(); // where mouse is now, in Java graphics coordinates (pixels from UL corner)
        //System.out.println("mouse moved to Java coordinates "+p.x+","+p.y);
        
        // now compute the appropriate stimulus.getTransforms() to apply to the stimulus polygon
        // we need to translate the java mouse coordinates to the stimulus frame and apply the correct translation
        
        Dimension d=getSize(); // the panel size in pixels
        
        // compute the miniumum screen dimension
        float size=Math.min(d.width,d.height);
        // compute scaling that transforms
        float scaling=size/Math.min(d.width,d.height);
        
        // translate mouse p to 0,0, in center
        Point p=(Point)mousePosition.clone(); // clone it so we don't transform mousePosition
        p.x=p.x-d.width/2;
        p.y=p.y-d.height/2;
        // scale to "stimulus" coordinates
        float px2=(float)p.x*(SCREEN_DIMENSION/size);
        float py2=(float)p.y*(-SCREEN_DIMENSION/size);
        //        System.err.println("px2 = " + px2 );
        //        System.err.println("py2 = " + py2 );
        
        
        // translateTo doesn't seem to work here
        //        stimulus.getTransforms().translateTo(px2,py2);
        
        // get a translation transform in stimulus coordinates to the stimulus-units mouse position
        mouseTransform=AffineTransform.getTranslateInstance(px2,py2);
        //System.out.println("stimulus.getTransforms() to stimulus is "+mouseTransform);
        repaint();  // call for a repaint to update picture
    }//GEN-LAST:event_formMouseMoved
    
    
    /** resize this TangentScreen to fill the parent container */
    public void refillFrame(){
        revalidate();
        spikeImage=null; // to force recreation
        //setSize(getPreferredSize());
        repaint();
    }
    
    // this is undone attempt to make graphics smoother
    //    Image offScreenBuffer;  // used for double buffering to prevent flicker
    //
    //    // from http://developer.java.sun.com/developer/technicalArticles/Interviews/DoubleBuffering/
    //    public void update(Graphics g) {
    //        Graphics gr;
    //        System.out.println("graphics update()");
    //        // Will hold the graphics context from the offScreenBuffer.
    //        // We need to make sure we keep our offscreen buffer the same size
    //        // as the graphics context we're working with.
    //        if (offScreenBuffer==null ||
    //        (! (offScreenBuffer.getWidth(this) == this.getSize().width
    //        && offScreenBuffer.getHeight(this) == this.getSize().height))) {
    //            offScreenBuffer = this.createImage(getSize().width, getSize().height);
    //        }
    //
    //        // We need to use our buffer Image as a Graphics object:
    //        gr = offScreenBuffer.getGraphics();
    //
    //        paintComponent(gr); // Passes our off-screen buffer to our paint method, which,
    //        // unsuspecting, paints on it just as it would on the Graphics
    //        // passed by the browser or applet viewer.
    //        g.drawImage(offScreenBuffer, 0, 0, this);
    //        // And now we transfer the info in the buffer onto the
    //        // graphics context we got from the browser in one smooth motion.
    //    }
    
    /**
     * Draws the background and stimulus in the tangent screen. You don't call this, it is called by the graphics pipeline automagically after
     * <code>repaint()</code>.
     */
    public void paintComponent(Graphics g) {
        //System.out.println("graphics paint()");
        //assert stimulus.getTransforms()!=null ;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // set rendering hints for speed
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); // antialiasing off for speed
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED); // antialiasing off for speed
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE); // antialiasing off for speed
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED); // antialiasing off for speed
        
        if(getStimulus()==null) return;  // not initialized yet
        
        // set rendering hints for speed
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); // antialiasing off for speed
        
        //        // fill background of screen
        Color c;
        if (stimulus instanceof ColorStimulus) {
            c = ((ColorStimulus) stimulus).getBackground();
        } else {
            float brightness=getStimulus().getBackgroundExcitationDensity();
            c=new Color(brightness,brightness,brightness);
        }
        g2.setPaint(c);
        g2.setBackground(c);
        Dimension d=getSize();  // get size of panel
//        g2.fillRect(0, 0, d.width, d.height);
        g2.clearRect(0, 0, d.width, d.height);
        
        // set stimulus.getTransforms() so all coordinates are scaled to screen size
        g2.setTransform(transformFromStimulusToScreen());
        
        // show photoreceptor shapes if so selected
        if(isPhotoreceptorsShown()){
            g2.setPaint(Color.red);
            g2.setStroke(new BasicStroke(.05f));
            
            Iterator li = simulationSetup.getReceptorShapes().listIterator();
            while (li.hasNext()) {
                Object cp = li.next();
                //assert cp instanceof ConvexPolygon;
                //System.out.println("receptor shape="+(ConvexPolygon)cp);
                g2.draw((ConvexPolygon)cp);
            }
        }
        
        // draw the spike triggered stimuli (if recording spikes)
        // draw this first so stimulus is on top
        spikeTriggeredStimuli.draw(g2);

        // draw the markers the user has placed
        markers.draw(g2);
        
        // set paint for the stimulus
        if (stimulus instanceof ColorStimulus) {
            c = ((ColorStimulus) stimulus).getForeground();
        } else {
            float brightness = getStimulus().getForegroundExcitationDensity();
            c = new Color(brightness,brightness,brightness);
        }
        g2.setPaint(c);
        
        //System.out.println("Stimulus was "+stimulus);
        stimulus.getTransforms().setTranslation(mouseTransform); // set up the translation of the stimulus.
        
        ShapeList l=(ShapeList)stimulus.getTransformedShapes();
        //System.out.println("rendering stimulus "+stimulus+" with transformed shapelist "+l);
        for(Iterator i=l.iterator();i.hasNext();){
            Shape s=(Shape)i.next();
            s.getBounds2D();
            g2.fill(s);
        }
        
        
        //        if(spikeImage!=null){
        //            AffineTransform t=g2.getTransform();
        //            g2.setTransform(new AffineTransform()); // push the transform and set to identity for drawing image
        //            // sets alpha here so that transparent spike image appears to overlay stimulus and photoreceptors
        //            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
        //            g2.drawImage(spikeImage,0,0,null);
        //            g2.setTransform(t); // get back transform
        //            //            System.out.println("tangent screen: rendered "+spikeImage);
        //        }
        
    }
    
    /**@param n the number of spikes to display when recording */
    public void setSpikeMemoryDepth(int n){
        spikeTriggeredStimuli.maxSpikes=n;
    }
    
    /**@return the number of spikes to display when recording */
    public int getSpikeMemoryDepth(){
        return spikeTriggeredStimuli.maxSpikes;
    }
    
    /** @param fade enables fading of spike-triggered stimuli */
    public void setSpikeFadeEnabled(boolean fade){
        spikeTriggeredStimuli.fadeEnabled=fade;
    }
    
    /** @return spike-triggered stimuli fading enabled */
    public boolean isSpikeFadeEnabled(){
        return spikeTriggeredStimuli.fadeEnabled;
    }
    
    /** @return spike-triggered stimuli are filled in */
    public boolean isSpikeFillEnabled(){
        return spikeTriggeredStimuli.fillEnabled;
    }
    
    /** @param f filling in spike-triggered stimuli */
    public void setSpikeFillEnabled(boolean f){ spikeTriggeredStimuli.fillEnabled=f;}
    
    SpikeTriggeredStimuli spikeTriggeredStimuli=new SpikeTriggeredStimuli();
    
    /** a list of stimuli, used for rendering spike-triggered display */
    class SpikeTriggeredStimuli extends LinkedList {
        
        int maxSpikes=30;
        boolean fadeEnabled=true;
        boolean fillEnabled=false;
        
        /** constructs a new instance */
        SpikeTriggeredStimuli(){
            //            ensureCapacity(maxSpikes);
        }
        
        /** renders the spike-triggering stimuli assuming the graphics context is already set for the correct transformations */
        synchronized void draw(Graphics2D spikeGraphics){
            if(size()==0) return;
            //            Graphics2D spikeGraphics=(Graphics2D)getGraphics();
            //           AffineTransform at=spikeGraphics.getTransform(); // save transform
            //            spikeGraphics.setTransform(transformFromStimulusToScreen());
            spikeGraphics.setStroke(new BasicStroke(0.01f));
            //        spikeGraphics.drawOval(mousePosition.x-1,mousePosition.y-1,2,2);
            
            Iterator k=iterator();
            int age=size(); // starts with number of spikes
            while(k.hasNext()){
                Stimulus stimulus=(Stimulus)k.next();
                // set paint for the stimulus
                Color c=null, bg=null;
                if (stimulus instanceof ColorStimulus) {
                    c = ((ColorStimulus) stimulus).getForeground();
                    bg= ((ColorStimulus) stimulus).getBackground();
                } else {
                    float brightness = stimulus.getForegroundExcitationDensity();
                    c = new Color(brightness,brightness,brightness);
                    brightness=stimulus.getBackgroundExcitationDensity();
                    bg = new Color(brightness,brightness,brightness);
                }
                
                if(fadeEnabled){
                    // oldest spike is faded to background, newest is drawn with foreground
                    float oldness=((float)age)/size(); // 1 for oldest
                    float newness=1-oldness;
                    
                    float[] f=c.getRGBColorComponents(null); // foreground components
                    float[] b=bg.getRGBColorComponents(null); // bg components
                    f[0]=(newness*f[0]+oldness*b[0]);
                    f[1]=(newness*f[1]+oldness*b[1]);
                    f[2]=(newness*f[2]+oldness*b[2]);
                    c=new Color(f[0],f[1],f[2]);
                }
                spikeGraphics.setPaint(c);
                
                // alpha composite drawing is very slow
                //                if(fadeEnabled){
                ////                    spikeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f)); // very slow...
                //                }else{
                //                    spikeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                //                }
                
                // draw the outline of the stimulus
                ShapeList l=(ShapeList)stimulus.getShapes();
                //System.out.println("rendering stimulus "+stimulus+" with transformed shapelist "+l);
                for(Iterator i=l.iterator();i.hasNext();){
                    Shape s=(Shape)i.next();
                    if(fillEnabled) spikeGraphics.fill(s);
                    else spikeGraphics.draw(s);
                }
                
                age--; // decrement age counter, starts with oldest=size()
            } // spike
            //            spikeGraphics.setTransform(at); // back to pushed transform
        }
        
        /** adds a stimulus to the list, removing the earliest if list has grown past {@link #setSpikeMemoryDepth size of buffer}
         * @param s the stimulus that was showing during the spike
         */
        synchronized void add(Stimulus s){
            //            ShapeList l=(ShapeList)stimulus.getTransformedShapes();
            //            super.add(l.clone());
            Stimulus s2=null;
            if(s instanceof ConcreteStimulus){
                s2=(ConcreteStimulus)((ConcreteStimulus)s).clone();
            }else if(s instanceof ColorStimulusImpl){
                s2=(ColorStimulusImpl)((ColorStimulusImpl)s).clone();
            }else{
                System.err.println("SpikeTriggeredStimulus: tried to add non stimulus "+s);
            }
            
            super.addLast(s2);
            if(size()>maxSpikes){
                removeFirst();
            }
        }
        
        /** synchronized version of super.clear() */
        synchronized public void clear(){
            super.clear();
        }
        
        
    }
    
    /** add a marker to the tangent screen */
    public void addMarker(){
        markers.add(stimulus);
    }
    
    /** clear all markers */
    public void clearMarkers(){
        markers.clear();
    }
    
    /** the markers that the user has placed */
    Markers markers=new Markers();
    
    /** a list of markers, used for marking borders of responses */
    class Markers extends LinkedList {
        
        int maxSpikes=30;
        boolean fadeEnabled=false;
        boolean fillEnabled=false;
        
        /** constructs a new instance */
        Markers(){
            //            ensureCapacity(maxSpikes);
        }
        
        /** renders the spike-triggering stimuli assuming the graphics context is already set for the correct transformations */
        synchronized void draw(Graphics2D spikeGraphics){
            if(size()==0) return;
            //            Graphics2D spikeGraphics=(Graphics2D)getGraphics();
            //           AffineTransform at=spikeGraphics.getTransform(); // save transform
            //            spikeGraphics.setTransform(transformFromStimulusToScreen());
            spikeGraphics.setStroke(new BasicStroke(0.01f));
            //        spikeGraphics.drawOval(mousePosition.x-1,mousePosition.y-1,2,2);
            
            Iterator k=iterator();
            int age=size(); // starts with number of spikes
            while(k.hasNext()){
                Stimulus stimulus=(Stimulus)k.next();
                // set paint for the stimulus
                Color c=null, bg=null;
                if (stimulus instanceof ColorStimulus) {
                    c = ((ColorStimulus) stimulus).getForeground();
                    bg= ((ColorStimulus) stimulus).getBackground();
                } else {
                    float brightness = stimulus.getForegroundExcitationDensity();
                    c = new Color(brightness,brightness,brightness);
                    brightness=stimulus.getBackgroundExcitationDensity();
                    bg = new Color(brightness,brightness,brightness);
                }
                
                if(fadeEnabled){
                    // oldest spike is faded to background, newest is drawn with foreground
                    float oldness=((float)age)/size(); // 1 for oldest
                    float newness=1-oldness;
                    
                    float[] f=c.getRGBColorComponents(null); // foreground components
                    float[] b=bg.getRGBColorComponents(null); // bg components
                    f[0]=(newness*f[0]+oldness*b[0]);
                    f[1]=(newness*f[1]+oldness*b[1]);
                    f[2]=(newness*f[2]+oldness*b[2]);
                    c=new Color(f[0],f[1],f[2]);
                }
                spikeGraphics.setPaint(c);
                
                // alpha composite drawing is very slow
                //                if(fadeEnabled){
                ////                    spikeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f)); // very slow...
                //                }else{
                //                    spikeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                //                }
                
                // draw the outline of the stimulus
                ShapeList l=(ShapeList)stimulus.getShapes();
                //System.out.println("rendering stimulus "+stimulus+" with transformed shapelist "+l);
                for(Iterator i=l.iterator();i.hasNext();){
                    Shape s=(Shape)i.next();
                    if(fillEnabled) spikeGraphics.fill(s);
                    else spikeGraphics.draw(s);
                }
                
                age--; // decrement age counter, starts with oldest=size()
            } // spike
            //            spikeGraphics.setTransform(at); // back to pushed transform
        }
        
        /** adds a stimulus to the list, removing the earliest if list has grown past {@link #setSpikeMemoryDepth size of buffer}
         * @param s the stimulus that was showing during the spike
         */
        synchronized void add(Stimulus s){
//            System.out.println("adding marker "+s);
            //            ShapeList l=(ShapeList)stimulus.getTransformedShapes();
            //            super.add(l.clone());
            Stimulus s2=null;
            if(s instanceof ConcreteStimulus){
                s2=(ConcreteStimulus)((ConcreteStimulus)s).clone();
            }else if(s instanceof ColorStimulusImpl){
                s2=(ColorStimulusImpl)((ColorStimulusImpl)s).clone();
            }else{
                System.err.println("SpikeTriggeredStimulus: tried to add non stimulus "+s);
            }
            
            super.addLast(s2);
            if(size()>maxSpikes){
                removeFirst();
            }
        }
        
        /** synchronized version of super.clear() */
        synchronized public void clear(){
            super.clear();
        }
        
        
    }
    
    private Rectangle2D dirty=new Rectangle2D.Float();
    
    // for rendering spikes that have occured
    private Image spikeImage;
    private Graphics2D spikeGraphics;
    private JViewport viewport;
    
    /** @deprecated */
    private void createSpikeImage(){
        Dimension dim=getSize();
        // make a buffered image to draw stimulus shapes into offscreen every time there is a recorded spike
        // image is ARGB with initial color all black with 0 alpha, so that when image is drawn into tangent screen panel,
        // it is transparent except where spikes are
        spikeImage=new java.awt.image.BufferedImage(dim.width,dim.height,java.awt.image.BufferedImage.TYPE_INT_ARGB);
        spikeGraphics=((BufferedImage)spikeImage).createGraphics();
        spikeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        spikeGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        //        System.out.println("making spikeImage");
        spikeGraphics.setColor(new Color(0,0,0,0));
        spikeGraphics.setBackground(new Color(0,0,0,0)); // transparent black
        spikeGraphics.fillRect(0,0, getSize().width, getSize().height);
        //        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        //        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        //        System.out.println("tangent screen: created spike image "+spikeImage+" with graphics "+spikeGraphics);
    }
    
    /** @deprecated */
    private void createVolatileSpikeImage(){
        Dimension dim=getSize();
        //        System.out.println("available accelerated graphics memory="+getGraphics().getAvailableAcceleratedMemory());
        spikeImage=createVolatileImage(dim.width,dim.height);
        System.out.println("created volatile image "+spikeImage);
        System.out.println("has");
        spikeGraphics=((VolatileImage)spikeImage).createGraphics();
        spikeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        spikeGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        //        System.out.println("making spikeImage");
        spikeGraphics.setColor(new Color(0,0,0,0));
        spikeGraphics.fillRect(0,0, getSize().width, getSize().height);
    }
    
    /** called by spike source ({@link ch.unizh.ini.friend.record.SpikeReporter}) when a spike is detected
     *      @param e the spike event
     *
     */
    public void spikeOccurred(SpikeEvent e) {
        spikeTriggeredStimuli.add(stimulus);
        //
        //        if(spikeImage==null) createSpikeImage();
        //        AffineTransform at=spikeGraphics.getTransform(); // save transform
        //        spikeGraphics.setTransform(transformFromStimulusToScreen());
        //        //        spikeGraphics.drawOval(mousePosition.x-1,mousePosition.y-1,2,2);
        //
        //        // set paint for the stimulus
        //        Color c=null;
        //        if (stimulus instanceof ColorStimulus) {
        //            c = ((ColorStimulus) stimulus).getForeground();
        //        } else {
        //            float brightness = getStimulus().getForegroundExcitationDensity();
        //            c = new Color(brightness,brightness,brightness);
        //        }
        //        spikeGraphics.setPaint(c);
        //        spikeGraphics.setStroke(new BasicStroke(0.01f));
        //        // draw the outline of the stimulus
        //        ShapeList l=(ShapeList)stimulus.getTransformedShapes();
        //        //System.out.println("rendering stimulus "+stimulus+" with transformed shapelist "+l);
        //        for(Iterator i=l.iterator();i.hasNext();){
        //            Shape s=(Shape)i.next();
        //            spikeGraphics.draw(s);
        //        }
        //        spikeGraphics.setTransform(at); // back to pushed transform
        repaint(300); // call for eventual repaint so we get refreshed even if mouse doesn't move
    }
    
    /** clears recorded spikes and markers */
    public void clearSpikeImage(){
        spikeTriggeredStimuli.clear();
        markers.clear();
        //        spikeImage=null;
    }
    
    // tobi removed because now using 1.4+ code
    //    /** overrides method in order to enable getting the keyboard focus by tabbing.
    //     See http://forum.java.sun.com/thread.jsp?forum=57&thread=201850.
    //     This method is deprecated as of 1.4 but we use it for 1.3 compatibility.
    //
    //     */
    //    public boolean isFocusTraversable(){
    //        return true;
    //    }
    
    
    // following not needed with BorderLayout used in parent
    //    /** set preferred size to size of parent, leaving room for status line */
    //    public Dimension getPreferredSize(){
    //        Dimension d=getParent().getSize();
    //        Dimension d2=new Dimension((int)d.getWidth(),(int)d.getHeight()-30);
    //        return d2;
    //
    //        Dimension d= super.getPreferredSize();
    //        System.out.println("super's preferred size="+d);
    //        return d;
    //    }
    
    
    //    /** overrides the minimum size */
    //    public Dimension getMinimumSize(){
    //        return new Dimension(0,100);
    //    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem foregroundMenuItem;
    private javax.swing.JMenuItem brightenMenuItem;
    private javax.swing.JMenuItem backgroundMenuItem;
    private javax.swing.JMenuItem darkenBackgroundMenuItem;
    private javax.swing.JPopupMenu stimulusControlPopupMenu;
    private javax.swing.JMenuItem invertContrastMenuItem;
    private javax.swing.JMenuItem rotateMenuItem;
    private javax.swing.JMenuItem darkenMenuItem;
    private javax.swing.JMenuItem brightenBackgroundMenuItem;
    // End of variables declaration//GEN-END:variables
    
}

/*
 $Log: TangentScreen.java,v $
 Revision 1.36  2004/11/12 13:37:12  tobi
 on shipping friend23 and updating manual. migrating to subversion now.

 Revision 1.35  2004/02/09 06:35:19  tobi
 cleanup with refactor

 Revision 1.34  2003/07/06 05:22:02  tobi
 *** empty log message ***

 Revision 1.33  2003/06/23 11:30:16  tobi
 greatly improved recording display speed, capability

 added full screen exclusive display

 Revision 1.32  2003/06/16 07:46:27  tobi
 fixed javadoc
 
 added target to build windows installer
 
 Revision 1.31  2003/06/15 19:17:31  tobi
 added capability of recording spikes from simulation or from microphone and plotting the
 corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 kind of a spike-tirggered average is possible.
 
 Revision 1.30  2003/06/12 06:28:45  tobi
 added microphone recording for hooking up friend chip. not fully functional yet.
 
 Revision 1.29  2003/05/10 17:27:43  jgyger
 Merge from color-branch
 
 Revision 1.28.2.9  2003/05/10 14:54:53  tobi
 foregound color menu was not getting turned on again, only turned off for mono simulation, fixed that.
 
 Revision 1.28.2.8  2003/05/10 09:48:28  jgyger
 Refresh preview panel before using it
 
 Revision 1.28.2.7  2003/05/10 08:29:58  jgyger
 Fancy lms preview in color-chooser
 
 Revision 1.28.2.6  2003/05/08 17:12:40  tobi
 Simulation type is now selectable from the GUI under the Simulation menu.
 Splash screen now includes all authors always, since simulation selectable.
 About dialog contains CVS rev date for About box.
 
 Revision 1.28.2.5  2003/05/06 10:52:45  jgyger
 Add guards to foreground/background color change
 
 Revision 1.28.2.4  2003/05/06 09:32:49  tobi
 javadoc for color stimulus popup
 
 Revision 1.28.2.3  2003/05/06 09:09:01  tobi
 reactivated mouse wheel events for changing stimulus rotation, size, and length
 since we are now comitted to java 1.4+
 
 Revision 1.28.2.2  2003/03/16 16:37:05  jgyger
 add popup menu items to change forground and background colors
 
 Revision 1.28.2.1  2003/03/12 20:54:29  jgyger
 add support for a color stimulus
 
 Revision 1.28  2002/10/24 12:05:49  cmarti
 add GPL header
 
 Revision 1.27  2002/10/17 12:47:23  tobi
 commented mouse wheel for 1.3 compatibiliity (again)
 
 Revision 1.26  2002/10/16 11:29:28  tobi
 Stimulus now has methods for geometrcal transformations of stimulus
 size and rotation. AbstractStimulus implements these methods.
 TangentScreen and FriendGUI now use these unified transformation methods.
 
 activitymeter now shows the spike rate in spikes/second instead of spikes/measurement interval.
 
 Revision 1.25  2002/10/15 19:26:55  tobi
 lots of javadoc added,
 mouse wheel enabled.
 
 Revision 1.24  2002/10/13 16:29:09  tobi
 many small changes from tuebingen trip.
 
 Revision 1.23  2002/10/10 16:26:49  tobi
 got invert contrast hot key to work again by taking it out of TangentScreen.
 
 Revision 1.22  2002/10/10 15:22:06  tobi
 moved accelerators for changing stimulus to FriendGUI and added stimulus controls
 to Stimulus menu.
 
 Revision 1.21  2002/10/08 16:24:02  tobi
 simulation now starts at startup.
 all menu items have mnumonics.
 F1 to bring up hotkeys is launched from GUI not tangent screen.
 repaints added to tangent screen to repaint after changes to sitmulus.
 
 Revision 1.20  2002/10/07 19:57:06  tobi
 GUI cells changed to Map from HashMap because underlying map is now TreeMap (which is sorted).
 
 TangentScreen popup menu now works.
 
 Revision 1.19  2002/10/07 15:05:43  tobi
 added popup trigger for popup menu but it doesn't work yet.
 
 Revision 1.18  2002/10/07 14:42:36  tobi
 added popup menu for stimulus control
 
 Revision 1.17  2002/10/07 13:02:08  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.
 
 Revision 1.16  2002/10/06 10:51:18  tobi
 fixed javadoc links
 
 Revision 1.15  2002/10/06 08:56:06  tobi
 added activity bar  and put in to right side of screen.
 removed status bar activity meter
 added menu item accelerator to show photoreceptors.,
 changed simulation start/stop to toggle with enter.
 changed "Plot" menu name to "Simulation"
 changed mouse cursor in TangentScrren to be a move cursor.
 
 Revision 1.14  2002/10/05 21:27:17  tobi
 changed rotation to PI/6
 
 Revision 1.13  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy
 
 Revision 1.12  2002/09/25 20:25:00  tobi
 fixed bug in referencing photoreceptorShapes in tangent scrren.
 the simulationSetup and stimulus are now passed into TangentScrren
 and the receptor shapes are extracted from the simulationSetup by tangentScreen directly.
 
 Revision 1.11  2002/09/24 07:32:30  tobi
 fixed window resizing bug by using revalidate after window resize
 
 Revision 1.10  2002/09/24 04:40:44  tobi
 added capabibilty to view photoreceptor shapes and added view menu to hold command.
 added rendering of photoreceptor shapes to paintComponent().
 
 Revision 1.9  2002/09/23 10:38:13  tobi
 added status line to gui
 
 Revision 1.8  2002/09/22 20:30:40  tobi
 setOpaque true for slightly faster repainting.  still have flicker on vertical edges.
 
 Revision 1.7  2002/09/22 19:29:54  tobi
 fixed mouse and repaint methods to correctly transform stimuli to 1:1 aspect ratio in correct position
 
 Revision 1.6  2002/09/19 06:51:34  tobi
 added more controls for stimulus, including scaling.
 added simple help dialog to show hotkeys
 activated help/contents to show help dialog
 
 stimulus now moves, scales, rotates, and changes constrast in a usable manner.
 
 Revision 1.5  2002/09/18 15:30:55  tobi
 added more stimulus control commands for controlling brightness of foreground, background,
 and flipping contrast of stimulus and background, flashing the stimulus, and
 rotating the stimulus.  rotation doesn't work yet because movement of stimulus
 after rotation is also rotated with respect to mouse movement.
 
 Revision 1.4  2002/09/18 06:44:03  tobi
 yay! stimulus now moves around...
 changed stimulus.getTransforms() to accord with new scheme of separate stimulus.getTransforms() used
 to affect stimulus shape.
 this is working revision but many additions need to be made to control
 stimulus stimulus.getTransforms() (rotation, scaling, etc) along with brightness controls
 for foreground and background.
 
 Revision 1.3  2002/09/15 21:45:43  tobi
 about dialog added
 
 window resize resizes tangent screen
 added prototype keyevent listener to tangent screen.
 
 Revision 1.2  2002/09/15 14:00:04  tobi
 still doesn't work but now provides some methods for compting transforms from stimulus to screen coordinates
 and vice versa.  stimulus still needs a reference position to allow
 mouse control to abolute location.
 
 Revision 1.1  2002/09/15 08:01:37  tobi
 initial verison of plotting screen, doesn't do anything sensible yet
 */
