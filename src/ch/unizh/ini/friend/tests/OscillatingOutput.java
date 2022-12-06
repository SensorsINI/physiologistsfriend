/*
 $Id: OscillatingOutput.java,v 1.6 2002/11/08 14:11:38 cmarti Exp $
 

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

 
 $Log: OscillatingOutput.java,v $
 Revision 1.6  2002/11/08 14:11:38  cmarti
 - move AbstractCell to package ch.unizh.ini.firend.simulation.cells
 - move AbstractSynapse to package ch.unizh.ini.firend.simulation.synapses

 Revision 1.5  2002/11/05 15:25:49  cmarti
 move OscillationgOutput, OutputPrinter and TimePrinter from simulation to test package.

 Revision 1.4  2002/10/31 21:52:20  cmarti
 extend AbstractCell

 Revision 1.3  2002/10/24 12:05:50  cmarti
 add GPL header

 Revision 1.2  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/29 13:53:58  cmarti
 initial version

 */

package ch.unizh.ini.friend.tests;

import ch.unizh.ini.friend.simulation.cells.AbstractCell;
/**
 * Simple waveform generator. Doesn't produce a constant frequency in 'real' time.
 * @author Christof Marti
 * @version $Revision: 1.6 $
 */
public class OscillatingOutput extends AbstractCell {
    
    /** Virtual time. */
    protected float vt = 0.0f;
    
    /** Virtual dt per iteration. */
    protected float vdt = (float)(2*Math.PI/500.0f);
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     *
     */
    public void compute(float dt) {
        vt += vdt;
        newValue = ((float)Math.sin(vt) + 1)/2.0f;
    }
    
}
