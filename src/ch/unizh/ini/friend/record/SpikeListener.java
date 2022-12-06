/*
 * $Id: SpikeListener.java,v 1.1 2003/06/12 06:28:47 tobi Exp $
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
 * Created on June 9, 2003, 8:14 PM
 */

package ch.unizh.ini.friend.record;

import java.util.EventListener;

/**
 * SpikeListeners should implement this interface in order to be able to register themselves as recipients of spike events.
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.1 $
 */
public interface SpikeListener extends EventListener {
    
    /** called by spike source ({@link SpikeReporter}) when a spike is detected 
     @param e the spike event
     */
    public void spikeOccurred(SpikeEvent e);
    
}

/*
 *$Log: SpikeListener.java,v $
 *Revision 1.1  2003/06/12 06:28:47  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
