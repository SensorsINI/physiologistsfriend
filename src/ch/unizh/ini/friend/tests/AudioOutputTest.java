/*
 $Id: AudioOutputTest.java,v 1.3 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: AudioOutputTest.java,v $
 Revision 1.3  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.2  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy

 Revision 1.1  2002/09/29 13:50:37  cmarti
 initial version

 */

package ch.unizh.ini.friend.tests;

import java.util.ArrayList;
import java.util.ListIterator;
import java.io.PrintStream;
import ch.unizh.ini.friend.graphics.*;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.simulation.cells.*;
import ch.unizh.ini.friend.stimulus.*;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * This class tests and demos the function of <code>AudioOutput</code>.
 * @author Christof Marti
 * @version $Revision: 1.3 $
 */
public class AudioOutputTest {

    /** The simulation setup. */
    protected SimulationSetup setup = new SimulationSetup();
        
    /** The audio output. */
    protected AudioOutput audioOutput;
    
    /**
     * Initializes the simulation.
     */
    public AudioOutputTest() {
        ArrayList cells = new ArrayList();
        
        PrintStream ostream = System.out;
        //cells.add(new TimePrinter(ostream));

        ServesOutput oscillator = new OscillatingOutput();
        cells.add(oscillator);
        audioOutput = new AudioOutput(oscillator);
        cells.add(audioOutput);
        
        SimulationStep simulationStep = new SimulationStep(cells, 1.0f);
        SimulationTask simulation = new ThreadedSimulation(simulationStep);

        setup.setSimulation(simulation);

        if (setup.getSimulation() instanceof ThreadedSimulation) {
            System.out.println("Starting threaded simulation.");
        }
        if (setup.getSimulation() instanceof TimedSimulation) {
            System.out.println("Starting timed simulation.");
        }
    }    

    /**
     * Bootstraps the application.
     */
    public static void main(String s[]) {
        final AudioOutputTest test = new AudioOutputTest();
        JFrame frame = new JFrame("AudioOutputTest");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                test.setup.getSimulation().stop();
//                System.out.println("AudioOutput stats:\n" + test.audioOutput.stats());
//                byte[] recordedSamples = test.audioOutput.getRecordedSamples();
//                System.out.print("data = [ ");
//                for (int i = 0; i < recordedSamples.length; i++) {
//                    System.out.print(recordedSamples[i] + " ");
//                }
//                System.out.println("];");
                System.out.println("Stopped.");
                System.exit(0);
            }
        });
        frame.pack();
        frame.setSize(new Dimension(200,5));
        frame.show();

        test.setup.getSimulation().start();
    }

}
