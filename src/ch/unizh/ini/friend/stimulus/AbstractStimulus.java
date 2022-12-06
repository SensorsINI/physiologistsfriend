/*
 $Id: AbstractStimulus.java,v 1.16 2003/07/03 16:54:28 tobi Exp $
 

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

 
 $Log: AbstractStimulus.java,v $
 Revision 1.16  2003/07/03 16:54:28  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.15  2003/05/10 17:27:44  jgyger
 Merge from color-branch

 Revision 1.14.2.2  2003/04/22 18:56:50  jgyger
 remove synchronized keyword

 Revision 1.14.2.1  2003/03/16 18:14:16  jgyger
 move excitation method from stimuli to photoreceptors

 Revision 1.14  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.13  2002/10/16 11:29:28  tobi
 Stimulus now has methods for geometrcal transformations of stimulus
 size and rotation. AbstractStimulus implements these methods.
 TangentScreen and FriendGUI now use these unified transformation methods.

 activitymeter now shows the spike rate in spikes/second instead of spikes/measurement interval.

 Revision 1.12  2002/10/15 19:27:07  tobi
 lots of javadoc added,
 mouse wheel enabled.

 Revision 1.11  2002/10/13 16:29:10  tobi
 many small changes from tuebingen trip.

 Revision 1.10  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.9  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.8  2002/09/25 16:55:02  tobi
 resynchonized the wrapper around the stimulus transform, to support
 threaded simulation update.

 made photoreceptor excitation computation simpler by always computing overlap area, even when intersection shape is null.
 (this may not be correct)

 Revision 1.7  2002/09/25 08:41:26  tobi
 fixed javadoc so no errors generated and added package descriptions.

 Revision 1.6  2002/09/24 04:29:53  tobi
 fixed bug where untransformed shape of stimulus was being used to compute
 excitation of photoreceptor.  Transformed shape now being used.

 Photoreptor excitation is now normalized by area of receptor.

 Revision 1.5  2002/09/22 19:37:58  tobi
 Major changes to support multiple shapes for a stimulus, embedding of
 stimulus transform within stimulus.

 synchrnized seperate transforms removed because simulation will probably
 be run from inside swing trhead.

 new methods in Steimulus for getting at transform and transformedShape

 Revision 1.4  2002/09/18 15:29:13  tobi
 added concrete methods to brightness and darken a stimulus by some ratios.
 these are used in TangentScreen to affect stimulus through keystrokes.

 Revision 1.3  2002/09/17 14:26:44  cmarti
 move 'implements Updateable' up to Stimulus

 Revision 1.2  2002/09/16 20:44:42  cmarti
 removing parameter dt from update() in Updateable

 Revision 1.1  2002/09/16 08:43:50  cmarti
 initial version

 Revision 1.1  2002/09/13 12:02:19  cmarti
 initial version

 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.graphics.*;

/**
 * Provides default implementations for a stimulus, including it's foreground and background brightness, its shapes' container, and
 the transform applied to the shapes.
 
 * @author Christof Marti, Tobi Delbruck
 * @version $Revision: 1.16 $
 */
public abstract class AbstractStimulus implements Stimulus {
        
    /** The foreground excitation density. */
    protected float foregroundExcitationDensity = FOREGROUND_BRIGHTNESS;
    
    /** The background excitation density. */
    protected float backgroundExcitationDensity = BACKGROUND_BRIGHTNESS;
    
    /** The new foreground excitation density. */
    protected float newForegroundExcitationDensity = FOREGROUND_BRIGHTNESS;
    
    /** The new background excitation density. */
    protected float newBackgroundExcitationDensity = BACKGROUND_BRIGHTNESS;
    
    /** last density for stimulus. used when stimulus is flashed off and on */
    private float lastExcitationDensity=foregroundExcitationDensity;
    
    /** factor to change brightness of foreground or background by for 
     {@link #brightenForeground}, etc.
     */
    public static final float BRIGHTNESS_CHANGE_RATIO=(float)Math.sqrt(2);
    
    /** Returns the background excitation density of the stimulus.
     * @return The excitation density.
     */
    public float getBackgroundExcitationDensity() {
        return newBackgroundExcitationDensity;
    }
    
    /** Returns the foreground excitation density of the stimulus.
     * @return The excitation density.
     */
    public float getForegroundExcitationDensity() {
        return newForegroundExcitationDensity;
    }
        
    /** Sets the background excitation density of the stimulus.
     * @param backgroundExcitationDensity The new excitation density.
     */
    public void setBackgroundExcitationDensity(float backgroundExcitationDensity) {
        newBackgroundExcitationDensity = backgroundExcitationDensity;
    }
    
    /** Sets the foreground excitation density of the stimulus.
     * @param foregroundExcitationDensity The new excitation density.
     */
    public void setForegroundExcitationDensity(float foregroundExcitationDensity) {
        newForegroundExcitationDensity = foregroundExcitationDensity;
    }

    /** make foreground brighter */
    public void brightenForeground(){
        setForegroundExcitationDensity(Math.min(1f,getForegroundExcitationDensity()*BRIGHTNESS_CHANGE_RATIO));
    }
    
    /** make foreground darker */
    public void darkenForeground(){
        setForegroundExcitationDensity(Math.min(1f,getForegroundExcitationDensity()/BRIGHTNESS_CHANGE_RATIO));
    }
    
    /** make background brighter */
    public void brightenBackground(){
        setBackgroundExcitationDensity(Math.min(1f,getBackgroundExcitationDensity()*BRIGHTNESS_CHANGE_RATIO));
    }
    
