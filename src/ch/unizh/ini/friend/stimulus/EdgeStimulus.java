/*
 * EdgeStimulus.java
 *
 * Created on September 21, 2002, 7:45 PM
 
 $Id: EdgeStimulus.java,v 1.3 2002/10/24 12:05:51 cmarti Exp $
 

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

 
 $Log: EdgeStimulus.java,v $
 Revision 1.3  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.2  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/22 19:34:24  tobi
 initial versions -- apparently working

 
 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.gui.TangentScreen;

/**
 * A stimulus consisting of an edge, with reference (mouse) position centered along edge of the bar.
 This stimulus is a {@link BarStimulus} with edge centering and sized large enough that other edges will never be visible.
 
 * @author  tobi
 @since $Revision: 1.3 $
 */
public class EdgeStimulus extends BarStimulus{
    
    /** Creates a new instance of EdgeStimulus with default parameters. 
     Since there are no parameters for an edge, there is only this
     single constructor.
     */
    public EdgeStimulus() {
        super(TangentScreen.SCREEN_DIMENSION*2,TangentScreen.SCREEN_DIMENSION*2,0f,TangentScreen.SCREEN_DIMENSION);
    }
    
}
