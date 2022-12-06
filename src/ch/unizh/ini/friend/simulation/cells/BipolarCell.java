/*
 $Id: BipolarCell.java,v 1.22 2003/07/06 05:22:03 tobi Exp $
 

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

 
 $Log: BipolarCell.java,v $
 Revision 1.22  2003/07/06 05:22:03  tobi
 *** empty log message ***

 Revision 1.21  2003/05/10 17:27:44  jgyger
 Merge from color-branch

 Revision 1.20.2.2  2003/05/05 15:33:05  jgyger
 Go back to version of trunk

 Revision 1.20  2002/11/08 17:12:48  cmarti
 fix javadoc

 Revision 1.19  2002/11/05 22:09:46  cmarti
 remove AbstractWeightedInputServesOutput and put a much more flexible solution using
 the concept of synapses in place

 Revision 1.18  2002/10/31 21:47:28  cmarti
 - split AbstractAcceptsInputServesOutput, added AbstractCell
 - rename currentOutput/newOutput to value/newValue

 Revision 1.17  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput

 Revision 1.16  2002/10/25 08:24:59  cmarti
 remove 'compliant' cruft

 Revision 1.15  2002/10/25 08:02:25  cmarti
 remove 'implements ServesOutput' (now implemented by super-class)

 Revision 1.14  2002/10/24 12:05:50  cmarti
 add GPL header

 Revision 1.13  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.12  2002/10/07 19:48:12  tobi
 javadoc for bipolar cell. reorganized computation of sigmoid.

 Revision 1.11  2002/10/06 10:51:18  tobi
 fixed javadoc links

 Revision 1.10  2002/10/01 20:50:08  tobi
 implemented Retinotopic and added construction of location from previous retina level
 in array list static helper methods.  seems to work.

 Revision 1.9  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.8  2002/09/29 17:14:08  cmarti
 provide more sensible default weights for the inputs

 Revision 1.7  2002/09/26 16:37:46  tobi
 no change

 Revision 1.6  2002/09/26 16:26:59  tobi
 no change, watch test

 Revision 1.5  2002/09/25 16:51:38  tobi
 classified cells as graded or spiking, added interfaces GradedCell, SpikingCell.
 made photorecepto, bipolar, horizontal cell graded cells with gradedOutput() method.

 Revision 1.4  2002/09/25 09:24:16  tobi
 javadoc

 Revision 1.3  2002/09/25 09:19:23  tobi
 javadoc moved to class comment

 Revision 1.2  2002/09/24 20:35:59  cmarti
 partial rewrite:
 - no separate output stages, each bipolar is either of 'rising' or 'falling' type
 - the type is determined by weights as implemented by friend.simulation.WeightedInputs

 Revision 1.1  2002/09/17 18:42:25  cmarti
 initial version
 
 
 

 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.Collection;
import java.util.ArrayList;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.topology.*;
import ch.unizh.ini.friend.simulation.synapses.*;

/**
 * Implements a bipolar cell. Each bipolar is either of 'rising' or 'falling' type
 - the type is determined by the synapses.
 
 * @author Christof Marti
 * @version $Revision: 1.22 $
 */
public class BipolarCell extends AbstractCell implements GradedCell, Retinotopic {
    
    /** The default input weight. */
    protected static final float INPUT_WEIGHT = 1.0f;
    
    /** Creates a new instance with currently no inputs. */
    public BipolarCell() {
        super(2);
    }

    /** Creates a new instance with the given collections of inputs and weights.
     * @param inputs The collection of inputs.
     */
    public BipolarCell(Collection inputs) {
        super(inputs);
    }
    
