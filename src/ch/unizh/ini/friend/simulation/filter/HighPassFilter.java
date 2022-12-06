/*
 $Id: HighPassFilter.java,v 1.4 2003/07/03 16:54:27 tobi Exp $
 

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

 
 $Log: HighPassFilter.java,v $
 Revision 1.4  2003/07/03 16:54:27  tobi
 fixed a bunch of javadoc errors.
 made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)

 made lowpass and highpass filters time constants settable.

 Revision 1.3  2002/10/24 12:05:51  cmarti
 add GPL header

 Revision 1.2  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.1  2002/09/17 13:21:37  cmarti
 intial version

 */

package ch.unizh.ini.friend.simulation.filter;

/**
 * A high-pass filter.
 * @author Christof Marti
 * @version $Revision: 1.4 $
 */
public class HighPassFilter implements Filter {
    
    /** The corresponding low-pass filter */
    protected LowPassFilter lowPass;
    
    
    /** Creates a new instance of HighPassFilter with the given time constant
     * and the given last output of the corresponding low-pass filter.
     * @param tau The time constant.
     * @param output The last output of the corresponding low-pass filter.
     */
    public HighPassFilter(float tau, float output) {
        lowPass = new LowPassFilter(tau, output);
    }
    
    /** Returns the output of the filter based on the given input and the passed time.
     * @param input The input value.
     * @param dt The time step.
     * @return The output of the filter.
     */
    public float filter(float input, float dt) {
        return input - lowPass.filter(input, dt);
    }

        /** Getter for property tau.
     * @return Value of property tau.
     *
     */
    public float getTau() {
        return lowPass.getTau();
    }
    
    /** Setter for property tau.
     * @param tau New value of property tau.
     *
     */
    public void setTau(float tau) {
        lowPass.setTau(tau);
    }

}
