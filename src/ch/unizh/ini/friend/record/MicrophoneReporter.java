package ch.unizh.ini.friend.record;

import ch.unizh.ini.friend.simulation.AudioOutput;
import ch.unizh.ini.friend.simulation.SimulationSetup;
import ch.unizh.ini.friend.simulation.Updateable;
import ch.unizh.ini.friend.simulation.filter.HighPassFilter;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
/*
 * $Id: MicrophoneReporter.java,v 1.3 2003/06/26 00:33:41 tobi Exp $
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
 *
 * Created on June 8, 2003, 8:38 AM
 */

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Uses the microphone input to detect and generate {@link SpikeEvent}'s.
 * This object is a thread that reads the microphone input and generates spikes from it.
 *To use this class, make a new instance, register {@link SpikeListener}'s,
 * and {@link #startReporting} the thread. To stop reporting, use {@link #stopReporting}.
 *
<p>
 *An instance of this class is constructed  by
 *{@link ch.unizh.ini.friend.gui.FriendGUI}  
 when microphone recording is activated;  listeners are added there.
 *<p>
 *A spike is positive edge triggered by the microphone input going above the {@link #setThreshold}; 
 a new spike is not generated until the input falls below the threshold-{@link #setHystersis hystersis level}.
 *
 *

 * @author  $Author: tobi $
 *@version $Revision: 1.3 $
 */
public class MicrophoneReporter extends Thread implements SpikeReporter, Updateable {
    
    AudioFormat format;
    float readbufferRatio=.5f; 
    int internalBufferSize=(int)(AudioOutput.LINE_BUFFER_SIZE*readbufferRatio); // it has to be the same size otherwise we can't get the line
    float sampleRate=AudioOutput.SAMPLE_RATE, sampleTime=1/sampleRate;
    long threadDelay=(long)(1000*internalBufferSize*sampleTime);
    TargetDataLine targetLine;
    //    SourceDataLine sourceLine;
    int numBits=8;
    //    HighPassFilter highpass;
    
    byte threshold=10, hysteresis=3;
    boolean isSpiking=false; // flag to mark if mic input is presently above threshold
    
    /** sets the threshold for detecting spikes.
     *@param t the threshold.
     */
    public void setThreshold(byte t){threshold=t;}
    /** @return the threshold
     * @see #setThreshold
     */
    public byte getThreshold(){return threshold;}
    
    /** sets the hystersis for spike detection. Set this to avoid triggering multiple spikes on a noisy signal.
     *A new {@link SpikeEvent} can be not generated until the input drops below the {@link #getThreshold threshold}-hystersis.
     */
    public void setHystersis(byte h){hysteresis=h;}
    /** @return hysteresis
     *@see #setHystersis
     */
    public byte getHystersis(){return hysteresis;}
    
    SimulationSetup setup;  // used for registering ourselves as a cell, etc
    
    /** 
     @see #MicrophoneReporter()
     *@param setup we are added to the setup as an updateable, but our update method does nothing
     *@throws LineUnavailableException if microphone input not available
     **/
    public MicrophoneReporter(SimulationSetup setup) throws LineUnavailableException {
        this();
        this.setup=setup;
    }
     
    /** Creates a new instance of SpikeTest. Opens the microphone input as the target line.
     * To start the reporting, {@link #start} the thread.
     * @throws LineUnavailableException if microphone input is not available
     */
    public MicrophoneReporter() throws LineUnavailableException {
//        getAudioInfo();
//        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 8, 1, 1, sampleRate, false);
        //format=new AudioFormat(sampleRate, numBits, 1, true, true);
        //        highpass=new HighPassFilter(1f, 0);
        
        //        Mixer.Info[] mixers=AudioSystem.getMixerInfo();
        //        for(int i=0;i<mixers.length;i++){
        //            System.out.println(mixers[i]);
        //        }
        //
        //        Line.Info[] lines=AudioSystem.getTargetLineInfo(new Line.Info(TargetDataLine.class));
        //        for(int i=0;i<lines.length;i++){
        //            System.out.println(lines[i]);
        //       }
        //
        //
        //        // ports are not supported in javax.sound before 1.4.2 and not yet in 1.4.2 in linux
        //        if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
        //            try {
        //                Port line = (Port) AudioSystem.getLine(Port.Info.MICROPHONE);
        //                System.out.println("mic line="+line);
        //            }catch(LineUnavailableException e){
        //                System.out.println(e);
        //            }
        //        }else{
        //            System.out.println("no microphone port supported");
        //        }
        
        // we use the audiooutput format here to make the source and target( microphone) the same format to make sure they are compatible.
        // if not, microphone won't open.
        DataLine.Info	targetInfo = new DataLine.Info(TargetDataLine.class, AudioOutput.audioFormat, AudioOutput.LINE_BUFFER_SIZE);
        //        DataLine.Info	sourceInfo = new DataLine.Info(SourceDataLine.class, format, internalBufferSize);
        //        try{
        
        targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
        //        sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
        targetLine.open(AudioOutput.audioFormat, AudioOutput.LINE_BUFFER_SIZE);
        System.out.println(targetLine+" opened");
        //        sourceLine.open(format, internalBufferSize);
        
        //        }catch(LineUnavailableException e){
        //            e.printStackTrace();
        //        }
    }
    
    /** starts acquisition from microphone port and generation of {@link SpikeEvent}'s. If line is not available it does nothing. */
    public void startReporting(){
        stop=false;
        if(targetLine!=null) targetLine.start(); else return;
        super.start();
    }
    
    private boolean stop=false;
    
    /** removes all spike event listeners, ends thread after first stopping microphone acquisition. This ends generation of {@link SpikeEvent}'s */
    public void stopReporting(){
        clearSpikeListeners();
        stop=true;
    }
    
    /** grabs samples from microphone input and generates {@link SpikeEvent}'s whenver spikes are detected.
     * Stopped by {@link #stopReporting}
     */
    public void run(){
        byte[] buffer=new byte[(int)(internalBufferSize)];
        float val=0f;
        byte max=Byte.MIN_VALUE, min=Byte.MAX_VALUE;
        System.out.println("mic recorder: started");
        while(!stop){
            if(listeners.size()==0) {
                try{Thread.currentThread().sleep(100);}catch(InterruptedException e){};
//                System.out.println("mic recorder: no listeners");
                continue;
            }// don't even bother unless someone is listening
            
            if(targetLine.available()<buffer.length){ // don't process until the buffer has enough data for our external buffer
//                System.out.println("mic recorder: yielding (only "+targetLine.available()+"/"+buffer.length+" available)");
                try{
                    sleep(threadDelay);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                continue;
            }
            
            long lineTime=targetLine.getMicrosecondPosition();
            int nRead=targetLine.read(buffer,0, buffer.length);
//            System.out.println("mic recorder: read "+nRead+" bytes");
            for(int i=0;i<nRead;i++){
                byte b=buffer[i];
                if(!isSpiking){
                    if(b>threshold){
                        isSpiking=true;
//                        System.out.println("mic recorder: spike");
                        informListeners(new SpikeEvent(this,(long)(lineTime/1000+i*sampleTime*1000)));
                    }
                }else{ // spiking
                    if(b<threshold-hysteresis){
                        isSpiking=false;
                    }
                }
                //                val=highpass.filter(b, sampleTime);
                //                max=(byte)(val>max?val:max);
                //                min=(byte)(val<min?val:min);
            }
            //            System.out.println(min+"\t"+max);
            try{
                sleep(threadDelay);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        targetLine.stop();
//        System.out.println("mic recorder: ended");
    }
    
    void informListeners(SpikeEvent e){
        Iterator i=listeners.iterator();
        while(i.hasNext()){
            SpikeListener l=(SpikeListener)i.next();
            l.spikeOccurred(e);
        }
        
    }
    
    /** Release the line on finialization. */
    protected void finalize() throws Throwable {
        if (targetLine.isOpen()) targetLine.close();
        super.finalize();
    }
    
    
    LinkedList listeners=new LinkedList();
    
    /** add a listener for all spikes. Listeners are {@link SpikeListener#spikeOccurred called} when a spike occurs and are passed a {@link SpikeEvent}.
     *@param listener the listener
     */
    public void addSpikeListener(SpikeListener listener){
        listeners.add(listener);
    }
    
    /** removes a listener
     *@param listener to remove
     */
    public void removeSpikeListener(SpikeListener listener){
        listeners.remove(listener);
    }
    
    /** remove all listeners */
    public void clearSpikeListeners(){
        listeners.clear();
    }
    
    /** test class by just printing . when it gets spikes */
    public static void main(String[] args){
        try{
            SpikeReporter reporter=new MicrophoneReporter();
            reporter.addSpikeListener(new SpikePrinter());
            reporter.startReporting();
        }  catch(LineUnavailableException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /** @return the Collection of listeners  */
    public Collection getSpikeListeners() {
        return listeners;
    }
    
    // prints some audio system information
    void getAudioInfo(){
        
        Mixer.Info[]	mixerInfos = AudioSystem.getMixerInfo();
        System.out.println(mixerInfos.length+" mixers");
        for (int i = 0; i < mixerInfos.length; i++) {
            
            Mixer	mixer = AudioSystem.getMixer(mixerInfos[i]);
            System.out.println("Mixer "+mixer);
            // target data lines
            Line.Info[]	lineInfos = mixer.getTargetLineInfo();
            System.out.println("\t"+lineInfos.length+" lineInfos");
            for (int j = 0; j < lineInfos.length; j++) {
                AudioFormat[] formats=((DataLine.Info)lineInfos[j]).getFormats();
                System.out.println("\t\t\t"+formats.length+" formats");
                for(int k=0;k<formats.length;k++){
                    System.out.println("\t\tFormat "+formats[k]);
                }
                Line	line = null;
                try {
                    line = mixer.getLine(lineInfos[j]);
                    System.out.println("\tLine "+line);
                }
                catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /** @return whether reporting is enabled ({@link java.lang.Thread#isAlive})  */
    public boolean isReporting() {
        return isAlive();
    }    
    
    /** Computes the new state of this component of the simulation.
     * @param dt The time that has passed since the last invocation.
     *
     */
    public void compute(float dt) {
        // does nothing
    }
    
    /** Updates the actual state to the newly computed - aka double-buffering.
     *
     */
    public void update() {
        // does nothing
    }
    
}

/*
 *$Log: MicrophoneReporter.java,v $
 *Revision 1.3  2003/06/26 00:33:41  tobi
 *
 *added simulation properties dialog and fixed simple and complex cells so that they work.
 *simple cell had incomplete RF. complex cell had time constant that was too long.
 *fiddled with audio input and output
 *
 *Revision 1.2  2003/06/16 07:46:27  tobi
 *fixed javadoc
 *
 *added target to build windows installer
 *
 *Revision 1.1  2003/06/15 19:17:31  tobi
 *added capability of recording spikes from simulation or from microphone and plotting the
 *corresponding locations of the stimulus when the spikes occur on an underlying image plane.
 *kind of a spike-tirggered average is possible.
 *
 *Revision 1.1  2003/06/12 06:28:47  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
