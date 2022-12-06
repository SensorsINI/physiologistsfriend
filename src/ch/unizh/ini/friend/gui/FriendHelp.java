/*
 * FirendHelp.java
 *
 * Created on October 19, 2002, 8:40 AM
 $Id: FriendHelp.java,v 1.6 2004/02/09 06:35:18 tobi Exp $
 

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

 
 $Log: FriendHelp.java,v $
 Revision 1.6  2004/02/09 06:35:18  tobi
 cleanup with refactor

 Revision 1.5  2002/11/08 17:36:27  cmarti
 fix javadoc

 Revision 1.4  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.3  2002/10/21 14:37:40  tobi
 added UML diagrams for some packages.
 added check for missing help to freindGUI

 Revision 1.2  2002/10/20 15:15:48  tobi
 implemented help system by using help system methods to add actionlisteners that
 give hotkey, contents, and quickstart help on selection of help menu items.
 added F1 help to show general help for pressing F1 anywhere in rootpane context.

 Revision 1.1  2002/10/19 15:47:49  tobi
 initial version

 
 */

package ch.unizh.ini.friend.gui;

import java.awt.event.*;
import java.net.*;
import javax.help.*;
import javax.swing.*;

/**
 * Shows help system. The {@link #actionPerformed} method is used when an instance of this class is registered with the
 gui's help contents menu item.
 
 * @author  $Author: tobi $
 @since $Revision: 1.6 $
 
 */
public class FriendHelp implements ActionListener {
        /*
         1. Import the JavaHelp system classes:
    import javax.help.*;
    [IMAGE]
    Be sure to add one of the JavaHelp system libraries (for example, jh.jar) to your application?s
    CLASSPATH.
    2. Find the HelpSet file and create the HelpSet object:
    try {
    URL hsURL = HelpSet.findHelpSet(null, "../help/myHelpSet.hs");
    hs = new HelpSet(null, hsURL);
    } catch (Exception ee) {
    System.out.println("HelpSet "+helpsetName+" not found")
    return;
    }
    3. Create a HelpBroker object:
    hb = hs.createHelpBroker();
    4. Create a "Help" menu item to trigger the help viewer:
    JMenu help = new JMenu("Help");
    menuBar.add(help);
    menu_help = new JMenuItem(helpsetLabel);
    menu help.addActionListener(new CSH.DisplayHelpFromSource(mainHB));
         */
    
    /** the <code>HelpSet</code> */
    public HelpSet hs;
    /** the <code>HelpBroker</code> */
    public HelpBroker hb;

    //    JHelp help;
    
    /** relative URL of help set file */
    public static final String HELPSET_URL="friend-help.hs";
    
    /** Creates a new instance of FirendHelp. Makaes and loads the {@link HelpSet} and makes the {@link HelpBroker}.
     @param gui the parent component. This is used to show the error message dialog when help is not found.
     */
    public FriendHelp(JFrame gui) {
        try {
            // the friend-help.jar archive of the help files must be on the classpath.
            // for running from netbeans, that means the jar must be mounted as a jar filesystem.
            // for running as an application, the jar must be in the classpath that is given to java on execution.
            // for running from java web start, the help jar must be one of the resources specified in the .jnlp file
            ClassLoader cl=FriendHelp.class.getClassLoader();
            URL hsURL=cl.getResource(HELPSET_URL);
//            URL hsURL = ClassLoader.getSystemResource(HELPSET_URL);
//            URL hsURL = HelpSet.findHelpSet(null, HELPSET_URL);
//            System.out.println("hsURL="+hsURL);
            hs = new HelpSet(null, hsURL);
            hb=hs.createHelpBroker();
        } catch (Exception ee) {
            try{
                JOptionPane.showMessageDialog(gui,"Couldn't load help from "+HELPSET_URL,"Help will not be available",JOptionPane.ERROR_MESSAGE);
                ee.printStackTrace();
            }catch(Exception e){}
        }
    }
    
    /**
     Shows the help system. This method uses {@link javax.help.CSH.DisplayHelpFromSource#actionPerformed} to show the help system
     in a {@link JHelp} window.
     @param e the event
     */
    public void actionPerformed(ActionEvent e) {
        if(hs==null) return;
//            System.out.println("showing help");
//            help=new JHelp(hs);
//            help.show();
        if(e.getActionCommand().equals("Hot keys")){
            try{hb.setCurrentID("Hot_Keys");}catch(BadIDException badid){badid.printStackTrace();}
            new CSH.DisplayHelpFromSource(hb).actionPerformed(e);
        }else{
            new CSH.DisplayHelpFromSource(hb).actionPerformed(e);
        }
    }    
}
