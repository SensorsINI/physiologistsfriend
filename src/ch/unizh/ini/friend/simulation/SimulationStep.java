/*
 $Id: SimulationStep.java,v 1.10 2004/11/12 13:37:12 tobi Exp $
 
 
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
 
 
 $Log: SimulationStep.java,v $
 Revision 1.10  2004/11/12 13:37:12  tobi
 on shipping friend23 and updating manual. migrating to subversion now.

 Revision 1.9  2003/06/26 00:33:47  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.8  2002/10/24 12:05:50  cmarti
 add GPL header
 
 Revision 1.7  2002/10/15 19:26:55  tobi
 lots of javadoc added,
 mouse wheel enabled.
 
 Revision 1.6  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.
 
 Revision 1.5  2002/10/05 23:06:13  tobi
 comments re double buffering
 
 Revision 1.4  2002/10/05 22:38:50  tobi
 added check for dt=0
 
 Revision 1.3  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy
 
 Revision 1.2  2002/09/27 15:30:31  cmarti
 add 'timeScale' (basically)
 
 Revision 1.1  2002/09/24 21:05:42  cmarti
 initial version
 
 */

package ch.unizh.ini.friend.simulation;

import java.util.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * Implements a single iteration in the simulation process.
 *This could actually be several iterations over the set of {@link #updateables }, depending on the value of
 *{@link #nIterate}.
 *
 * @author Christof Marti/tobi
 * @version $Revision: 1.10 $
 */
public class SimulationStep {
    
    /** number of times to iterate for a single step */
    protected int nIterate=4;
    
    /** @param n the number of iterations per step
     * @see #nIterate */
    public void setNumberOfIterationsPerStep(int n){
        nIterate=n;
        System.out.println("SimulationStep: set number of iterations to "+nIterate);
    }
    
    /** @return the number of iterations per step
     * @see #nIterate */
    public int getNumberOfIterationsPerStep(){
        return nIterate;
    }
    
    /** Time during previous step. */
    protected long previousTime;
    
    /** Time scale. */
    protected float timeScale = 1.0e-3f;
    
    /** The collection of updateable objects. */
    protected Collection updateables;
    
    /** The scalar 'real-time' will be multiplied with. */
    protected float speed;
    
    /** Creates a new instance of <code>SimulationStep</code> with the
     * specified <code>Collection</code> of
     * {@link Updateable} instances and a specified
     * speed multiplier.
     * @param updateables The updateable collection.
     * @param speed The speed multiplier relative to 'real-time'.
     */
    public SimulationStep(java.util.Collection updateables, float speed) {
        this.updateables = updateables;
        this.speed = speed;
    }
    
    /** (Re-)Initializes this instance at the begin of the simulation. */
    public void init() {
        previousTime = System.currentTimeMillis();
    }
    
    /** Makes all the cells first {@link Updateable#compute}, and then all the cells then {@link Updateable#update}
     * for a single simulation step.
     */
    public void step() {
        int iteration;
        for(iteration=0;iteration<nIterate;iteration++){
            
            Iterator i;
            long currentTime = System.currentTimeMillis();
            float dt = speed*(currentTime - previousTime)*timeScale;
            if(dt==0) return;  // don't update if no time has passed.  this has consequence of increased latency from tangent plane stimulation to resulting stimulation.
            
            // first compute all the new output values based on inputs
            // these inputs do not change based on simulation because they are double-buffered following update()'s
            i = updateables.iterator();
            while (i.hasNext()) {
                Object current = i.next();
                //assert current instanceof Updateable : "is not Updateable (1)";
                ((Updateable)current).compute(dt);
            }
            
            // now update the outputs to be the new outputs just computed
            i = updateables.iterator();
            while (i.hasNext()) {
                Object current = i.next();
                //assert current instanceof Updateable : "is not Updateable (2)";
                ((Updateable)current).update();
            }
            
            previousTime = currentTime;
        }
    }
    
    /** Returns the collection of <code>Updateable</code>.
     * @return The updateables.
     */
    public Collection getUpdateables() {
        return updateables;
    }
    
}
