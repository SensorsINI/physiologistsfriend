/*
 $Id: IntegrateFireCell.java,v 1.25 2003/07/03 16:54:27 tobi Exp $
 
 
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
 
  */

package ch.unizh.ini.friend.simulation.cells;

import java.util.Collection;
import java.util.ArrayList;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.simulation.filter.*;
import ch.unizh.ini.friend.topology.*;

/**
 * Implementation of a spiking cell with an integrate-and-fire spike mechanism.
 * The cells sums its inputs at each time step, resulting in a membrane {@link #integrationPotential potential}.
 * If the potential exceeds the {@link #threshold}, it is reset on the next update to 0. Calling {@link #isSpike} duing this time step will show
 * that the cell is emitting a spike. The cell's potential is clamped on the negative side by
 * {@link #potasiumReversal the potasium reversal potential}, so that the cell cannot be infinitely inhibited. Thus the cell can recover quickly from massive
 * inhibition.
 * The cell has a passive leak to the resting potential (zero) whose time constant is {@link #leakTime}.
 *
 * <p>
 * The {@link #output analog output} of a spiking neuron is computed as the instantaneous spike rate -- the reciprocal of the time between the last
 * two spikes.  There are three caveats:
 * <ul>
 * <li>
 * This spike rate is computed as the rate per {@link #SPIKE_RATE_TIME_SCALE}.
 * This normalization to a time of typically 20ms makes the spike rate typically a number around 1. </li>
 * <li>
 * This number is smoothed a bit to reduce
 * the fluctuations. This smoothing is a first order lowpass filter with a time constant of {@link #spikeRateMeasurementWindow}.
 * </li>
 * <li>
 * The maximum spike rate is set by {@link #maxRate}.
 * </li>
 * </ul>
 * The output variables are double-buffered
 * so that each simulation step only sees the last potential, not the one that will become the new one after {@link #update} is called.
 *
 * @see ch.unizh.ini.friend.simulation.Updateable
 *
 * @author Christof Marti/Tobi Delbruck
 * @version $Revision: 1.25 $
 */
public class IntegrateFireCell extends AbstractSpikingCell {
    
    /** The leak time constant in seconds. */
    public float leakTime = 3.0f;
    
    /** The default input weight. ({@value}) */
    protected static final float INPUT_WEIGHT = 14.0f;
    
    /** The threshold where the cell will fire. */
    protected float threshold = 0.2f;
    
    /** the low pass filtering time constant of the cell potential, in seconds */
    //    protected float membraneLowPassTau=0f;
    
    
    /**
     * Potasium reversal potential.  The cell potential will not go below this value.
     * This is the lower power rail of the cell. It prevents the cell from becoming infinitely
     * hyperpolarized.
     */
    protected float potasiumReversal=-0.3f;
    
    /** @see #potasiumReversal
     *@param r the reversal potential value, default is -0.3
     */
    public void setPotasiumReversal(float r){ potasiumReversal=r;}
    
    /** @see #potasiumReversal
     *@return the reversal potential
     */
    public float getPotasiumReversal(){return potasiumReversal;}
    
    
    /** spike rate time scale in seconds. The {@link #output} of the cell is the rate of spikes per this time.
     * This scale can be used to normalize spike rates to near-unity values. (Value is {@value}.)
     */
    public static final float SPIKE_RATE_TIME_SCALE=0.02f;
    
    /** spike rate measurement time window in seconds. The spike rate is measured with a fading memory of this duration. */
    protected float spikeRateMeasurementWindow=0.1f;
    
    //    public void setSpikeRateMeasurementWindow(
    private LowPassFilter rateMeasurementFilter=new LowPassFilter(spikeRateMeasurementWindow,0f);
    
    /** Encodes whether the cell is making a spike now. */
    protected boolean spike=false;
    
    /** Encodes whether the cell will make a spike after the next {@link #update}. */
    protected boolean newSpike = false;
    
    
    /** The currently integrated membrane potential. */
    protected float integrationPotential = 0.0f;
    
    
    /** the maximum spike rate, measured in spikes/{@link #SPIKE_RATE_TIME_SCALE}. */
    protected float maxRate=2f;
    
    
    /** Creates a new instance with currently no inputs. */
    public IntegrateFireCell() {
        super();
    }
    
