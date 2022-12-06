/*
 $Id: AbstractAcceptsInput.java,v 1.22 2003/07/07 02:44:21 tobi Exp $
 

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

 
 $Log: AbstractAcceptsInput.java,v $
 Revision 1.22  2003/07/07 02:44:21  tobi

 added time constant setter/getter to hcell.  messed with javadoc.

 Revision 1.21  2002/11/08 13:46:53  cmarti
 add copyright notice to cloneObject()

 Revision 1.20  2002/11/07 19:40:15  cmarti
 make use of a trick to deep-clone objects...

 Revision 1.19  2002/11/07 18:48:39  cmarti
 - let AcceptsInput extend Cloneable and require clone() to be public
 - implement clone()
 - add static methods getCollectionInstance(), connectOneToOne() and connectOneToAll()

 Revision 1.18  2002/11/05 19:46:30  cmarti
 add integrateInputs()

 Revision 1.17  2002/10/31 22:47:28  cmarti
 - remove Monitor, use AcceptsInput instead
 - rename get-/setMonitoredInput() in AbstractMonitor to get-/setInput()
 - move both methods up into AbstractAcceptsInput and add them to AcceptsInput
 - adapt other classes to these changes

 Revision 1.16  2002/10/31 21:13:26  cmarti
 - AbstractMonitor and AbstractAcceptsInputServesOutput implement Updateable
 - AbstractAcceptsInput no longer implements Updateable

 Revision 1.15  2002/10/31 20:34:39  cmarti
 correct typo in javadoc

 Revision 1.14  2002/10/29 12:00:37  cmarti
 move empty update() from AbstractWeightedInputServesOutput up into AbstractAcceptsInput (was thinko in previous commit)

 Revision 1.13  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput

 Revision 1.12  2002/10/25 09:52:37  cmarti
 - AcceptsInput and Monitor don't implement Updateable anymore
 - Compile fixes in ManyInputs and FriendGUI

 Revision 1.11  2002/10/25 08:24:59  cmarti
 remove 'compliant' cruft

 Revision 1.10  2002/10/25 08:13:49  cmarti
 - make Photoreceptor extend ManyInputs (was: implements AcceptsInput)
 - provide more generic constructor in ManyInputs

 Revision 1.9  2002/10/24 12:05:49  cmarti
 add GPL header

 Revision 1.8  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.7  2002/10/05 17:36:04  tobi
 clarified assert

 Revision 1.6  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.5  2002/09/24 20:41:25  cmarti
 refinement of the 'compliant' methods invocation (solves problem with Constructor chaining)

 Revision 1.4  2002/09/17 12:53:53  cmarti
 added AcceptsInput as an intermediate interface between Updateable and ManyInputs

 Revision 1.3  2002/09/16 11:20:43  cmarti
 removal of UpdateSource/Listener

 Revision 1.2  2002/09/13 08:39:04  cmarti
 adaption to the splitting of Updateable into Updateable and UpdateSource

 Revision 1.1  2002/09/10 20:39:09  cmarti
 intial version

 */

package ch.unizh.ini.friend.simulation;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Provides the infrastructure for a simulation component with many inputs.
 * @author Christof Marti
 * @version $Revision: 1.22 $
 */
public abstract class AbstractAcceptsInput implements AcceptsInput {
    
    /** The input components for this one. */
    public Collection inputs;
    
    /** Creates a new instance with currently no inputs. */
    public AbstractAcceptsInput() {
        inputs = new ArrayList();
    }

    /** Creates a new instance with currently no inputs and
     * with the given initial capacity.
     * @param n Number of initial capacity for inputs.
     */
    public AbstractAcceptsInput(int n) {
        inputs = new ArrayList(n);
    }

    /** Creates a new instance with the given collection of inputs.
     * @param inputs The collection of inputs.
     */
    public AbstractAcceptsInput(Collection inputs) {
        this.inputs = inputs;
    }
    
    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public AbstractAcceptsInput(Object input) {
        //assert input != null;
        inputs = new ArrayList(1);
        inputs.add(input);
    }
    
