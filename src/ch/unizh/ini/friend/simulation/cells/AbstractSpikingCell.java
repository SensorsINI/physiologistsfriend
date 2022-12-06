/*
 $Id: AbstractSpikingCell.java,v 1.8 2003/07/06 05:22:03 tobi Exp $
 
 
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
 
 
 $Log: AbstractSpikingCell.java,v $
 Revision 1.8  2003/07/06 05:22:03  tobi
 *** empty log message ***

 Revision 1.7  2003/06/26 00:33:48  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.6  2002/11/07 17:28:07  cmarti
 - avoid passing the whole setup to factory methods in AbstractSpikingCell
 - this cures a NullPointerException introduced by the reordering of the statements in the SimulationSetupFactory

 Revision 1.5  2002/11/05 22:09:46  cmarti
 remove AbstractWeightedInputServesOutput and put a much more flexible solution using
 the concept of synapses in place

 Revision 1.4  2002/10/29 11:42:52  cmarti
 add gpl-header

 Revision 1.3  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput

 Revision 1.2  2002/10/29 09:04:32  cmarti
 - add generic factory method to AbstractSpikingCell, capable of creating any concrete subclass of AbstractSpikingCell
 - move constructors from cortical cells up in the hirarchy to AbstractSpikingCell making them factory methods
 - change SimulationSetupFactory to use the new factory methods
 - commit (almost) empty cortical cells (remove state)

 Revision 1.1  2002/10/28 12:46:45  cmarti
 adding AbstractSpikingCell and making IntegrateFireCell a descendant of it

 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.topology.*;
import ch.unizh.ini.friend.simulation.synapses.*;

/**
 * Abstract class providing common methods for spiking cells.
 * @author Christof Marti
 * @version $Revision: 1.8 $
 */
abstract public class AbstractSpikingCell extends AbstractCell implements SpikingCell {
    
    /** Creates a new instance with currently no inputs. */
    public AbstractSpikingCell() {
        super();
    }
    
    /** Creates a new instance with currently no inputs and
     * with the given initial capacity.
     * @param n Number of initial capacity for inputs.
     */
    public AbstractSpikingCell(int n) {
        super(n);
    }
    
    /** Creates a new instance with the given collections of inputs.
     * @param inputs The collection of inputs.
     */
    public AbstractSpikingCell(Collection inputs) {
        super(inputs);
    }
    
    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public AbstractSpikingCell(ServesOutput input) {
        super(input);
    }
    
    /** Returns an instance of a subclass of <code>AbstractSpikingCell</code> given by the name.
     * @param classname Name of the subclass of <code>AbstractSpikingCell</code> to create.
     * @return Instance of the specified subclass.
     */
    public static AbstractSpikingCell factory(String classname) {
        Class c;
        try {
            c = Class.forName("ch.unizh.ini.friend.simulation.cells." + classname);
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + classname);
        }
        
        Object o;
        try {
            o = c.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access: " + classname);
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiation exception: " + classname);
        } catch (ExceptionInInitializerError e) {
            throw new RuntimeException("Exception in initializer: " + classname);
        } catch (SecurityException e) {
            throw new RuntimeException("Security exception: " + classname);
        }
        
        if (!(o instanceof AbstractSpikingCell)) {
            throw new RuntimeException("Not a descendant of AbstractSpikingCell: " + classname);
        }
        
