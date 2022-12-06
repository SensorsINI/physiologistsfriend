/*
 $Id: SimulationSetup.java,v 1.31 2003/07/03 16:54:26 tobi Exp $
 
 
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
 
 
 $Log: SimulationSetup.java,v $
 Revision 1.31  2003/07/03 16:54:26  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.30  2003/06/15 19:17:31  tobi
 added capability of recording spikes from simulation or from microphone and plotting the
 corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 kind of a spike-tirggered average is possible.

 Revision 1.29  2003/06/12 06:28:45  tobi
 added microphone recording for hooking up friend chip. not fully functional yet.
 
 Revision 1.28  2003/05/10 17:27:43  jgyger
 Merge from color-branch
 
 Revision 1.27.2.2  2003/05/08 17:14:02  tobi
 javadoc links fixed
 
 Revision 1.27.2.1  2003/03/16 16:26:21  jgyger
 move starting cell name into simulation setup
 
 Revision 1.27  2002/11/07 14:58:00  cmarti
 implement setMonitoredCell() properly
 
 Revision 1.26  2002/11/07 14:49:25  cmarti
 fix BUG in putMonitor() (the currently monitored cell didn't get registered to the additional monitor)
 
 Revision 1.25  2002/10/31 22:47:28  cmarti
 - remove Monitor, use AcceptsInput instead
 - rename get-/setMonitoredInput() in AbstractMonitor to get-/setInput()
 - move both methods up into AbstractAcceptsInput and add them to AcceptsInput
 - adapt other classes to these changes
 
 Revision 1.24  2002/10/24 12:05:50  cmarti
 add GPL header
 
 Revision 1.23  2002/10/15 19:26:55  tobi
 lots of javadoc added,
 mouse wheel enabled.
 
 Revision 1.22  2002/10/08 12:59:37  tobi
 added exception to handle nonexistent cell being monitored.
 
 Revision 1.21  2002/10/06 10:57:48  tobi
 fixed javadoc links
 
 Revision 1.20  2002/10/05 16:17:03  tobi
 fixed tag
 
 Revision 1.19  2002/10/04 12:58:16  tobi
 added on and off ganglion cells as special layers of cells to be returned by
 getters in order to facilitate building cortical cells.
 
 Revision 1.18  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy
 
 Revision 1.17  2002/09/29 13:57:11  cmarti
 - remove field audioOutput (an AudioOutput now gets added by the factory, this is required for thread-saftey)
 - get at the AudioOutput through the monitors map
 
 Revision 1.16  2002/09/28 20:00:10  cmarti
 provisional use of the monitoring facility
 
 Revision 1.15  2002/09/28 19:22:23  cmarti
 revert previous bogus change
 
 Revision 1.14  2002/09/28 19:11:05  cmarti
 solve problem with the type cast in getMonitor() if get() returns null
 
 Revision 1.13  2002/09/28 18:31:31  cmarti
 add map of monitors and supporting methods
 
 Revision 1.12  2002/09/28 17:57:32  cmarti
 explicitly support the addition and removal of Updateables to and from the simulation
 
 Revision 1.11  2002/09/27 14:15:06  tobi
 simplified audio output method
 
 Revision 1.10  2002/09/27 09:59:29  tobi
 added rendering for audio output, but doesn't work corectly yet.
 added photoreceptor shape addition to simulationSetupFactory, so tthat
 GUI can render the shapes.
 
 Revision 1.9  2002/09/27 09:36:51  tobi
 javadoc
 
 Revision 1.8  2002/09/27 09:31:36  tobi
 added getMonitoredCell() method to find out what cell should be listened to.
 
 Revision 1.7  2002/09/27 09:29:39  tobi
 added methods setMontiroedCell for setting cell to render audibly in simulation.
 
 Revision 1.6  2002/09/27 09:17:12  tobi
 removed commented-out methods for specific cells.
 
 Revision 1.5  2002/09/26 20:32:34  tobi
 HashMap for cell now implemented so GUI can use them for building its Cell menu.
 
 Revision 1.4  2002/09/26 16:06:16  tobi
 changed output cells to hashmap.
 adding fiducial cells as named hashmap entries.  these will be used by
 GUI to construct cell menu.
 
 Revision 1.3  2002/09/25 16:53:18  tobi
 added fidicial cells and getter/setter methods.  SimulationFactory now sets the fiducical
 (special) cells that we can monitor from the gui.
 
 Revision 1.2  2002/09/25 09:46:57  tobi
 added possible getter/setter methods for specific cells.
 commented out for now.
 
 Revision 1.1  2002/09/24 21:23:23  cmarti
 initial version
 
 */

