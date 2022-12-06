/*
 $Id: TimePrinter.java,v 1.5 2002/11/05 15:24:49 cmarti Exp $
 

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

 
 $Log: TimePrinter.java,v $
 Revision 1.5  2002/11/05 15:24:49  cmarti
 move OscillationgOutput, OutputPrinter and TimePrinter from simulation to test package.

 Revision 1.4  2002/10/24 12:05:50  cmarti
 add GPL header

 Revision 1.3  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.2  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.1  2002/09/17 18:57:29  cmarti
 initial version

 */

package ch.unizh.ini.friend.tests;

import java.io.PrintStream;
import ch.unizh.ini.friend.simulation.*;

/**
 * Prints the time dt, which is passed to all <code>Updateable</code> instances, to a stream.
 * @author Christof Marti
 * @version $Revision: 1.5 $
 */
public class TimePrinter implements Updateable {
    
    /** The stream to print to. */
    protected PrintStream stream;
    
    /** Initializes the instance to print to the given stream.
     * @param stream The stream to write to.
     */
    public TimePrinter(PrintStream stream) {
        //assert stream != null;
        this.stream = stream;
    }
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
        stream.println("Time: " + dt);
    }
    
    /** Updates the actual state to the newly computed - aka double-buffering.
     * Doesn't need to do anything here.
     */
    public void update() {
    }
    
}
