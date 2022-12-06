/*
 $Id: SimulationSetupFactory.java,v 1.48 2003/07/07 02:44:21 tobi Exp $
 
 
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
 
 
 */

package ch.unizh.ini.friend.simulation;

import java.util.ArrayList;
import ch.unizh.ini.friend.simulation.cells.*;
import ch.unizh.ini.friend.stimulus.*;
import ch.unizh.ini.friend.topology.*;
import ch.unizh.ini.friend.simulation.synapses.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory methods for creating instances of <code>SimulationSetup</code>. This is where the simulation is constructed
 * and all the architecture is arranged.
 *
 * @author Christof Marti
 * @author Johann Gyger
 * @version $Revision: 1.48 $
 */
public class SimulationSetupFactory {
    
    /** default timer delay in ms: {@value} */
    public static final int DELAY=5;
    
    /** The delay between the timer events in milliseconds. */
    protected static int delay = DELAY;
    
    /** size of one edge of retinal array. Number of total retinal cells in one layer is given by
     * <pre>
     * NUM_CELLS=(5/2)*SIZE^2-(3/2)*SIZE
     * </pre>
     * Eeach increase by 1 increases the number of cells by 6*(SIZE-1).
     * For specific SIZE, the number of cells is as follows:
     * <pre>
     * SIZE   NUM_CELLS
     * 1  1
     * 2  7
     * 3  19
     * 4  27
     * 5  51
     * </pre>
     * The value of <code>SIZE</code> is {@value}.
     */
    public static final int SIZE=4;
    
    /** Creates a simulation task.
     * @param step SimulationStep means of time stepping the simulation
     * @return the task, by which the simulation can later be referenced
     */
    protected static SimulationTask getSimulationTask(SimulationStep step) {
        //return new TimedSimulation(step, delay);
        return new ThreadedSimulation(step, delay);
    }
    
