/*
 $Id: SpotStimulus.java,v 1.2 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: SpotStimulus.java,v $
 Revision 1.2  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.1  2002/10/20 15:17:06  tobi
 added spot stimulus

 
 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.graphics.ConvexPolygon;

/**
 *  A stimulus consisting of a single bar.
 * @author  tobi
 @since $Revision: 1.2 $
 */
public class SpotStimulus extends ConcreteStimulus {
    
    /** default diameter of bar: {@value} */
    public static final float DIAMETER=1;
    
    /** number of side of the polygon making up the stimulus: {@value} */
    public static final int NSIDES=6; // n>10 (at most) crashes in intersection computation somewhere
    
    /** Creates a new instance of BarStimulus with default size. */
    public SpotStimulus() {
        super(ConvexPolygon.getNGonInstance(DIAMETER/2,NSIDES));
    }

    /** Creates a new instance of <code>SpotStimulus</code> with the given diameter. 
     @see ch.unizh.ini.friend.gui.TangentScreen#SCREEN_DIMENSION
     @param diameter diameter of spot in screen coordinate units
     */
    public SpotStimulus(float diameter) {
        super(ConvexPolygon.getNGonInstance(DIAMETER/2,NSIDES));
    }

}
