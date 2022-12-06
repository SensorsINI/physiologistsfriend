/*
 * $Id: SpikeEvent.java,v 1.1 2003/06/12 06:28:46 tobi Exp $
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
 * Created on June 8, 2003, 2:20 PM
 */

package ch.unizh.ini.friend.record;

import java.util.EventObject;

/**
 * Represents a spike.
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.1 $
 */
public class SpikeEvent extends EventObject {
    
    long time=0;
    
    /** Creates a new instance of SpikeEvent. The {@link #getTime time} of the spike is set to System.currentTimeMillis().
     @param source the object that generated the spike
     */
    public SpikeEvent(Object source) {
        super(source);
        time=System.currentTimeMillis();
    }
    
    /** Creates a new instance of SpikeEvent 
     @param source the object that generated the spike
     *@param time the time of the spike
     */
    public SpikeEvent(Object source, long time){
        this(source);
        this.time=time;
    }
    
    public long getTime(){ return time; }
    public void setTime(long t){time=t;}
    
    /** @return time of spike as string */
    public String toString(){
        return Long.toString(time);
    }
    
}

/*
 *$Log: SpikeEvent.java,v $
 *Revision 1.1  2003/06/12 06:28:46  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
