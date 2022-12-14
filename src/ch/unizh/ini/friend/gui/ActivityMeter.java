/*
 $Id: ActivityMeter.java,v 1.4 2002/10/24 12:05:49 cmarti Exp $
 

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

 
 
 $Log: ActivityMeter.java,v $
 Revision 1.4  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.3  2002/10/16 11:29:28  tobi
 Stimulus now has methods for geometrcal transformations of stimulus
 size and rotation. AbstractStimulus implements these methods.
 TangentScreen and FriendGUI now use these unified transformation methods.

 activitymeter now shows the spike rate in spikes/second instead of spikes/measurement interval.

 Revision 1.2  2002/10/08 14:40:47  tobi
 fixed sizes and layout to stretch correclly using border layout -- after a long struggle...

 
 * ActivityMeter.java
 *
 * Created on October 6, 2002, 8:45 AM
 */

package ch.unizh.ini.friend.gui;

import ch.unizh.ini.friend.simulation.cells.*;
import java.text.*;

/**
 * Shows activity of monitored neuron. 
 * @author  tobi
 */
public class ActivityMeter extends javax.swing.JPanel {
    
    private float activity = 0f;
    
    /** Creates new form ActivityMeter */
    public ActivityMeter() {
        initComponents();
    }
    
    /** Set the cell activity level shown. Activity is shown on a pair of progress bars and in a text box.
     Positive activity is shown in green, negative in red.
     @param activity a float ranging from -2 to 2
     */
    public void setActivity(float activity) {
        
        activityTextField.setText(activityFormat.format(activity/IntegrateFireCell.SPIKE_RATE_TIME_SCALE));
        if(activity>0){
            positiveActivityBar.setValue(Math.round(activity*50));
            negativeActivityBar.setValue(100);
        }else{
            positiveActivityBar.setValue(0);
            negativeActivityBar.setValue(100-Math.round(-activity*50));
        }
    }

    // format for cell activity
    private NumberFormat activityFormat = NumberFormat.getInstance(); {
        activityFormat.setMaximumFractionDigits(1);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        positiveActivityBar = new javax.swing.JProgressBar();
        negativeActivityBar = new javax.swing.JProgressBar();
        activityTextField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(33, 500));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(30, 400));
        positiveActivityBar.setBackground(new java.awt.Color(0, 0, 0));
        positiveActivityBar.setForeground(new java.awt.Color(51, 255, 51));
        positiveActivityBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
        positiveActivityBar.setToolTipText("Excitation");
        positiveActivityBar.setBorder(null);
        positiveActivityBar.setBorderPainted(false);
        positiveActivityBar.setMaximumSize(new java.awt.Dimension(30, 32767));
        positiveActivityBar.setMinimumSize(new java.awt.Dimension(30, 100));
        positiveActivityBar.setPreferredSize(new java.awt.Dimension(30, 100));
        jPanel1.add(positiveActivityBar, java.awt.BorderLayout.CENTER);

        negativeActivityBar.setBackground(new java.awt.Color(255, 0, 0));
        negativeActivityBar.setForeground(new java.awt.Color(0, 0, 0));
        negativeActivityBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
        negativeActivityBar.setToolTipText("Inhibition");
        negativeActivityBar.setValue(100);
        negativeActivityBar.setBorder(null);
        negativeActivityBar.setBorderPainted(false);
        negativeActivityBar.setMaximumSize(new java.awt.Dimension(30, 32767));
        negativeActivityBar.setMinimumSize(new java.awt.Dimension(30, 100));
        negativeActivityBar.setPreferredSize(new java.awt.Dimension(30, 100));
        jPanel1.add(negativeActivityBar, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        activityTextField.setBackground(new java.awt.Color(1, 1, 1));
        activityTextField.setColumns(3);
        activityTextField.setEditable(false);
        activityTextField.setFont(new java.awt.Font("Dialog", 0, 14));
        activityTextField.setForeground(new java.awt.Color(255, 255, 255));
        activityTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        activityTextField.setText("act");
        activityTextField.setToolTipText("Show cell activity, either graded or spike rate");
        activityTextField.setMinimumSize(new java.awt.Dimension(30, 23));
        activityTextField.setPreferredSize(new java.awt.Dimension(33, 23));
        add(activityTextField, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar negativeActivityBar;
    private javax.swing.JProgressBar positiveActivityBar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField activityTextField;
    // End of variables declaration//GEN-END:variables
    
}
