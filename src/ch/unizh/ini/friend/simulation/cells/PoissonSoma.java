/*
 * PoissonSoma.java
 *
 * Created on September 25, 2002, 12:12 PM
 * $Id: PoissonSoma.java,v 1.6 2002/11/05 22:09:46 cmarti Exp $
 

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
 * $Log: PoissonSoma.java,v $
 * Revision 1.6  2002/11/05 22:09:46  cmarti
 * remove AbstractWeightedInputServesOutput and put a much more flexible solution using
 * the concept of synapses in place
 *
 * Revision 1.5  2002/10/31 21:47:28  cmarti
 * - split AbstractAcceptsInputServesOutput, added AbstractCell
 * - rename currentOutput/newOutput to value/newValue
 *
 * Revision 1.4  2002/10/29 10:25:32  cmarti
 * - only inherit from AbstractSpikingCell
 * - use the example from SpikeSoundTest to get this functional
 *
 * Revision 1.3  2002/10/24 12:05:51  cmarti
 * add GPL header
 *
 * Revision 1.2  2002/10/01 16:16:54  cmarti
 * change package and import names to new hierarchy
 *
 * Revision 1.1  2002/09/25 16:52:27  tobi
 * initial version, not functional
 *
 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.Random;
import ch.unizh.ini.friend.simulation.Updateable;
import ch.unizh.ini.friend.simulation.AcceptsInput;
import java.util.Collection;
import java.util.ArrayList;

/**
 * A poisson spiking mechanism.
 * @author  $Author: cmarti $
 @since $Revision: 1.6 $
 
 */
public class PoissonSoma extends AbstractSpikingCell {
    
    /** Spike rate at <code>weightedInputs() == 1.0f</code>. */
    protected static final float MAX_SPIKE_RATE = 500.0f;
    
    /** The pseudo-random generator. */
    protected Random r = new Random();
    
    /** Is the cell making a spike now?  */
    public boolean isSpike() {
        return newValue > 0.5f;
    }
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
        newValue = 0.0f;
        if (r.nextFloat() < dt*integrateInputs()*MAX_SPIKE_RATE) {
            newValue = 1.0f;
        }
    }
    
}