        return (AbstractSpikingCell)o;
    }
        
    /** Returns an instance of a subclass of <code>AbstractSpikingCell</code> given by the name
     * with the given collections of inputs and weights.
     * @param classname Name of the subclass of <code>AbstractSpikingCell</code> to create.
     * @param inputs The collection of inputs.
     * @param weights The collection of weights (Float instances).
     * @return Instance of the specified subclass.
     */
    public static AbstractSpikingCell factory(String classname, Collection inputs, Collection weights) {
        AbstractSpikingCell cell = factory(classname);
        ArrayList synapses = new ArrayList(inputs.size());
        Iterator iInputs = inputs.iterator();
        Iterator iWeights = weights.iterator();
        
        while (iInputs.hasNext() && iWeights.hasNext()) {
            synapses.add(new ScalingSynapse((ServesOutput)iInputs.next(), ((Float)iWeights.next()).floatValue()));
        }
        
        cell.setInputs(synapses);
        return cell;
    }
    
    /** Returns an instance of a subclass of <code>AbstractSpikingCell</code> given by the name
     * with currently no inputs and with the given initial capacity.
     * @param classname Name of the subclass of <code>AbstractSpikingCell</code> to create.
     * @param n Number of initial capacity for inputs.
     * @return Instance of the specified subclass.
     */
    public static AbstractSpikingCell factory(String classname, int n) {
        return factory(classname, new ArrayList(n), new ArrayList(n));
    }
    
    /** Returns an instance of a subclass of <code>AbstractSpikingCell</code> given by the name
     * with the given input and weight.
     * @param classname Name of the subclass of <code>AbstractSpikingCell</code> to create.
     * @param input The input.
     * @param weight Its weight.
     * @return Instance of the specified subclass.
     */
    public static AbstractSpikingCell factory(String classname, ServesOutput input, float weight) {
        ArrayList inputs = new ArrayList(1);
        inputs.add(input);
        ArrayList weights = new ArrayList(1);
        weights.add(new Float(weight));
        
        return factory(classname, inputs, weights);
    }

    /** Returns a new instance of ComplexCell. A complex-type simple cell, composed of simple cells
     all with the same orientation but with varying phase. This complex cell sums the outputs from
     two simple cells, with same orientation and symmetry, but with opposite polarity, at the same
     retionotopic location. These component simple cells are added to the simulation provided in
     the argument to the constructor.
     
     @param classname Name of the subclass of AbstractSpikingCell to be created.
     @param onCells The on-ganglion cells.
     @param offCells The off-ganglion cells.
     @param cells The cells of the simulation. CLEAN UP: remove
     @param centeredOn the cell is centered here
     @param ori the orientation. This orientation is added to rotate the cells RF.  Orientation 0 means a cell that is horizontally tuned.
     @return The newly created object.
     */
    public static AbstractSpikingCell getComplexCellInstance(String classname, Collection onCells, Collection offCells, Collection cells, Retinotopic centeredOn, HexDirection ori) {
        int NUM_SIMPLE=2;
        AbstractSpikingCell[] simpleCells=new AbstractSpikingCell[NUM_SIMPLE];
        /** scales all synapses by this amount */
        float SYNAPSE_STRENGTH=1f;

        ArrayList weights=new ArrayList(NUM_SIMPLE);
        ArrayList inputs=new ArrayList(NUM_SIMPLE);

        Retinotopic center=centeredOn;
//        Retinotopic west=RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, new HexDirection(HexDirection.W));
//        Retinotopic east=RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, new HexDirection(HexDirection.E));

        // add two simple cells, with same orientation and symmetry, but with opposite polarity, at the same retionotopic location.
        
        // make the cells
        simpleCells[0]=getOddSimpleCellInstance(classname, onCells, offCells, center, ori);
        simpleCells[1]=getOddSimpleCellInstance(classname, onCells, offCells, center, new HexDirection(ori.get()+3));  // this is same cell but rotated 180 degrees
        
        // add each of them as updateables and add an input and weight from each to thie cell
        cells.add(simpleCells[0]); //CLEAN UP: remove
        inputs.add(simpleCells[0]);
        weights.add(new Float(SYNAPSE_STRENGTH));

        cells.add(simpleCells[1]); //CLEAN UP: remove
        inputs.add(simpleCells[1]);
        weights.add(new Float(SYNAPSE_STRENGTH));
        
        IntegrateFireCell c=(IntegrateFireCell)factory(classname, inputs, weights);
        c.setLeakTime(.25f);
//        c.setMaxRate(2);
        c.setPotasiumReversal(-0.1f);
        c.setThreshold(.1f);

        return c;
    }

    /** Returns a new instance of a direction-selective simple cell, composed with input from two simple cells, 
     one effectively shunting the input from the other.
     The two inputs to are spatially separated.  One excites the cell, the other inhibits it.  Because the membrane potential is clamped
     in the negative direction by the {@link ch.unizh.ini.friend.simulation.cells.IntegrateFireCell#potasiumReversal potasium reversal potential},
     the inhibitory input acts effectively as a shunting inhibition. The result is a null-inhibition-type Barlow-Levick direction selective cell.

     @param classname Name of the subclass of AbstractSpikingCell to be created.
     @param onCells The on-ganglion cells.
     @param offCells The off-ganglion cells.
     @param cells The cells of the simulation. CLEAN UP: remove
     @param centeredOn the cell is centered here
     @param ori the orientation. This orientation is added to rotate the cells RF.  Orientation 0 means a cell that is horizontally tuned.
     @return The newly created object.
     */
    public static AbstractSpikingCell getDSSimpleCellInstance(String classname, Collection onCells, Collection offCells, Collection cells, Retinotopic centeredOn, HexDirection ori) {
        int NUM_SIMPLE=2;
    
        /** scales all synapses by this amount */
        float SYNAPSE_STRENGTH=32f;
    
        ArrayList weights=new ArrayList(NUM_SIMPLE);
        ArrayList inputs=new ArrayList(NUM_SIMPLE);

        Retinotopic center=centeredOn;
        // get our neighbor from the west.  the west is the best.
        Retinotopic neighbor=RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, new HexDirection(HexDirection.W));
