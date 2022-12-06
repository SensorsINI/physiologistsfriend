/*
 $Id: ThreadedSimulation.java,v 1.13 2003/06/26 00:33:47 tobi Exp $
 
 
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
 
 
 $Log: ThreadedSimulation.java,v $
 Revision 1.13  2003/06/26 00:33:47  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.12  2002/10/24 12:05:50  cmarti
 add GPL header
 
 Revision 1.11  2002/10/15 19:27:07  tobi
 lots of javadoc added,
 mouse wheel enabled.
 
 Revision 1.10  2002/10/15 09:46:44  tobi
 added commented out delay selection based on OS
 
 Revision 1.9  2002/10/08 12:57:50  tobi
 fixed thread restarting bug.  you can't re-start a thread that has exited.  you must make  a new thread
 if you don't get fancy and use a thread pool.
 
 Revision 1.8  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.
 
 Revision 1.7  2002/10/08 07:47:50  tobi
 
 added complex cell, on the way, made some changes in odd and even cell.
 threadedsimulation crashes on first REstart, can't figure out why.
 
 Revision 1.6  2002/10/07 07:54:17  tobi
 changed back to threaded but with explicit sleep and with priority set plus 1.
 this mean simulation thread runs more predictably in turn with GUI.
 this seems to be a good setup.
 
 Revision 1.5  2002/10/06 10:51:18  tobi
 fixed javadoc links
 
 Revision 1.4  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy
 
 Revision 1.3  2002/10/01 10:43:17  tobi
 put a Thread.currentThread().yield in the simulation update because it seems to
 improve the regularity of the GUI and sound output.
 
 Revision 1.2  2002/09/28 17:57:32  cmarti
 explicitly support the addition and removal of Updateables to and from the simulation
 
 Revision 1.1  2002/09/24 20:59:06  cmarti
 initial version
 
 */

package ch.unizh.ini.friend.simulation;

/**
 * Implementation of the simulation loop in a separate thread.
 * @author Christof Marti
 * @version $Revision: 1.13 $
 */
public class ThreadedSimulation extends AbstractSimulation implements Runnable {
    
    /** The simulation thread. */
    protected Thread thread;
    
    /** Creates a new instance with the given simulation step.
     * @param step The simulation step instance.
     */
    public ThreadedSimulation(SimulationStep step) {
        super(step);
        thread = new Thread(this);
        thread.setName("FriendSimulation");
        //        String system=System.getProperty("os.name");
        //System.err.println("system = " + system );
        //        if(system.startsWith("Windows")){
        //            delay=10;
        //        }else if(system.startsWith("Mac")){
        //            delay=20;
        //        }
    }
    
    /** @param step the simulation step
     *@param delay sleep delay between iterations
     **/
    public ThreadedSimulation(SimulationStep step, long delay){
        this(step);
        this.delay=delay;
    }
    
    
    /** The main-loop of the simulation. */
    public void run() {
        step.init();
        while (on) {
            step.step();
            try{
                Thread.currentThread().sleep(delay);
            }catch(InterruptedException e){}
        }
    }
    
    /** Starts the thread on the main-loop. */
    public void start() {
        //assert !on;
        on = true;
        if(!thread.isAlive()) thread=new Thread(this); // thread may need to be new'ed if it was stopped.
        // we cannot restart a thread that has exited. see http://forum.java.sun.com/thread.jsp?forum=54&thread=109450
        thread.start();
        thread.setPriority(Thread.NORM_PRIORITY+1);
    }
    
    /** Tells the main-loop to stop by the end of the current iteration. */
    public void stop() {
        //assert on;
        on = false;
        try { thread.join(); }
        catch (InterruptedException e) {
            // assert false;
        }
    }
    
    /** Adds an updateable to the simulation. The precondition is that the simulation isn't running.
     * @param u An updateable.
     */
    public void addUpdateable(Updateable u) {
        //assert !isOn() : "simulation is running!";
        //assert !getStep().getUpdateables().contains(u) : "allready added!";
        getStep().getUpdateables().add(u);
    }
    
    /** Removes an updateable from the simulation. The precondition is that the simulation isn't running.
     * @param u An updateable.
     */
    public void removeUpdateable(Updateable u) {
        //assert !isOn() : "simulation is running!";
        //assert getStep().getUpdateables().contains(u) : "isn't in the simulation!";
        getStep().getUpdateables().remove(u);
    }
    
}
