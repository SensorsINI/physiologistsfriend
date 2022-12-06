/*
 $Id: OutputPrinter.java,v 1.10 2003/07/03 16:54:28 tobi Exp $
 

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

 
 $Log: OutputPrinter.java,v $
 Revision 1.10  2003/07/03 16:54:28  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.9  2002/11/05 15:25:23  cmarti
 move OscillationgOutput, OutputPrinter and TimePrinter from simulation to test package.

 Revision 1.8  2002/10/31 20:50:09  cmarti
 make OutputPrinter a descendant of AbstractMonitor

 Revision 1.7  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput

 Revision 1.6  2002/10/24 12:05:50  cmarti
 add GPL header

 Revision 1.5  2002/10/08 12:12:05  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.4  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.3  2002/09/16 20:44:42  cmarti
 removing parameter dt from update() in Updateable

 Revision 1.2  2002/09/16 11:20:43  cmarti
 removal of UpdateSource/Listener

 Revision 1.1  2002/09/10 20:38:51  cmarti
 intial version

 */

package ch.unizh.ini.friend.tests;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import ch.unizh.ini.friend.simulation.*;

/**
 * Prints the output value of an <code>ServesOutput</code> instance to a stream.
 * @author Christof Marti
 * @version $Revision: 1.10 $
 */
public class OutputPrinter extends AbstractMonitor {
    
    /**
     * The string to prepend to the ouput.
     */
    protected String pre;
    
    /**
     * The string to append to the ouput.
     */
    protected String post;
    
    /**
     * The stream to print to.
     */
    protected PrintStream stream;
    
    /**
     * Initializes the instance to prepend a given string and append
     * another given string on output on the given stream.
     * @param inputs A collection of inputs.
     * @param pre The string to prepend.
     * @param post The string to append.
     * @param stream The stream to write to.
     */
    public OutputPrinter(Collection inputs, String pre, String post, PrintStream stream) {
        super(inputs);
        //assert pre != null;
        this.pre = pre;
        //assert post != null;
        this.post = post;
        //assert stream != null;
        this.stream = stream;
    }
    
    /**
     * Initializes the instance to prepend a given string and append
     * another given string on output on the given stream.
     * @param input A collection of inputs.
     * @param pre The string to prepend.
     * @param post The string to append.
     * @param stream The stream to write to.
     */
    public OutputPrinter(ServesOutput input, String pre, String post, PrintStream stream) {
        super(input);
        //assert pre != null;
        this.pre = pre;
        //assert post != null;
        this.post = post;
        //assert stream != null;
        this.stream = stream;
    }
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
        Iterator i = inputs.iterator();
        int j = 0;
        
        while (i.hasNext()) {
            Object input = i.next();
            //assert input instanceof ServesOutput;
            stream.println("[" + j + "] " + pre + ((ServesOutput)input).output() + post);
            j++;
        }
    }
    
}
