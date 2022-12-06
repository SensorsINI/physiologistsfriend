/*
 $Id: AbstractTransformable.java,v 1.5 2003/07/03 16:54:24 tobi Exp $
 

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

 
 $Log: AbstractTransformable.java,v $
 Revision 1.5  2003/07/03 16:54:24  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.4  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.3  2002/10/07 13:02:07  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.

 Revision 1.2  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.1  2002/09/16 08:34:04  cmarti
 initial version

 */

package ch.unizh.ini.friend.graphics;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Provides default implementations for all methods of <code>Transformable</code>
 * except <code>apply</code> on which all other methods depend.
 * @author Christof Marti
 * @version $Revision: 1.5 $
 */
public abstract class AbstractTransformable implements Transformable {
    
    /** Rotates the geometric object by the given angle around the origin.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi) {
        return apply(AffineTransform.getRotateInstance(phi));
    }
    
    /** Rotates the geometric object by the given angle around the given point.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi, float x, float y) {
        return apply(AffineTransform.getRotateInstance(phi, x, y));
    }
    
    /** Scales the geometric object by the given values in x- and y-coordinates with
     * the origin as center.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scale(float sx, float sy) {
        return apply(AffineTransform.getScaleInstance(sx, sy));
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
        return apply(new AffineTransform(sx, sy, 0.0f, 0.0f, (1 - sx)*x, (1 - sy)*y));
    }
    
    /** Translates the geometric object by the given coordinates.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable translate(float dx, float dy) {
        return apply(AffineTransform.getTranslateInstance(dx, dy));
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
    
}
