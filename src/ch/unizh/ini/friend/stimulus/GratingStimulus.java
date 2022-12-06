/*
 * GratingStimulus.java
 *
 * Created on September 21, 2002, 7:49 PM
 
 $Id: GratingStimulus.java,v 1.8 2002/10/24 12:05:51 cmarti Exp $
 

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

 
 $Log: GratingStimulus.java,v $
 Revision 1.8  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.7  2002/10/13 16:29:10  tobi
 many small changes from tuebingen trip.

 Revision 1.6  2002/10/08 12:12:06  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.5  2002/10/06 10:51:18  tobi
 fixed javadoc links

 Revision 1.4  2002/10/06 09:02:12  tobi
 reduced size to twice screen size to speed up intersection computations.

 Revision 1.3  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.2  2002/09/23 08:24:28  tobi
 changed PERIOD to a static so it can be referenced in default constructor

 Revision 1.1  2002/09/22 19:34:29  tobi
 initial versions -- apparently working

 
 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.gui.TangentScreen;
import ch.unizh.ini.friend.graphics.ShapeList;
import ch.unizh.ini.friend.graphics.Transformable;
import ch.unizh.ini.friend.graphics.ConvexPolygon;
/**
 * A grating stimulus, which consists of a set of {@link BarStimulus} with a specified spatial period.
 * @author  tobi
 @since $Revision: 1.8 $
 */
public class GratingStimulus extends ConcreteStimulus {
    
    /** the default spatial period of the grating: {@value}. 
     Compare this dimension with the {@linkplain ch.unizh.ini.friend.gui.TangentScreen#SCREEN_DIMENSION screen dimensions}.*/
    protected static final float PERIOD=2f;
    
    /** size of the grating stimulus as a multiple of the screen size */
    protected static final float SCREEN_MULTIPLE=2f;
    
    
    /** Creates a new instance of GratingStimulus with default spatial period {@link #PERIOD}.*/
    public GratingStimulus() {
        this(PERIOD);
    }

    /** Creates a new instance of GratingStimulus with specified spatial period. The number of bars is computed so that the
     grating covers the screen even when mouse reference position moves to the edge of the screen.
     
     @param period Period of grating in units of {@link ch.unizh.ini.friend.gui.TangentScreen#SCREEN_DIMENSION}. A period of 1 means
     a black and white grating pair every {@linkplain ch.unizh.ini.friend.gui.TangentScreen#SCREEN_DIMENSION screen dimension units}.
     
     */
    public GratingStimulus(float period) {
        //assert period>0 ;
        int nBars=(int)Math.ceil(SCREEN_MULTIPLE*TangentScreen.SCREEN_DIMENSION/period);
        float w=TangentScreen.SCREEN_DIMENSION*4;
        float h=period/2;
        
        ShapeList l=new ShapeList();
        for(int i=0;i<nBars;i++){
            Transformable bar=ConvexPolygon.getRectangleInstance(-w/2,period*(i-nBars/2),w,h);
            l.add(bar);
        }
        setShapes(l);
    }
    
            // test stimulus a bit
    public static void main(String[] args){
        Stimulus s=new GratingStimulus();
        System.out.println("orginal grating="+s);
        s.getTransforms().translate(1,1);
        System.out.println("after translation "+s.getTransformedShapes());
    }        
 
}
