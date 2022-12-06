/*
 $Id: SimpleOutputMonitor.java,v 1.11 2002/10/31 22:47:28 cmarti Exp $
 

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

 
 $Log: SimpleOutputMonitor.java,v $
 Revision 1.11  2002/10/31 22:47:28  cmarti
 - remove Monitor, use AcceptsInput instead
 - rename get-/setMonitoredInput() in AbstractMonitor to get-/setInput()
 - move both methods up into AbstractAcceptsInput and add them to AcceptsInput
 - adapt other classes to these changes

 Revision 1.10  2002/10/31 20:47:48  cmarti
 - make AbstractMonitor support multiple inputs
 - adapt AudioOutput and SimpleOutputMonitor (they still support only one input)

 Revision 1.9  2002/10/29 12:25:36  cmarti
 clean up mess introduced by the last commit

 Revision 1.8  2002/10/29 12:16:35  cmarti
 - add AbstractMonitor in the hierachy between AbstractAcceptsInput and SimpleOutputMonitor
 - move implementation of the Monitor interface from SimpleOutputMonitor up into AbstractMonitor

 Revision 1.7  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput

 Revision 1.6  2002/10/25 08:39:10  cmarti
 'extend ManyInputs' instead of 'implement AcceptsInput'

 Revision 1.5  2002/10/24 12:05:50  cmarti
 add GPL header

 Revision 1.4  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.3  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.2  2002/10/01 12:30:01  tobi
 comment added

 Revision 1.1  2002/09/28 19:52:36  cmarti
 initial version

 */

package ch.unizh.ini.friend.simulation;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.SwingUtilities;

/**
 * Provides synchronization between the gui and the simulation needed for the gui to monitor
 * the output of a simulation component.
 * @author Christof Marti
 * @version $Revision: 1.11 $
 */
public class SimpleOutputMonitor extends AbstractMonitor {
    
    /**
     * An abstract class whose heirs' instances can be passed to <code>invokeLater()</code>
     * by an instance of <code>SimpleOutputMonitor</code>. This class will (usually) be inherited
     * from by specific classes of the gui.
     */
    public static abstract class Deliverable implements Runnable, Cloneable {
            
            /** The value carried by this deliverable. */
            protected float value = 0.0f;
            
            /** Sets the value carried by this deliverable. */
            public void setValue(float value) {
                this.value = value;
            }
            
            /** Clone this. */
            public Object clone() {
                try {
                    return super.clone();
                }
                catch (CloneNotSupportedException e) {
                    //assert false : "not cloneable!";
                    return null;
                }
            }
            
    }
    
    /** The prototype of the deliverable. */
    protected Deliverable deliverable;
    
    /** The update interval in milliseconds. */
    protected long updateInterval = 100;
    
    /** The time of the next update. */
    protected long nextUpdate = 0;
    
    /** Creates a new instance of SimpleOutputMonitor */
    public SimpleOutputMonitor(Deliverable deliverable) {
        this(deliverable, null);
    }
    
    /** Creates a new instance of SimpleOutputMonitor with the given input. */
    public SimpleOutputMonitor(Deliverable deliverable, ServesOutput input) {
        super(input);
        this.deliverable = deliverable;
    }
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     *
     */
    public void compute(float dt) {
        ServesOutput input = getInput();
        long currentTime = System.currentTimeMillis();
        if (currentTime >= nextUpdate) {
            nextUpdate = currentTime + updateInterval;

            float value;
            if (input == null) {
                value = 0.0f;
            } else {
                value = input.output();
            }

            // we should clone here because we may get called again before swing processes this deliverable.
            Deliverable acctualDeliverable = (Deliverable)deliverable.clone();
            acctualDeliverable.setValue(value);
            SwingUtilities.invokeLater(acctualDeliverable);
        }
    }
    
}
