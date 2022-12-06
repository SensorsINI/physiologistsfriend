/*
 $Id: Intersectable.java,v 1.4 2002/10/24 12:05:48 cmarti Exp $
 

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

 
 $Log: Intersectable.java,v $
 Revision 1.4  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.3  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.2  2002/09/21 14:53:40  tobi
 javadoc additions

 Revision 1.1  2002/09/16 08:23:38  cmarti
 initial version

 */

package ch.unizh.ini.friend.graphics;

/**
 * Declares the interface for a <code>Transformable</code> that can be
 * intersected with another shape.
 * @author Christof Marti
 * @version $Revision: 1.4 $
 */
public interface Intersectable extends Transformable {

    /**
     * Computes the area of intersection between this object and the given <code>other</code> object.
     * @param other The object to intersect with.
     * @return The intersection area.
     */
    public Intersectable intersect(Intersectable other);
    
    /**
     * Computes the area of this geometric object.
     * @return The area.
     */
    public float area();
    
}
