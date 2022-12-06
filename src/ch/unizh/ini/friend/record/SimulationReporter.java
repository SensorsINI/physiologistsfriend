/*
 * $Id: SimulationReporter.java,v 1.3 2003/06/16 07:46:27 tobi Exp $
 *
 * Created on June 10, 2003, 3:51 PM
 */

package ch.unizh.ini.friend.record;

import ch.unizh.ini.friend.simulation.AbstractMonitor;
import ch.unizh.ini.friend.simulation.ServesOutput;
import ch.unizh.ini.friend.simulation.SimulationSetup;
import ch.unizh.ini.friend.simulation.cells.SpikingCell;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generates a {@link SpikeEvent} when it detects that the simulation's 
 {@link ch.unizh.ini.friend.simulation.SimulationSetup#setMonitoredCell monitored cell} has made a spike.
 *An instance of this class is constructed 
 in the {@link ch.unizh.ini.friend.simulation.SimulationSetupFactory} 
 for each simulation type (color, luminance, etc.)
  *{@link ch.unizh.ini.friend.gui.FriendGUI} sets the monitored cell based on the user's menu choice of cell, 
 and when recording is activated, it adds listeners for the appropriate monitor.
 *
 *#see SpikeReporter
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.3 $
 */
public class SimulationReporter extends AbstractMonitor  implements SpikeReporter {
    
    SimulationSetup setup=null;
    
    /** Creates a new instance of SpikeEventMonitor */
    public SimulationReporter(SimulationSetup setup) {
        this.setup=setup;
    }
    
    private long startTime=0;
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     *
     */
    public void compute(float dt) {
        if(reportingEnabled==false) return;
        if(setup==null || getSpikeListeners().size()==0) return; // nothing to do
        ServesOutput input = getInput(); // get the cell
        if(isSpikingCell(input)){
            // if its spiking, inform the spike event listeners
            //                    inputValue = input.output();
            if (((SpikingCell)input).isSpike() ){
                Iterator i=getSpikeListeners().iterator();
                long t=System.currentTimeMillis()-startTime;
                while(i.hasNext()){
                    SpikeListener l=(SpikeListener)i.next();
                    SpikeEvent e=new SpikeEvent(setup,t);
                    l.spikeOccurred(e);
                }
            }
        }
    }

    private Object lastInput=null;  // cache to speed up reflection
    private boolean lastSpikingCellReturn=false;  // last returned value for spiking cell type
    
    
    // examines object using reflection to see if it implements SpikingCell
    private boolean isSpikingCell(Object o){
        if( o!=lastInput || o!=null ){ // check if object changed (different cell selected)
            lastInput=o;
            Class c=o.getClass();
            //            System.out.println("c = " + c );
            boolean is=true;
            try{
                Method m=c.getMethod("isSpike",null); // if the class has isSpike method
            }catch(NoSuchMethodException e){
                is=false;
            }
            
            lastSpikingCellReturn=is;
            return lastSpikingCellReturn;
        }else{  // use cached value
            return lastSpikingCellReturn;
        }
        
    }
    
    LinkedList listeners=new LinkedList();
    
    /** add a listener for all spikes. Listeners are {@link SpikeListener#spikeOccurred called} when a spike occurs and are passed a {@link SpikeEvent}.
     * @param listener the listener
     *
     */
    public void addSpikeListener(SpikeListener listener) {
        listeners.add(listener);
    }
    
    /** remove all listeners  */
    public void clearSpikeListeners() {
        listeners.clear();
    }
    
    /** @return the Collection of listeners  */
    public Collection getSpikeListeners() {
        return listeners;
    }
    
    /** removes a listener
     * @param listener to remove
     *
     */
    public void removeSpikeListener(SpikeListener listener) {
        listeners.remove(listener);
    }
    
    private boolean reportingEnabled=false;
    
    /** starts acquisition of spikes and generation of {@link SpikeEvent}'s. If source is not available it does nothing.  */
    public void startReporting() {
        startTime=System.currentTimeMillis();
        setup.getSimulatedCells().add(this);
        setup.getMonitors().put("SpikeReporter", this);
        setup.setMonitoredCell(setup.getMonitoredCell());  // add us to monitors
        reportingEnabled=true;
    }
    
    /** removes all spike event listeners, ends thread after first stopping microphone acquisition. This ends generation of {@link SpikeEvent}'s  */
    public void stopReporting() {
        clearSpikeListeners();
        try{
        setup.getSimulatedCells().remove(this);
        setup.getMonitors().remove(this);
        }catch(NullPointerException e){
            System.out.println("sim reporter: couldn't remove");
            e.printStackTrace();
        }
        reportingEnabled=false;
    }
    
    /** @return whether reporting is enabled  */
    public boolean isReporting() {
        return reportingEnabled;
    }
    
}

/*
 *$Log: SimulationReporter.java,v $
 *Revision 1.3  2003/06/16 07:46:27  tobi
 *fixed javadoc
 *
 *added target to build windows installer
 *
 *Revision 1.2  2003/06/16 06:32:52  tobi
 *added more mystery cells for daniel
 *
 *Revision 1.1  2003/06/15 20:21:04  tobi
 *initial version
 *
 *Revision 1.1  2003/06/12 06:28:46  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
