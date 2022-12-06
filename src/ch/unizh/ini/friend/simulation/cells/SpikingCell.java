/*
 * SpikingCell.java
 *
 * Created on September 25, 2002, 11:53 AM
 * $Id: SpikingCell.java,v 1.3 2002/10/24 12:05:51 cmarti Exp $
 

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

 
 *
 * <hr>
 * <pre>
 * $Log: SpikingCell.java,v $
 * Revision 1.3  2002/10/24 12:05:51  cmarti
 * add GPL header
 *
 * Revision 1.2  2002/10/01 16:16:54  cmarti
 * change package and import names to new hierarchy
 *
 * Revision 1.1  2002/09/25 16:51:38  tobi
 * classified cells as graded or spiking, added interfaces GradedCell, SpikingCell.
 * made photorecepto, bipolar, horizontal cell graded cells with gradedOutput() method.
 *
 * </pre>
 * @author $Author: cmarti $
 */

package ch.unizh.ini.friend.simulation.cells;

/**
 A cell that makes spike output.
 
 * @author  $Author: cmarti $
 * @since $Revision: 1.3 $
 */
public interface SpikingCell {
    
    /** is the cell making a spike now? */
    public boolean isSpike();
    
}
