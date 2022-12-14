/*
 
 $Id: StatusPanel.java,v 1.22 2004/02/09 06:35:18 tobi Exp $
 
 
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
 * Created on September 23, 2002, 10:36 AM
 
 */

package ch.unizh.ini.friend.gui;

import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.simulation.Updateable;
import ch.unizh.ini.friend.stimulus.*;
import java.util.*;
import javax.swing.*;

/**
 * Shows the status of the simulation, and more important, displays a combo box that users use to choose the cell
 * to be monitored.
 *
 * @author  tobi
 * @version $Revision: 1.22 $
 *
 */
public class StatusPanel extends javax.swing.JPanel implements Updateable {
    
    private String cellName = "ON Ganglion Cell";
    
    private float activity = 0f;
    
    private String stimulusName = "Bar";
    
    private SimulationSetup setup;
    private Stimulus stimulus;
    
    /** Creates new form StatusPanel
     * @param setup the simulation
     * @param stimulus the stimulus
     */
    public StatusPanel(SimulationSetup setup, Stimulus stimulus) {
        this.stimulus=stimulus;  // not used but left in for future possible use of display of combo box for stimulus selection
        initComponents();
        setRequestFocusEnabled(false);
        setSimulationSetup(setup);
        setFocusable(false);   // this is 1.4+ method
    }
    
    /** sets the {@link ch.unizh.ini.friend.simulation.SimulationSetup}. This removes all cells from comboBox chooser and
     *then rebuilds the combo box from the {@link ch.unizh.ini.friend.simulation.SimulationSetup#getOutputCells output cells} of the
     *new simulation.
     * @param setup the {@link ch.unizh.ini.friend.simulation.SimulationSetup}
     */
    public void setSimulationSetup(SimulationSetup setup){
        this.setup=setup;
//        System.out.println("StatusPanel: rebuilding cell combo box");
        // fill the combo box for choosing the cell
        cellComboBox.removeAllItems(); // in case setup has changed
        Map cells=setup.getOutputCells();
        Iterator i=cells.entrySet().iterator();
        while(i.hasNext()){
            Map.Entry m=(Map.Entry)i.next();
            cellComboBox.addItem(m.getKey());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        plottingEnabledTextField = new javax.swing.JTextField();
        cellComboBox = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(0, 0, 0));
        setToolTipText("Status");
        setMaximumSize(new java.awt.Dimension(32767, 30));
        plottingEnabledTextField.setBackground(new java.awt.Color(0, 0, 0));
        plottingEnabledTextField.setColumns(8);
        plottingEnabledTextField.setEditable(false);
        plottingEnabledTextField.setForeground(new java.awt.Color(255, 255, 255));
        plottingEnabledTextField.setToolTipText("Is plotting running now?");
        add(plottingEnabledTextField);

        cellComboBox.setBackground(new java.awt.Color(0, 0, 0));
        cellComboBox.setForeground(new java.awt.Color(255, 255, 255));
        cellComboBox.setMaximumRowCount(20);
        cellComboBox.setToolTipText("Selects the cell to monitor");
        cellComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cellComboBoxItemStateChanged(evt);
            }
        });

        add(cellComboBox);

    }//GEN-END:initComponents

    private void cellComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cellComboBoxItemStateChanged
            JComboBox cb = (JComboBox)evt.getSource();
            String cellName = (String)cb.getSelectedItem();
            if(cellName==null) return; // we get events when combo box is emptied on simulation change but there is no cell selected yet
            setup.setMonitoredCell(cellName);
            if(getParent()!=null){
                getParent().requestFocus();
            }
    }//GEN-LAST:event_cellComboBoxItemStateChanged
        
    /** Updates the actual state to the newly computed - aka double-buffering.
     */
    public void update() {
    }
    
    /** Set the name of the cell being listened to.
     * This is shown in the status bar unless it is set invisible by {@link #setCellNameVisible}.
     */
    public void setCellName(String name) {
        Set m=setup.getOutputCells().entrySet();
        Iterator i=m.iterator();
        int n=0;
        while(i.hasNext()){
            Map.Entry e=(Map.Entry)i.next();
            if(e.getKey().equals(name)){
                cellComboBox.setSelectedIndex(n);
                break;
            }
            n++;
        }
    }
    
    private boolean cellVisibleEnabled=true;
    
    /** sets visibility of cell name. This can be used to hide the cell type.
     * @param f true means show cell name
     * @see #setCellName
     */
    public void setCellNameVisible(boolean f){
        cellComboBox.setVisible(f);
    }
    
    /** is cell name visible? */
    public boolean isCellNameVisible(){ return cellComboBox.isVisible(); }
    
    //    // format for cell activity
    //    private NumberFormat activityFormat = NumberFormat.getInstance(); {
    //        activityFormat.setMaximumFractionDigits(2);
    //    }
    
    /** set plotting running now status */
    public void setPlottingEnabled(boolean f){
        if(f)
            plottingEnabledTextField.setText("Running");
        else
            plottingEnabledTextField.setText("Not running");
    }
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
    }
    
    // not needed with parent's border layout
    //    /** overrides the preferred size */
    //    public Dimension getPreferredSize(){
    //        Dimension d=getParent().getSize();
    //        Dimension d2=new Dimension((int)d.getWidth(),(int)getMinimumSize().getHeight());
    //        //System.err.println("d2 = " + d2 );
    //        return d2;
    //    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField plottingEnabledTextField;
    private javax.swing.JComboBox cellComboBox;
    // End of variables declaration//GEN-END:variables
    
}

