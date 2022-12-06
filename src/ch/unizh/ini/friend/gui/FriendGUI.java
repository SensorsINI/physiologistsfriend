/*
 $Id: FriendGUI.java,v 1.66 2004/02/09 06:35:17 tobi Exp $
 
 
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
 
 
 * Created on September 2, 2002, 8:04 PM
 
 */

package ch.unizh.ini.friend.gui;

import javax.swing.JDialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import ch.unizh.ini.friend.browser.*;
import ch.unizh.ini.friend.graphics.*;
import ch.unizh.ini.friend.record.MicrophoneReporter;
import ch.unizh.ini.friend.record.SimulationReporter;
import ch.unizh.ini.friend.record.SpikeReporter;
import ch.unizh.ini.friend.stimulus.*;
import java.util.Random;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.simulation.ServesOutput;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Iterator;
import java.util.Map;
import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.sound.sampled.LineUnavailableException;
//import java.util.prefs.*;
import javax.swing.*;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 * Main GUI.
 * @author  tobi
 * @version $Revision: 1.66 $
 *
 */
public class FriendGUI extends javax.swing.JFrame {
    
    // warning cannot convert string warnings
    // see http://forum.java.sun.com/thread.jsp?thread=220363&forum=17&message=1033470 for ENTER key problems under X windows
    
    private SimulationSetup simulationSetup=null;
    
    /** sets the {@link ch.unizh.ini.friend.simulation.SimulationSetup} to simulate. Reassigns the TangentScreen and StatusPanel simulatons.
     * If this is changed from an existing running setup, the old simulation is stopped and the new one is started.
     * @param setup the new setup
     */
    public void setSimulationSetup(SimulationSetup setup){
        if(getSimulationSetup()!=null){
            getSimulationSetup().getSimulation().stop();
        }
        this.simulationSetup=setup;
        this.stimulus=simulationSetup.getStimulus();
        tangentScreen.setSimulationSetup(simulationSetup);
        statusPanel.setSimulationSetup(simulationSetup);
        
        // we add an Updateable object to the simulation that simply updates the activity in the activity bar
        SimpleOutputMonitor monitor = new SimpleOutputMonitor(new SimpleOutputMonitor.Deliverable() {
            
            /** Updates the progress bar */
            public void run() {
                activityMeter.setActivity(value);
            }
            
        });
        simulationSetup.getSimulation().addUpdateable(monitor);
        simulationSetup.putMonitor("ProgressBar", monitor);
        
        if(isSimulationEnabled()){
            getSimulationSetup().getSimulation().start();
        }
        makeCellMenu();
    }
    
    /** @return the current {@link ch.unizh.ini.friend.simulation.SimulationSetup} */
    public SimulationSetup getSimulationSetup(){
        return simulationSetup;
    }
    
    
    private Stimulus stimulus;
    
    /** the plotting area */
    public TangentScreen tangentScreen;
    
    /** the status panel. */
    public StatusPanel statusPanel;
    
    /** fraction of screen horizontal occupied by Frame */
    public static final float FRAME_SCREEN_FRACTION=.6f;
    
    /** height of status panel in pixels */
    public static final int STATUS_HEIGHT=90;
    
    /** the acitivyt meter */
    public ActivityMeter activityMeter;
    
    // preferred size of window keys as stored in preferences
    private static final String WIDTH = "WIDTH";
    private static final String HEIGHT = "HEIGHT";
    // key for last chosen cell
    private static final String CELL = "CELL";
    
    private SpikeReporter spikeReporter=null;
    
    // the help system
    private FriendHelp help=null;
    
    // preferences, initialized in constructer
    // we can't use preferences because we run from java web start, which doesn't allow them without signed application, which we don't want to deal with.
    //    private Preferences prefs; {
    //        try{ // try/catch this because we may be running under java web start, which disallows preferences in simplest deployment
    //            prefs=Preferences.userNodeForPackage(this.getClass());
    //        }catch(SecurityException e){
    //            System.err.println("can't store preferences, probably running in unsecure sandbox");
    //            //new ExceptionDialog(this,false,e,"<html><body>Sorry, can't store preferences.<p>To avoid this message, install the PhysioFriend locally.</body></html>").show();
    //        }
    //    }
    
    /** Creates new form FriendGUI.
     * @param simulationSetup the simulation setup.  This is intialized by {@link ch.unizh.ini.friend.simulation.SimulationSetupFactory}.
     * It contains a stimulus as well as the neural architecture.
     */
    public FriendGUI(SimulationSetup simulationSetup) {
        this.simulationSetup=simulationSetup;
        this.stimulus=simulationSetup.getStimulus();
        
        // this prefs is used to load stored preferences, e.g. windown size
        // see http://developer.java.sun.com/developer/technicalArticles/releases/preferences/
        // and http://java.sun.com/j2se/1.4/docs/guide/lang/preferences.html
        // under windows XP, these prefs are stored automagically in the registry under
        // HKEY_CURRENT_USER/Software/JavaSoft/friend/gui  (friend/gui is the current package)
        
        setGUISize();  // sets the size of the window based on screen size, previous choice, etc.
        
        initComponents();
        
        // set up the help system.
        help=new FriendHelp(this); // make the help object that encapsulates javahelp
        if(help.hb!=null){
            help.hb.enableHelpOnButton(hotkeytMenuItem, "Hot_Keys", null); // set up to show hot key help when help/hot keys help menu item is selected
            help.hb.enableHelpOnButton(helpContents, "Intro", null); // show intro help when help/contents is asked for
            help.hb.enableHelpOnButton(quickStartMenuItem, "Quick_Start",null); // quick start
            help.hb.enableHelpKey(this.getRootPane(), "Intro", null); // show intro help when user hits F1
        }else{
            JOptionPane.showMessageDialog(this,"Couldn't load help system","Couldn't load help",JOptionPane.ERROR_MESSAGE);
        }
        
        // setup button groups for stimulus and cell type
        stimulusButtonGroup.add(barRadioButtonMenuItem);
        stimulusButtonGroup.add(edgeRadioButtonMenuItem);
        stimulusButtonGroup.add(gratingRadioButtonMenuItem);
        stimulusButtonGroup.add(spotStimulusRadioButtonItem);
        
        simulationButtonGroup.add(monochromeSimulationButton);
        simulationButtonGroup.add(colorSimulationButton);
        
        recordButtonGroup.add(fromSimulationMenuButtonItem);
        recordButtonGroup.add(fromMicrophoneMenuButtonItem);
        
        // get the special cells specifed by SimulationSetupFactory and add them to the Cell... menu
        makeCellMenu();
        
        
        // set the layout of the content pane to accomodate tangent screen panel and status panel
        //       getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        getContentPane().setLayout(new BorderLayout());
        
        // make the tangent screen panel
        tangentScreen=new TangentScreen(simulationSetup);
        //tangentScreen.setSize(new Dimension(getSize().width,getSize().height-STATUS_HEIGHT));
        // add it to the the main content pane
        getContentPane().add(tangentScreen,BorderLayout.CENTER);
        
        // activity meter
        activityMeter=new ActivityMeter();
        getContentPane().add(activityMeter,BorderLayout.EAST);
        
        
        statusPanel=new StatusPanel(simulationSetup, stimulus);
        //statusPanel.setSize(new Dimension(getSize().width,STATUS_HEIGHT));
        getContentPane().add(statusPanel,BorderLayout.NORTH);
        
        // provisional use of the monitoring facility.
        // we add an Updateable object to the simulation that simply updates the activity in the activity bar
        SimpleOutputMonitor monitor = new SimpleOutputMonitor(new SimpleOutputMonitor.Deliverable() {
            
            /** Updates the progress bar */
            public void run() {
                activityMeter.setActivity(value);
            }
            
        });
        simulationSetup.getSimulation().addUpdateable(monitor);
        simulationSetup.putMonitor("ProgressBar", monitor);
        
        statusPanel.setCellName(simulationSetup.getStartingCellName());
        
        if(getSimulationSetup().getName().equals("color")){
            colorSimulationButton.setSelected(true);
        }else{
            monochromeSimulationButton.setSelected(true);
        }
        
    }
    
    
    // set the size of the main application window to some fraction of screen size, unless
    // window was previously resized. in this case, set it to the stored size.
    // see formComponentResized for storing window size values
    private void setGUISize(){
        // first compute initial prefererd size, which is some fraction of screen
        Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
        int initWidth=(int)(s.width*FRAME_SCREEN_FRACTION), initHeight=(int)(s.width*FRAME_SCREEN_FRACTION);
        
        int prefWidth, prefHeight;
        if(initWidth==0 || initHeight==0){ // some funny screen
            initWidth=500;
            initHeight=500;
        }
        prefHeight=initHeight;
        prefWidth=initWidth;
        //        // get ready to read preferences for this package
        //        if(prefs==null) { // couldn't load for some reason (most likely security exception under java web start)
        //            prefHeight=initHeight;
        //            prefWidth=initWidth;
        //        }else{
        //            // read stored prefs if they exist, otherwise used computed values
        //            prefWidth = prefs.getInt(WIDTH, initWidth);
        //            prefHeight = prefs.getInt(HEIGHT, initHeight);
        //        }
        //        System.out.println("prefWidth = " + prefWidth+ " prefHeight = " + prefHeight );
        
        // set JFrame window size
        Dimension newSize=new Dimension(prefWidth, prefHeight);
        //        System.out.println("calling setSize()");
        setSize(newSize);
        //        System.out.println("setSize() done");
    }
    
