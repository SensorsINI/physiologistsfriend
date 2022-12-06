package ch.unizh.ini.friend.record;
/*
 * $Id: SpikeReporter.java,v 1.3 2003/06/16 07:46:27 tobi Exp $
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
 * Created on June 8, 2003, 8:38 AM
 */



/**
Interface that is implemented by objects that can report spikes.
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.3 $
 */
public interface SpikeReporter extends SpikeEventSource {
    
    /** starts acquisition of spikes and generation of {@link SpikeEvent}'s. If source is not available it does nothing. */
    public void startReporting();
    
    /** ends thread after first stopping microphone acquisition. This ends generation of {@link SpikeEvent}'s */
    public void stopReporting();
    
    /** @return whether reporting is enabled */
    public boolean isReporting();
    
}
/*
 *$Log: SpikeReporter.java,v $
 *Revision 1.3  2003/06/16 07:46:27  tobi
 *fixed javadoc
 *
 *added target to build windows installer
 *
 *Revision 1.2  2003/06/15 19:17:31  tobi
 *added capability of recording spikes from simulation or from microphone and plotting the
 *corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 *kind of a spike-tirggered average is possible.
 *

 */
