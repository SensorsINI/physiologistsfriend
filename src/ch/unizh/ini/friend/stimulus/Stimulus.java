/*
 $Id: Stimulus.java,v 1.18 2003/06/15 19:17:32 tobi Exp $
 

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

 
 $Log: Stimulus.java,v $
 Revision 1.18  2003/06/15 19:17:32  tobi
 added capability of recording spikes from simulation or from microphone and plotting the
 corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 kind of a spike-tirggered average is possible.

 Revision 1.17  2003/05/10 17:27:45  jgyger
 Merge from color-branch

 Revision 1.16.2.1  2003/03/16 18:14:16  jgyger
 move excitation method from stimuli to photoreceptors

 Revision 1.16  2002/11/08 16:55:47  cmarti
 remove {@value} tag from RESIZE_RATIO's javadoc

 Revision 1.15  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.14  2002/10/16 11:29:28  tobi
 Stimulus now has methods for geometrcal transformations of stimulus
 size and rotation. AbstractStimulus implements these methods.
 TangentScreen and FriendGUI now use these unified transformation methods.

 activitymeter now shows the spike rate in spikes/second instead of spikes/measurement interval.

 Revision 1.13  2002/10/13 16:29:10  tobi
 many small changes from tuebingen trip.

 Revision 1.12  2002/10/10 07:38:07  tobi
 added command names

 Revision 1.11  2002/10/06 10:57:59  tobi
 fixed javadoc links

 Revision 1.10  2002/10/06 10:51:18  tobi
 fixed javadoc links

 Revision 1.9  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.8  2002/09/25 08:41:26  tobi
 fixed javadoc so no errors generated and added package descriptions.

 Revision 1.7  2002/09/22 19:35:35  tobi
 more methods added to interface
 brighten/darken foreground/background, etc.

 Revision 1.6  2002/09/17 14:26:44  cmarti
 move 'implements Updateable' up to Stimulus

 Revision 1.5  2002/09/16 08:42:10  cmarti
 - adapt excitation() to use Intersectable
 - remove translate(), rotate(), scale() methods

 Revision 1.4  2002/09/15 07:58:43  tobi
 added javadoc about screen coordinates

 Revision 1.3  2002/09/13 12:04:41  cmarti
 add fore-/background excitation density get-/setters

 Revision 1.2  2002/09/13 08:33:48  cmarti
 initial version

 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.graphics.*;
import ch.unizh.ini.friend.simulation.Updateable;

/**
 * Interface that a stimulus has to implement.
  <p>
A stimulus has a background and foreground brightness, a set of shapes, and a transformation.  Methods here give access to these
 chanacteristics of the stimulus.
 <p>
 A stimulus can be transformed using its transformation ({@link #getTransforms}, e.g.:
 <pre>
 <code>
 Stimulus s; // initialized elsewhere
 s.getTransforms().translate(1,1);
 </code>
 </pre>

 Stimulus coordinate system is defined in {@link ch.unizh.ini.friend.gui.TangentScreen} as square coordinate systems with maximum value
 {@link ch.unizh.ini.friend.gui.TangentScreen#SCREEN_DIMENSION} in each direction. Screen is this high and wide and 0,0 is in the center of the screen.
<p>
 These coordinates are scaled on rendering to the size of the {@link ch.unizh.ini.friend.gui.TangentScreen}.
 
 * @author Christof Marti/Tobi Delbruck
 * @version $Revision: 1.18 $
 */
public interface Stimulus extends Updateable {
    
    /** the default foreground brightness: {@value} */
    public static final float FOREGROUND_BRIGHTNESS=.75f;

    /** the default background brightness: {@value} */
    public static final float BACKGROUND_BRIGHTNESS=.25f;
    
    /** a command that a stimulus will accept. Used for menu action event handlers. */
    public static final String BRIGHTEN_FOREGROUND="Brighten Foreground", DARKEN_FOREGROUND="Darken Foreground", BRIGHTEN_BACKGROUND="Brighten Background", DARKEN_BACKGROUND="Darken Background";
    
