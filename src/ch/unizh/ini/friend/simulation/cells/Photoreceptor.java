/*
 $Id: Photoreceptor.java,v 1.20 2003/07/06 05:22:03 tobi Exp $
 

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

 
 $Log: Photoreceptor.java,v $
 Revision 1.20  2003/07/06 05:22:03  tobi
 *** empty log message ***

 Revision 1.19  2003/07/03 16:54:27  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.18  2003/05/10 17:27:44  jgyger
 Merge from color-branch

 Revision 1.17.2.1  2003/03/16 18:14:17  jgyger
 move excitation method from stimuli to photoreceptors

 Revision 1.17  2002/10/31 21:47:28  cmarti
 - split AbstractAcceptsInputServesOutput, added AbstractCell
 - rename currentOutput/newOutput to value/newValue

 Revision 1.16  2002/10/29 12:05:31  cmarti
 extend AbstractAcceptsInputServesOutput, use its implementation of ServesOutput

 Revision 1.15  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput

 Revision 1.14  2002/10/25 08:13:49  cmarti
 - make Photoreceptor extend ManyInputs (was: implements AcceptsInput)
 - provide more generic constructor in ManyInputs

 Revision 1.13  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.12  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.11  2002/10/05 17:35:25  tobi
 added some clarifying javadoc

 fixed photoreceptor gethex arrangement method to correctly set the retinotopic location of the resutling
 photoreceptors, after they are rotated into the new locaitons.

 Revision 1.10  2002/10/01 20:50:08  tobi
 implemented Retinotopic and added construction of location from previous retina level
 in array list static helper methods.  seems to work.

 Revision 1.9  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.8  2002/09/29 19:07:09  tobi
 shortened high pass filter time constant to 1 second, to make it more realistic.

 Revision 1.7  2002/09/25 16:51:38  tobi
 classified cells as graded or spiking, added interfaces GradedCell, SpikingCell.
 made photorecepto, bipolar, horizontal cell graded cells with gradedOutput() method.

 Revision 1.6  2002/09/25 09:15:52  tobi
 enhanced javadoc

 Revision 1.5  2002/09/24 20:32:20  cmarti
 turn the hexagonal array of photoreceptors by 90 degrees

 Revision 1.4  2002/09/24 04:31:45  tobi
 excitation of receptor now depends only on history of excitation and instantaneous brightness
 of stimulus.  bug was that excitation depended on dt before, which doesn't
 make sense for receptor.

 Revision 1.3  2002/09/23 14:36:52  tobi
 added ADAPTATION_TIME_CONSTANT parameter

 Revision 1.2  2002/09/17 13:23:02  cmarti
 - add a high-pass filter (field highPass)
 - add a gain to the higher frequencies (field gain)

 Revision 1.1  2002/09/17 12:44:01  cmarti
 renaming PhotoReceptor to Photoreceptor...

 Revision 1.9  2002/09/16 20:44:42  cmarti
 removing parameter dt from update() in Updateable

 Revision 1.8  2002/09/16 11:20:27  cmarti
 removal of UpdateSource/Listener

 Revision 1.7  2002/09/13 12:00:21  cmarti
 use objects with the Stimulus interface to represent the stimulus

 Revision 1.6  2002/09/13 08:40:01  cmarti
 - adaption to the splitting of Updateable into Updateable and UpdateSource
 - adaption of the splitting of update method into compute and update method

 Revision 1.5  2002/09/10 20:35:30  cmarti
 added fireUpdateEvent to update method...

 Revision 1.4  2002/09/10 20:25:50  cmarti
 removed System.out.println from update method

 Revision 1.3  2002/09/10 19:52:26  cmarti
 now 'extends Updateable' instead of 'implements'

 Revision 1.2  2002/09/10 18:11:30  cmarti
 removed Compound

 Revision 1.1  2002/09/10 15:53:21  cmarti
 intial version

 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import ch.unizh.ini.friend.graphics.*;
import ch.unizh.ini.friend.simulation.filter.*;
import ch.unizh.ini.friend.stimulus.Stimulus;
import ch.unizh.ini.friend.topology.*;

/**
 * This class implements the notion of a photoreceptor.
 * @author Christof Marti
 * @version $Revision: 1.20 $
 */
public class Photoreceptor extends AbstractCell implements GradedCell, Retinotopic {
    
    /** adaptation time constant of photoreceptor in seconds: {@value} */
    public static final float ADAPTATION_TIME_CONSTANT=0.5f;
    
    /** the transient gain of the photoreceptor, as a multiple of the DC gain: {@value} */
    public static final float TRANSIENT_GAIN=5f;
    

    /**
     * The shape of this photoreceptor.
     */
    protected ConvexPolygon shape;
    
    /**
     * The stimulus for this photoreceptor (and likely the whole simulation).
     */
    protected Stimulus stimulus;
    
    /** The gain of the high-pass output. */
    protected float gain = TRANSIENT_GAIN;
    
    /** A high-pass filter that is used to compute the transient gain. */
    protected Filter highPass = new HighPassFilter(ADAPTATION_TIME_CONSTANT, 0.0f);

    /**
     * Creates a new photoreceptor with a given shape and attached
     * to a given stimulus and a certain gain in higher frequencies.
     * @param shape The shape of the photoreceptor.
     * @param stimulus The stimulus used to stimulate this photoreceptor.
     */
    public Photoreceptor(ConvexPolygon shape, Stimulus stimulus) {
        super(stimulus);
        this.shape = shape;
        this.stimulus = stimulus;
        setRetinotopicLocation(new RetinotopicLocation(shape.getCenter()));
    }
    