    /** Creates a new instance with currently no inputs and
     * with the given initial capacity.
     * @param n Number of initial capacity for inputs.
     */
    public IntegrateFireCell(int n) {
        super(n);
    }
    
    /** Creates a new instance with the given collections of inputs.
     * @param inputs The collection of inputs.
     */
    public IntegrateFireCell(Collection inputs) {
        super(inputs);
    }
    
    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public IntegrateFireCell(ServesOutput input) {
        super(input);
    }
    
    private float time=0;
    private float lastSpikeTime=0;
    
    /** Computes the new state of this component of the simulation.
     * The membrane potential's inputs (the result of {@link #integrateInputs}) are multiplied by the time step <code>dt</code>
     *and summed to the present potential; the sum is multiplied by
     * exp(-dt/{@link #leakTime}). If the membrane exceeds {@link #threshold} it is reset to zero.
     * The potential is clamped to a miniumum of {@link #potasiumReversal}.
     *
     * <p>
     * If the cell has spiked (gone past threshold), it will show up after the next {@link #update}. This spike will last one cycle.
     * The spike will not be visible during the present cycle.
     *
     *<p>
     *The instantaneous spike rate is computed as 
     *<pre>
        float instantaneousRate=SPIKE_RATE_TIME_SCALE/(time-lastSpikeTime+Float.MIN_VALUE);
        if(instantaneousRate>maxRate) instantaneousRate=maxRate;
        newValue = rateMeasurementFilter.filter(instantaneousRate,dt);
    </pre>
     <code>rateMeasurementFilter</code> is an instance of {@link ch.unizh.ini.friend.simulation.filter.LowPassFilter} 
     with time constant {@link #spikeRateMeasurementWindow}.
     <code>newValue</code> then becomes the value returned as the cell's {@link #output}
     * @param dt The time that has passed since the last invocation in seconds.
     */
    public void compute(float dt) {
        time+=dt; // to measure spike rate
        if (spike) {     // if we had made a spike last time
            newSpike = false; //  now we aren't anymore
        } else {
            if(dt<leakTime/3){ // optimize for short time steps
                integrationPotential = (integrationPotential + dt*integrateInputs())*(float)Math.exp(-dt/leakTime); 
                // exponential decay to zero with time constant leakTime when there is no input
            }else{
                integrationPotential = (integrationPotential + dt*integrateInputs())*(1-dt/leakTime);
                // for small dt/leakTime, approximate exp
            }
            if(integrationPotential<potasiumReversal){  // negative clamp
                integrationPotential=potasiumReversal;
            }else if (integrationPotential > threshold) {  // spike
                integrationPotential = 0.0f;
                newSpike=true; // this codes for a spike
                lastSpikeTime=time;  // we save the time of this spike
            }
        }
        float instantaneousRate=SPIKE_RATE_TIME_SCALE/(time-lastSpikeTime+Float.MIN_VALUE);
        if(instantaneousRate>maxRate) instantaneousRate=maxRate;
        newValue = rateMeasurementFilter.filter(instantaneousRate,dt);
    }
    
    /** Updates the actual state to the newly computed - aka double-buffering.
     */
    public void update() {
        super.update();
        spike = newSpike;
    }
        
    /** is the cell making a spike now?
     * @return true if the cell just made a spike
     * @see #output
     */
    public boolean isSpike() {
        return spike;
    }
    
    /** Getter for property spikeRateMeasurementWindow.
     * @return Value of property spikeRateMeasurementWindow.
     *
     */
    public float getSpikeRateMeasurementWindow() {
        return spikeRateMeasurementWindow;
    }
    
    /** Setter for property spikeRateMeasurementWindow.
     * @param spikeRateMeasurementWindow New value of property spikeRateMeasurementWindow.
     *
     */
    public void setSpikeRateMeasurementWindow(float spikeRateMeasurementWindow) {
        this.spikeRateMeasurementWindow = spikeRateMeasurementWindow;
        rateMeasurementFilter.setTau(spikeRateMeasurementWindow);
    }
    
    /** Getter for property maxRate.
     * @return Value of property maxRate.
     *
     */
    public float getMaxRate() {
        return maxRate;
    }
    
    /** Setter for property maxRate.
     * @param maxRate New value of property maxRate.
     *
     */
    public void setMaxRate(float maxRate) {
        this.maxRate = maxRate;
    }
    