    /** Creates new form FriendGUI.  This constructor just shows the form. Use the constructor that specifies the tangent screen and
     * stimulus to build the real GUI.
     */
    public FriendGUI() {
        initComponents();
    }
    
    // builds the Cell menu. the combobox is in the StatusPanel
    private void makeCellMenu(){
        cellMenu.removeAll(); // clear the menu in case simulation setup has changed
        Map cells=(Map)simulationSetup.getOutputCells();
        int cellNum=0;
        for(Iterator i=cells.entrySet().iterator();i.hasNext();){
            addCell((Map.Entry)i.next(),++cellNum);
        }
    }
    
    /** adds cell to cell menu from a map entry gotten from
     * the hashMap of cells that are populated by {@link ch.unizh.ini.friend.simulation.SimulationSetupFactory}. (These cells are obtained by
     * {@link ch.unizh.ini.friend.simulation.SimulationSetup#getOutputCells})
     * @param e the Map.Entry (key + object) from the HashMap of cells by name
     * @param cellNumber the hot key by which to call up the cell, e.g. 1 means "1" will make that cell be monitored.
     */
    private void addCell(Map.Entry e, int cellNumber){
        JRadioButtonMenuItem item= new javax.swing.JRadioButtonMenuItem((String)e.getKey());
        item.setToolTipText("Monitor "+e.getKey());
        item.setAccelerator(KeyStroke.getKeyStroke(Integer.toString(cellNumber)));
        item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cellButtonMenuItemActionPerformed(evt);
            }
        });
        cellMenu.add(item);
        cellButtonGroup.add(item);
    }
    
    /** all cell choices come here. they are distinguished by the ActionEvent.
     * This method set the name of the cell in the status panel, and tells the simulationSetup to
     * monitor this cell.  It gets the name from the menu selection.
     * The menus are built from the names of the cells as returne by the SimulationSetup.
     * @param evt ActionEvent describing the cell choce.
     */
    private void cellButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        String cellName=((JMenuItem)(evt.getSource())).getText(); // get the name of the cell as shown in the menu (and as built by StimulusSetupFactory)
        statusPanel.setCellName(cellName); // show it
        simulationSetup.setMonitoredCell(cellName);    // monitor it
        //        if(prefs==null)  prefs=Preferences.userNodeForPackage(this.getClass());
        //
        //        prefs.put(CELL, cellName);
        //        try{ prefs.flush(); }catch(BackingStoreException e){System.out.println("Can't store preferences for cell choice, sorry...");}
        
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPopupMenu1 = new javax.swing.JPopupMenu();
        stimulusButtonGroup = new javax.swing.ButtonGroup();
        cellButtonGroup = new javax.swing.ButtonGroup();
        simulationButtonGroup = new javax.swing.ButtonGroup();
        recordButtonGroup = new javax.swing.ButtonGroup();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        fullScreenCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        receptorViewMenuItem = new javax.swing.JCheckBoxMenuItem();
        mutedViewMenuItem = new javax.swing.JCheckBoxMenuItem();
        hideCellNameMenuItem = new javax.swing.JCheckBoxMenuItem();
        cellMenu = new javax.swing.JMenu();
        mysteryCellItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        stimulusMenu = new javax.swing.JMenu();
        spotStimulusRadioButtonItem = new javax.swing.JRadioButtonMenuItem();
        barRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        edgeRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        gratingRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        toggleStimulusVisibility = new javax.swing.JMenuItem();
        invertStimulusContrast = new javax.swing.JMenuItem();
        brightenForegroundStimulus = new javax.swing.JMenuItem();
        darkenForegroundStimulus = new javax.swing.JMenuItem();
        brightenBackgroundStimulus = new javax.swing.JMenuItem();
        darkenBackgroundStimulus = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        rotateStimulus = new javax.swing.JMenuItem();
        makeWider = new javax.swing.JMenuItem();
        makeNarrower = new javax.swing.JMenuItem();
        makeTaller = new javax.swing.JMenuItem();
        makeShorter = new javax.swing.JMenuItem();
        simulationMenu = new javax.swing.JMenu();
        toggleSimulationRunning = new javax.swing.JCheckBoxMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        monochromeSimulationButton = new javax.swing.JRadioButtonMenuItem();
        colorSimulationButton = new javax.swing.JRadioButtonMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        simulationPropertiesMenuItem = new javax.swing.JMenuItem();
        recordMenu = new javax.swing.JMenu();
        markScreenMenuItem = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        recordCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        clearSpikesMenuItem = new javax.swing.JMenuItem();
        setPersistenceMenuItem = new javax.swing.JMenuItem();
        fadeSpikesCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        fillSpikesCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        jSeparator8 = new javax.swing.JSeparator();
        fromSimulationMenuButtonItem = new javax.swing.JRadioButtonMenuItem();
        fromMicrophoneMenuButtonItem = new javax.swing.JRadioButtonMenuItem();
        helpMenu = new javax.swing.JMenu();
        hotkeytMenuItem = new javax.swing.JMenuItem();
        helpContents = new javax.swing.JMenuItem();
        quickStartMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        webPageMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        getContentPane().setLayout(null);

        setTitle("Physiologist's Friend");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");
        exitMenuItem.setMnemonic('X');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        viewMenu.setMnemonic('V');
        viewMenu.setText("View");
        fullScreenCheckBoxMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        fullScreenCheckBoxMenuItem.setMnemonic('F');
        fullScreenCheckBoxMenuItem.setText("Full screen");
        fullScreenCheckBoxMenuItem.setToolTipText("Toggles full screen mode");
        fullScreenCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullScreenCheckBoxMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(fullScreenCheckBoxMenuItem);

        receptorViewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, 0));
        receptorViewMenuItem.setMnemonic('R');
        receptorViewMenuItem.setText("Photoreceptors");
        receptorViewMenuItem.setToolTipText("Show photoreceptor shapes");
        receptorViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receptorViewMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(receptorViewMenuItem);

        mutedViewMenuItem.setMnemonic('M');
        mutedViewMenuItem.setText("Mute Audio");
        mutedViewMenuItem.setToolTipText("Turns off sound output");
        mutedViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mutedViewMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(mutedViewMenuItem);

        hideCellNameMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, 0));
        hideCellNameMenuItem.setMnemonic('H');
        hideCellNameMenuItem.setText("Hide cell type");
        hideCellNameMenuItem.setToolTipText("Hides cell type in status bar");
        hideCellNameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideCellNameMenuItemActionPerformed(evt);
            }
        });

        viewMenu.add(hideCellNameMenuItem);

        menuBar.add(viewMenu);

        cellMenu.setMnemonic('C');
        cellMenu.setText("Cell");
        mysteryCellItem.setText("Choose Random Cell");
        mysteryCellItem.setToolTipText("Choose a mystery cell at random from possible cells");
        mysteryCellItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mysteryCellItemActionPerformed(evt);
            }
        });

        cellMenu.add(mysteryCellItem);

        cellMenu.add(jSeparator4);

        menuBar.add(cellMenu);

        stimulusMenu.setMnemonic('T');
        stimulusMenu.setText("Stimulus");
        spotStimulusRadioButtonItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 0));
        spotStimulusRadioButtonItem.setMnemonic('S');
        spotStimulusRadioButtonItem.setText("Spot stimulus");
        spotStimulusRadioButtonItem.setToolTipText("Makes a spot stimulus");
        spotStimulusRadioButtonItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spotStimulusRadioButtonItemActionPerformed(evt);
            }
        });

        stimulusMenu.add(spotStimulusRadioButtonItem);

        barRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, 0));
        barRadioButtonMenuItem.setMnemonic('A');
        barRadioButtonMenuItem.setText("Bar");
        barRadioButtonMenuItem.setToolTipText("Make bar stimulus");
        barRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barRadioButtonMenuItemActionPerformed(evt);
            }
        });

        stimulusMenu.add(barRadioButtonMenuItem);

        edgeRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        edgeRadioButtonMenuItem.setMnemonic('E');
        edgeRadioButtonMenuItem.setText("Edge");
        edgeRadioButtonMenuItem.setToolTipText("Make edge stimulus");
        edgeRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgeRadioButtonMenuItemActionPerformed(evt);
            }
        });

        stimulusMenu.add(edgeRadioButtonMenuItem);

        gratingRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, 0));
        gratingRadioButtonMenuItem.setMnemonic('G');
        gratingRadioButtonMenuItem.setText("Grating");
        gratingRadioButtonMenuItem.setToolTipText("Makes grating stimulus");
        gratingRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gratingRadioButtonMenuItemActionPerformed(evt);
            }
        });

        stimulusMenu.add(gratingRadioButtonMenuItem);

        stimulusMenu.add(jSeparator2);

        toggleStimulusVisibility.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        toggleStimulusVisibility.setText("Toggle visibility");
        toggleStimulusVisibility.setToolTipText("Toggles visibility of stimulus");
        toggleStimulusVisibility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleStimulusVisibilityActionPerformed(evt);
            }
        });

        stimulusMenu.add(toggleStimulusVisibility);

        invertStimulusContrast.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, 0));
        invertStimulusContrast.setText("Invert contrast");
        invertStimulusContrast.setToolTipText("Swaps stimulus and background brightnesses");
        invertStimulusContrast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertStimulusContrastActionPerformed(evt);
            }
        });

        stimulusMenu.add(invertStimulusContrast);

        brightenForegroundStimulus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, 0));
        brightenForegroundStimulus.setText("Brighten Foreground");
        brightenForegroundStimulus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stimulusAction(evt);
            }
        });

        stimulusMenu.add(brightenForegroundStimulus);

        darkenForegroundStimulus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, 0));
        darkenForegroundStimulus.setText("Darken Foreground");
        darkenForegroundStimulus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stimulusAction(evt);
            }
        });

        stimulusMenu.add(darkenForegroundStimulus);

        brightenBackgroundStimulus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.SHIFT_MASK));
        brightenBackgroundStimulus.setText("Brighten Background");
        brightenBackgroundStimulus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stimulusAction(evt);
            }
        });

        stimulusMenu.add(brightenBackgroundStimulus);

        darkenBackgroundStimulus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK));
        darkenBackgroundStimulus.setText("Darken Background");
        darkenBackgroundStimulus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stimulusAction(evt);
            }
        });

        stimulusMenu.add(darkenBackgroundStimulus);

        stimulusMenu.add(jSeparator3);

        rotateStimulus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, 0));
        rotateStimulus.setText("Rotate");
        rotateStimulus.setToolTipText("Rotates stimulus Pi/6");
        rotateStimulus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateStimulusActionPerformed(evt);
            }
        });

        stimulusMenu.add(rotateStimulus);

        makeWider.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, 0));
        makeWider.setText("Make wider");
        makeWider.setToolTipText("Makes stimulus wider");
        makeWider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeWiderActionPerformed(evt);
            }
        });

        stimulusMenu.add(makeWider);

        makeNarrower.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, 0));
        makeNarrower.setText("Make narrower");
        makeNarrower.setToolTipText("Makes stimulus narrower");
        makeNarrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeNarrowerActionPerformed(evt);
            }
        });

        stimulusMenu.add(makeNarrower);

        makeTaller.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0));
        makeTaller.setText("Make taller");
        makeTaller.setToolTipText("Makes stimulus taller");
        makeTaller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeTallerActionPerformed(evt);
            }
        });

        stimulusMenu.add(makeTaller);

        makeShorter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0));
        makeShorter.setText("Make shorter");
        makeShorter.setToolTipText("Makes stimulus shorter");
        makeShorter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeShorterActionPerformed(evt);
            }
        });

        stimulusMenu.add(makeShorter);

        menuBar.add(stimulusMenu);

        simulationMenu.setMnemonic('S');
        simulationMenu.setText("Simulation");

        toggleSimulationRunning.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        toggleSimulationRunning.setMnemonic('S');
        toggleSimulationRunning.setSelected(true);
        toggleSimulationRunning.setText("Run simulation");
        toggleSimulationRunning.setToolTipText("Start and stop simulation of cell  responses");
        toggleSimulationRunning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSimulationRunningActionPerformed(evt);
            }
        });

        simulationMenu.add(toggleSimulationRunning);

        simulationMenu.add(jSeparator6);

        monochromeSimulationButton.setText("Luminance simulation");
        monochromeSimulationButton.setToolTipText("Sets the simulation to simulate luminance cells");
        monochromeSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monochromeSimulationButtonActionPerformed(evt);
            }
        });

        simulationMenu.add(monochromeSimulationButton);

        colorSimulationButton.setText("Color cell simulation");
        colorSimulationButton.setToolTipText("Sets the simulation to simulate color selective cells");
        colorSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorSimulationButtonActionPerformed(evt);
            }
        });

        simulationMenu.add(colorSimulationButton);

        simulationMenu.add(jSeparator9);

        simulationPropertiesMenuItem.setText("Properties...");
        simulationPropertiesMenuItem.setToolTipText("Opens dialog to set simulation properties");
        simulationPropertiesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationPropertiesMenuItemActionPerformed(evt);
            }
        });

        simulationMenu.add(simulationPropertiesMenuItem);

        menuBar.add(simulationMenu);

        recordMenu.setMnemonic('R');
        recordMenu.setText("Record");
        markScreenMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, 0));
        markScreenMenuItem.setMnemonic('M');
        markScreenMenuItem.setText("Mark point");
        markScreenMenuItem.setToolTipText("Marks this point on the tangent screen");
        markScreenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markScreenMenuItemActionPerformed(evt);
            }
        });

        recordMenu.add(markScreenMenuItem);

        recordMenu.add(jSeparator10);

        recordCheckBoxMenuItem.setMnemonic('E');
        recordCheckBoxMenuItem.setText("Enable recording");
        recordCheckBoxMenuItem.setToolTipText("Records spike-triggered stimuli from simulation or microphone and plots them");
        recordCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordCheckBoxMenuItemActionPerformed(evt);
            }
        });

        recordMenu.add(recordCheckBoxMenuItem);

        recordMenu.add(jSeparator7);

        clearSpikesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, 0));
        clearSpikesMenuItem.setMnemonic('C');
        clearSpikesMenuItem.setText("Clear");
        clearSpikesMenuItem.setToolTipText("Clears spike marks from tangent screen");
        clearSpikesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSpikesMenuItemActionPerformed(evt);
            }
        });

        recordMenu.add(clearSpikesMenuItem);

        setPersistenceMenuItem.setText("Set spike persistence...");
        setPersistenceMenuItem.setToolTipText("Opens a dialog box to set the persistence of recorded spike-triggered stimuli");
        setPersistenceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPersistenceMenuItemActionPerformed(evt);
            }
        });

        recordMenu.add(setPersistenceMenuItem);

        fadeSpikesCheckBoxMenuItem.setMnemonic('F');
        fadeSpikesCheckBoxMenuItem.setSelected(true);
        fadeSpikesCheckBoxMenuItem.setText("Fade spike-triggered stimuli");
        fadeSpikesCheckBoxMenuItem.setToolTipText("Recorded stimulil are faded out");
        fadeSpikesCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fadeSpikesCheckBoxMenuItemActionPerformed(evt);
            }
        });

        recordMenu.add(fadeSpikesCheckBoxMenuItem);

        fillSpikesCheckBoxMenuItem.setMnemonic('I');
        fillSpikesCheckBoxMenuItem.setText("Fill in spike-triggered stimuli");
        fillSpikesCheckBoxMenuItem.setToolTipText("Causes recorded spike-triggered stimulil to be filled in");
        fillSpikesCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillSpikesCheckBoxMenuItemActionPerformed(evt);
            }
        });

        recordMenu.add(fillSpikesCheckBoxMenuItem);

        recordMenu.add(jSeparator8);

        fromSimulationMenuButtonItem.setSelected(true);
        fromSimulationMenuButtonItem.setText("From simulation");
        fromSimulationMenuButtonItem.setToolTipText("Record the spikes from the simulation");
        fromSimulationMenuButtonItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromSimulationMenuButtonItemActionPerformed(evt);
            }
        });

        recordMenu.add(fromSimulationMenuButtonItem);

        fromMicrophoneMenuButtonItem.setText("From microphone");
        fromMicrophoneMenuButtonItem.setToolTipText("Record the spikes from the microphone");
        fromMicrophoneMenuButtonItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromMicrophoneMenuButtonItemActionPerformed(evt);
            }
        });

        recordMenu.add(fromMicrophoneMenuButtonItem);

        menuBar.add(recordMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");
        hotkeytMenuItem.setMnemonic('K');
        hotkeytMenuItem.setText("Hot keys");
        hotkeytMenuItem.setToolTipText("Quck guide to hot keys");
        helpMenu.add(hotkeytMenuItem);

        helpContents.setMnemonic('C');
        helpContents.setText("Contents");
        helpContents.setToolTipText("Shows builtin help");
        helpMenu.add(helpContents);

        quickStartMenuItem.setText("Quick Start");
        quickStartMenuItem.setToolTipText("Show quick start help");
        helpMenu.add(quickStartMenuItem);

        helpMenu.add(jSeparator5);

        webPageMenuItem.setMnemonic('W');
        webPageMenuItem.setText("Home page");
        webPageMenuItem.setToolTipText("Go to the PhysioFriend Web site");
        webPageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webPageMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(webPageMenuItem);

        helpMenu.add(jSeparator1);

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        aboutMenuItem.setToolTipText("Program and version information");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

    }//GEN-END:initComponents

    private void markScreenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markScreenMenuItemActionPerformed
        tangentScreen.addMarker();
    }//GEN-LAST:event_markScreenMenuItemActionPerformed

    private void simulationPropertiesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationPropertiesMenuItemActionPerformed
        JDialog d=new SimulationProperties(this,true, simulationSetup);
        d.show();
        
        // Add your handling code here:
    }//GEN-LAST:event_simulationPropertiesMenuItemActionPerformed

    private void fillSpikesCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillSpikesCheckBoxMenuItemActionPerformed
        if(tangentScreen!=null) tangentScreen.setSpikeFillEnabled(fillSpikesCheckBoxMenuItem.isSelected());
        // Add your handling code here:
    }//GEN-LAST:event_fillSpikesCheckBoxMenuItemActionPerformed

    private void fadeSpikesCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fadeSpikesCheckBoxMenuItemActionPerformed
        if(tangentScreen!=null) tangentScreen.setSpikeFadeEnabled(fadeSpikesCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_fadeSpikesCheckBoxMenuItemActionPerformed

    private void setPersistenceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPersistenceMenuItemActionPerformed
        String numberString=JOptionPane.showInputDialog("Number of past spikes to display?",Integer.toString(tangentScreen.getSpikeMemoryDepth()));
        try{
            int number=Integer.parseInt(numberString);
            if (number<0) number=0;
            tangentScreen.setSpikeMemoryDepth(number);
        }catch(NumberFormatException e){
        }
    }//GEN-LAST:event_setPersistenceMenuItemActionPerformed
    
    boolean fullScreenEnabled=false;
    
    private void fullScreenCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullScreenCheckBoxMenuItemActionPerformed
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        GraphicsDevice device=devices[0];  // default screen
        
        System.out.println("isFullScreenSupported="+device.isFullScreenSupported());
        if(fullScreenCheckBoxMenuItem.isSelected()){
            dispose(); // to undecorate
            setUndecorated(true);
            device.setFullScreenWindow(this);
//            System.out.println("getDisplayMode()="+device.getDisplayMode());
//            DisplayMode m=device.getDisplayMode();
//            System.out.println("w="+m.getWidth());
            show();
        }else if(!fullScreenCheckBoxMenuItem.isSelected()){
            dispose();
            setUndecorated(false);
            device.setFullScreenWindow(null);
           System.out.println("getDisplayMode()="+device.getDisplayMode());
            show();
        }
    }//GEN-LAST:event_fullScreenCheckBoxMenuItemActionPerformed
    
    private void clearSpikesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSpikesMenuItemActionPerformed
        tangentScreen.clearSpikeImage();
    }//GEN-LAST:event_clearSpikesMenuItemActionPerformed
    
    private void fromMicrophoneMenuButtonItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromMicrophoneMenuButtonItemActionPerformed
