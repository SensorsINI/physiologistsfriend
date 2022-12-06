/*
 $Id: AbstractCell.java,v 1.5 2003/07/03 16:54:26 tobi Exp $
 
 
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
 
 
 $Log: AbstractCell.java,v $
 Revision 1.5  2003/07/03 16:54:26  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.4  2002/11/08 14:06:19  cmarti
 move to package ch.unizh.ini.friend.simulation.cells

 Revision 1.3  2002/11/05 18:04:55  cmarti
 remove AbstractAcceptsInputServesOutput from class hierarchy

 Revision 1.2  2002/11/01 16:18:29  cmarti
 move value and output() from AbstractAcceptsInputServesOutput down to AbstractCell

 Revision 1.1  2002/10/31 21:47:28  cmarti
 - split AbstractAcceptsInputServesOutput, added AbstractCell
 - rename currentOutput/newOutput to value/newValue

 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.Collection;
import ch.unizh.ini.friend.simulation.*;

/**
 * Default implementations for all cells.
 * @author Christof Marti
 * @version $Revision: 1.5 $
 */
public abstract class AbstractCell extends AbstractAcceptsInput implements ServesOutput, Updateable {
    
    /** The current value. */
    protected float value = 0.0f;
    
    /** The new output value. */
    protected float newValue = 0.0f;
    
    /** Creates a new instance with currently no inputs. */
    public AbstractCell() {
        super();
    }

    /** Creates a new instance with currently no inputs and
     * with the given initial capacity.
     * @param n Number of initial capacity for inputs.
     */
    public AbstractCell(int n) {
        super(n);
    }

    /** Creates a new instance with the given collection of inputs.
     * @param inputs The collection of inputs.
     */
    public AbstractCell(Collection inputs) {
        super(inputs);
    }
    
    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public AbstractCell(Object input) {
        super(input);
    }
    
    /** Updates the actual state to the newly computed - aka double-buffering. */
    public void update() {
        value = newValue;
    }
    
    /** Returns the current output value of the component.
     * @return The ouput value.
     */
    public float output() {
        return value;
    }
    
}
