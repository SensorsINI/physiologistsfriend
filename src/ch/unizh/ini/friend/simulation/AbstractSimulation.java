/*
 $Id: AbstractSimulation.java,v 1.5 2003/06/26 00:33:42 tobi Exp $
 

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

 
 $Log: AbstractSimulation.java,v $
 Revision 1.5  2003/06/26 00:33:42  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.4  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.3  2002/10/08 12:59:08  tobi
 made on flag volatile according to some obscure possibility that it could be optimized away...

 Revision 1.2  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/24 20:57:13  cmarti
 initial version

 */

package ch.unizh.ini.friend.simulation;

/**
 * Provides a few default implementations for the <code>SimulationTask</code> interface.
 * @author Christof Marti
 * @version $Revision: 1.5 $
 */
public abstract class AbstractSimulation implements SimulationTask {
    
    /** False iff the simulation should stop. */
    protected volatile boolean on; // don't optimize this away because several threads could be accessing on
    
    /** The simulation step. */
    protected SimulationStep step;
    
    /** delay in ms between thread simulation update cycles */
    protected long delay=10;
        
    /** Creates a new instance with the given simulation step.
     * @param step The simulation step instance.
     */
    public AbstractSimulation(SimulationStep step) {
        this.step = step;
    }
        
    /** Returns the simulation step instance.
     * @return The simulation step.
     */
    public SimulationStep getStep() {
        return step;
    }
    
    /** Returns true if the simulation is running.
     * @return True if the simulation is running.
     */
    public boolean isOn() {
        return on;
    }
    
    /** Adds an updateable to the simulation.
     * @param u An updateable.
     *
     */
    public abstract void addUpdateable(Updateable u) ;
    
    /** @return the delay between simulation steps in ms
     * @see #setDelay
     *
     */
    public long getDelay() {
        return delay;
    }
    
    /** @return the number of simulation iterations per step
     * @see #setNumberOfIterationsPerStep
     *
     */
    public int getNumberOfIterationsPerStep() {
        return getStep().getNumberOfIterationsPerStep();
    }
    
    /** Removes an updateable from the simulation.
     * @param u An updateable.
     *
     */
    public abstract void removeUpdateable(Updateable u) ;
    
    /** set the timer delay in ms between each simulation step
     * @param ms the delay in ms
     * @see #getDelay
     *
     */
    public void setDelay(long ms) {
        delay=ms;
        System.out.println("AbstractSimulation: set delay to "+delay+" ms");
    }
    
    /** set the number of iterations over the updateables for each step.
     * This is used for example to propogate an input through a feedforward network more quickly per GUI update. Since a change at the
     * input of a network that is N deep takes at least N cycles to get to the output (owing to the double-buffering), you should set
     * the number of iterations to N if you want an input to have an effect in the same simulation step.
     *
     * @param n the number
     *
     */
    public void setNumberOfIterationsPerStep(int n) {
        getStep().setNumberOfIterationsPerStep(n);
    }
    
    /** Starts the simulation.  */
    public abstract void start();
    
    /** Stops the simulation.  */
    public abstract void stop();    
}
