/*
 $Id: SynchronizedSeparateTransforms.java,v 1.7 2003/07/03 16:54:25 tobi Exp $
 

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

 
 $Log: SynchronizedSeparateTransforms.java,v $
 Revision 1.7  2003/07/03 16:54:25  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.6  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.5  2002/10/07 13:02:07  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.

 Revision 1.4  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.3  2002/09/25 16:48:11  tobi
 fixed spelling typo

 Revision 1.2  2002/09/22 19:31:57  tobi
 added toString()

 Revision 1.1  2002/09/16 08:21:50  cmarti
 initial version

 */

package ch.unizh.ini.friend.graphics;

import java.awt.geom.AffineTransform;

/**
 * Wrapper for <code>SeperateTransforms</code> to be thread-safe.
 * @author Christof Marti
 * @version $Revision: 1.7 $
 */
public class SynchronizedSeparateTransforms implements SeparateTransforms {
    
    /** The object to wrap. */
    protected SeparateTransforms st;
    
    /**
     * Creates a new instance that wraps the given object
     * @param st The object to wrap.
     */
    public SynchronizedSeparateTransforms(SeparateTransforms st) {
        //assert st != null;
        this.st = st;
    }
    
    /** Applies the given transformation to the geometric object.
     * @param at The affine transformation to apply.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable apply(AffineTransform at) {
        st.apply(at);
        return this;
    }
    
    /** Returns the original geometric object.
     * @return The geometric object.
     */
    public synchronized Transformable getGeometry() {
        return st.getGeometry();        
    }
    
    /** Returns the buffered rotation.
     * @return The rotation.
     */
    public synchronized AffineTransform getRotation() {
        return st.getRotation();
    }
    
    /** Returns the buffered scaling.
     * @return The scaling.
     */
    public synchronized AffineTransform getScaling() {
        return st.getScaling();
    }
    
    /** Returns the buffered generic transformation.
     * @return The generic transformation.
     */
    public synchronized AffineTransform getTransformation() {
        return st.getTransformation();
    }
    
    /** Returns a <code>Transformable</code> with the buffered
     * tranformation applied. <b>The returned object should
     * be a copy that is independent of <code>this</code>.</b>
     * @return The transformed geometric object.
     */
    public synchronized Transformable getTransformed() {
        return st.getTransformed();
    }
    
    /** Returns the buffered translation.
     * @return The translation.
     */
    public synchronized AffineTransform getTranslation() {
        return st.getTranslation();
    }
    
    /** Rotates the geometric object by the given angle around the origin.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable rotate(float phi) {
        st.rotate(phi);
        return this;
    }
    
    /** Rotates the geometric object by the given angle around the given point.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable rotate(float phi, float x, float y) {
        st.rotate(phi, x, y);
        return this;
    }
    
    /** Scales the geomtric object by the given values in x- and y-coordinates with
     * the origin as center.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable scale(float sx, float sy) {
        st.scale(sx, sy);
        return this;
    }
    
    /** Scales the geomtric object by the given values in x- and y-coordinates with
     * the given center (x, y).
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @param x x-coordinate of the center.
     * @param y y-coordinate of the center.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable scale(float sx, float sy, float x, float y) {
        st.scale(sx, sy, x, y);
        return this;
    }
    
    /** Sets the original geometric object.
     * @param geometry The geometric object.
     */
    public synchronized void setGeometry(Transformable geometry) {
        st.setGeometry(geometry);
    }
    
    /** Sets the buffered rotation.
     * @param rotation The rotation.
     */
    public synchronized void setRotation(AffineTransform rotation) {
        st.setRotation(rotation);
    }
    
    /** Sets the buffered scaling.
     * @param scaling The scaling.
     */
    public synchronized void setScaling(AffineTransform scaling) {
        st.setScaling(scaling);
    }
    
    /** Sets the buffered generic transformation.
     * @param transformation The generic transformation.
     */
    public synchronized void setTransformation(AffineTransform transformation) {
        st.setTransformation(transformation);
    }
    
    /** Sets the buffered translation.
     * @param translation The translation.
     */
    public synchronized void setTranslation(AffineTransform translation) {
        st.setTranslation(translation);
    }
    
    /** Translates the geometric object by the given coordinates.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable translate(float dx, float dy) {
        st.translate(dx, dy);
        return this;
    }
    
    /**
     * Clones this geometric object.
     * @return The clone.
     */
    public synchronized Object clone() {
        return new SynchronizedSeparateTransforms((SeparateTransforms)st.clone());
    }
    
    /** Rotates the geometric object by the given angle around the origin.
     * Overwrites the current rotation.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable rotateTo(float phi) {
        st.rotateTo(phi);
        return this;
    }
    
    /** Rotates the geometric object by the given angle around the given point.
     * Overwrites the current rotation.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable rotateTo(float phi, float x, float y) {
        st.rotateTo(phi, x, y);
        return this;
    }
    
    /** Scales the geomtric object by the given values in x- and y-coordinates with
     * the origin as center. Overwrites the current scaling.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable scaleTo(float sx, float sy) {
        st.scaleTo(sx, sy);
        return this;
    }
    
    /** Scales the geomtric object by the given values in x- and y-coordinates with
     * the given center (x, y). Overwrites the current scaling.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @param x x-coordinate of the center.
     * @param y y-coordinate of the center.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable scaleTo(float sx, float sy, float x, float y) {
        st.scaleTo(sx, sy, x, y);
        return this;
    }
    
    /** Translates the geometric object by the given coordinates.
     * Overwrites the current translation.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public synchronized Transformable translateTo(float dx, float dy) {
        st.translateTo(dx, dy);
        return this;
    }
    
        /** return string representation of the transforms */
    public String toString(){
        String s="SynchronizedSeparateTransforms with geometry "+getGeometry()+" and transformation "+getTransformation();
        return s;
    }

}