//        Retinotopic east=RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, new HexDirection(HexDirection.E));

        // add two simple cells, with same orientation and symmetry, but with opposite polarity, at the same retionotopic location.
        
        // make the cells
        AbstractSpikingCell excite=getOddSimpleCellInstance(classname, onCells, offCells, center, ori);
        AbstractSpikingCell inhibit=getOddSimpleCellInstance(classname, onCells, offCells, neighbor, new HexDirection(ori.get()));
//        OddSimpleCell excite2=new OddSimpleCell(setup, center, new HexDirection(ori.get()+3));
//        OddSimpleCell inhibit2=new OddSimpleCell(setup, neighbor, new HexDirection(ori.get()+3));
        
//commented because ASC lacks spikeRateMeasWin - TO FIX        inhibit.spikeRateMeasurementWindow=.5f; // smooth the inhibition over time to increase its length of action
//        inhibit2.spikeRateMeasurementWindow=.5f;
        
//        excite.leakTime=.1f;
//        excite2.leakTime=.1f;
        
        // add each of them as updateables and add an input and weight from each to thie cell
        cells.add(excite); //CLEAN UP: remove
        inputs.add(excite);
        weights.add(new Float(SYNAPSE_STRENGTH));

        cells.add(inhibit); //CLEAN UP: remove
        inputs.add(inhibit);
        weights.add(new Float(-16*SYNAPSE_STRENGTH));

