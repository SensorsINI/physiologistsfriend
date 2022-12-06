/*
 $Id: Transformable.java,v 1.5 2003/07/03 16:54:25 tobi Exp $
 

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

 
 $Log: Transformable.java,v $
 Revision 1.5  2003/07/03 16:54:25  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.4  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.3  2002/10/05 17:34:11  tobi
 added some clarifying javadoc

 Revision 1.2  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/16 08:21:07  cmarti
 initial version

 */

package ch.unizh.ini.friend.graphics;

import java.awt.geom.AffineTransform;

/**
 * Interface a transformable geometric object might implement.
 * Whether the methods return <code>this</code> or a clone is
 * up to the concrete implementation.
 @see AbstractTransformable
 
 * @author Christof Marti
 * @version $Revision: 1.5 $
 */
public interface Transformable extends Cloneable {
    
    /**
     * Translates the geometric object by the given coordinates.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable translate(float dx, float dy);
    
    /**
     * Rotates the geometric object by the given angle around the given point.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi, float x, float y);
    
    /**
     * Rotates the geometric object by the given angle around the origin.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi);
    
    /**
     * Scales the geomtric object by the given values in x- and y-coordinates with
     * the given center (x, y).
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @param x x-coordinate of the center.
     * @param y y-coordinate of the center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scale(float sx, float sy, float x, float y);

    /**
     * Scales the geomtric object by the given values in x- and y-coordinates with
     * the origin as center.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scale(float sx, float sy);

    /**
     * Applies the given transformation to the geometric object.
     * @param at The affine transformation to apply.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable apply(AffineTransform at);

    /**
     * Clones this geometric object.
     * @return The clone.
     */
    public Object clone();
    
}
