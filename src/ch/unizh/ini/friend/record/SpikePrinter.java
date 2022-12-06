/*
 * $Id: SpikePrinter.java,v 1.1 2003/06/12 06:28:47 tobi Exp $
 *
 * Created on June 8, 2003, 3:07 PM
 */

package ch.unizh.ini.friend.record;

import java.util.EventListener;
import java.util.EventListenerProxy;
import java.util.EventObject;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.1 $
 */
public class SpikePrinter implements SpikeListener  {
    
    int N=10;
    int n=0;
    long startTime=System.currentTimeMillis();
 
    /** called by spike source ({@link SpikeReporter}) when a spike is detected
     *      @param e the spike event
     *
     */
    public void spikeOccurred(SpikeEvent e) {
        System.out.print(e.getTime()-startTime+"\t");
        if(++n%N==0){
            n=0; System.out.println("");
        }
    }
    
}

/*
 *$Log: SpikePrinter.java,v $
 *Revision 1.1  2003/06/12 06:28:47  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