    /** Getter for property threshold.
     * @return Value of property threshold.
     *
     */
    public float getThreshold() {
        return threshold;
    }
    
    /** Setter for property threshold.
     * @param threshold New value of property threshold.
     *
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
    
    /** Getter for property leakTime.
     * @return Value of property leakTime.
     *
     */
    public float getLeakTime() {
        return leakTime;
    }
    
    /** Setter for property leakTime.
     * @param leakTime New value of property leakTime.
     *
     */
    public void setLeakTime(float leakTime) {
        this.leakTime = leakTime;
    }
    
}

/* 
 $Log: IntegrateFireCell.java,v $
 Revision 1.25  2003/07/03 16:54:27  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.24  2003/06/26 00:33:48  tobi
 
 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output
 
 Revision 1.23  2002/11/08 17:13:00  cmarti
 fix javadoc
 
 Revision 1.22  2002/11/05 22:09:46  cmarti
 remove AbstractWeightedInputServesOutput and put a much more flexible solution using
 the concept of synapses in place
 
 Revision 1.21  2002/10/31 21:47:28  cmarti
 - split AbstractAcceptsInputServesOutput, added AbstractCell
 - rename currentOutput/newOutput to value/newValue
 
 Revision 1.20  2002/10/28 12:46:45  cmarti
 adding AbstractSpikingCell and making IntegrateFireCell a descendant of it
 
 Revision 1.19  2002/10/25 08:02:25  cmarti
 remove 'implements ServesOutput' (now implemented by super-class)
 
 Revision 1.18  2002/10/24 12:05:51  cmarti
 add GPL header
 
 Revision 1.17  2002/10/16 11:27:24  tobi
 made spikerate measurement window filter time constant lower case to show
 that it is an instance field.
 
 changed  the DS simple cell so that the inhibitory cell has a long spike rate averaging filter.
 this seems to improve the DS. not sure why.
 
 Revision 1.16  2002/10/15 19:27:07  tobi
 lots of javadoc added,
 mouse wheel enabled.
 
 Revision 1.15  2002/10/15 09:45:39  tobi
 measured rate is double buffered like spike and potential
 
 Revision 1.14  2002/10/10 19:29:03  tobi
 initial version of DS cell, made some I&F parameters public instance instead of public static final so that they can be set for individual cells.
 
 Revision 1.13  2002/10/08 19:41:33  tobi
 changed spike rate measurement to be a fading window, to smooth spike output over time
 
 Revision 1.12  2002/10/07 19:47:41  tobi
 made threshold a public static final like the other cell parameters.  may need to change this to make threshold adaptive.
 
 reduced potassium reversal potential to -0.3 to speed up responses of cortical cells to grating. (cells can not be so hyperpolarized now).
 
 Revision 1.11  2002/10/06 17:01:00  tobi
 added potasium reveral potential to keep cell from becoming infinitely inhibibited.
 
 Revision 1.10  2002/10/06 08:57:35  tobi
 changed output to spike rate (can use isSpike to see if its spiking at this time step).
 changed scale of output to be spikes/20ms in order to normalize more physiologically.
 
 Revision 1.9  2002/10/05 23:00:34  tobi
 added maxRate (refractory period) to i&f cell to limit output
 
 Revision 1.8  2002/10/05 22:44:00  tobi
 integrate and fire cell analog output() is now instantaneous spike rate.
 odd simple cell works, but not great yet.
 
 Revision 1.7  2002/10/05 17:35:25  tobi
 added some clarifying javadoc
 
 fixed photoreceptor gethex arrangement method to correctly set the retinotopic location of the resutling
 photoreceptors, after they are rotated into the new locaitons.
 
 Revision 1.6  2002/10/04 11:01:38  tobi
 added SpkingCell interface to give isSpike() output for audioOuptut
 
 Revision 1.5  2002/10/01 20:50:08  tobi
 implemented Retinotopic and added construction of location from previous retina level
 in array list static helper methods.  seems to work.
 
 Revision 1.4  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy
 
 Revision 1.3  2002/10/01 11:28:51  cmarti
 change to digital output
 
 Revision 1.2  2002/09/29 17:26:10  cmarti
 - rearanged compute()
 - more sensible defaults for leakTime and INPUT_WEIGHT
 
 Revision 1.1  2002/09/26 14:39:16  cmarti
 initial version
 
*/