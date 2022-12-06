/*
 $Id: ConcreteSeparateTransforms.java,v 1.6 2002/10/24 12:05:48 cmarti Exp $
 

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

 
 $Log: ConcreteSeparateTransforms.java,v $
 Revision 1.6  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.5  2002/10/07 13:02:07  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.

 Revision 1.4  2002/10/05 17:34:11  tobi
 added some clarifying javadoc

 Revision 1.3  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.2  2002/09/22 19:33:48  tobi
 added toString() for debugging

 Revision 1.1  2002/09/16 08:33:43  cmarti
 initial version

 */

package ch.unizh.ini.friend.graphics;

import java.awt.geom.AffineTransform;

/**
 * Provides the facilities to keep a geometric object in its original state while
 * buffering applied transformations. A transformed geometric object will only be
 * created on request. The individual transformations 'general', 'scaling',
 * 'rotation', 'translation' are kept atored individuallly each and are applied in that
 * order.
 * @author Christof Marti
 * @version $Revision: 1.6 $
 */
public class ConcreteSeparateTransforms implements SeparateTransforms {
    
    /** <code>Transformable</code> against which transformations are buffered */
    protected Transformable geometry;
    
    /** A transformed clone, <code>null</code> if uncached. */
    protected Transformable transformed = null;
    
    /** The generic transformations. */
    protected AffineTransform transformation = null;
    
    /** The scalings. */
    protected AffineTransform scaling = null;
    
    /** The rotations. */
    protected AffineTransform rotation = null;
    
    /** The translations. */
    protected AffineTransform translation = null;
    
    /**
     * Creates a new instance with the given geometric object
     * @param geometry The geometric object the transformations will be buffered against
     */ 
    public ConcreteSeparateTransforms(Transformable geometry) {
        //assert geometry != null;
        this.geometry = geometry;
    }
    
    /** Applies the given transformation to the geometric object.
     * <b>Does not necessarily copy the passed transform.</b>
     * @param at The affine transformation to apply.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable apply(AffineTransform at) {
        if (transformation == null) {
            transformation = at;
        } else {
            transformation.preConcatenate(at);
        }
        transformed = null;  // set the transformed to null so it will be recomputed on next getTransformed()
        return this;
    }
    
    /** Rotates the geometric object by the given angle around the origin.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi) {
        AffineTransform at = AffineTransform.getRotateInstance(phi);
        if (rotation == null) {
            rotation = at;
        } else {
            rotation.preConcatenate(at);
        }
        transformed = null;
        return this;
    }
    
    /** Rotates the geometric object by the given angle around the given point.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi, float x, float y) {
        AffineTransform at = AffineTransform.getRotateInstance(phi, x, y);
        if (rotation == null) {
            rotation = at;
        } else {
            rotation.preConcatenate(at);
        }
        transformed = null;
        return this;
    }
    
    /** Scales the geometric object by the given values in x- and y-coordinates with
     * the origin as center.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scale(float sx, float sy) {
        AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
        if (scaling == null) {
            scaling = at;
        } else {
            scaling.preConcatenate(at);
        }
        transformed = null;
        return this;
    }
    
    /** Scales the geometric object by the given values in x- and y-coordinates with
     * the given center (x, y).
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @param x x-coordinate of the center.
     * @param y y-coordinate of the center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scale(float sx, float sy, float x, float y) {
        AffineTransform at = new AffineTransform(sx, sy, 0.0f, 0.0f, (1 - sx)*x, (1 - sy)*y);
        if (scaling == null) {
            scaling = at;
        } else {
            scaling.preConcatenate(at);
        }
        transformed = null;
        return this;
    }
    
    /** Translates the geometric object by the given coordinates.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable translate(float dx, float dy) {
        AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
        if (translation == null) {
            translation = at;
        } else {
            translation.preConcatenate(at);
        }
        transformed = null;
        return this;
    }
    
    /** Returns the original geometric object.
     * @return The geometric object.
     */
    public Transformable getGeometry() {
        return geometry;
    }
    