    /**
     * Sets the foreground excitation density of the stimulus.
     * @param foregroundExcitationDensity The new excitation density.
     */
    public void setForegroundExcitationDensity(float foregroundExcitationDensity);
    
    /**
     * Returns the foreground excitation density of the stimulus.
     * @return The excitation density.
     */
    public float getForegroundExcitationDensity();
    
    /**
     * Sets the background excitation density of the stimulus.
     * @param backgroundExcitationDensity The new excitation density.
     */
    public void setBackgroundExcitationDensity(float backgroundExcitationDensity);
    
    /**
     * Returns the background excitation density of the stimulus.
     * @return The excitation density.
     */
    public float getBackgroundExcitationDensity();
    
    /** make foreground brighter */
    public void brightenForeground();
    
    /** make foreground darker */
    public void darkenForeground();
    
    /** make background brighter */
    public void brightenBackground();
    
    /** make background darker */
    public void darkenBackground();
    
    /** swap brightness of foreground and background */
    public void flipContrast();

    /** set state of stimulus to be hidden or shown. 
     Stimulus is hidden by setting its brightness the same as the background.
     */
    public void setVisible(boolean flag);
    
    /** is stimulus hidden by its foreground brightness being set to the background? */
    public boolean isVisible();

    /** Returns {@link java.util.List list} of shapes ({@link ConvexPolygon}) that make up the stimulus. */
    //public java.util.List getShapeList();

    /** return {@link ch.unizh.ini.friend.graphics.SeparateTransforms transforms} on this stimulus.  This method can be used
     to modify the transformation of the stimulus.  E.g.:
     <code>
     <pre>
     Stimulus s;  // initialized elsewhere
     s.getTransforms().translateTo(1f,2f);
     </pre>
     <code>
     */
    public SeparateTransforms getTransforms(); 
    
    /** set the transforms of this stimulus. This can be used, e.g., to set the transforms of a new stimulus
     to the same as the previous one.
     @param transforms the transforms to set for the stimulus
     */
    public void setTransforms(SeparateTransforms transforms);
    
    /** return list of untransformed shapes making up this stimulus */
    public ShapeList getShapes();
    
    /** get list of transformed shapes. All the transformations of the stimulus are applied to the returned shapes. */
    public ShapeList getTransformedShapes();
    
    /** set list of shapes making up stimulus */
    public void setShapes(ShapeList list);
    
    /** add a shape to the list of shapes making up the stimulus */
    public void addShape(Transformable shape);
    
    /** remove a shape from the list of shapes */
    public void removeShape(Transformable shape);
    
    /** clear the list of shapes */
    public void clearShapes();

    /** ratio by which to resize dimensions of stimulus */
    public static final float RESIZE_RATIO=(float)Math.pow(2,1./12);
    
    /** shorten by ratio {@link Stimulus#RESIZE_RATIO} */
    public void shorten();
    
    /** lengthen by ratio {@link Stimulus#RESIZE_RATIO} */
    public void lengthen();
    
    /** fatten by ratio {@link Stimulus#RESIZE_RATIO} */
    public void fatten();
    
    /** thin by ratio {@link Stimulus#RESIZE_RATIO} */
    public void thin();
    
    /** expand by ratio {@link Stimulus#RESIZE_RATIO} in both dimensions */
    public void expand();
    
    /** shrink by ratio {@link Stimulus#RESIZE_RATIO} in both dimensions */
    public void shrink();
    
    /** unit angle by which to rotate stimulus in radians: {@value} */
    public static final float ROTATE_BY=(float)Math.PI/6;
    
    /** rotate CW by {@link Stimulus#ROTATE_BY} */
    public void rotateCW();
    
    /** rotate CCW by {@link Stimulus#ROTATE_BY} */
    public void rotateCCW();

    /** rotate by <code>n</code>*{@link Stimulus#ROTATE_BY} 
     @param n number of {@link Stimulus#ROTATE_BY} units to rotate
     */
    public void rotate(int n);
    
}