//        setup.getSimulation().addUpdateable(excite2);
//        inputs.add(excite2);
//        weights.add(new Float(SYNAPSE_STRENGTH));
//
//        setup.getSimulation().addUpdateable(inhibit2);
//        inputs.add(inhibit2);
//        weights.add(new Float(-16*SYNAPSE_STRENGTH));
        Object o=factory(classname, inputs, weights);
        IntegrateFireCell c;
        if(o instanceof IntegrateFireCell){
            c=(IntegrateFireCell)o;
            c.setLeakTime(.3f);
            c.setPotasiumReversal(-.5f);
            c.setThreshold(.2f);
            o=c;
        }
        return (AbstractSpikingCell)o; 
    }

    /** Returns a new instance of a even-type simple cell. The receptive field is push-pull, with even
     symmetry. The on ganglions from the center stripe excite, and the off ganglions from the flanks excite.
     The ganglions with opposing polarity are connected with inhibitory synapses.
     @param classname Name of the subclass of AbstractSpikingCell to be created.
     @param onCells The on-ganglion cells.
     @param offCells The off-ganglion cells.
     @param centeredOn the cell is centered here
     @param ori the orientation. This orientation is added to rotate the cells RF.  
     Orientation {@link HexDirection#E} is a cell that is tuned for bright stripes at 60 deg CCW from horizontal orientation.
     @return The newly created object.
     */
    public static AbstractSpikingCell getEvenSimpleCellInstance(String classname, Collection onCells, Collection offCells, Retinotopic centeredOn, HexDirection ori) {
        /** scales all synapses by this amount */
        float SYNAPSE_STRENGTH=1f;
    
        ArrayList weights=new ArrayList(20);
        ArrayList inputs=new ArrayList(20);

        // flanks
        int[] flankDirs={0,5,2,3}; // lower right and uppper left pairs if ori==0
        for(int i=0;i<flankDirs.length;i++){
            HexDirection d=new HexDirection(flankDirs[i]+ori.get()); // find a direction
            GanglionCell r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, offCells, d);
            if(r==null){ continue; }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(   r    );
            weights.add(new Float(SYNAPSE_STRENGTH));
            // push pull opposites
            r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, d);
            if(r==null){ continue; }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(   r    );
            weights.add(new Float(-SYNAPSE_STRENGTH));
        }
        
        // center stripe
        int[] stripeDirs={1,4};
        for(int i=0;i<stripeDirs.length;i++){
            HexDirection d=new HexDirection(stripeDirs[i]+ori.get()); // find a direction
            GanglionCell r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, d);
            if(r==null){ continue; }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(   r    );
            weights.add(new Float(2*SYNAPSE_STRENGTH));
            // push pull opposites
            r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, offCells, d);
            if(r==null){ continue; }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(   r    );
            weights.add(new Float(-2*SYNAPSE_STRENGTH));
        }
        
        // center location

        inputs.add((GanglionCell)RetinotopicLocation.findCorresponding(centeredOn, onCells));
        weights.add(new Float(2*SYNAPSE_STRENGTH));
        inputs.add((GanglionCell)RetinotopicLocation.findCorresponding(centeredOn, offCells));
        weights.add(new Float(-2*SYNAPSE_STRENGTH));
        
        return factory(classname, inputs, weights);
    }

    /** Returns a new instance of a odd-type simple cell. The receptive field is push-pull, with odd symmetry.
     The on ganglions from the right and the off ganglions from the left are connected with excitatory synapses.
     The ganglions with opposing polarity are connected with inhibitory synapses.
     @param classname Name of the subclass of AbstractSpikingCell to be created.
     @param onCells The on-ganglion cells.
     @param offCells The off-ganglion cells.
     @param centeredOn a Retinotopic cell whose on which the instance is centered
     @param ori the orientation of the cell. {@link ch.unizh.ini.friend.topology.HexDirection#E}, 
     for example, results in an <code>OddSimpleCell</code>
     with vertical orientation, which is excited by brightness on the right and darkness on the left.
     @return The newly created object.
     */
    public static AbstractSpikingCell getOddSimpleCellInstance(String classname, Collection onCells, Collection offCells, Retinotopic centeredOn, HexDirection ori) {
        /** scales all synapses by this amount */
        float SYNAPSE_STRENGTH=1f;
    
        ArrayList weights=new ArrayList(20);
        ArrayList inputs=new ArrayList(20);
        for(int i=ori.get()-1;i<ori.get()+2;i++){
            GanglionCell r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, new HexDirection(i));
            if(r==null){
                continue;
            }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(   r    );
            weights.add(new Float(SYNAPSE_STRENGTH));
        }
        for(int i=ori.get()-1;i<ori.get()+2;i++){
            GanglionCell r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, offCells, new HexDirection(i));
            if(r==null){
                continue;
            }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(  r    );
            weights.add(new Float(-SYNAPSE_STRENGTH));
        }
        
        for(int i=ori.get()+2;i<ori.get()+6;i++){
            GanglionCell r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, onCells, new HexDirection(i));
            if(r==null){
                continue;
            }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(   r    );
            weights.add(new Float(-SYNAPSE_STRENGTH));
        }
        for(int i=ori.get()+2;i<ori.get()+6;i++){
            GanglionCell r=(GanglionCell)RetinotopicLocation.findNearestNeighbor(centeredOn, offCells, new HexDirection(i));
            if(r==null){
                continue;
            }
//            System.out.println("adding input at "+r.getRetinotopicLocation());
            inputs.add(   r    );
            weights.add(new Float(SYNAPSE_STRENGTH));
        }

        return factory(classname, inputs, weights);
    }
 
}