    /** Creates the monchrome simulation setup.
     * The {@link SimulationSetup#getName name} is set to "monochrome".
     * This the the main method for actually constructing the architecture and connections of the
     * simulation of the neurons.  It makes the cells, their connections, and decides on the fiducial cells that can be listened to.
     * It also makes a {@link SimulationStep} to step the simulation over the collection of cells,
     * and from that, it also make the {@link SimulationTask}.
     *
     * @return setup for simulation - a reference to use to get hold of cells
     * @see SimulationSetup
     */
    public static SimulationSetup getSimulationSetup() {
        
        
        SimulationSetup setup = new SimulationSetup();
        
        setup.setName("luminance");
        
        Stimulus stimulus = new BarStimulus();
        setup.setStimulus(stimulus);
        
        // Create the network of cells.
        List cells = setup.getSimulatedCells();
        
        ArrayList shapes = new ArrayList();
        ArrayList receptors = Photoreceptor.getHexagonalArrayListInstance(SIZE, 0.5f, 0.01f, stimulus, shapes);
        cells.addAll(receptors);
        setup.setReceptorShapes(shapes);
        
        Collection synapses = AbstractAcceptsInput.getCollectionInstance(new ScalingSynapse(1.0f/receptors.size()), receptors.size());
        AbstractAcceptsInput.connectOneToOne(receptors, synapses);
        HorizontalCell horizontalCell = new HorizontalCell(synapses);
        cells.add(horizontalCell);
        
        ArrayList bipolarsRising = BipolarCell.getRisingArrayListInstance(receptors, horizontalCell);
        cells.addAll(bipolarsRising);
        
        ArrayList bipolarsFalling = BipolarCell.getFallingArrayListInstance(receptors, horizontalCell);
        cells.addAll(bipolarsFalling);
        
        ArrayList ganglionsOn = GanglionCell.getArrayListInstance(bipolarsRising);
        cells.addAll(ganglionsOn);
        setup.setOnGanglions(ganglionsOn); //CLEAN UP: remove
        
        ArrayList ganglionsOff = GanglionCell.getArrayListInstance(bipolarsFalling);
        cells.addAll(ganglionsOff);
        setup.setOffGanglions(ganglionsOff); //CLEAN UP: remove
        
        AbstractSpikingCell oddSimple=AbstractSpikingCell.getOddSimpleCellInstance("IntegrateFireCell", ganglionsOn, ganglionsOff, RetinotopicLocation.findCenterCell(ganglionsOn),new HexDirection(HexDirection.E));
        cells.add(oddSimple);
        
        AbstractSpikingCell evenSimple=AbstractSpikingCell.getEvenSimpleCellInstance("IntegrateFireCell", ganglionsOn, ganglionsOff, RetinotopicLocation.findCenterCell(ganglionsOn),new HexDirection(HexDirection.E));
        cells.add(evenSimple);
        
        AbstractSpikingCell complexCell=AbstractSpikingCell.getComplexCellInstance("IntegrateFireCell", ganglionsOn, ganglionsOff, cells, RetinotopicLocation.findCenterCell(ganglionsOn), new HexDirection(HexDirection.E));
        cells.add(complexCell);
        
        AbstractSpikingCell dsCell=AbstractSpikingCell.getDSSimpleCellInstance("IntegrateFireCell", ganglionsOn, ganglionsOff, cells, RetinotopicLocation.findCenterCell(ganglionsOn), new HexDirection(HexDirection.E));
        cells.add(dsCell);
        
        AbstractSpikingCell mysteryEvenSimple=AbstractSpikingCell.getEvenSimpleCellInstance("IntegrateFireCell", ganglionsOn, ganglionsOff,
        RetinotopicLocation.findNearestNeighbor(RetinotopicLocation.findCenterCell(ganglionsOn), ganglionsOn, new HexDirection(HexDirection.NE)),
        new HexDirection(HexDirection.SW));
        cells.add(evenSimple);
        
        AbstractSpikingCell mysteryComplexCell=AbstractSpikingCell.getComplexCellInstance("IntegrateFireCell", ganglionsOn, ganglionsOff, cells,
        RetinotopicLocation.findNearestNeighbor(RetinotopicLocation.findCenterCell(ganglionsOn), ganglionsOn, new HexDirection(HexDirection.E)),
        new HexDirection(HexDirection.NE));
        cells.add(complexCell);
        
        AudioOutput audioOutput = new AudioOutput();
        cells.add(audioOutput);
        
        
        //        System.out.println("center of receptors is "+RetinotopicLocation.findCenterCell(receptors).getRetinotopicLocation());
        //        System.out.println("center of ganglionsOn is "+RetinotopicLocation.findCenterCell(ganglionsOn).getRetinotopicLocation());
        //        System.out.println("center of bipolarsFalling is "+RetinotopicLocation.findCenterCell(bipolarsFalling).getRetinotopicLocation());
        
        
        Map outputCells = setup.getOutputCells(); 
        // this overwrites the HashMap that is created in SimulationSetup
        outputCells.put("0 Photoreceptor", RetinotopicLocation.findCenterCell(receptors));
        outputCells.put("1 Horizontal Cell", horizontalCell);
        outputCells.put("2a Bipolar ON Cell", RetinotopicLocation.findCenterCell(bipolarsRising));
        outputCells.put("2b Bipolar OFF Cell" ,RetinotopicLocation.findCenterCell(bipolarsFalling));
        outputCells.put("3a Ganglion ON Cell", RetinotopicLocation.findCenterCell(ganglionsOn));
        outputCells.put("3b Ganglion OFF Cell" ,RetinotopicLocation.findCenterCell(ganglionsOff));
        outputCells.put("4a Simple cell with odd symmetry", oddSimple);
        outputCells.put("4b Simple cell with even symmetry", evenSimple);
        outputCells.put("4c Direction Selective Simple cell", dsCell);
        outputCells.put("5a Complex cell", complexCell);
        Retinotopic centerOff=RetinotopicLocation.findCenterCell(ganglionsOff);
        HexDirection dir=new HexDirection(HexDirection.NE);
        Retinotopic offNE=RetinotopicLocation.findNearestNeighbor(centerOff, ganglionsOff, dir);
        offNE=RetinotopicLocation.findNearestNeighbor(offNE, ganglionsOff, dir);  // march in this direction for a little while
        outputCells.put("6a Mystery cell #1", offNE );
        outputCells.put("6b Mystery cell #2", oddSimple);
        outputCells.put("6c Mystery cell #3", evenSimple);
        outputCells.put("6d Mystery cell #4", complexCell);
        setup.setOutputCells(outputCells);
        
        setup.setStartingCellName("0 Photoreceptor");
        
        Map monitors = new HashMap();
        monitors.put("AudioOutput", audioOutput);
        setup.setMonitors(monitors);
        
        SimulationStep simulationStep = new SimulationStep(cells, 1.0f);
        SimulationTask simulation = getSimulationTask(simulationStep);
        setup.setSimulation(simulation);
        
        return setup;
    }
    
