/*
 $Id: AbstractSynapse.java,v 1.2 2002/11/08 14:04:48 cmarti Exp $
 
 
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
 
 
 $Log: AbstractSynapse.java,v $
 Revision 1.2  2002/11/08 14:04:48  cmarti
 move to package ch.unizh.ini.friend.simulation.synapses

 Revision 1.1  2002/11/05 18:05:08  cmarti
 intial version

 */

package ch.unizh.ini.friend.simulation.synapses;

import java.util.Collection;
import ch.unizh.ini.friend.simulation.*;

/**
 * Default implementations for synapses.
 * @author Christof Marti
 * @version $Revision: 1.2 $
 */
public abstract class AbstractSynapse extends AbstractAcceptsInput implements ServesOutput {
    
    /** Creates a new instance with currently no inputs. */
    public AbstractSynapse() {
        super();
    }

    /** Creates a new instance with currently no inputs and
     * with the given initial capacity.
     * @param n Number of initial capacity for inputs.
     */
    public AbstractSynapse(int n) {
        super(n);
    }

    /** Creates a new instance with the given collection of inputs.
     * @param inputs The collection of inputs.
     */
    public AbstractSynapse(Collection inputs) {
        super(inputs);
    }
    
    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public AbstractSynapse(Object input) {
        super(input);
    }
    
}
