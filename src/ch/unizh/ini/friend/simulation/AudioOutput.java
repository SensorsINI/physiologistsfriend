/*
 $Id: AudioOutput.java,v 1.24 2003/06/26 00:33:46 tobi Exp $
 
 
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
 
 
 $Log: AudioOutput.java,v $
 Revision 1.24  2003/06/26 00:33:46  tobi

 added simulation properties dialog and fixed simple and complex cells so that they work.
 simple cell had incomplete RF. complex cell had time constant that was too long.
 fiddled with audio input and output

 Revision 1.23  2003/06/15 19:17:31  tobi
 added capability of recording spikes from simulation or from microphone and plotting the
 corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 kind of a spike-tirggered average is possible.

 Revision 1.22  2003/06/12 06:28:45  tobi
 added microphone recording for hooking up friend chip. not fully functional yet.

 Revision 1.21  2002/10/31 22:47:28  cmarti
 - remove Monitor, use AcceptsInput instead
 - rename get-/setMonitoredInput() in AbstractMonitor to get-/setInput()
 - move both methods up into AbstractAcceptsInput and add them to AcceptsInput
 - adapt other classes to these changes
 
 Revision 1.20  2002/10/31 20:47:48  cmarti
 - make AbstractMonitor support multiple inputs
 - adapt AudioOutput and SimpleOutputMonitor (they still support only one input)
 
 Revision 1.19  2002/10/29 12:30:40  cmarti
 extend AbstractMonitor and remove implementation of the Monitor interface
 
 Revision 1.18  2002/10/29 11:25:30  cmarti
 - rename ManyInputs to AbstractAcceptsInput
 - rename WeightedInputs to AbstractWeightedInputServesOutput
 
 Revision 1.17  2002/10/29 08:52:39  cmarti
 add finalizer that closes the line
 
 Revision 1.16  2002/10/25 08:39:10  cmarti
 'extend ManyInputs' instead of 'implement AcceptsInput'
 
 Revision 1.15  2002/10/24 12:05:49  cmarti
 add GPL header
 
 Revision 1.14  2002/10/07 13:02:08  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.
 
 Revision 1.13  2002/10/06 20:09:56  tobi
 changed the spike sound to cos^2, fiddled with duration for good sound on win XP machine.
 
 Revision 1.12  2002/10/05 22:40:41  tobi
 added method to mute output
 
 Revision 1.11  2002/10/05 20:33:54  tobi
 fixed check for spiking cell to check for method instead of interface, because
 inhertied interfaces don't show up.
 
 Revision 1.10  2002/10/04 16:09:04  tobi
 removed unused inputValue assignment
 
 Revision 1.9  2002/10/04 11:38:03  tobi
 changed method name to isSpikingCell to make it more clear
 
 Revision 1.8  2002/10/04 11:37:27  tobi
 now using refection to decide if a cell is a SpikingCell, if so, playing spikes.
 if not, no audio output.
 
 Revision 1.7  2002/10/02 21:46:48  tobi
 added ExceptionDialog for when autio unavailable.
 
 Revision 1.6  2002/10/01 16:16:53  cmarti
 change package and import names to new hierarchy
 
 Revision 1.5  2002/10/01 11:28:51  cmarti
 change to digital output
 
 Revision 1.4  2002/09/30 09:48:41  tobi
 added print to say no sound will be played.
 
 Revision 1.3  2002/09/30 09:45:58  tobi
 added boolean to check if audio line available, if not, don't play audio.
 
 Revision 1.2  2002/09/29 13:52:40  cmarti
 - rewrite again
 - no additional thread
 - collect samples and put them on the line as soon as the simulation has some dt != 0
 
 Revision 1.1  2002/09/26 14:44:09  cmarti
 rewrite from SpikeSound
 
 Revision 1.5  2002/09/23 14:33:11  tobi
 changed spike sound back to simpler square wave and increased amplitude
 to maxiumum
 
 Revision 1.4  2002/09/15 08:00:35  tobi
 fiddled with spike sound to make it sound better... perhaps
 
 Revision 1.3  2002/09/13 20:50:18  tobi
 now works, makes the sound of a spike.
 
 Revision 1.2  2002/09/13 06:17:24  tobi
 initial add, not yet working nor does it even compile
 
 Revision 1.1  2002/09/12 07:30:48  tobi
 makes the sound of a spike.  not yet working,
 
 */

package ch.unizh.ini.friend.simulation;

import ch.unizh.ini.friend.gui.*;
import ch.unizh.ini.friend.simulation.cells.*;
import java.lang.reflect.*;
import java.util.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Plays the output of a <code>ServesOutput</code> on the speaker,
 * if the cell being played makes spikes. Every time there is a spike, a buffer holding a spike-like sound is placed on the audio output buffer.
 *<p>
 *An instance of this class is constructed in {@link SimulationSetupFactory} and made one of the updateables. The user uses the GUI to set 
 the monitored cell
 *
 * @author Christof Marti/Tobi Delbruck
 * @version $Revision: 1.24 $
 */

public class AudioOutput extends AbstractMonitor {
    
    SpikeSound spikeSound=null;
   
    
    private boolean muted=false;
    /** sets audio muted. */
    public void setMuted(boolean f){muted=f;}
    /** is audio muted? */
    public boolean isMuted(){return muted;}
    
    
    /** Creates a now instance with no current input. */
    public AudioOutput() {
        this(null);
    }
    
   void printLineInfo(SourceDataLine l){
       System.out.println(l);
       System.out.println("buffer size="+l.getBufferSize());
       System.out.println("format="+l.getFormat());
       System.out.println("line info="+l.getLineInfo());
   }
   
    
    /** Creates a new instance with the given input.
     * @param input The input.
     */
    public AudioOutput(ServesOutput input) {
        super(input);
//        System.out.println("LINE_BUFFER_SIZE (requesting)="+LINE_BUFFER_SIZE);
        
        spikeSound=new SpikeSound();
        
    }
    
    /** Release the line on finialization. */
    protected void finalize() throws Throwable {
//        if (line.isOpen()) line.close();
        super.finalize();
    }
    
    private Object lastInput=null;  // cache to speed up reflection
    private boolean lastSpikingCellReturn=false;  // last returned value for spiking cell type
    
    // examines object using reflection to see if it implements SpikingCell
    private boolean isSpikingCell(Object o){
        if( o!=lastInput || o==null ){ // check if object changed (different cell selected)
            lastInput=o;
            Class c=o.getClass();
            //            System.out.println("c = " + c );
            boolean is=true;
            try{
                Method m=c.getMethod("isSpike",null); // if the class has isSpike method
            }catch(NoSuchMethodException e){
                is=false;
            }
            
            lastSpikingCellReturn=is;
            return lastSpikingCellReturn;
        }else{  // use cached value
            return lastSpikingCellReturn;
        }
        
    }
    
    
    /** Computes the new state of this component of the simulation.
     * Feeds the source line with audio data.
     * @param dt The time that has passed since the last invocation.
     */
    public void compute(float dt) {
        ServesOutput input = getInput();
        if(muted) return;  // just return if no line available or we are muted

            if(isSpikingCell(input)){
                //                    inputValue = input.output();
                if (((SpikingCell)input).isSpike() ){
                    spikeSound.play();
                }
            }
            
    }
        
}
