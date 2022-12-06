/*
 * BarStimulus.java
 *
 * Created on September 21, 2002, 7:36 PM
 
 $Id: BarStimulus.java,v 1.5 2003/06/23 11:30:16 tobi Exp $
 

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

 
 $Log: BarStimulus.java,v $
 Revision 1.5  2003/06/23 11:30:16  tobi
 greatly improved recording display speed, capability

 added full screen exclusive display

 Revision 1.4  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.3  2002/10/06 10:51:18  tobi
 fixed javadoc links

 Revision 1.2  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/22 19:34:27  tobi
 initial versions -- apparently working

 
 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.graphics.ConvexPolygon;

/**
 *  A stimulus consisting of a single bar.
 * @author  tobi
 @since $Revision: 1.5 $
 */
public class BarStimulus extends ConcreteStimulus {
    
    /** default wdith of bar: {@value} */
    public static final float WIDTH=6;
    /** default height of bar: {@value} */
    public static final float HEIGHT=.5f;
    
    /** Creates a new instance of BarStimulus with default size. */
    public BarStimulus() {
        super(ConvexPolygon.getRectangleInstance(-WIDTH/2,-HEIGHT/2,WIDTH,HEIGHT));
    }

    /** Creates a new instance of BarStimulus with a width and height of the bar. 
     @see ch.unizh.ini.friend.gui.TangentScreen#SCREEN_DIMENSION
     @param width width of bar
     @param height height of bar
     */
    public BarStimulus(float width, float height) {
        this(-width/2,-height/2,width,height);
    }

    /** Creates a new instance of BarStimulus with a width and height and center of the bar. 
     @see ch.unizh.ini.friend.gui.TangentScreen#SCREEN_DIMENSION
     @param width width of bar
     @param height height of bar
     @param centerX horizontal center of bar
     @param centerY vertical center of bar
     */
    public BarStimulus(float width, float height, float centerX, float centerY) {
        super(ConvexPolygon.getRectangleInstance(-width/2+centerX,-height/2+centerY,width,height));
    }
    
        // test stimulus a bit
    public static void main(String[] args){
        Stimulus s=new BarStimulus();
        System.out.println("orginal bar="+s);
        s.getTransforms().translate(1,1);
        System.out.println("after translation bar="+s);
        System.out.println("transformed bar="+s.getTransformedShapes());
        System.out.println("after getTransformedShapes() call bar="+s);
    }        
        


}
