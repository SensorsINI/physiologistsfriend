/*
 $Id: StimulusAction.java,v 1.3 2004/02/09 06:35:19 tobi Exp $
 

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

 
 $Log: StimulusAction.java,v $
 Revision 1.3  2004/02/09 06:35:19  tobi
 cleanup with refactor

 Revision 1.2  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.1  2002/10/15 10:13:45  tobi
 initial version

 
 * StimulusAction.java
 *
 * Created on October 12, 2002, 2:48 PM
 */

package ch.unizh.ini.friend.gui;

import ch.unizh.ini.friend.stimulus.*;
import java.awt.event.*;
import javax.swing.*;

/**
 Actions that can happen to the stimulus.
 
 Use these classes in a GUI by simply adding a new action to a menu, e.g.:
 
 <pre>
 Stimulus stimulus; // defined earlier
 menu.add(new StimulusAction.Rotate(stimulus);
 </pre>
 
 The {@link #actionPerformed} method will be called automagically when the menu item is selected.
     
 @author  tobi $Author: tobi $
 @version $Revision: 1.3 $
 */
public abstract class StimulusAction extends AbstractAction {
    

    /** name of action that can happen to Stimulus */
    protected static final String rotate="Rotate", 
    brighten="Brighten",
    darken="Darken",
    brightenBackground="Brighten background",
    darkenBackground="Darken background",
    flipContrast="Invert contrast";
    
    /** the stimulus */
    protected Stimulus stimulus;
    
    /** Creates a new instance of StimulusAction */
    protected StimulusAction() {
    }
    
    protected StimulusAction(String name){
        super(name);
    }
    
    protected StimulusAction(String name, Icon icon){
        super(name,icon);
    }
    
    
    /** rotates the stimulus. 
     */
    public static class Rotate extends StimulusAction {

        public Rotate(Stimulus stimulus){
            super(rotate);
            this.stimulus=stimulus;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("X"));
        }
        
        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e) {
            stimulus.getTransforms().rotate((float)Math.PI/6);
        }
        
    }
    

    /** brightens the stimulus */
    public static class Brighten extends StimulusAction {
        public Brighten(Stimulus stimulus){
            super(brighten);
            this.stimulus=stimulus;
        }
        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e) {
            stimulus.brightenForeground();
        }
    }
    
    /** darkens the stimulus */
    public static class Darken extends StimulusAction {
        public Darken(Stimulus stimulus){
            super(darken);
            this.stimulus=stimulus;
        }
        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e) {
            stimulus.darkenForeground();
        }
    }
    
    /** brightens background */
    public static class BrightenBackground extends StimulusAction {
        public BrightenBackground(Stimulus stimulus){
            super(brightenBackground);
            this.stimulus=stimulus;
        }
        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e) {
            stimulus.brightenBackground();
        }
    }
    
    /** darkens background */
    public static class DarkenBackground extends StimulusAction {
        public DarkenBackground(Stimulus stimulus){
            super(darkenBackground);
            this.stimulus=stimulus;
        }
        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e) {
            stimulus.darkenForeground();
        }
    }
    
    /** Swaps foreground and background brightnesses. */
    public static class FlipContrast extends StimulusAction {
        public FlipContrast(Stimulus stimulus){
            super(flipContrast);
            this.stimulus=stimulus;
        }
        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e) {
            stimulus.flipContrast();
        }
    }
    
}