package ch.unizh.ini.friend.simulation;

import ch.unizh.ini.friend.record.SpikeEventSource;
import ch.unizh.ini.friend.record.SpikeListener;
import java.util.ArrayList;
import java.util.Iterator;
import ch.unizh.ini.friend.stimulus.Stimulus;
import ch.unizh.ini.friend.simulation.cells.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A collection of references to objects that together describe the current state of the simulation.
 * <p>
 * The collection of fidicial cells (cells that can monitored) is built by {@link SimulationSetupFactory}.
 * These cells are entered into a <code>HsshMap</code>.  You can get a particular cell by the key name it was stored under, using
 * {@link #getOutputCell}, or you can get at the entire list with {@link #getOutputCells}.
 * You can set the cell to monitor with {@link #setMonitoredCell}.
 *
 * @author Christof Marti
 * @version $Revision: 1.31 $
 */
public class SimulationSetup {
    
    /** The simulation. */
    protected SimulationTask simulation = null;
    
    /** The stimulus. */
    protected Stimulus stimulus = null;
    
    /**
     * The cells that are simulated. This list is iterated over by a {@link SimulationStep}.
     *It is an ArrayList.
     * @see #outputCells
     */
    protected List simulatedCells=new ArrayList();
    
    /** The cells that can be monitored. We use tree map to keep entries sorted alphabetically for menu generation.
     * @see #simulatedCells
     * see #setOutputCells
     */
    protected Map outputCells = new TreeMap();
    
    private String name=null;
    /** @return the name; used for determining type of simulation */
    public String getName(){ return name; }
    /** @param n the name */
    public void setName(String n){name=n;}
    
    /** the on-type ganglion cells */
    protected Collection onGanglions;
    /** the off-type ganglion cells */
    protected Collection offGanglions;
    
    
    /** Monitors that can be configured to listen to a specific cell. */
    protected Map monitors = null;
    
    /** The shapes of the photoreceptors. */
    protected ArrayList receptorShapes = null;
    
    
    /** Returns the current simulation instance.
     * @return The simulation.
     */
    public SimulationTask getSimulation() {
        return simulation;
    }
    
    /** Sets the current simulation instance.
     * @param simulation The new simulation.
     */
    public void setSimulation(SimulationTask simulation) {
        this.simulation = simulation;
    }
    
    /** Returns the current stimulus instance.
     * @return The stimulus.
     */
    public Stimulus getStimulus() {
        return stimulus;
    }
    
    /** Sets the current stimulus instance.
     * @param stimulus The new stimulus.
     */
    public void setStimulus(Stimulus stimulus) {
        this.stimulus = stimulus;
    }
    
    /** Returns the cells that can be monitored (listened to).
     * @return The cells.
     */
    public Map getOutputCells() {
        return outputCells;
    }
    
    /** Sets the cells that can be monitored (listened to).
     * @param cells The cells.
     */
    public void setOutputCells(Map cells) {
        this.outputCells = cells;
    }
    
    
    /** returns a particlar output cell by its key. Object must be cast to the appropriate type.
     *
     * @param key the key the cell was stored in the map by
     *@return null if the cell doesn't exist in the map
     */
    public Object getOutputCell(String key){
        if(outputCells!=null){
            return outputCells.get(key);
        }else return null;
    }
    
    /** Returns the cells that are simulated
     * @return The cells.
     */
    public List getSimulatedCells() {
        return simulatedCells;
    }
    
    /** Sets the cells that are simulated
     * @param cells The cells.
     */
    public void setSimulatedCells(List cells) {
        this.simulatedCells = cells;
    }
    
    /** Return the retinal ganglion on-type cells
     * @return list on retinal ganglion on-type
     */
    public Collection getOnGanglions(){
        return onGanglions;
    }
    
    /** Return the retinal ganglion off-type cells
     * @return list off retinal ganglion on-type
     */
    public Collection getOffGanglions(){
        return offGanglions;
    }
    
    public void setOnGanglions(Collection c){ onGanglions=c;}
    public void setOffGanglions(Collection c){ offGanglions=c;}
    
    
    
    /** put a an object into the output cells map.
     * @param key the storage key
     * @param cell the cell to store
     */
    public void putOutputCell(String key, Object cell){
        outputCells.put(key,cell);
    }
    
    /** Returns the monitors that are currently registered.
     * @return The monitors.
     */
    public Map getMonitors() {
        return monitors;
    }
    
    /** Sets the Map of monitors.
     * Monitors are only used in SimulationSetup when monitored cell is being set, so that each monitor can have
     * its monitored cell set. The monitors are not updated; they need to be added as updateables to the set of cells obtained from {@link #getOutputCells}.
     *
     * @param monitors The monitors.
     */
    public void setMonitors(Map monitors) {
        this.monitors = monitors;
    }
    
    /** returns a particlar monitor by its key.
     * @param key the key the monitor was stored in the map by
     *@see #setMonitors
     */
    public AcceptsInput getMonitor(String key){
        return (AcceptsInput)monitors.get(key);
    }
    
    /** put a an object into the monitors map.
     *@see #setMonitors
     * @param key the storage key
     * @param monitor the monitor to store
     */
    public void putMonitor(String key, AcceptsInput monitor){
        monitor.setInput(getMonitoredCell());
        monitors.put(key,monitor);
    }
    
    /** Returns the shapes of the photoreceptors.
     * @return The shapes.
     */
    public ArrayList getReceptorShapes() {
        return receptorShapes;
    }
    
    /** Sets the shapes of the photoreceptors.
     * @param receptorShapes The shapes.
     */
    public void setReceptorShapes(ArrayList receptorShapes) {
        this.receptorShapes = receptorShapes;
    }
    
    /** set the monitored cell in the simulation using the String key.
     * This cell will be rendered audibly or visually (on the status line).
     * @param key The key by which the cell was {@link #putOutputCell put} into the collection of
     * fiducial {@link #outputCells output cells}.
     * @see #setMonitoredCell(ServesOutput)
     * @throws NoSuchCellException if you try to monitor a cell that doesn't exist
     */
    public void setMonitoredCell(String key){
        if(getOutputCell(key)==null)
            throw new NoSuchCellException(key);
        
        setMonitoredCell((ServesOutput)getOutputCell(key));
    }
    
    /** the monitored cell.
     @see #setMonitoredCell
     */
    protected ServesOutput monitoredCell=null;
    
    /** set the monitored cell in the simulation directly.
     *
     * This cell will be rendered audibly by {@link AudioOutput} and visually in the {@link ch.unizh.ini.friend.gui.ActivityMeter}.
     * @param cell The key by which the cell was {@link #putOutputCell put} into the collection of
     * fiducial {@link #outputCells output cells}.
     * @see #setMonitoredCell(String)
     */
    public void setMonitoredCell(ServesOutput cell){
        if(cell==null){
            System.out.println("tried to set null monitored cell - ignoring");
            return;
        }
        monitoredCell = cell;
        
        // iterate over all monitors and set their monitored cell (their input)
        Iterator i = monitors.values().iterator();
        while (i.hasNext()) {
            ((AcceptsInput)i.next()).setInput(cell);
        }
    }
    
    /** returns the monitored cell
     * @return the ServesOutput object to monitor
     */
    public ServesOutput getMonitoredCell(){
        return monitoredCell;
    }
    
    /** The cell which is monitored first. */
    protected String startingCellName;
    
    /**
     * Gets the starting cell name.
     *
     * @return String the starting cell name
     */
    public String getStartingCellName() {
        return startingCellName;
    }
    
    /**
     * Sets the starting cell Name.
     *
     * @param startingCellName The starting cell name to set
     */
    public void setStartingCellName(String startingCellName) {
        this.startingCellName = startingCellName;
    }
    
    
    /** indicates we tried to monitor a nonexistent cell */
    public class NoSuchCellException extends RuntimeException {
        /** @param cause the cause of the exception */
        public NoSuchCellException(String cause){
            super(cause);
        }
    }
    
    
}
