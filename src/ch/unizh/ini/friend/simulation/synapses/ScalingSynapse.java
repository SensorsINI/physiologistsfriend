/*
 $Id: ScalingSynapse.java,v 1.4 2002/11/07 17:47:26 cmarti Exp $
 
 
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
 
 
 $Log: ScalingSynapse.java,v $
 Revision 1.4  2002/11/07 17:47:26  cmarti
 add getter/setter for property weight

 Revision 1.3  2002/11/05 20:40:50  cmarti
 change order of constructor parameters

 Revision 1.2  2002/11/05 20:24:45  cmarti
 only accept an instance of ServesOutput as a single input

 Revision 1.1  2002/11/05 19:49:31  cmarti
 initial version

 */

package ch.unizh.ini.friend.simulation.synapses;

import java.util.Collection;
import ch.unizh.ini.friend.simulation.*;

/**
 * A simple scaling synapse.
 * @author Christof Marti
 * @version $Revision: 1.4 $
 */
public class ScalingSynapse extends AbstractSynapse {
    
    /** The default weight with which the input will be multiplied. */
    public static float DEFAULT_WEIGHT = 1.0f;
    
    /** The weight with which the input will be multiplied. */
    protected float weight;
    
    /** Creates a new instance with currently no inputs and the given scaling factor.
     * @param weight The scaling factor/weight.
     */
    public ScalingSynapse(float weight) {
        super();
        this.weight = weight;
    }

    /** Creates a new instance with currently no inputs and the default weight. */
    public ScalingSynapse() {
        super(null);
        this.weight = DEFAULT_WEIGHT;
    }

    /** Creates a new instance with the given collection of inputs and weight.
     * @param inputs The collection of inputs.
     * @param weight The scaling factor/weight.
     */
    public ScalingSynapse(Collection inputs, float weight) {
        super(inputs);
        this.weight = weight;
    }
    
    /** Creates a new instance with the given input and weight.
     * @param input The input.
     * @param weight The scaling factor/weight.
     */
    public ScalingSynapse(ServesOutput input, float weight) {
        super(input);
        this.weight = weight;
    }
    
    /** Returns the current output value of the component.
     * @return The ouput value (current / potential).
     *
     */
    public float output() {
        return weight*integrateInputs();
    }
    
    /** Returns the weight with which the input will be multiplied.
     * @return The weight.
     *
     */
    public float getWeight() {
        return weight;
    }
    
    /** Sets the weight with which the input will be multiplied.
     * @param weight New weight.
     *
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
    
}
