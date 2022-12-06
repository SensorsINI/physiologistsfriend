/*
 $Id: TimedSimulation.java,v 1.7 2003/06/26 00:33:47 tobi Exp $
 
 
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
 
 
 $Log: TimedSimulation.java,v $
 Revision 1.7  2003/06/26 00:33:47  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.6  2002/10/24 12:05:50  cmarti
 add GPL header
 
 Revision 1.5  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.
 
 Revision 1.4  2002/10/06 08:58:44  tobi
 changed setCoalesce to false because this seems to result in a smoother simulation.
 
 Revision 1.3  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy
 
 Revision 1.2  2002/09/28 17:57:32  cmarti
 explicitly support the addition and removal of Updateables to and from the simulation
 
 Revision 1.1  2002/09/24 21:01:33  cmarti
 initial version
 
 */

package ch.unizh.ini.friend.simulation;

import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

/**
 * Implementation of the simulation loop with the javax.swing.Timer mechanism.
 *The Timer calls are not coalesced into a single call if they queue up.
 * @author Christof Marti
 * @version $Revision: 1.7 $
 */
public class TimedSimulation extends AbstractSimulation implements ActionListener {
    
    /** The simulation timer. */
    protected Timer timer;
    
    /** Creates a new instance with the given simulation step.
     * @param step The simulation step instance.
     */
    public TimedSimulation(SimulationStep step, int delay) {
        super(step);
        timer = new Timer((int)getDelay(), this);
        timer.setCoalesce(false);
    }
    
    /** Gets notified by the timer. */
    public void actionPerformed(ActionEvent actionEvent) {
        if (on) {
            step.step();
        }
    }
    
    /** Starts the simulation (timer). */
    public void start() {
        //assert !on;
        on = true;
        step.init();
        timer.start();
    }
    
    /** Stops the simulation (timer). */
    public void stop() {
        //assert on;
        on = false;
        timer.stop();
    }
    
    /** Adds an updateable to the simulation.
     * @param u An updateable.
     */
    public void addUpdateable(Updateable u) {
        //assert !getStep().getUpdateables().contains(u) : "allready added!";
        getStep().getUpdateables().add(u);
    }
    
    /** Removes an updateable from the simulation.
     * @param u An updateable.
     */
    public void removeUpdateable(Updateable u) {
        //assert getStep().getUpdateables().contains(u) : "isn't in the simulation!";
        getStep().getUpdateables().remove(u);
    }
    
}
