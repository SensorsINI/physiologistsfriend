/*
 $Id: SeparateTransforms.java,v 1.6 2003/07/03 16:54:25 tobi Exp $
 

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

 
 $Log: SeparateTransforms.java,v $
 Revision 1.6  2003/07/03 16:54:25  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.5  2003/06/23 11:30:15  tobi
 greatly improved recording display speed, capability

 added full screen exclusive display

 Revision 1.4  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.3  2002/10/05 17:34:11  tobi
 added some clarifying javadoc

 Revision 1.2  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/16 08:22:45  cmarti
 initial version

 */

package ch.unizh.ini.friend.graphics;

import java.awt.geom.AffineTransform;

/**
 * Provides the interface to keep a geometric object in its original state while
 * buffering applied transformations separately.
 @see ConcreteSeparateTransforms
 
 * @author Christof Marti
 * @version $Revision: 1.6 $
 */
public interface SeparateTransforms extends BufferedTransform, Cloneable {

    /** Returns the original geometric object.
     * @return The geometric object.
     */
    public Transformable getGeometry();

    /** Sets the original geometric object.
     * @param geometry The geometric object.
     */
    public void setGeometry(Transformable geometry);
    
    /** Returns the transformed geometric object.
     * @return The geometical object.
     */
    public Transformable getTransformed();
    
    /** Returns the buffered generic transformation.
     * @return The generic transformation.
     */
    public AffineTransform getTransformation();
    
    /** Sets the buffered generic transformation.
     * @param transformation The generic transformation.
     */
    public void setTransformation(AffineTransform transformation);
    
    /** Returns the buffered scaling.
     * @return The scaling.
     */
    public AffineTransform getScaling();
    
    /** Sets the buffered scaling.
     * @param scaling The scaling.
     */
    public void setScaling(AffineTransform scaling);
    
    /** Returns the buffered rotation.
     * @return The rotation.
     */
    public AffineTransform getRotation();
    
    /** Sets the buffered rotation.
     * @param rotation The rotation.
     */
    public void setRotation(AffineTransform rotation);
    
    /** Returns the buffered translation.
     * @return The translation.
     */
    public AffineTransform getTranslation();
    
    /** Sets the buffered translation.
     * @param translation The translation.
     */
    public void setTranslation(AffineTransform translation);

    /**
     * Translates the geometric object by the given coordinates.
     * Overwrites the current translation.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable translateTo(float dx, float dy);
    
    /**
     * Rotates the geometric object by the given angle around the given point.
     * Overwrites the current rotation.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotateTo(float phi, float x, float y);
    
    /**
     * Rotates the geometric object by the given angle around the origin.
     * Overwrites the current rotation.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotateTo(float phi);
    
    /**
     * Scales the geomtric object by the given values in x- and y-coordinates with
     * the given center (x, y). Overwrites the current scaling.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @param x x-coordinate of the center.
     * @param y y-coordinate of the center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scaleTo(float sx, float sy, float x, float y);

    /**
     * Scales the geomtric object by the given values in x- and y-coordinates with
     * the origin as center. Overwrites the current scaling.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scaleTo(float sx, float sy);

}