    /** Sets the original geometric object.
     * @param geometry The geometric object.
     */
    public void setGeometry(Transformable geometry) {
        //assert geometry != null;
        this.geometry = geometry;
        transformed = null;
    }
    
    /** Returns the transformed geometric object.
     * @return The geometical object.
     */
    public Transformable getTransformed() {
        if (transformed == null) {
            //System.out.println("null transformed, making new cached transformed shape");
            AffineTransform at = null;
            if (transformation != null) {
                at = (AffineTransform)transformation.clone();
            }
            if (scaling != null) {
                if (at == null) {
                    at = (AffineTransform)scaling.clone();
                } else {
                    at.preConcatenate(scaling);
                }
            }
            if (rotation != null) {
                if (at == null) {
                    at = (AffineTransform)rotation.clone();
                } else {
                    at.preConcatenate(rotation);
                }
            }
            if (translation != null) {
                if (at == null) {
                    at = (AffineTransform)translation.clone();
                } else {
                    at.preConcatenate(translation);
                }
            }
            
            transformed = (Transformable)geometry.clone();
            if (at != null) {
                transformed.apply(at);
            }
        }
        
        return transformed;
    }
    
    /** Returns the buffered generic transformation.
     * @return The generic transformation.
     */
    public AffineTransform getTransformation() {
        return transformation;
    }
    
    /** Sets the buffered generic transformation.
     * @param transformation The generic transformation.
     */
    public void setTransformation(AffineTransform transformation) {
        this.transformation = transformation;
        transformed = null;
    }
    
    /** Returns the buffered scaling.
     * @return The scaling.
     */
    public AffineTransform getScaling() {
        return scaling;
    }
    
    /** Sets the buffered scaling.
     * @param scaling The scaling.
     */
    public void setScaling(AffineTransform scaling) {
        this.scaling = scaling;
        transformed = null;
    }
    
    /** Returns the buffered rotation.
     * @return The rotation.
     */
    public AffineTransform getRotation() {
        return rotation;
    }
    
    /** Sets the buffered rotation.
     * @param rotation The rotation.
     */
    public void setRotation(AffineTransform rotation) {
        this.rotation = rotation;
        transformed = null;
    }
    
    /** Returns the buffered translation.
     * @return The translation.
     */
    public AffineTransform getTranslation() {
        return translation;
    }
    
    /** Sets the buffered translation.
     * @param translation The translation.
     */
    public void setTranslation(AffineTransform translation) {
        this.translation = translation;
        transformed = null;
    }
 
    /**
     * Clones this geometric object.
     * @return The clone.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            //assert false : "CloneNotSupportedException";
            return null;
        }
    }
    
    /** Rotates the geometric object by the given angle around the origin.
     * Overwrites the current rotation.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotateTo(float phi) {
        rotation = AffineTransform.getRotateInstance(phi);
        return this;
    }
    
    /** Rotates the geometric object by the given angle around the given point.
     * Overwrites the current rotation.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotateTo(float phi, float x, float y) {
        rotation = AffineTransform.getRotateInstance(phi, x, y);
        return this;
    }
    
    /** Scales the geomtric object by the given values in x- and y-coordinates with
     * the origin as center. Overwrites the current scaling.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scaleTo(float sx, float sy) {
        scaling = AffineTransform.getScaleInstance(sx, sy);
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
    public Transformable scaleTo(float sx, float sy, float x, float y) {
        scaling = new AffineTransform(sx, sy, 0.0f, 0.0f, (1 - sx)*x, (1 - sy)*y);
        return this;
    }
    
    /** Translates the geometric object by the given coordinates.
     * Overwrites the current translation.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable translateTo(float dx, float dy) {
        translation = AffineTransform.getTranslateInstance(dx, dy);
        return this;
    }
    
    /** return string representation of the transforms */
    public String toString(){
        String s="ConcreteSeparateTransforms "+transformation;
        return s;
    }
    
}
