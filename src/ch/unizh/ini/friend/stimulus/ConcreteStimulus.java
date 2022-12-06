/*
 $Id: ConcreteStimulus.java,v 1.13 2003/06/23 11:30:17 tobi Exp $
 

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

 
 $Log: ConcreteStimulus.java,v $
 Revision 1.13  2003/06/23 11:30:17  tobi
 greatly improved recording display speed, capability

 added full screen exclusive display

 Revision 1.12  2003/05/10 17:27:45  jgyger
 Merge from color-branch

 Revision 1.11.2.1  2003/05/08 17:14:02  tobi
 javadoc links fixed

 Revision 1.11  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.10  2002/10/15 19:27:07  tobi
 lots of javadoc added,
 mouse wheel enabled.

 Revision 1.9  2002/10/08 12:12:06  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.8  2002/10/06 10:51:18  tobi
 fixed javadoc links

 Revision 1.7  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.6  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.5  2002/09/25 17:05:58  tobi
 added comment about commented-out double buffering.

 Revision 1.4  2002/09/25 08:41:26  tobi
 fixed javadoc so no errors generated and added package descriptions.

 Revision 1.3  2002/09/22 19:36:23  tobi
 all mehtods moved the AbstractStimulus, only constructors remain.

 Revision 1.2  2002/09/16 20:44:42  cmarti
 removing parameter dt from update() in Updateable

 Revision 1.1  2002/09/16 08:43:39  cmarti
 initial version

 Revision 1.1  2002/09/13 12:02:19  cmarti
 initial version

 */

package ch.unizh.ini.friend.stimulus;

import ch.unizh.ini.friend.graphics.*;

/**
 * Provides a generic implementation of a stimulus.  This stimulus has a set of shapes, a set of transforms, a foreground and background
 brightness. It is an entire stimulus.  Specific subclasses of this class correspond to specific stimuli shapes.
 
<p>
 The excitation of a {@link ch.unizh.ini.friend.simulation.cells.Photoreceptor} can be computed using the {@link ch.unizh.ini.friend.simulation.cells.Photoreceptor#excitation} method.
 <p>
 If the stimulus transform is modified, use {@link #update} to update the cached stimulus shape???
 
 @see BarStimulus
 @see EdgeStimulus
 @see GratingStimulus
 
 * @author Christof Marti/Tobi Delbruck
 * @version $Revision: 1.13 $
 */
public class ConcreteStimulus extends AbstractStimulus implements Cloneable {
    
    /** creates a new instance of <code>ConcreteStimulus</code> with a null {@link AbstractStimulus#shapes list of shapes}. */
    public ConcreteStimulus(){
    }
    
    /** Creates a new instance with the given shapes. 
     @param shapes A list of Transformable shapes.
     @see ConcreteStimulus
     */
    public ConcreteStimulus(ShapeList shapes) {
        //assert shapes != null;
//        System.out.println("making ConcreteStimulus with shapes "+shapes);
        setShapes(shapes);
//        this.newShape = shapes;
//        update();
    }
    
    /** create a <code>ConcreteStimulus</code> with a single Transformable shape.
     This constuctor constructs the ShapeList from the shape.
     @param shape some Transformable shape, constructed from e.g. {@link ch.unizh.ini.friend.graphics.ConvexPolygon#getRectangleInstance}.
     */
    public ConcreteStimulus(Transformable shape){
        this(new ShapeList(shape));
//         System.out.println("made new ConcreteStimulus with shape "+shape);
   }
     
    // tobi took out double buffering for now, since this is a feedforward simulation
    
    /**
     * Updates the actual state to the newly computed - AKA double-buffering.
     */
    public synchronized void update() {
        super.update();
        Object tmp;
        if (newShape instanceof ShapeList) {
            tmp = ((BufferedTransform)newShape).getTransformed();
        } else {
            tmp = newShape.clone();
        }
        //assert tmp instanceof ShapeList;
        shapes = (ShapeList)tmp;
    }
       
    /** clones the foreground density, background density, and the transformed ShapeList */
    public Object clone(){
        ConcreteStimulus s=new ConcreteStimulus();
        s.setForegroundExcitationDensity(getForegroundExcitationDensity());
        s.setBackgroundExcitationDensity(getBackgroundExcitationDensity());
        s.setShapes((ShapeList)getTransformedShapes().clone());
        return s;
    }
    
}