    /**
     * Creates a simulation setup for color vision cells.
     * The {@link SimulationSetup#getName name} is set to "color".
     *
     * @return setup for simulation - a reference to use to get hold of cells
     */
    public static SimulationSetup getColorSimulationSetup() {
        int l = ColorPhotoreceptor.L_CONE;
        int m = ColorPhotoreceptor.M_CONE;
        int s = ColorPhotoreceptor.S_CONE;
        
        SimulationSetup setup = new SimulationSetup();
        setup.setName("color");
        
        // Create a color stimulus.
        ColorStimulus stimulus = new ColorStimulusImpl();
        stimulus.setShapes(StimulusShapeFactory.makeBarShape());
        setup.setStimulus(stimulus);
        
        // Create the network of cells. These are passed to the constuctor for SimulationStep and becomes it collection of updateables
        List cells=setup.getSimulatedCells();
        
        // L, M and S type photoreceptors (and one shape for each triple)
        ArrayList shapes = new ArrayList();
        // receptors: 3 ArrayLists, one for each color
        ArrayList[] receptors =
        ColorPhotoreceptor.getHexagonalArrayLists(
        SIZE,
        0.5f,
        0.01f,
        stimulus,
        shapes);
        
        int numCones = ColorPhotoreceptor.NUM_CONES;
        int numRecep = receptors[0].size();
        int totalRecep = numCones * numRecep;
        for (int i = 0; i < numCones; i++)
            cells.addAll(receptors[i]);
        setup.setReceptorShapes(shapes);
        
        // L horizontal cell
        ScalingSynapse synapse = new ScalingSynapse(1.0f / numRecep);
        Collection synapses = AbstractAcceptsInput.getCollectionInstance(synapse, numRecep);
        AbstractAcceptsInput.connectOneToOne(receptors[l], synapses);
        HorizontalCell lHC = new HorizontalCell(synapses);
        cells.add(lHC);
        
        // M horizontal cell
        synapse = new ScalingSynapse(1.0f / numRecep);
        synapses =  AbstractAcceptsInput.getCollectionInstance(synapse, numRecep);
        AbstractAcceptsInput.connectOneToOne(receptors[m], synapses);
        HorizontalCell mHC = new HorizontalCell(synapses);
        cells.add(mHC);
        
        // L+M horizontal cell (connects to L and M cones) and averages them
        ArrayList lmSynapses = new ArrayList(2 * numRecep);
        synapse = new ScalingSynapse(1.0f / (2 * numRecep));
        for (int i = 0; i < 2; i++) {
            synapses =
            AbstractAcceptsInput.getCollectionInstance(synapse, numRecep);
            AbstractAcceptsInput.connectOneToOne(receptors[i], synapses);
            lmSynapses.addAll(synapses);
        }
        HorizontalCell lmHC = new HorizontalCell(lmSynapses);
        cells.add(lmHC);
        
        // L+M+S horizontal cell (connects to all three cone types) and averages them
        ArrayList lmsSynapses = new ArrayList(totalRecep);
        synapse = new ScalingSynapse(1.0f / totalRecep);
        for (int i = 0; i < numCones; i++) {
            synapses =
            AbstractAcceptsInput.getCollectionInstance(synapse, numRecep);
            AbstractAcceptsInput.connectOneToOne(receptors[i], synapses);
            lmsSynapses.addAll(synapses);
        }
        HorizontalCell lmsHC = new HorizontalCell(lmsSynapses);
        cells.add(lmsHC);
        
        // Red-on bipolars
        ArrayList redOnBipolar =
        BipolarCell.getRisingArrayListInstance(receptors[l], lmHC);
        cells.addAll(redOnBipolar);
        
        // Red-off bipolars
        ArrayList redOffBipolar =
        BipolarCell.getFallingArrayListInstance(receptors[l], lmHC);
        cells.addAll(redOffBipolar);
        
        // Green-on bipolars
        ArrayList greenOnBipolar =
        BipolarCell.getRisingArrayListInstance(receptors[m], lmHC);
        cells.addAll(greenOnBipolar);
        
        // Green-off bipolars
        ArrayList greenOffBipolar =
        BipolarCell.getFallingArrayListInstance(receptors[m], lmHC);
        cells.addAll(greenOffBipolar);
        
        // Blue-on bipolar
        ArrayList blueOnBipolar =
        BipolarCell.getRisingArrayListInstance(receptors[s], lmHC);
        cells.addAll(blueOnBipolar);
        
        // Red-on ganglions
        ArrayList redOnGanglion =
        GanglionCell.getArrayListInstance(redOnBipolar);
        cells.addAll(redOnGanglion);
        
        // Red-off ganglions
        ArrayList redOffGanglion =
        GanglionCell.getArrayListInstance(redOffBipolar);
        cells.addAll(redOffGanglion);
        
        // Green-on ganglions
        ArrayList greenOnGanglion =
        GanglionCell.getArrayListInstance(greenOnBipolar);
        cells.addAll(greenOnGanglion);
        
        // Green-off ganglions
        ArrayList greenOffGanglion =
        GanglionCell.getArrayListInstance(greenOffBipolar);
        cells.addAll(greenOffGanglion);
        
        // Blue-on ganglions
        ArrayList blueOnGanglion =
        GanglionCell.getArrayListInstance(blueOnBipolar);
        cells.addAll(blueOnGanglion);
        
        // Let's listen to the music...
        AudioOutput audioOutput = new AudioOutput();
        cells.add(audioOutput);
        
        Map outputCells = setup.getOutputCells();
        
        // we use tree map to keep entries sorted alphabetically for menu generation
        String lConePhotoreceptor = "0a  L-cone photoreceptor";
        outputCells.put(
        lConePhotoreceptor,
        RetinotopicLocation.findCenterCell(receptors[l]));
        outputCells.put(
        "0b  M-cone photoreceptor",
        RetinotopicLocation.findCenterCell(receptors[m]));
        outputCells.put(
        "0c  S-cone photoreceptor",
        RetinotopicLocation.findCenterCell(receptors[s]));
        outputCells.put("1a Horizontal L cell", lHC);
        outputCells.put("1b Horizontal M cell", mHC);
        outputCells.put("1c Horizontal L+M cell", lmHC);
        outputCells.put("1d Horizontal L+M+S cell", lmsHC);
        outputCells.put(
        "2a  Bipolar red-on cell",
        RetinotopicLocation.findCenterCell(redOnBipolar));
        outputCells.put(
        "2b  Bipolar red-off cell",
        RetinotopicLocation.findCenterCell(redOffBipolar));
        outputCells.put(
        "2c  Bipolar green-on cell",
        RetinotopicLocation.findCenterCell(greenOnBipolar));
        outputCells.put(
        "2d  Bipolar green-off cell",
        RetinotopicLocation.findCenterCell(greenOffBipolar));
        outputCells.put(
        "2e  Bipolar blue-on cell",
        RetinotopicLocation.findCenterCell(blueOnBipolar));
        outputCells.put(
        "3a  Ganglion red-on cell",
        RetinotopicLocation.findCenterCell(redOnGanglion));
        outputCells.put(
        "3b  Ganglion red-off cell",
        RetinotopicLocation.findCenterCell(redOffGanglion));
        outputCells.put(
        "3c  Ganglion green-on cell",
        RetinotopicLocation.findCenterCell(greenOnGanglion));
        outputCells.put(
        "3d  Ganglion green-off cell",
        RetinotopicLocation.findCenterCell(greenOffGanglion));
        outputCells.put(
        "3e  Ganglion blue-on cell",
        RetinotopicLocation.findCenterCell(blueOnGanglion));
        setup.setOutputCells(outputCells);
        
        setup.setStartingCellName(lConePhotoreceptor);
        
        Map monitors = new HashMap();
        monitors.put("AudioOutput", audioOutput);
        
        setup.setMonitors(monitors);
        // monitors are only used in SimulationSetup when monitored cell is being set, so that each monitor can have
        // its monitored cell set. the monitors are not updated; they need to be added as updateables to the set of cells.
        
        SimulationStep simulationStep = new SimulationStep(cells, 1.0f);
        SimulationTask simulation = getSimulationTask(simulationStep);
        setup.setSimulation(simulation);
        
        return setup;
    }
    
}
/*
 *$Log: SimulationSetupFactory.java,v $
 *Revision 1.48  2003/07/07 02:44:21  tobi
 *
 *added time constant setter/getter to hcell.  messed with javadoc.
 *
 *Revision 1.47  2003/07/06 05:22:03  tobi
 **** empty log message ***
 *
 *Revision 1.46  2003/07/03 16:54:26  tobi
 *fixed a bunch of javadoc errors.
 *made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)
 *
 *made lowpass and highpass filters time constants settable.
 *
 *Revision 1.45  2003/06/26 00:33:46  tobi
 *
 *added simulation properties dialog and fixed simple and complex cells so that they work.
 *simple cell had incomplete RF. complex cell had time constant that was too long.
 *fiddled with audio input and output
 *
 *Revision 1.44  2003/06/16 06:32:52  tobi
 *added more mystery cells for daniel
 *
 *Revision 1.43  2003/06/15 19:17:31  tobi
 *added capability of recording spikes from simulation or from microphone and plotting the
 *corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 *kind of a spike-tirggered average is possible.
 *
 Revision 1.42  2003/06/12 06:28:45  tobi
 added microphone recording for hooking up friend chip. not fully functional yet.
 
 Revision 1.41  2003/05/10 17:27:44  jgyger
 Merge from color-branch
 
 Revision 1.40.2.8  2003/05/08 17:14:02  tobi
 javadoc links fixed
 
 Revision 1.40.2.7  2003/05/06 12:35:33  jgyger
 Add/change horizontal cells
 
 Revision 1.40.2.6  2003/05/05 15:31:44  jgyger
 Fix blue, remove diffuse cells
 
 Revision 1.40.2.5  2003/04/24 15:49:33  tobi
 comments added
 
 Revision 1.40.2.4  2003/03/16 18:14:17  jgyger
 move excitation method from stimuli to photoreceptors
 
 Revision 1.40.2.3  2003/03/16 16:30:12  jgyger
 - add own factory method for color simulation
 - move starting cell name into simulation setup
 
 Revision 1.40.2.2  2003/03/14 17:01:27  jgyger
 replace photoreceptors by color-selective ones
 
 Revision 1.40.2.1  2003/03/12 20:53:51  jgyger
 add support for a color stimulus
 
 Revision 1.40  2002/11/07 18:52:10  cmarti
 - remove getEquallyWeightedInputsInstance() from HorizontalCell
 - use the new construction methods from AbstractAcceptsInputs in SimulationSetupFactory
    for creating a HorizontalCell with synapses
 
 Revision 1.39  2002/11/07 17:28:07  cmarti
 - avoid passing the whole setup to factory methods in AbstractSpikingCell
 - this cures a NullPointerException introduced by the reordering of the statements in the SimulationSetupFactory
 
 Revision 1.38  2002/11/07 15:30:50  cmarti
 reorder statements for better overview
 
 Revision 1.37  2002/11/05 22:09:46  cmarti
 remove AbstractWeightedInputServesOutput and put a much more flexible solution using
 the concept of synapses in place
 
 Revision 1.36  2002/10/31 06:11:17  tobi
 javadoc about SIZE added.  computed the number of resulting cells and gave formula
 
 Revision 1.35  2002/10/29 09:04:32  cmarti
 - add generic factory method to AbstractSpikingCell, capable of creating any concrete subclass of AbstractSpikingCell
 - move constructors from cortical cells up in the hirarchy to AbstractSpikingCell making them factory methods
 - change SimulationSetupFactory to use the new factory methods
 - commit (almost) empty cortical cells (remove state)
 
 Revision 1.34  2002/10/25 08:01:38  cmarti
 make HorizontalCell extend WeightedInputs
 
 Revision 1.33  2002/10/24 12:05:50  cmarti
 add GPL header
 
 Revision 1.32  2002/10/20 23:42:26  tobi
 added mystery cells
 
 Revision 1.31  2002/10/15 19:26:55  tobi
 lots of javadoc added,
 mouse wheel enabled.
 
 Revision 1.30  2002/10/13 16:29:09  tobi
 many small changes from tuebingen trip.
 
 Revision 1.29  2002/10/11 08:44:36  tobi
 changed constructeor of status panel to include gui and stimulus, in accord with
 new status panel
 
 Revision 1.28  2002/10/10 19:29:39  tobi
 added direction selective cell
 
 Revision 1.27  2002/10/08 19:40:58  tobi
 javadoc
 
 Revision 1.26  2002/10/08 12:58:32  tobi
 defined starting cell name as public static final that FriendGUI can use to choose
 initial cell to show and choose.
 
 Revision 1.25  2002/10/08 07:47:49  tobi
 
 added complex cell, on the way, made some changes in odd and even cell.
 threadedsimulation crashes on first REstart, can't figure out why.
 
 Revision 1.24  2002/10/07 18:55:15  tobi
 changed hashmap to treemap and changed cell names to be numerically preprended with a "layer number"
 in order to have cells show up in Cell menu in reasonable order. A treemap is a sorted map, sorted by the keys.
 
 Revision 1.23  2002/10/07 07:58:33  tobi
 back to threaded now that it sleeps and has higher pripority....
 
 Revision 1.22  2002/10/06 17:03:02  tobi
 added even type simple cell
 
 Revision 1.21  2002/10/06 08:59:37  tobi
 made photoreceptor array larger (simulation still runs ok)
 and cahnged simulation type to timed simulation (it was threaded) because
 this seems to run more smoothly.
 
 Revision 1.20  2002/10/05 22:39:34  tobi
 added odd simple cell.
 
 Revision 1.19  2002/10/05 17:39:39  tobi
 started adding cortical cell
 
 Revision 1.18  2002/10/04 12:58:16  tobi
 added on and off ganglion cells as special layers of cells to be returned by
 getters in order to facilitate building cortical cells.
 
 Revision 1.17  2002/10/01 20:49:08  tobi
 added and commented location of center cell of each type to check retinotopiclocation stuff
 
 Revision 1.16  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy
 
 Revision 1.15  2002/09/30 13:07:43  cmarti
 change output-cells to be the center ones.
 
 Revision 1.14  2002/09/29 17:26:46  cmarti
 don't provide the ganglions with weights as they use their own defaults now
 
 Revision 1.13  2002/09/29 13:58:41  cmarti
 - switch to the threaded simulation
 - add an AudioOutput to the map of monitors
 
 Revision 1.12  2002/09/28 19:21:33  cmarti
 exchange invocation order of setMonitors() and setMoinitoredCell(), which caused NullPointerException
 
 Revision 1.11  2002/09/28 19:05:07  cmarti
 initialize the monitors in SimulationSetup with an empty HashMap
 
 Revision 1.10  2002/09/27 09:59:29  tobi
 added rendering for audio output, but doesn't work corectly yet.
 added photoreceptor shape addition to simulationSetupFactory, so tthat
 GUI can render the shapes.
 
 Revision 1.9  2002/09/27 09:37:49  tobi
 added Photoreceptor as initially monitored cell.
 
 Revision 1.8  2002/09/27 09:20:52  tobi
 removed some commented out code
 
 Revision 1.7  2002/09/26 20:32:34  tobi
 HashMap for cell now implemented so GUI can use them for building its Cell menu.
 
 Revision 1.6  2002/09/26 16:06:09  tobi
 changed output cells to hashmap.
 adding fiducial cells as named hashmap entries.  these will be used by
 GUI to construct cell menu.
 
 Revision 1.5  2002/09/26 14:40:29  cmarti
 add on/off ganglions
 
 Revision 1.4  2002/09/25 16:53:18  tobi
 added fidicial cells and getter/setter methods.  SimulationFactory now sets the fiducical
 (special) cells that we can monitor from the gui.
 
 Revision 1.3  2002/09/25 15:36:26  tobi
 added default delay final int for javadoc
 
 Revision 1.2  2002/09/25 15:14:05  tobi
 changed stimulus to BarStimulus, because the Stimulus shape was being double wrapped by
 the existing code.
 
 Revision 1.1  2002/09/24 21:26:25  cmarti
 initial version
 
 */