//        System.out.println("mic selected");// Add your handling code here:
    }//GEN-LAST:event_fromMicrophoneMenuButtonItemActionPerformed
    
    private void fromSimulationMenuButtonItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromSimulationMenuButtonItemActionPerformed
//        System.out.println("simulation selected");// Add your handling code here:
    }//GEN-LAST:event_fromSimulationMenuButtonItemActionPerformed
    
    private void recordCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordCheckBoxMenuItemActionPerformed
        if(recordCheckBoxMenuItem.isSelected()){
            startRecording();
        }else if(!recordCheckBoxMenuItem.isSelected()){
            stopRecording();
        }
        
    }//GEN-LAST:event_recordCheckBoxMenuItemActionPerformed
    
    private void colorSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorSimulationButtonActionPerformed
        setSimulationSetup(SimulationSetupFactory.getColorSimulationSetup());
    }//GEN-LAST:event_colorSimulationButtonActionPerformed
    
    private void monochromeSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monochromeSimulationButtonActionPerformed
        setSimulationSetup(SimulationSetupFactory.getSimulationSetup());
    }//GEN-LAST:event_monochromeSimulationButtonActionPerformed
    
    private void spotStimulusRadioButtonItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spotStimulusRadioButtonItemActionPerformed
        ShapeList newShapes=StimulusShapeFactory.makeSpotShape();
        stimulus.getTransforms().setGeometry(newShapes);
        tangentScreen.repaint();
    }//GEN-LAST:event_spotStimulusRadioButtonItemActionPerformed
    
    /** @return recording active */
    private boolean isRecording(){
        if(spikeReporter!=null) return spikeReporter.isReporting();
        else return false;
    }
        
    /** start the spike reporter according to mic or simulation input */
    private void startRecording(){
        stopRecording();
//        System.out.println("start recording");
        if(fromMicrophoneMenuButtonItem.isSelected()){
            //                System.out.println("from microphone");
            try{
                spikeReporter=new MicrophoneReporter(simulationSetup);
            }catch(LineUnavailableException e){
                JOptionPane.showMessageDialog(this,e,"LineUnavailableException",JOptionPane.ERROR_MESSAGE);
                recordCheckBoxMenuItem.setSelected(false);
            }
        }else if(fromSimulationMenuButtonItem.isSelected()){
            //                System.out.println("from simulation");
            spikeReporter=new SimulationReporter(simulationSetup);
        }
        
        spikeReporter.addSpikeListener(tangentScreen);
        spikeReporter.startReporting();
    }
    
    /** stops the spike reporter */
    private void stopRecording(){
        //            System.out.println("stop recording");// stop recording
        if(spikeReporter!=null && spikeReporter.isReporting()){
            spikeReporter.stopReporting();
        }
    }
    
    
    private void mysteryCellItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mysteryCellItemActionPerformed
        // choose one of the cells but don't show which one it is
        Random random=new Random();
        int numCells=simulationSetup.getOutputCells().size();
        int cellNum=random.nextInt(numCells);
        Map cells=simulationSetup.getOutputCells();
        Set set=cells.entrySet();
        Iterator i=set.iterator();
        int j=0;
        Map.Entry me=null;
        while(i.hasNext() && j<=cellNum){
            me=(Map.Entry)i.next();
            //            System.out.println("j="+j+" me="+me);
            j++;
        }
        //        System.out.println("me="+me);
        simulationSetup.setMonitoredCell((ServesOutput)simulationSetup.getOutputCell((String)me.getKey()));
        statusPanel.setCellNameVisible(false);
    }//GEN-LAST:event_mysteryCellItemActionPerformed
    
    private void toggleStimulusVisibilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleStimulusVisibilityActionPerformed
        stimulus.setVisible(!stimulus.isVisible()); tangentScreen.repaint();
        // Add your handling code here:
    }//GEN-LAST:event_toggleStimulusVisibilityActionPerformed
    
    
    private void makeShorterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeShorterActionPerformed
        stimulus.shorten(); tangentScreen.repaint();
    }//GEN-LAST:event_makeShorterActionPerformed
    
    private void makeTallerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeTallerActionPerformed
        stimulus.lengthen(); tangentScreen.repaint();
    }//GEN-LAST:event_makeTallerActionPerformed
    
    private void makeNarrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeNarrowerActionPerformed
        stimulus.thin(); tangentScreen.repaint();
    }//GEN-LAST:event_makeNarrowerActionPerformed
    
    private void makeWiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeWiderActionPerformed
        stimulus.fatten(); tangentScreen.repaint();
    }//GEN-LAST:event_makeWiderActionPerformed
    
    private void rotateStimulusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateStimulusActionPerformed
        stimulus.rotateCW();   tangentScreen.repaint();      // Add your handling code here:
    }//GEN-LAST:event_rotateStimulusActionPerformed
    
    private void invertStimulusContrastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertStimulusContrastActionPerformed
        stimulus.flipContrast();    tangentScreen.repaint();     // Add your handling code here:
    }//GEN-LAST:event_invertStimulusContrastActionPerformed
    
    private void stimulusAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stimulusAction
        String cmd=evt.getActionCommand();
        if(cmd.equals(Stimulus.BRIGHTEN_FOREGROUND)){
            stimulus.brightenForeground();
        }else if(cmd.equals(Stimulus.DARKEN_FOREGROUND)){
            stimulus.darkenForeground();
        }else if(cmd.equals(Stimulus.BRIGHTEN_BACKGROUND)){
            stimulus.brightenBackground();
        }else if(cmd.equals(Stimulus.DARKEN_BACKGROUND)){
            stimulus.darkenBackground();
        }
        tangentScreen.repaint();
    }//GEN-LAST:event_stimulusAction
    
    private void hideCellNameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideCellNameMenuItemActionPerformed
        //        System.out.println("evt "+evt);
        //        System.out.println("hideisSelected="+hideCellNameMenuItem.isSelected());
        statusPanel.setCellNameVisible(!hideCellNameMenuItem.isSelected());
    }//GEN-LAST:event_hideCellNameMenuItemActionPerformed
    
    private void toggleSimulationRunningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleSimulationRunningActionPerformed
        toggleSimulation();
    }//GEN-LAST:event_toggleSimulationRunningActionPerformed
    
    private void mutedViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mutedViewMenuItemActionPerformed
        ((AudioOutput)simulationSetup.getMonitor("AudioOutput")).setMuted(mutedViewMenuItem.isSelected());
    }//GEN-LAST:event_mutedViewMenuItemActionPerformed
    
    /** the URL string for home page: {@value} */
    public static final String URL_HOME="http://www.ini.unizh.ch/~tobi/friend";
    
    //    /** the URL string for program help: {@value} */
    //    public static final String URL_HELP="file:install/index.html";
    
    /** tries to launch browser to show web site for phyio friend */
    private void webPageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webPageMenuItemActionPerformed
        
        // following is attempt to build a URL string for referencing help html contained in jar file
        // browser doesn't understand resulting URL, in particular, URL when friend is run from jar looks like this:
        // helpURL.toExternalForm()=jar:file:/D:/Documents%20and%20Settings/tobi/My%20Documents/cvsfriend/Java/build/friend.jar!/index.html
        
        //        ClassLoader cl=this.getClass().getClassLoader();
        //        System.err.println("cl = " + cl );
        //        URL helpURL=cl.getResource("index.html");
        //        System.err.println("helpURL = " + helpURL );
        //        System.out.println("helpURL.toExternalForm()="+helpURL.toExternalForm());
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        boolean suceeded=true;
        try{
            BrowserLauncher.openURL(URL_HOME);
        }catch(java.io.IOException e){
            suceeded=false;
            JOptionPane.showMessageDialog(null, "Couldn't launch browser to show "+URL_HOME,"Can't launch browser", JOptionPane.ERROR_MESSAGE);
        }catch(SecurityException es){
            // following uses jnlp calls (web start library) to try to load local web page.  this requires jnlp.jar.
            
            URL url;
            try{
                url=new URL(URL_HOME);
            }catch(MalformedURLException e){
                suceeded=false;
                e.printStackTrace();
                return;
            }
            suceeded=false;
            
            try {
                // Lookup the javax.jnlp.BasicService object
                BasicService bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
                // Invoke the showDocument method
                suceeded=bs.showDocument(url);
            } catch(UnavailableServiceException ue) {
                JOptionPane.showMessageDialog(null, "Couldn't launch browser to show "+URL_HOME,"Can't launch browser", JOptionPane.ERROR_MESSAGE);
                // Service is not supported
                suceeded=false;
            }
        }
        
        if(!suceeded){
            webPageMenuItem.setText("Couldn't launch browser for "+URL_HOME);
            webPageMenuItem.setEnabled(false);
        }
        
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
        
        
        
    }//GEN-LAST:event_webPageMenuItemActionPerformed
    
    /** called when GUI update timer fires */
    /** called to show photoreceptor shapes */
    private void receptorViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receptorViewMenuItemActionPerformed
        tangentScreen.setPhotoreceptorsShown(!tangentScreen.isPhotoreceptorsShown());
        tangentScreen.repaint();
    }//GEN-LAST:event_receptorViewMenuItemActionPerformed
    
    /** called for grating stimulus selection */
    private void gratingRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gratingRadioButtonMenuItemActionPerformed
        ShapeList newShapes=StimulusShapeFactory.makeGratingShape();
        stimulus.getTransforms().setGeometry(newShapes);
        tangentScreen.repaint();
    }//GEN-LAST:event_gratingRadioButtonMenuItemActionPerformed
    
    private void edgeRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edgeRadioButtonMenuItemActionPerformed
        ShapeList newShapes=StimulusShapeFactory.makeEdgeShape();
        stimulus.getTransforms().setGeometry(newShapes);
        tangentScreen.repaint();
        
    }//GEN-LAST:event_edgeRadioButtonMenuItemActionPerformed
    
    private void barRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barRadioButtonMenuItemActionPerformed
        ShapeList newShapes=StimulusShapeFactory.makeBarShape();
        stimulus.getTransforms().setGeometry(newShapes);
        tangentScreen.repaint();
    }//GEN-LAST:event_barRadioButtonMenuItemActionPerformed
    
    private boolean simulationEnabled=false;
    private void setSimulationEnabled(boolean f){simulationEnabled=f;}
    private boolean isSimulationEnabled(){ return simulationEnabled;}
    
    //    private Timer simulationTimer=null;
    //    private Random r=new Random();
    //    private SpikeSound spikeSound=new SpikeSound();
    
    // this action listener is called for a simulation update by the timer
    //    private ActionListener simulationListener=new ActionListener(){
    //        public void actionPerformed(ActionEvent e){
    //            if(ch.unizh.ini.friend.Main.simulation!=null){
    //                friend.Main.simulation.step();
    //                ServesOutput cell=(ServesOutput)Main.centerPhotoreceptor;
    //                float activity=cell.output();
    //                statusPanel.setActivity(activity);
    //                if(r.nextFloat()<activity) spikeSound.play();
    //            }
    //
    //        }
    //    };
    
    /** the interval in ms between simulation updates */
    public static final int SIMULATION_UPDATE_INTERVAL_MS=10;
    
    /** toggle simulation running */
    public void toggleSimulation(){
        if(isSimulationEnabled())
            stopSimulation();
        else
            startSimulation();
    }
    
    /** start the simulation running. Does nothing if simulation is already running. */
    public void startSimulation(){
        if(isSimulationEnabled()) return;  // don't start twice
        simulationSetup.getSimulation().start();
        setSimulationEnabled(true);
        statusPanel.setPlottingEnabled(true);
    }
    
    /** stops the simulation. Does nothing if simulation is alraedy stopped. */
    public void stopSimulation(){
        statusPanel.setPlottingEnabled(false);
        if(!isSimulationEnabled()) return;
        setSimulationEnabled(false);
        simulationSetup.getSimulation().stop();
    }
    
    
    // come here when window resized.
    // we make sure tangent screen is resized and we store the new window size as a preference
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if(tangentScreen!=null){
            tangentScreen.refillFrame();
        }
        // see http://developer.java.sun.com/developer/technicalArticles/releases/preferences/
        // and http://java.sun.com/j2se/1.4/docs/guide/lang/preferences.html
        //        if(prefs==null)  prefs=Preferences.userNodeForPackage(this.getClass());
        //
        //        //        System.out.println("in formComponentResized()");
        //        prefs.putInt(WIDTH, getSize().width);
        //        prefs.putInt(HEIGHT, getSize().height);
        //        //        System.out.println("flushing prefs for window size "+getSize().width+" by "+getSize().height);
        //        try{ prefs.flush(); }catch(BackingStoreException e){System.out.println("Can't store preferences for window size, sorry...");}
        
    }//GEN-LAST:event_formComponentResized
    
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        AboutDialog a= new AboutDialog(new javax.swing.JFrame("About"), false);
        a.setLocation((int)d.getWidth()/2-a.getWidth()/2,(int)d.getHeight()/2-a.getHeight()/2);
        a.show();
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new FriendGUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup cellButtonGroup;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JCheckBoxMenuItem fadeSpikesCheckBoxMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JRadioButtonMenuItem barRadioButtonMenuItem;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JMenuItem quickStartMenuItem;
    private javax.swing.JMenuItem brightenForegroundStimulus;
    private javax.swing.JRadioButtonMenuItem colorSimulationButton;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem toggleStimulusVisibility;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JMenuItem webPageMenuItem;
    private javax.swing.JCheckBoxMenuItem fullScreenCheckBoxMenuItem;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.ButtonGroup simulationButtonGroup;
    private javax.swing.JMenuItem invertStimulusContrast;
    private javax.swing.JMenuItem makeShorter;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JMenuItem hotkeytMenuItem;
    private javax.swing.JRadioButtonMenuItem fromMicrophoneMenuButtonItem;
    private javax.swing.JMenu recordMenu;
    private javax.swing.JCheckBoxMenuItem hideCellNameMenuItem;
    private javax.swing.JMenuItem makeWider;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JMenuItem setPersistenceMenuItem;
    private javax.swing.JCheckBoxMenuItem receptorViewMenuItem;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JRadioButtonMenuItem gratingRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem monochromeSimulationButton;
    private javax.swing.JMenuItem darkenBackgroundStimulus;
    private javax.swing.JMenuItem markScreenMenuItem;
    private javax.swing.JRadioButtonMenuItem fromSimulationMenuButtonItem;
    private javax.swing.JRadioButtonMenuItem spotStimulusRadioButtonItem;
    private javax.swing.JCheckBoxMenuItem recordCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem mutedViewMenuItem;
    private javax.swing.JMenu cellMenu;
    private javax.swing.JMenu stimulusMenu;
    private javax.swing.ButtonGroup stimulusButtonGroup;
    private javax.swing.JMenuItem makeTaller;
    private javax.swing.JRadioButtonMenuItem edgeRadioButtonMenuItem;
    private javax.swing.ButtonGroup recordButtonGroup;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JMenuItem simulationPropertiesMenuItem;
    private javax.swing.JMenuItem brightenBackgroundStimulus;
    private javax.swing.JMenu simulationMenu;
    private javax.swing.JMenuItem darkenForegroundStimulus;
    private javax.swing.JMenuItem makeNarrower;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem mysteryCellItem;
    private javax.swing.JCheckBoxMenuItem fillSpikesCheckBoxMenuItem;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem rotateStimulus;
    private javax.swing.JMenuItem helpContents;
    private javax.swing.JCheckBoxMenuItem toggleSimulationRunning;
    private javax.swing.JMenuItem clearSpikesMenuItem;
    // End of variables declaration//GEN-END:variables
    
}

