/*

 * JDialog.java
 *
 * Created on September 19, 2002, 7:27 AM
  $Id: HelpDialog.java,v 1.8 2002/10/24 12:05:49 cmarti Exp $
 

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

 
 $Log: HelpDialog.java,v $
 Revision 1.8  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.7  2002/10/20 15:14:19  tobi
 deprecated in favor of help system

 Revision 1.6  2002/10/06 12:32:42  tobi
 made hot key table a table

 Revision 1.5  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.4  2002/09/29 19:46:34  tobi
 activated OK button to close help window

 Revision 1.3  2002/09/25 08:41:26  tobi
 fixed javadoc so no errors generated and added package descriptions.

 Revision 1.2  2002/09/21 20:34:14  tobi
 minor changes to javadoc and to activate some menus (stimuli) in FriendGUI

 */

package ch.unizh.ini.friend.gui;

import javax.swing.JDialog;

/**
 * The Help dialog.
 @deprecated Replaced by the help system 
 @see FriendHelp
 * @author  tobi
  @version $Revision: 1.8 $

 */
public class HelpDialog extends javax.swing.JDialog {
    
    /** Creates new form JDialog */
    public HelpDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        helpLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        okButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.FlowLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        helpLabel.setText("<html>\n<body>\n<table cellpadding=\"1\" cellspacing=\"1\" border=\"1\" width=\"400\">\n  <tbody>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">r<br>\n      </td>\n      <td valign=\"top\" width=\"300\"><b>R</b>otate stimulus Pi/6<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">SPACE<br>\n      </td>\n      <td valign=\"top\" width=\"300\">Flash stimulus<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">i<br>\n      </td>\n      <td valign=\"top\" width=\"300\"><b>I</b>nvert contrast<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">b|d<br>\n      </td>\n      <td valign=\"top\" width=\"300\">Make foreground <b>b</b>righter|<b>d</b>arker<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">B|D<br>\n      </td>\n      <td valign=\"top\" width=\"300\">Make background <b>B</b>righter|<b>D</b>arker<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">UP|DOWN arrows<br>\n      </td>\n      <td valign=\"top\" width=\"300\">Increase|Decrease height<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">LEFT|RIGHT arrows<br>\n      </td>\n      <td valign=\"top\" width=\"300\">Increase|Decrese width<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\" width=\"150\">ENTER<br>\n      </td>\n      <td valign=\"top\" width=\"300\">Toggle simulation running<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\">\n      <blockquote>m<br>\n      </blockquote>\n      </td>\n      <td valign=\"top\">Mute audio<br>\n      </td>\n    </tr>\n    <tr>\n      <td valign=\"top\" align=\"right\">\n      <blockquote>p<br>\n      </blockquote>\n      </td>\n      <td valign=\"top\">Show Photoreceptor shapes<br>\n      </td>\n    </tr>\n  </tbody>\n</table>\n<br>\n</body>\n</html>\n\n\n\n");
        getContentPane().add(helpLabel);

        getContentPane().add(jSeparator1);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        getContentPane().add(okButton);

        pack();
    }//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new JDialog(new javax.swing.JFrame(), true).show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
}
