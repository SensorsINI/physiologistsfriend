/*
 * GanglionCell.java
 *
 * Created on September 25, 2002, 5:44 PM
 * $Id: GanglionCell.java,v 1.8 2003/07/03 16:54:26 tobi Exp $
 

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

 
 *
 * $Log: GanglionCell.java,v $
 * Revision 1.8  2003/07/03 16:54:26  tobi
 * fixed a bunch of javadoc errors.
 * made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)
 *
 * made lowpass and highpass filters time constants settable.
 *
 * Revision 1.7  2003/05/10 17:27:44  jgyger
 * Merge from color-branch
 *
 * Revision 1.6.2.2  2003/04/22 18:58:08  jgyger
 * add negative weight support
 *
 * Revision 1.6.2.1  2003/03/16 16:42:28  jgyger
 * modify/add factory methods to support multiple cell inputs
 *
 * Revision 1.6  2002/11/05 22:09:46  cmarti
 * remove AbstractWeightedInputServesOutput and put a much more flexible solution using
 * the concept of synapses in place
 *
 * Revision 1.5  2002/10/24 12:05:51  cmarti
 * add GPL header
 *
 * Revision 1.4  2002/10/08 12:12:05  tobi
 * commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 * for 1.3 compliance.
 *
 * Revision 1.3  2002/10/05 17:35:25  tobi
 * added some clarifying javadoc
 *
 * fixed photoreceptor gethex arrangement method to correctly set the retinotopic location of the resutling
 * photoreceptors, after they are rotated into the new locaitons.
 *
 * Revision 1.2  2002/10/01 16:16:53  cmarti
 * change package and import names to new hierarchy
 *
 * Revision 1.1  2002/09/25 16:52:27  tobi
 * initial version, not functional
 *
 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.ArrayList;
import java.util.Collection;
import ch.unizh.ini.friend.topology.*;
import ch.unizh.ini.friend.simulation.synapses.*;
import ch.unizh.ini.friend.simulation.*;

/**
 * Represents a retinal ganglion cell.
 * @author  $Author: tobi $
 @since $Revision: 1.8 $
 
 */
public class GanglionCell extends IntegrateFireCell implements Retinotopic {
    
    /** Creates a new instance of GanglionCell */
    public GanglionCell() {
    }

    /** Creates a new instance with the given collections of inputs.
     * @param inputs The collection of inputs.
     */
    public GanglionCell(Collection inputs) {
        super(inputs);
    }

    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public GanglionCell(ServesOutput input) {
        super(input);
    }
    
    /** Returns a list of <code>GanglionCell</code> with the default input weight and
     * one input from the given list of inputs.
     * @param inputs A list of cells, each providing the first input to one bipolar cell.
     * @return The list of created cells.
     */
    public static ArrayList getArrayListInstance(ArrayList inputs) {
        ArrayList[] inputsArray = { inputs };
        float[] weights = { INPUT_WEIGHT };
        return getArrayListInstance(inputsArray, weights);
    }

    /** 
     * Returns a list of <code>GanglionCell</code>s with positive/negative
     * default input weight and one input from the given two list of inputs.
     *
     * @param on_inputs positive inputs.
     * @param off_inputs negative inputs.
     * @return Created cells.
     */
    public static ArrayList getArrayListInstance(
        ArrayList on_inputs,
        ArrayList off_inputs) {
        ArrayList[] inputsArray = { on_inputs, off_inputs };
        float[] weights = {INPUT_WEIGHT, -INPUT_WEIGHT};
        return getArrayListInstance(inputsArray, weights);
    }

    /** 
     * Returns a list of <code>GanglionCell</code>s with the given input weight
     * and one input from the given list of inputs (for each array index). The 
     * {@link #getRetinotopicLocation location} is set the same as the 
     * corresponding input cell.
     * 
     * @param inputs An array of lists of cells, each providing the first input 
     * to one bipolar cell.
     * @param weights The input weights.
     * @return The list of created cells.
     */
    public static ArrayList getArrayListInstance(
        ArrayList[] inputs,
        float[] weights) {

        int n = inputs[0].size();
        ArrayList cells = new ArrayList(inputs[0].size());

        for (int i = 0; i < n; i++) {
            ArrayList cellInputs = new ArrayList(inputs.length);
            for (int j = 0; j < inputs.length; j++)
                cellInputs.add(
                    new ScalingSynapse(
                        (ServesOutput) inputs[j].get(i),
                        weights[j]));

            GanglionCell gc = new GanglionCell(cellInputs);
            gc.setRetinotopicLocation(
                ((Retinotopic) inputs[0].get(i)).getRetinotopicLocation());
            cells.add(gc);
        }

        return cells;
    }

    // the location of this cell
    private RetinotopicLocation location;
    
    /** return the RetinotopicLocaton of the cell
     *      @return RetinotopicLocation of cell
     *
     */
    public RetinotopicLocation getRetinotopicLocation() {
        return location;
    }
    
    /** set the location
     *      @param p to set the cell to. This is generally returned from another cell
     and is set during construction.
     *
     */
    public void setRetinotopicLocation(RetinotopicLocation p) {
        location=p;
    }

    
}
