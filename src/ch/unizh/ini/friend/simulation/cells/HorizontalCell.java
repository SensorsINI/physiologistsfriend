/*
 $Id: HorizontalCell.java,v 1.13 2003/07/07 02:44:21 tobi Exp $
 

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

 
 $Log: HorizontalCell.java,v $
 Revision 1.13  2003/07/07 02:44:21  tobi

 added time constant setter/getter to hcell.  messed with javadoc.

 Revision 1.12  2003/07/03 16:54:27  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.11  2002/11/07 18:52:10  cmarti
 - remove getEquallyWeightedInputsInstance() from HorizontalCell
 - use the new construction methods from AbstractAcceptsInputs in SimulationSetupFactory
    for creating a HorizontalCell with synapses

 Revision 1.10  2002/11/05 22:09:46  cmarti
 remove AbstractWeightedInputServesOutput and put a much more flexible solution using
 the concept of synapses in place

 Revision 1.9  2002/10/31 21:47:28  cmarti
 - split AbstractAcceptsInputServesOutput, added AbstractCell
 - rename currentOutput/newOutput to value/newValue

 Revision 1.8  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput

 Revision 1.7  2002/10/25 08:01:38  cmarti
 make HorizontalCell extend WeightedInputs

 Revision 1.6  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.5  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.4  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.3  2002/09/25 16:51:38  tobi
 classified cells as graded or spiking, added interfaces GradedCell, SpikingCell.
 made photorecepto, bipolar, horizontal cell graded cells with gradedOutput() method.

 Revision 1.2  2002/09/17 13:23:33  cmarti
 - add a low-pass filter (field lowPass)
 - low-pass filter the output

 Revision 1.1  2002/09/17 12:57:18  cmarti
 renaming AveragingCell to HorizontalCell

 Revision 1.4  2002/09/16 20:44:42  cmarti
 removing parameter dt from update() in Updateable

 Revision 1.3  2002/09/16 11:20:18  cmarti
 removal of UpdateSource/Listener

 Revision 1.2  2002/09/13 08:40:29  cmarti
 adaption of the splitting of update method into compute and update method

 Revision 1.1  2002/09/10 20:39:32  cmarti
 intial version

 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.simulation.filter.*;
import ch.unizh.ini.friend.simulation.synapses.*;

/**
 * Cell that averages the inputs and outputs the lowpass-filtered value. You can set the lowpass time constant with {@link #setTau}.
 * @author Christof Marti
 * @version $Revision: 1.13 $
 */
public class HorizontalCell extends AbstractCell implements GradedCell {
    
    /** the lowpass time constant of the HorizontalCell */
    protected float tau=1f;
    
    
    /** A low-pass filter. */
    protected Filter lowPass = new LowPassFilter(tau, 0.0f);
    
    /**
     * Creates a new instance with currently no inputs.
     */
    public HorizontalCell() {
        super();
    }

    /**
     * Creates a new instance with currently no inputs and
     * with the given initial capacity.
     * @param n Number of initial capacity for inputs.
     */
    public HorizontalCell(int n) {
        super(n);
    }

    /**
     * Creates a new instance with the given collections of inputs and weights.
     * @param inputs The collection of inputs.
     */
    public HorizontalCell(Collection inputs) {
        super(inputs);
    }
    
    /** Computes the new state of this component of the simulation.
     *The horizontal cell sums the inputs, so the synapses from the photoreceptors should be scaled inversely with the 
     number of inputs to compute an average photoreceptor output.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
        newValue = lowPass.filter(integrateInputs(), dt);
    }
    
    /** returns the graded output from the cell  */
    public float getGradedOutput() {
        return output();
    }
    
    /** Getter for property tau.
     * @return Value of property tau.
     *@see #tau
     *
     */
    public float getTau() {
        return tau;
    }
    
    /** Setter for property tau.
     * @param tau New value of property tau.
     *@see #tau
     *
     */
    public void setTau(float tau) {
        this.tau = tau;
        ((LowPassFilter)lowPass).setTau(tau);
    }
    
}