/*
 $Log: FriendGUI.java,v $
 Revision 1.66  2004/02/09 06:35:17  tobi
 cleanup with refactor

 Revision 1.65  2003/07/06 05:22:01  tobi
 *** empty log message ***

 Revision 1.64  2003/06/26 00:33:41  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.63  2003/06/23 11:30:16  tobi
 greatly improved recording display speed, capability

 added full screen exclusive display

 Revision 1.62  2003/06/15 19:17:30  tobi
 added capability of recording spikes from simulation or from microphone and plotting the
 corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 kind of a spike-tirggered average is possible.
 
 Revision 1.61  2003/06/12 06:28:45  tobi
 added microphone recording for hooking up friend chip. not fully functional yet.
 
 Revision 1.60  2003/06/05 13:19:17  tobi
 changed menu to luminance from monochrome
 
 Revision 1.59  2003/05/10 17:27:42  jgyger
 Merge from color-branch
 
 Revision 1.58.2.7  2003/05/10 16:44:20  tobi
 fixed missing change of cellMenu in setSimulationSetup
 
 Revision 1.58.2.6  2003/05/10 15:10:46  tobi
 
 fixed merge conflict.
 
 Revision 1.58.2.5  2003/05/10 15:05:00  tobi
 javadoc
 
 Revision 1.58.2.2  2003/05/08 17:12:40  tobi
 Simulation type is now selectable from the GUI under the Simulation menu.
 Splash screen now includes all authors always, since simulation selectable.
 About dialog contains CVS rev date for About box.
 
 Revision 1.58.2.1  2003/03/16 16:25:03  jgyger
 move starting cell name into simulation setup
 
 Revision 1.58  2002/10/25 09:52:37  cmarti
 - AcceptsInput and Monitor don't implement Updateable anymore
 - Compile fixes in ManyInputs and FriendGUI
 
 Revision 1.57  2002/10/24 12:05:49  cmarti
 add GPL header
 
 Revision 1.56  2002/10/24 07:37:35  tobi
 checking for zero screen size now to deal with funny window managers that don't supply screen size
 
 Revision 1.55  2002/10/21 14:37:40  tobi
 added UML diagrams for some packages.
 added check for missing help to freindGUI
 
 Revision 1.54  2002/10/20 23:45:29  tobi
 added mystery cells.
 
 Revision 1.53  2002/10/20 15:15:48  tobi
 implemented help system by using help system methods to add actionlisteners that
 give hotkey, contents, and quickstart help on selection of help menu items.
 added F1 help to show general help for pressing F1 anywhere in rootpane context.
 
 Revision 1.52  2002/10/19 15:48:16  tobi
 added help system
 
 Revision 1.51  2002/10/17 12:48:58  tobi
 fixed web page launcher to use browser launcher when not running
 from jws, and basicservice when running from jws.
 
 Revision 1.50  2002/10/16 11:29:28  tobi
 Stimulus now has methods for geometrcal transformations of stimulus
 size and rotation. AbstractStimulus implements these methods.
 TangentScreen and FriendGUI now use these unified transformation methods.
 
 activitymeter now shows the spike rate in spikes/second instead of spikes/measurement interval.
 
 Revision 1.49  2002/10/13 16:29:09  tobi
 many small changes from tuebingen trip.
 
 Revision 1.48  2002/10/11 09:24:38  tobi
 made cell combo box to select cell type functional.  removed display of stimulus type
 becaus user can see it.
 
 Revision 1.47  2002/10/11 08:43:51  tobi
 started changing status bar to show combobox for selecting cell type.
 
 Revision 1.46  2002/10/10 15:22:06  tobi
 moved accelerators for changing stimulus to FriendGUI and added stimulus controls
 to Stimulus menu.
 
 Revision 1.45  2002/10/10 12:55:04  tobi
 added commented attempt to read help html from jar.  this doesn't work because
 browsers don't know how to read from a jar file.
 
 Revision 1.44  2002/10/10 07:36:55  tobi
 added more stimulus control menu ijtems
 
 Revision 1.43  2002/10/08 16:24:02  tobi
 simulation now starts at startup.
 all menu items have mnumonics.
 F1 to bring up hotkeys is launched from GUI not tangent screen.
 repaints added to tangent screen to repaint after changes to sitmulus.
 
 Revision 1.42  2002/10/08 15:36:18  tobi
 fixed polarity of hide switch
 
 Revision 1.41  2002/10/08 14:53:23  tobi
 minor changes to make preliminary release tag
 
 Revision 1.40  2002/10/08 13:00:13  tobi
 choosing starting cell from SimulationSetupFactory public static.
 
 Revision 1.39  2002/10/08 07:49:49  tobi
 added capability to hide cell name.  other minor twiddling.
 
 Revision 1.38  2002/10/07 19:57:06  tobi
 GUI cells changed to Map from HashMap because underlying map is now TreeMap (which is sorted).
 
 TangentScreen popup menu now works.
 
 Revision 1.37  2002/10/07 14:43:34  tobi
 no change
 
 Revision 1.36  2002/10/07 13:02:07  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.
 
 Revision 1.35  2002/10/06 10:51:18  tobi
 fixed javadoc links
 
 Revision 1.34  2002/10/06 08:56:06  tobi
 added activity bar  and put in to right side of screen.
 removed status bar activity meter
 added menu item accelerator to show photoreceptors.,
 changed simulation start/stop to toggle with enter.
 changed "Plot" menu name to "Simulation"
 changed mouse cursor in TangentScrren to be a move cursor.
 
 Revision 1.33  2002/10/05 21:19:25  tobi
 added audio muting to view menu
 
 Revision 1.32  2002/10/03 14:34:57  tobi
 added web page launching for help.  this depends on jnlp.jar, which is part
 of the web start developers kit.  jnlp.jar is committed as Java/3rdPartyJara/jnlp.jar.
 i don't know if the code runs without jnlp installed.
 
 Revision 1.31  2002/10/02 21:47:34  tobi
 initial version of new exception dialog and usage of it in GUI
 
 Revision 1.30  2002/10/01 18:57:22  tobi
 ch.unizh.ini in commented out code fixed
 
 Revision 1.29  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy
 
 Revision 1.28  2002/09/30 20:31:34  tobi
 
 now checking if Preferences throws exception or error. This happens when running
 from web start, so in that case, we just don't store or read preferences...
 
 Revision 1.27  2002/09/30 12:09:45  tobi
 fixed plotting status so that it is initialized and updated correctly in startSimulation and stopSimulation
 
 Revision 1.26  2002/09/30 08:18:10  tobi
 added starting cell as preference item that is stored and recalled on next startup
 
 Revision 1.25  2002/09/30 06:15:32  tobi
 added window size preference storing using Preferences.  this now works.
 (needed to remove generated setSize(fixedSize) that came from GUI builder.
 
 Revision 1.24  2002/09/29 20:45:18  tobi
 now initializing the status panel and simulation running status to some defaults
 in the constructor.
 
 Revision 1.23  2002/09/29 19:42:53  tobi
 removed timer bean from netbeans examples because we're not using it.
 
 Revision 1.22  2002/09/28 20:00:10  cmarti
 provisional use of the monitoring facility
 
 Revision 1.21  2002/09/28 06:21:19  tobi
 added KeyStrokes for selecting cells to addCell()
 
 Revision 1.20  2002/09/27 10:02:10  tobi
 added audio rendering of cell to cellButtonMenuItemActionPerformed.
 
 Revision 1.19  2002/09/26 21:05:14  tobi
 started adding activity to progress bar, but lacking correct method.
 
 Revision 1.18  2002/09/26 20:30:32  tobi
 automagically adding cell menu items now, using the HashMap of cells constructed when the simulation is built
 by StimulationSetupFactory
 
 Revision 1.17  2002/09/26 16:08:02  tobi
 changed constuctor to single simulationSetup, stimulus is extracted from this.
 
 Revision 1.16  2002/09/25 20:24:54  tobi
 fixed bug in referencing photoreceptorShapes in tangent scrren.
 the simulationSetup and stimulus are now passed into TangentScrren
 and the receptor shapes are extracted from the simulationSetup by tangentScreen directly.
 
 Revision 1.15  2002/09/25 17:18:56  tobi
 added more cells to listen to, not functional yet.
 
 Revision 1.14  2002/09/25 16:50:01  tobi
 changed simulation setup to pass in simulation and stimulus on constrction
 so that we have a handle to the simulation for grabbing hold of cells, and
 to remove refs to statics in Main.
 
 Revision 1.13  2002/09/25 16:47:50  tobi
 fixed spelling typo
 
 Revision 1.12  2002/09/24 19:50:56  tobi
 recommited
 
 Revision 1.11  2002/09/24 07:33:36  tobi
 started to fix bug where new stimulus was not being passed to simulation, but
 this requies method to change stimulus passed to photoreceptors in simulation.
 
 Revision 1.10  2002/09/24 04:45:57  tobi
 fixed typo, missing ; on actionlistener
 
 Revision 1.9  2002/09/24 04:41:33  tobi
 added capabibilty to view photoreceptor shapes and added view menu to hold command.
 added rendering of photoreceptor shapes to paintComponent().
 
 Revision 1.8  2002/09/23 15:18:44  tobi
 the activity now shows up on the status panel
 
 Revision 1.7  2002/09/23 10:38:13  tobi
 added status line to gui
 
 Revision 1.6  2002/09/23 06:47:16  tobi
 added spike sound to simulation timer update.  doesn't work yet because photoreceptor output is not sensible
 
 Revision 1.5  2002/09/22 19:29:16  tobi
 changed stimulus and cell setlection menus to button menu items
 
 Revision 1.4  2002/09/21 20:34:14  tobi
 minor changes to javadoc and to activate some menus (stimuli) in FriendGUI
 
 Revision 1.3  2002/09/19 06:51:34  tobi
 added more controls for stimulus, including scaling.
 added simple help dialog to show hotkeys
 activated help/contents to show help dialog
 
 stimulus now moves, scales, rotates, and changes constrast in a usable manner.
 
 Revision 1.2  2002/09/15 21:45:43  tobi
 about dialog added
 
 window resize resizes tangent screen
 added prototype keyevent listener to tangent screen.
 
 Revision 1.1  2002/09/15 08:02:50  tobi
 more revs to menus, still not functional
 
 Revision 1.2  2002/09/13 21:03:06  tobi
 started adding menu items
 
 Revision 1.1  2002/09/09 18:49:22  tobi
 initial add
 
 Revision 1.2  2002/09/03 06:58:33  tobi
 initial commits, not yet functional
 
 Revision 1.1  2002/09/03 06:57:56  tobi
 initial commits, not yet functional
 */

