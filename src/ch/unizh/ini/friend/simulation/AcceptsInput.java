/*
 $Id: AcceptsInput.java,v 1.9 2002/11/08 17:07:28 cmarti Exp $
 

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

 
 $Log: AcceptsInput.java,v $
 Revision 1.9  2002/11/08 17:07:28  cmarti
 small fix in javadoc

 Revision 1.8  2002/11/07 19:40:15  cmarti
 make use of a trick to deep-clone objects...

 Revision 1.7  2002/11/07 18:48:39  cmarti
 - let AcceptsInput extend Cloneable and require clone() to be public
 - implement clone()
 - add static methods getCollectionInstance(), connectOneToOne() and connectOneToAll()

 Revision 1.6  2002/10/31 22:47:28  cmarti
 - remove Monitor, use AcceptsInput instead
 - rename get-/setMonitoredInput() in AbstractMonitor to get-/setInput()
 - move both methods up into AbstractAcceptsInput and add them to AcceptsInput
 - adapt other classes to these changes

 Revision 1.5  2002/10/31 20:30:38  cmarti
 add setter for inputs

 Revision 1.4  2002/10/25 09:52:37  cmarti
 - AcceptsInput and Monitor don't implement Updateable anymore
 - Compile fixes in ManyInputs and FriendGUI

 Revision 1.3  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.2  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.1  2002/09/17 12:53:53  cmarti
 added AcceptsInput as an intermediate interface between Updateable and ManyInputs

 */

package ch.unizh.ini.friend.simulation;

import java.util.Collection;
import java.io.Serializable;

/**
 * Common interface to simulation components that accept inputs.
 * @author Christof Marti
 * @version $Revision: 1.9 $
 */
public interface AcceptsInput extends Serializable {
    
    /** Returns a collection of all inputs to this simulation component.
     * @return The inputs.
     */
    public Collection getInputs();
    
    /** Sets the collection of all inputs to this simulation component to the given.
     * @param inputs The inputs.
     */
    public void setInputs(Collection inputs);
    
    /** Returns the first (if any) input.
     * @return The input.
     */
    public ServesOutput getInput();
    
    /** Sets the first (and only) input.
     * @param input The input.
     */
    public void setInput(ServesOutput input);
    
}