    /**
     * Helper method (e.g., for <code>getHexagonalArrayListInstance</code>) that adds
     * a column of hexagonal photoreceptors to a given list of photoreceptors.
     * @param prs List where the created photoreceptors get added.
     * @param x x-coordinate of the center of the first photoreceptor.
     * @param y y-coordinate of the center of the first photoreceptor.
     * @param r Radius of the enclosing circle of a single photoreceptor.
     * @param dy Displacement of the y-coordinate between photoreceptors.
     * @param n The number of photoreceptors to add.
     * @param stimulus The stimulus to attach to the photoreceptors.
     * @param shapes A collection where the shapes of the photoreceptors are added to.
     */
    private static void addHexCol(ArrayList prs, float x, float y, float r, float dy, int n, Stimulus stimulus, Collection shapes) {
        for (int i = 0; i < n; i++, y += dy) {
            ConvexPolygon cp = ConvexPolygon.getNGonInstance(x, y, r, 6);
            Photoreceptor pr = new Photoreceptor(cp, stimulus);
            prs.add(pr);
            if (shapes != null) {
                boolean res = shapes.add(cp);
                //assert res;
            }
        }
    }
    
    /**
     * Returns a hexagonal arranged list of hexagonal photoreceptors. 
     
     The array is arranged so that 
     the x spacing between the centers is the given <code>spacing</code> plus twice the radius <code>r</code>, 
     and the y spacing is smaller by <code>cos(Pi/6)</code>.  
     
     This results in an array with one of the principal axes horizontal, the other
     two ases are at +/-60 degrees.
     
     * @param a The number of photoreceptors making one side of the list. E.g. a=2 returns a 7-photoreptor array.
     * @param r The radius of the enclosing circle arround one photoreceptor.
     * @param spacing The additional distance between the photoreceptors.
     * @param stimulus The stimulus to attach to the photoreceptors.
     * @param shapes A collection the shapes of the photoreceptors are added to.
     * @return The created list.
     */
    public static ArrayList getHexagonalArrayListInstance(int a, float r, float spacing, Stimulus stimulus, Collection shapes) {
        //assert a > 0;
        //assert r > 0.0f;
        
        int n = 1 + 3*a*(a-1);
        ArrayList al = new ArrayList(n);  // the returned list of photoreceptors
        
        float dx = (2.0f*r + spacing)*(float)Math.cos(Math.PI/6.0);
        float dy = 2*r + spacing;
        addHexCol(al, 0.0f, -(a - 1)*dy, r, dy, 2*a - 1, stimulus, shapes);
        for (int i = 1; i < a; i++) {
            addHexCol(al, -i*dx, -(a - 1 - i/2.0f)*dy, r, dy, 2*a - 1 - i, stimulus, shapes);
            addHexCol(al, i*dx, -(a - 1 - i/2.0f)*dy, r, dy, 2*a - 1 - i, stimulus, shapes);
        }
        
        // why is the array rotated here?
        // it is rotated to give arrangement with x axis as principal
        Iterator li = shapes.iterator();
        while (li.hasNext()) {
            Object current = li.next();
            //assert current instanceof Transformable;
            ((Transformable)current).rotate((float)Math.PI/2.0f);
        }
        
        // we must fix up the locations of the photoreceptors since they have been transformed....
        li=al.iterator();
        while(li.hasNext()){
            Photoreceptor p=(Photoreceptor)li.next();
            p.setRetinotopicLocation(new RetinotopicLocation(p.shape.getCenter()));
        }
        return al;
    }
    
    /** 
     * Computes the new state of this photoreceptor. The new state is the input
     * (computed from the intersection of the receptor with the stimulus and
     * background and their respective brightnesses) plus the {@link #gain}
     * times the {@link #highPass high-pass filtered} input.
     *
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
        newValue = excitation();
        newValue = newValue + gain*highPass.filter(newValue, dt);
    }
    
    /** 
     * Calculate the exitation of the given object (photoreceptor) from 
     * the stimulus. The excitation is the foreground brightness if the
     * receptor is fully illuminated. This excitation is 1 if the photoreceptor
     * is fully illuminated by a brightness of 1. If the receptor is completely
     * unilluminated, the excitation is the background brightness. (The area
     * of the receptor is normalized away).
     *
     * @return The excitation.
     */
    public float excitation() {
        float fg = stimulus.getForegroundExcitationDensity();
        float bg = stimulus.getBackgroundExcitationDensity();
        float receptorArea = shape.area();

        ShapeList transformedShapes = stimulus.getTransformedShapes();
        Intersectable intersection = transformedShapes.intersect(shape);
        float interArea = intersection.area();

        float excit = fg * interArea + bg * (receptorArea - interArea);
        excit = excit / receptorArea;

        return excit;
    }
    
    /** returns the graded output from the cell  */
    public float getGradedOutput() {
        return output();
    }    

    // the location of this cell
    private RetinotopicLocation location;
    
    /** return the RetinotopicLocaton of the cell
     *      @return RetinotopicLocation of cell
     *
     */
    public RetinotopicLocation getRetinotopicLocation() {
        return location;
    }
    
    /** set the location
     *      @param p to set the cell to. This is generally returned from another cell
     and is set during construction.
     *
     */
    public void setRetinotopicLocation(RetinotopicLocation p) {
        location=p;
    }
    
}
