/*
 $Id: Updateable.java,v 1.8 2002/10/24 12:05:50 cmarti Exp $
 

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

 
 $Log: Updateable.java,v $
 Revision 1.8  2002/10/24 12:05:50  cmarti
 add GPL header

 Revision 1.7  2002/10/15 19:27:07  tobi
 lots of javadoc added,
 mouse wheel enabled.

 Revision 1.6  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.5  2002/09/16 20:44:42  cmarti
 removing parameter dt from update() in Updateable

 Revision 1.4  2002/09/13 08:35:51  cmarti
 - split-off UpdateSource
 - add compute method for double-buffer support

 Revision 1.3  2002/09/10 20:27:45  cmarti
 added static methods addListenerToAll, removeListenerFromAll

 Revision 1.2  2002/09/10 19:53:34  cmarti
 - changed to an abstract class
 - added add/removeUpdateListener, fireUpdateEvent, updateListeners

 Revision 1.1  2002/09/10 15:57:03  cmarti
 initial version

 */

package ch.unizh.ini.friend.simulation;

/**
 * This interface provides members common to all classes
 * that participate in a simulation.
 The proper way to process all cells is to first iterate over all cells doing a {@link #compute} on each one, 
 then, in a separate iteration,
 {@link #update} all their values.
 
 * @author Christof Marti
 * @version $Revision: 1.8 $
 */
public interface Updateable {
    
    /**
     * Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt);
    
    /**
     * Updates the actual state to the newly computed - aka double-buffering.
     */
    public void update();
        
}