    /** Returns a deep-clone of the given object. The object and all objects it references directly or
     * indirectly have to implement the <code>Serializable</code> interface for this hack to work.
     * This method has been copied from the Java FAQ at <a href="http://www.afu.com/">http://www.afu.com/</a>
     * and is subject to the following conditions:
     * <h3>Copyright</h3>
     * Copyright (c), 1997,1998,1999,2000,2001 Peter van der Linden. Permission to copy all or part of this
     * work is granted for individual use, and for copies within a scholastic or academic setting. Copies
     * may not be made or distributed for resale. The no warranty, and copyright notice must be retained
     * verbatim and be displayed conspicuously. You need written authorization before you can include this
     * FAQ in a book and/or a CDROM archive, and/or make a translation, and/or publish/mirror on a website
     * (scholastic and academic use excepted). If anyone needs other permissions that aren't covered by the
     * above, please contact the author.
     * <h3>No Warranty</h3>
     * This work is provided on an "as is" basis. The copyright holder makes no warranty whatsoever, either
     * express or implied, regarding the work, including warranties with respect to merchantability or
     * fitness for any purpose. Furthermore the author has been known to wear socks that don't match his
     * pants, and to commit other egregious lapses of good fashion sense.
     * @param o The first node of the object graph to be cloned.
     * @return The corresponding node of the cloned object graph.
     */
    public static Object cloneObject(Object o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bOut);
        
        out.writeObject(o);
        
        ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bIn);
        
        return in.readObject();
    }
    
    /** Returns a collection of the given number of clones of the given object.
     * @param o The object to be cloned.
     * @param n The number of clones.
     * @return The collection of clones.
     */
    public static Collection getCollectionInstance(AcceptsInput o, int n) {
        ArrayList list = new ArrayList(n);
        for (int i = 0; i < n; i++) {
            try {
                list.add(cloneObject(o));
            }
            catch (Exception e) {
                throw new RuntimeException("failed to deep-clone");
            }
        }
        return list;
    }
    
    /** Registers the i-th <code>Object</code> from the first as an additional input of the i-th
     * <code>AcceptInput</code> from the second given collection.
     * @param source The data-'source' collection.
     * @param sink The data-'sink' collection.
     */
    public static void connectOneToOne(Collection source, Collection sink) {
        java.util.Iterator iSource = source.iterator();
        java.util.Iterator iSink = sink.iterator();
        while (iSource.hasNext() && iSink.hasNext()) {
            ((AcceptsInput)iSink.next()).getInputs().add(iSource.next());
        }
    }
    
    /** Registers the given <code>Object</code> as an additional input of each of the
     * <code>AcceptInput</code>s from the given collection.
     * @param source The data-'source' object.
     * @param sink The data-'sink' collection.
     */
    public static void connectOneToAll(Object source, Collection sink) {
        java.util.Iterator iSink = sink.iterator();
        while (iSink.hasNext()) {
            ((AcceptsInput)iSink.next()).getInputs().add(source);
        }
    }
    
    /** Returns summed inputs.
     * @return The summed inputs.
     */
    protected float integrateInputs() {
        java.util.Iterator iInputs = inputs.iterator();
        float sum = 0.0f;
        
        while (iInputs.hasNext()) {
            sum += ((ServesOutput)iInputs.next()).output();
        }
        
        return sum;
    }
    
    /** Returns averaged inputs.
     * @return The averaged inputs.
     */
    protected float averageInputs() {
        java.util.Iterator iInputs = inputs.iterator();
        float sum = 0.0f;
        
        while (iInputs.hasNext()) {
            sum += ((ServesOutput)iInputs.next()).output();
        }
        
        return sum/inputs.size();
    }
    
    /** Returns a collection of all inputs to this simulation component.
     * @return The inputs.
     */
    public Collection getInputs() {
        return inputs;
    }
    
    /** Sets the collection of all inputs to this simulation component.
     * @param inputs The inputs.
     */
    public void setInputs(Collection inputs) {
        this.inputs = inputs;
    }
    
    /** Returns the first (if any) input.
     * @return The input.
     */
    public ServesOutput getInput() {
        return inputs == null || inputs.size() <= 0 ? null : (ServesOutput)(inputs.iterator().next());
    }
    
    /** Sets the first (and only) input.
     * @param input The input.
     */
    public void setInput(ServesOutput input) {
        if (input == null) {
            inputs = null;
        } else {
            if (inputs == null) {
                inputs = new ArrayList(1);
            } else {
                inputs.clear();
            }
            inputs.add(input);
        }
    }

}