    /** make background darker */
    public void darkenBackground(){
        setBackgroundExcitationDensity(Math.min(1f,getBackgroundExcitationDensity()/BRIGHTNESS_CHANGE_RATIO));
    }
    
    /** swap brightness of foreground and background */
    public void flipContrast(){
        float fb=getForegroundExcitationDensity();
        float bb=getBackgroundExcitationDensity();
        setForegroundExcitationDensity(bb);
        setBackgroundExcitationDensity(fb);
    }
    
    /** set state of stimulus to be hidden or shown. 
     Stimulus is hidden by setting its brightness the same as the background.
     */
    public void setVisible(boolean flag){
        if(flag==false){
            lastExcitationDensity=getForegroundExcitationDensity();
            setForegroundExcitationDensity(getBackgroundExcitationDensity());
        }else{
            setForegroundExcitationDensity(lastExcitationDensity);
        }
    }
    
    /** is stimulus hidden by its foreground brightness being set to the background? */
    public boolean isVisible(){
        return !(getForegroundExcitationDensity()==getBackgroundExcitationDensity());
    }
    
    /**
     * Computes the new state of this component of the simulation.
     * This does nothing because the new state of the stimulus is
     * determined by the gui which runs independently.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
    }
    
    /**
     * Updates the actual state to the newly computed - aka double-buffering.
     */
    public void update() {
        foregroundExcitationDensity = newForegroundExcitationDensity;
        backgroundExcitationDensity = newBackgroundExcitationDensity;
    }
        
    
    /** the list of original (untransformed) shapes making up the stimulus */
    protected ShapeList shapes;
    
    /** the new set of shapes */
    protected ShapeList newShape;
    
    /** return list of original (untransformed) shapes making up this stimulus  */
    public ShapeList getShapes() {
        return shapes;
    }
    
    /** set list of untransformed shapes making up stimulus.  
     The <code>ShapeList</code> is wrapped inside a new set of {@link ch.unizh.ini.friend.graphics.SeparateTransforms}, so
     any existing transformations are lost. Hence this method
     resets the transform applied to the shapes. 
     @param list the shapes to use
     */
    public void setShapes(ShapeList list) {
        shapes=list;
        transforms= new SynchronizedSeparateTransforms(new ConcreteSeparateTransforms(shapes));
        //transforms= new ConcreteSeparateTransforms(shapes);
    }

    /** add a shape to the list of shapes making up the stimulus  */
    public void addShape(Transformable shape) {
        shapes.add(shape);
    }
    
    /** remove a shape from the list of shapes  */
    public void removeShape(Transformable shape) {
        shapes.remove(shape);
    }
    
    /** clear the list of shapes  */
    public void clearShapes() {
        shapes.clear();
    }
    
    /** The transforms of the stimulus. */
    public SeparateTransforms transforms; // = new SynchronizedSeparateTransforms(new ConcreteSeparateTransforms(shapes));

    /** return transforms operating on this stimulus  */
    public SeparateTransforms getTransforms() {
        return  transforms;
    }
    
        /** set the transforms of this stimulus. This can be used, e.g., to set the transforms of a new stimulus
     to the same as the previous one.
     @param transforms the transforms to set for the stimulus
     */
    public void setTransforms(SeparateTransforms transforms){
        this.transforms=transforms;
    }

    
    /** return transformed shapes.  These can be rendered to show the actual stimulus. */
    public ShapeList getTransformedShapes(){
        return (ShapeList)transforms.getTransformed();
    }
        
    /** String representation of stimulus */
    public String toString(){
        String s="Stimulus with Foreground: "+getForegroundExcitationDensity()+" Background: "+getBackgroundExcitationDensity()
            +" ShapeList: "+getShapes() 
            +" Transforms: "+getTransforms()
            +" Transformed Shapes: "+getTransformedShapes();
        return s;
    }

    /** lengthen by ratio {@link Stimulus#RESIZE_RATIO}  */
    public void lengthen() {
        getTransforms().scale(Stimulus.RESIZE_RATIO,1f);
    }
    
    /** make shorter by ratio {@link Stimulus#RESIZE_RATIO}  */
    public void shorten() {
        getTransforms().scale(1/Stimulus.RESIZE_RATIO,1f);
    }
    
    /** fatten by ratio {@link Stimulus#RESIZE_RATIO}  */
    public void fatten() {
        getTransforms().scale(1f,Stimulus.RESIZE_RATIO);
    }
    
    /** thin by ratio {@link Stimulus#RESIZE_RATIO}  */
    public void thin() {
        getTransforms().scale(1f,1/Stimulus.RESIZE_RATIO);
    }
    
    /** expand by ratio {@link Stimulus#RESIZE_RATIO} in both dimensions  */
    public void expand() {
        getTransforms().scale(Stimulus.RESIZE_RATIO,Stimulus.RESIZE_RATIO);
    }

    /** shrink by ratio {@link Stimulus#RESIZE_RATIO} in both dimensions */
    public void shrink(){
        getTransforms().scale(1/Stimulus.RESIZE_RATIO,1/Stimulus.RESIZE_RATIO);
    }
    
    /** rotate CCW by {@link Stimulus#ROTATE_BY}  */
    public void rotateCCW() {
        getTransforms().rotate(Stimulus.ROTATE_BY);
    }    
    
    /** rotate CW by {@link Stimulus#ROTATE_BY}  */
    public void rotateCW() {
        getTransforms().rotate(-Stimulus.ROTATE_BY);
    }
    
    /** rotate by <code>n</code>*{@link Stimulus#ROTATE_BY} 
     @param n number of {@link Stimulus#ROTATE_BY} units to rotate
     */
    public void rotate(int n){
        getTransforms().rotate((float)Math.PI/48*n);
    }

    
}
