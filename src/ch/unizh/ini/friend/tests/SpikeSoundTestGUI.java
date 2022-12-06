/*
 * SpikeSoundTestGUI.java
 
 $Id: SpikeSoundTestGUI.java,v 1.3 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: SpikeSoundTestGUI.java,v $
 Revision 1.3  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.2  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/13 20:48:38  tobi
 inital add

 
 *
 * Created on September 13, 2002, 9:24 PM
 */

package ch.unizh.ini.friend.tests;

/**
 * Makes a tiny GUI for testing spike sound generation. Used by {@link SpikeSoundTest}.
 * @author  tobi
 @version $Revision: 1.3 $
 
 */
public class SpikeSoundTestGUI extends javax.swing.JFrame {
    
    /** Creates new form SpikeSoundTestGUI */
    public SpikeSoundTestGUI() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jSlider1 = new javax.swing.JSlider();

        setTitle("SpikeRate Control");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jSlider1.setToolTipText("Slide to set rate of spikes");
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        getContentPane().add(jSlider1, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        SpikeSoundTest.spikeRate=(float)jSlider1.getValue();
    }//GEN-LAST:event_jSlider1StateChanged
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new SpikeSoundTestGUI().show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables
    
}
