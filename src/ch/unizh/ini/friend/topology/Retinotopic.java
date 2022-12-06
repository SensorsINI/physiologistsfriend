/*
 * Retinotopic.java
 *
 * Created on October 1, 2002, 5:23 PM
 
 $Id: Retinotopic.java,v 1.4 2003/07/03 16:54:28 tobi Exp $
 

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

 
 $Log: Retinotopic.java,v $
 Revision 1.4  2003/07/03 16:54:28  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.3  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.2  2002/10/01 20:50:55  tobi
 javadoc

 Revision 1.1  2002/10/01 18:43:23  tobi
 initial version
 fixed locaiton to start adding helper methods

 
 */

package ch.unizh.ini.friend.topology;

/**
 * Represents a cell with a {@link RetinotopicLocation}.  These cells all have a location, retinotopically assigned
 during construction, which ultimately is inherited from the photoreceptor location they lie over.  This interface is
 used for retinal cells like BipolarCells and GanglionCells.
 
 Cells which implement {@link Retinotopic} can {@link #getRetinotopicLocation provide a location}. This location {@link #setRetinotopicLocation is set}
 by a method that accepts another <code>RetionotopicLocation</code> that provides the location.
 
 * @author  $Author: tobi $
 @version $Revision: 1.4 $
 
 */
public interface Retinotopic {
 
    /** return the RetinotopicLocaton of the cell 
     @return RetinotopicLocation of cell
     */
    public RetinotopicLocation getRetinotopicLocation();
    
    /** set the location 
     @param p to set the cell to. This is generally returned from another cell.
     */
    public void setRetinotopicLocation(RetinotopicLocation p);
}
