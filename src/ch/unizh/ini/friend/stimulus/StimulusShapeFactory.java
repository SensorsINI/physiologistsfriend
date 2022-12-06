/*
 * StimulusShapeFactory.java
 *
 * Created on September 24, 2002, 8:39 PM
 
 $Id: StimulusShapeFactory.java,v 1.4 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: StimulusShapeFactory.java,v $
 Revision 1.4  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.3  2002/10/20 15:17:06  tobi
 added spot stimulus

 Revision 1.2  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/24 19:49:12  tobi
 initial version, works ok

 
 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.graphics.ShapeList;

/**
 A helper class with static methods for making standard stimulus shapes. These can be used to replace the shape of a stimulus without
 changing the transformations, the brightness values, etc.
 *
 * @author  tobi
 @since $Revision: 1.4 $
 */
public class StimulusShapeFactory {
    
    /** Creates a new instance of StimulusShapeFactory */
    private StimulusShapeFactory() {
        // private since only static methods here
    }
    
    /** returns a bar shape */
    public static ShapeList makeBarShape(){
        Stimulus s=new BarStimulus();
        return s.getShapes();
    }
    
    /** returns an edge shape */
    public static ShapeList makeEdgeShape(){
        Stimulus s=new EdgeStimulus();
        return s.getShapes();
    }
    
    /** returns grating shapes */
    public static ShapeList makeGratingShape(){
        Stimulus s=new GratingStimulus();
        return s.getShapes();
    }
    
    /** returns a spot shape */
    public static ShapeList makeSpotShape(){
        Stimulus s=new SpotStimulus();
        return s.getShapes();
    }
    
    
}