    /** Returns a list of <code>BipolarCell</code> with the given input weights.
     The {@link #getRetinotopicLocation location} is set to be the same as the underlying photoreceptor.
     * The inputs of the bipolar cells will be set according to the given list
     * and single instance of <code>ServesOutput</code>.
     * @param inputs A list of cells, each providing the first input to one bipolar cell.
     * @param inputsWeight These inputs' weight.
     * @param input A cells providing the second input to all bipolar cells.
     * @param inputWeight This input's weight.
     * @return The list of created cells.
     */
    public static ArrayList getArrayListInstance(ArrayList inputs, float inputsWeight, ServesOutput input, float inputWeight) {
        //assert inputs != null : "needs inputs";
        //assert input != null : "needs input";
        //assert weights != null && weights.size() == 2 : "needs exactly two weights!";
        
        int n = inputs.size();
        ArrayList cells = new ArrayList(n);
        
        for (int i = 0; i < n; i++) {
            ArrayList cellInputs = new ArrayList(2);
            cellInputs.add(new ScalingSynapse((ServesOutput)inputs.get(i), inputsWeight));
            cellInputs.add(new ScalingSynapse(input, inputWeight));
            
            BipolarCell bc = new BipolarCell(cellInputs);
            // set the location of this bipolar cell the same as the first input cell
            bc.setRetinotopicLocation(((Retinotopic)inputs.get(i)).getRetinotopicLocation());
            cells.add(bc);
        }
        
        return cells;
    }
    
    /** Returns a list of <code>BipolarCell</code> with input weights for a falling output.
     * The inputs of the bipolar cells will be set according to the given list
     * and single instance of <code>ServesOutput</code>.
     * @param inputs A list of cells, each providing the first input to one bipolar cell.
     * @param input A cell providing the second input to all bipolar cells.
     * @return The list of created cells.
     */
    public static ArrayList getFallingArrayListInstance(ArrayList inputs, ServesOutput input) {
        //assert inputs != null : "needs inputs";
        //assert input != null : "needs input";
        return getArrayListInstance(inputs, -INPUT_WEIGHT, input, INPUT_WEIGHT);
    }

    /** Returns a list of <code>BipolarCell</code> with input weights for a rising output.
     * The inputs of the bipolar cells will be set according to the given list
     * and single instance of <code>ServesOutput</code>.
     * @param inputs A list of cells, each providing the first input to one bipolar cell.
     * @param input A cell providing the second input to all bipolar cells.
     * @return The list of created cells.
     */
    public static ArrayList getRisingArrayListInstance(ArrayList inputs, ServesOutput input) {
        //assert inputs != null : "needs inputs";
        //assert input != null : "needs input";
        return getArrayListInstance(inputs, INPUT_WEIGHT, input, -INPUT_WEIGHT);
    }

    /** Computes the tanh(x).
     * @param x The argument x.
     * @return tanh(x)
     */
    public static float tanh(float x) {
        float e1 = (float)Math.exp(x);
        float e2 = (float)Math.exp(-x);
        return (e1 - e2)/(e1 + e2);
    }
    

    private final float offset=1f;
    
    /** 
     Computes the new state of this bipolar cell.
     <p>
     The transfer function of the bipolar is a function that takes the result of {@link #integrateInputs} and produces the output. 
     It looks like a signmoid, but is about 0.1 at the origin.  
     It saturates at 1 for large positive input and goes to 0 for large negative input. It looks like this:
     <p>
     <img src="doc-files/bipolarXferFunction.gif">
     
     @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
        float x=integrateInputs();
        float e1 = (float)Math.exp(x-offset);
        float e2 = (float)Math.exp(-x+offset);
        float r= ((e1 - e2)/(e1 + e2)+1)/2;
        newValue = r;
    }
    
    /** returns the graded output from the cell  */
    public float getGradedOutput() {
        return output();
    }
    
        // the location of this cell
    private RetinotopicLocation location;
    
    /** return the RetinotopicLocaton of the cell
     * @return RetinotopicLocation of cell
     *
     */
    public RetinotopicLocation getRetinotopicLocation() {
        return location;
    }
    
    /** set the location
     *      @param p  RetinotopicLocation to set the cell to. This is generally returned from another cell
     and is set during construction.
     *
     */
    public void setRetinotopicLocation(RetinotopicLocation p) {
        location=p;
    }

    
}
