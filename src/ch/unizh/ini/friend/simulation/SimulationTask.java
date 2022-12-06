/*
 $Id: SimulationTask.java,v 1.5 2003/06/26 00:33:47 tobi Exp $
 

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

 
 $Log: SimulationTask.java,v $
 Revision 1.5  2003/06/26 00:33:47  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.4  2002/10/24 12:05:50  cmarti
 add GPL header

 Revision 1.3  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.2  2002/09/28 17:57:32  cmarti
 explicitly support the addition and removal of Updateables to and from the simulation

 Revision 1.1  2002/09/24 20:53:51  cmarti
 initial version

 */

package ch.unizh.ini.friend.simulation;

/**
 * The interface of a simulation for the controlling thread.
 * @author Christof Marti
 * @version $Revision: 1.5 $
 */
public interface SimulationTask {
    
    /** Starts the simulation. */
    public void start();
    
    /** Stops the simulation. */
    public void stop();
    
    /** Returns the simulation step instance.
     * @return The simulation step.
     */
    public SimulationStep getStep();

   /** Adds an updateable to the simulation.
     * @param u An updateable.
     */
    public void addUpdateable(Updateable u);
    
    /** Removes an updateable from the simulation.
     * @param u An updateable.
     */
    public void removeUpdateable(Updateable u);
    
    /** set the number of iterations over the updateables for each step. 
     *This is used for example to propogate an input through a feedforward network more quickly per GUI update. Since a change at the
     *input of a network that is N deep takes at least N cycles to get to the output (owing to the double-buffering), you should set 
     *the number of iterations to N if you want an input to have an effect in the same simulation step.
     *
     *@param n the number
     */
    public void setNumberOfIterationsPerStep(int n);
    
    /** @return the number of simulation iterations per step 
     *@see #setNumberOfIterationsPerStep
     */
    public int getNumberOfIterationsPerStep();
    
    /** set the timer delay in ms between each simulation step
     *@param ms the delay in ms
     *@see #getDelay
     **/
    public void setDelay(long ms);
    
    /** @return the delay between simulation steps in ms
     *@see #setDelay
     */
    public long getDelay();
    
}
