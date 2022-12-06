/*
 * $Id: SpikeEventSource.java,v 1.1 2003/06/12 06:28:47 tobi Exp $
 *
 * Created on June 10, 2003, 3:07 PM
 */

package ch.unizh.ini.friend.record;

import java.util.Collection;

/**
 * Classes that are sources of {@link SpikeEvent}'s implement this interface, so that {@link SpikeListener}'s can register themselves to receive spikes.
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.1 $
 */
public interface SpikeEventSource {
        /** add a listener for all spikes. Listeners are {@link SpikeListener#spikeOccurred called} when a spike occurs and are passed a {@link SpikeEvent}.
     *@param listener the listener
     */
    public void addSpikeListener(SpikeListener listener);
    
    /** removes a listener 
     *@param listener to remove
     */
    public void removeSpikeListener(SpikeListener listener);
    
    /** remove all listeners */
    public void clearSpikeListeners();

    /** @return the Collection of listeners */
    public Collection getSpikeListeners();
}

/*
 *$Log: SpikeEventSource.java,v $
 *Revision 1.1  2003/06/12 06:28:47  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
