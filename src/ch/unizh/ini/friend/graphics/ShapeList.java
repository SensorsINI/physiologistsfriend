/*
 * ShapeList.java
 *
 * Created on September 21, 2002, 4:15 PM
 
 $Id: ShapeList.java,v 1.10 2003/07/03 16:54:25 tobi Exp $
 

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

 
 $Log: ShapeList.java,v $
 Revision 1.10  2003/07/03 16:54:25  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.9  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.8  2002/10/15 19:26:55  tobi
 lots of javadoc added,
 mouse wheel enabled.

 Revision 1.7  2002/10/07 13:02:07  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.

 Revision 1.6  2002/10/06 10:51:18  tobi
 fixed javadoc links

 Revision 1.5  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.4  2002/09/25 16:48:53  tobi
 expanded excitation() to make debugging easier

 Revision 1.3  2002/09/25 08:41:26  tobi
 fixed javadoc so no errors generated and added package descriptions.

 Revision 1.2  2002/09/22 19:32:36  tobi
 added clone() method that does deep clone of the individual shapes.
 this seemed to be necessary in order to correctly buffer transforms.

 Revision 1.1  2002/09/21 20:33:27  tobi
 initial commit

 
 */

package ch.unizh.ini.friend.graphics;

import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

/**
 *  An ArrayList of shapes which are {@link Transformable} and {@link Intersectable} as a whole. 
 <p>
 A {@link ch.unizh.ini.friend.stimulus.GratingStimulus} (for example) is constructed by adding 
 a number of {@link ConvexPolygon ConvexPolygons} to a ShapeList in
 a {@link ch.unizh.ini.friend.stimulus.ConcreteStimulus}.
 
 * @author  tobi
 @since $Revision: 1.10 $
 */
public class ShapeList extends java.util.ArrayList implements Intersectable, Transformable, Cloneable {
    
    // this class extends from ArrayList so it already has the ArrayList to hold the shapes
    
    /** Creates a new instance of ShapeList with an empty shape list. */
    public ShapeList() {
    }
    
    /** creates a new ShapeList initialized with the given shape.
     @param t the starting shape
     */
    public ShapeList(Transformable t){
        //System.out.println("making new ShapeList with shape "+t);
        add(t);
    }
    
    /** Applies the given transformation to the geometric object.
     * @param at The affine transformation to apply.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable apply(AffineTransform at) {
        for(Iterator i=iterator();i.hasNext();){
            Transformable t=(Transformable)i.next();
            t.apply(at);
        }
        return this;
    }
    
    /** Computes the total area of the shapes.
     * @return The area.
     */
    public float area() {
        float area=0;
        for(Iterator i=iterator();i.hasNext();){
            Intersectable t=(Intersectable)i.next();
            area+=t.area();
        }
        return area;
    }
    
    /** Computes the area of intersection between <code>this</code> and the given <code>other</code> object.
     * @param other The object to intersect with.
     * @return The intersection object.
     */
    public Intersectable intersect(Intersectable other) {
        //assert other instanceof Intersectable; 
        ShapeList intersection=new ShapeList(); // will hold the intersection
        for(Iterator i=iterator();i.hasNext();){ // for all shapes in this
            Intersectable t=(Intersectable)i.next(); // get this's shape
            Intersectable inter=t.intersect(other);  // get the intersection with other
            if(inter!=null && inter.area()>0) intersection.add(inter); // add it to the list of intersections
        }
        return intersection;
    }
    
    /** Rotates the geometric object by the given angle around the origin.
     * @param phi Rotation angle.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi) {
        for(Iterator i=iterator();i.hasNext();){
            Transformable t=(Transformable)i.next();
            t.rotate(phi);
        }
        return this;
    }
    
    /** Rotates the geometric object by the given angle around the given point.
     * @param phi Rotation angle.
     * @param x x-coordinate of the rotation center.
     * @param y y-coordinate of the rotation center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable rotate(float phi, float x, float y) {
        for(Iterator i=iterator();i.hasNext();){
            Transformable t=(Transformable)i.next();
            t.rotate(phi,x,y);
        }
        return this;
    }
    
    /** Scales the geomtric object by the given values in x- and y-coordinates with
     * the origin as center.
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scale(float sx, float sy) {
        for(Iterator i=iterator();i.hasNext();){
            Transformable t=(Transformable)i.next();
            t.scale(sx,sy);
        }
        return this;
    }
    
    /** Scales the geomtric object by the given values in x- and y-coordinates with
     * the given center (x, y).
     * @param sx Scalar for x-coordinates.
     * @param sy Scalar for y-coordinates.
     * @param x x-coordinate of the center.
     * @param y y-coordinate of the center.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable scale(float sx, float sy, float x, float y) {
        for(Iterator i=iterator();i.hasNext();){
            Transformable t=(Transformable)i.next();
            t.scale(sx,sy,x,y);
        }
        return this;
    }
    
    /** Translates the geometric object by the given coordinates.
     * @param dx Translation in x-coordinates.
     * @param dy Translation in y-coordinates.
     * @return this for easy concatenation or a transformed clone.
     */
    public Transformable translate(float dx, float dy) {
        for(Iterator i=iterator();i.hasNext();){
            Transformable t=(Transformable)i.next();
            t.translate(dx,dy);
        }
        return this;
    }
    
    
    /** string regpresentation of ShapeList */
    public String ShapeList(){
        String s="ShapeList ";
        for(Iterator i=iterator();i.hasNext();){
            s+=(Transformable)i.next()+" ";
        }
        return s;
    }

    /** clones this list of shapes, including cloning of the members of the list */
    public Object clone(){
        ShapeList r=new ShapeList();
        for(Iterator i=iterator();i.hasNext();){
            r.add(((Transformable)i.next()).clone());
        }
        return r;
     }

}