/*
 
 
 
 $Log: StatusPanel.java,v $
 Revision 1.22  2004/02/09 06:35:18  tobi
 cleanup with refactor

 Revision 1.21  2003/06/26 00:33:41  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.20  2003/06/15 19:17:30  tobi
 added capability of recording spikes from simulation or from microphone and plotting the
 corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 kind of a spike-tirggered average is possible.

 Revision 1.19  2003/05/10 17:27:43  jgyger
 Merge from color-branch
 
 Revision 1.18.2.3  2003/05/10 15:05:00  tobi
 javadoc
 
 Revision 1.18.2.2  2003/05/10 14:53:51  tobi
 changed max count in cell combo box to 20 to show all cells.
 
 Revision 1.18.2.1  2003/05/08 17:12:40  tobi
 Simulation type is now selectable from the GUI under the Simulation menu.
 Splash screen now includes all authors always, since simulation selectable.
 About dialog contains CVS rev date for About box.
 
 Revision 1.18  2002/10/24 12:05:49  cmarti
 add GPL header
 
 Revision 1.17  2002/10/15 09:44:36  tobi
 resolved conflict with requestFocus
 
 Revision 1.16  2002/10/13 16:29:09  tobi
 many small changes from tuebingen trip.
 
 Revision 1.15  2002/10/11 09:24:38  tobi
 made cell combo box to select cell type functional.  removed display of stimulus type
 becaus user can see it.
 
 Revision 1.14  2002/10/11 08:43:51  tobi
 started changing status bar to show combobox for selecting cell type.
 
 Revision 1.13  2002/10/10 07:37:21  tobi
 tried to keep focus from being stolen from tangent screen.
 
 Revision 1.12  2002/10/09 10:52:41  tobi
 fixed status panel cell descriptor width.
 fixed onestep.html to more clearly specify what's happening.
 
 Revision 1.11  2002/10/08 07:49:49  tobi
 added capability to hide cell name.  other minor twiddling.
 
 Revision 1.10  2002/10/07 13:02:08  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.
 
 Revision 1.9  2002/10/06 08:56:06  tobi
 added activity bar  and put in to right side of screen.
 removed status bar activity meter
 added menu item accelerator to show photoreceptors.,
 changed simulation start/stop to toggle with enter.
 changed "Plot" menu name to "Simulation"
 changed mouse cursor in TangentScrren to be a move cursor.
 
 Revision 1.8  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy
 
 Revision 1.7  2002/09/29 20:44:23  tobi
 added negative activity bar and fixed sizes of components to fit.
 
 Revision 1.6  2002/09/25 17:19:50  tobi
 added progress bar to monitor graded cell output.
 
 Revision 1.5  2002/09/24 07:35:01  tobi
 added CVS tags
 
 */
