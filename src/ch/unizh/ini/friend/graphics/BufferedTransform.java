/*
 $Id: BufferedTransform.java,v 1.3 2002/10/24 12:05:48 cmarti Exp $
 

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

 
 $Log: BufferedTransform.java,v $
 Revision 1.3  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.2  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/16 08:33:52  cmarti
 initial version

 */

package ch.unizh.ini.friend.graphics;

import java.awt.geom.AffineTransform;

/**
 * Allows for uniform access to <code>Transformable</code> objects
 * that want/need to buffer the transformations applied to them.
 * @author Christof Marti
 * @version $Revision: 1.3 $
 */
public interface BufferedTransform extends Transformable {
    
    /**
     * Returns a <code>Transformable</code> with the buffered
     * tranformation applied. <b>The returned object should
     * be a copy which is independent of <code>this</code>.</b>
     * @return The transformed geometric object.
     */
    public Transformable getTransformed();
    
}
