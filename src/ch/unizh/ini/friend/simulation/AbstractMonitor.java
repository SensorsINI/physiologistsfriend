/*
 $Id: AbstractMonitor.java,v 1.7 2002/10/31 22:47:28 cmarti Exp $
 
 
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
 
 
 $Log: AbstractMonitor.java,v $
 Revision 1.7  2002/10/31 22:47:28  cmarti
 - remove Monitor, use AcceptsInput instead
 - rename get-/setMonitoredInput() in AbstractMonitor to get-/setInput()
 - move both methods up into AbstractAcceptsInput and add them to AcceptsInput
 - adapt other classes to these changes

 Revision 1.6  2002/10/31 21:13:26  cmarti
 - AbstractMonitor and AbstractAcceptsInputServesOutput implement Updateable
 - AbstractAcceptsInput no longer implements Updateable

 Revision 1.5  2002/10/31 20:47:48  cmarti
 - make AbstractMonitor support multiple inputs
 - adapt AudioOutput and SimpleOutputMonitor (they still support only one input)

 Revision 1.4  2002/10/29 12:28:39  cmarti
 add comment why getInputs() is overloaded (it's additionally synchronized)

 Revision 1.3  2002/10/29 12:25:36  cmarti
 clean up mess introduced by the last commit

 Revision 1.2  2002/10/29 12:17:09  cmarti
 correct typo in javadoc

 Revision 1.1  2002/10/29 12:16:35  cmarti
 - add AbstractMonitor in the hierachy between AbstractAcceptsInput and SimpleOutputMonitor
 - move implementation of the Monitor interface from SimpleOutputMonitor up into AbstractMonitor

 */

package ch.unizh.ini.friend.simulation;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Default implementation for a monitor.
 * @author Christof Marti
 * @version $Revision: 1.7 $
 */
public abstract class AbstractMonitor extends AbstractAcceptsInput implements Updateable {
    
    /** Creates a new instance with currently no inputs. */
    public AbstractMonitor() {
        super();
    }

    /** Creates a new instance with currently no inputs and
     * with the given initial capacity.
     * @param n Number of initial capacity for inputs.
     */
    public AbstractMonitor(int n) {
        super(n);
    }

    /** Creates a new instance with the given collection of inputs.
     * @param inputs The collection of inputs.
     */
    public AbstractMonitor(Collection inputs) {
        super(inputs);
    }
    
    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public AbstractMonitor(ServesOutput input) {
        super(input);
    }
    
    /** Returns a collection of all inputs to this simulation component. Note that the method is
     * synchronized (it's otherwise identical to the overloaded one).
     * @return The inputs.
     */
    public synchronized Collection getInputs() {
        return super.getInputs();
    }
    
    /** Sets the collection of all inputs to this simulation component. Note that the method is
     * synchronized (it's otherwise identical to the overloaded one).
     * @param inputs The inputs.
     */
    public synchronized void setInputs(Collection inputs) {
        super.setInputs(inputs);
    }
    
    /** Returns the first (if any) input. Note that the method is
     * synchronized (it's otherwise identical to the overloaded one).
     * @return The input.
     */
    public synchronized ServesOutput getInput() {
        return super.getInput();
    }
    
    /** Sets the first (and only) input. Note that the method is
     * synchronized (it's otherwise identical to the overloaded one).
     * @param input The input.
     */
    public synchronized void setInput(ServesOutput input) {
        super.setInput(input);
    }
    
    /** Updates the actual state to the newly computed - aka double-buffering. Does nothing here. */
    public void update() {
    }
    
}
