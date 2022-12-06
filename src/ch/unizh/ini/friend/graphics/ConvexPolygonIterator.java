/*
 $Id: ConvexPolygonIterator.java,v 1.3 2002/10/24 12:05:48 cmarti Exp $
 

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

 
 $Log: ConvexPolygonIterator.java,v $
 Revision 1.3  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.2  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/03 20:44:05  tobi
 initial (re)add to move files to correct hierarchy

 Revision 1.2  2002/09/02 19:14:47  tobi
 added CVS header strings

 
 * ConvexPolygonIterator.java
 *
 * Created on September 1, 2002, 4:15 PM
 */

package ch.unizh.ini.friend.graphics;

import java.awt.geom.PathIterator;

/**
 * <code>ConvexPolygonIterator</code> implements the <code>PathIterator</code>
 * interface for <code>ConvexPolygon</code>. Changes on the corresponding
 * instance of <code>ConvexPolygon</code> during the iteration are currently
 * <b>not</b> supported!
 *
 * @version $Revision: 1.3 $
 * @author Christof Marti
 */
public class ConvexPolygonIterator implements PathIterator {
    
    /**
     * The instance of <code>ConvexPolygon</code> this iterator
     * is instantiated for.
     */
    private ConvexPolygon source;
    
    /**
     * The current postition of iteration.
     */
    private int current = 0;
    
    /**
     * Constructor yielding an instance that iterates along
     * the given polygon.
     * @param source Instance of <code>ConvexPolygon</code> this
     * iterator will use.
     */
    public ConvexPolygonIterator(ConvexPolygon source) {
        this.source = source;
        //System.out.println(source);
    }
    
    /**
     * Fills the array with coordinates and returns the type
     * of the current segment.
     * @see PathIterator
     * @param values The array that will be filled with
     * coordinates of the current segment.
     * @return The type of the current segment.
     */
    public int currentSegment(double[] values) {
        if (current <= 2*source.npoints) {
            values[0] = source.points[current%(2*source.npoints)];
            values[1] = source.points[(current+1)%(2*source.npoints)];

            if (current == 0) {
                return SEG_MOVETO;
            }
            return SEG_LINETO;
        } else {
            return SEG_CLOSE;
        }
    }
    
    /**
     * Fills the array with coordinates and returns the type
     * of the current segment.
     * @see PathIterator
     * @param values The array that will be filled with
     * coordinates of the current segment.
     * @return The type of the current segment.
     */
    public int currentSegment(float[] values) {
        if (current <= 2*source.npoints) {
            values[0] = source.points[current%(2*source.npoints)];
            values[1] = source.points[(current+1)%(2*source.npoints)];
            //System.out.println("iterator: (" + values[0] + "," + values[1] + ")");

            if (current == 0) {
                return SEG_MOVETO;
            }
            return SEG_LINETO;
        } else {
            return SEG_CLOSE;
        }
    }

    /** 
     * Returns the winding rule for determining the interior of the path.
     * @see PathIterator
     * @return The winding rule.
     */
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }
    
    /**
     * Returns true iff the end of the iteration is reached.
     * @return True iff the end of the iteration is reached.
     */
    public boolean isDone() {
        return current > 2*source.npoints;
    }
    
    /**
     * Moves the iterator to the next segment.
     */
    public void next() {
        current += 2;
    }
    
}
