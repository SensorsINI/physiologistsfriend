/*
 $Id: SimulationTest.java,v 1.15 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: SimulationTest.java,v $
 Revision 1.15  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.14  2002/10/08 12:12:06  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.13  2002/10/01 20:50:26  tobi
 no change

 Revision 1.12  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.11  2002/09/27 09:16:26  tobi
 changed cell listened to to use getOutputCell(String) call

 Revision 1.10  2002/09/26 14:44:41  cmarti
 add AudioOutput

 Revision 1.9  2002/09/24 21:29:56  cmarti
 - use SimulationSetupFactory to create a simulation setup

 Revision 1.8  2002/09/17 18:58:24  cmarti
 add usage of TimePrinter

 Revision 1.7  2002/09/17 18:44:04  cmarti
 add usage of BipolarCell

 Revision 1.6  2002/09/17 12:56:15  cmarti
 renaming AveragingCell to HorizontalCell

 Revision 1.5  2002/09/17 12:44:01  cmarti
 renaming PhotoReceptor to Photoreceptor...

 Revision 1.4  2002/09/16 11:21:44  cmarti
 removal of UpdateSource/Listener

 Revision 1.3  2002/09/16 08:48:06  cmarti
 adapt for new classes in friend.stimulus and friend.graphics

 Revision 1.2  2002/09/13 12:09:15  cmarti
 use new ConvexPolygonStimulus for stimulus representation
 minor adaption to the split-up of Updateable
 on exit, tell the simulation to stop and wait for it to do so

 Revision 1.1  2002/09/10 20:38:07  cmarti
 initial version

 */

package ch.unizh.ini.friend.tests;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.io.PrintStream;
import ch.unizh.ini.friend.graphics.*;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.simulation.cells.*;
import ch.unizh.ini.friend.stimulus.*;

/**
 * This class tests and demos the function of <code>Simulation</code>.
 * @author Christof Marti
 * @version $Revision: 1.15 $
 */
public class SimulationTest extends JPanel {

    /** The simulation setup. */
    protected SimulationSetup setup = SimulationSetupFactory.getSimulationSetup();
        
    /**
     * Initializes the intersections, initializes the simulation,
     * registers an <code>OuputPrinter</code> instance to each
     * cell and starts a new thread for the simulation.
     */
    public SimulationTest() {
        ArrayList helpers = new ArrayList();
        PrintStream ostream = System.out;

        //helpers.add(new OutputPrinter(setup.getCells(), "output: ", "", ostream));
        //helpers.add(new OutputPrinter((ServesOutput)(setup.getCells().get(setup.getCells().size() - 4)), "ONBIPOLAR.output = ", "", ostream));
        //helpers.add(new OutputPrinter((ServesOutput)(setup.getCells().get(setup.getCells().size() - 2)), "ONGANGLION.output = ", "", ostream));
        //helpers.add(new TimePrinter(ostream));
        helpers.add(new AudioOutput((ServesOutput)(setup.getOutputCell("On Bipolar Cell"))));
        
        setup.getSimulation().getStep().getUpdateables().addAll(helpers);
        
        //setup.setSimulation(new TimedSimulation(setup.getSimulation().getStep(), 0));
        setup.setSimulation(new ThreadedSimulation(setup.getSimulation().getStep()));

        if (setup.getSimulation() instanceof ThreadedSimulation) {
            System.out.println("Starting threaded simulation.");
        }
        if (setup.getSimulation() instanceof TimedSimulation) {
            System.out.println("Starting timed simulation.");
        }
        

        setup.getSimulation().start();
    }
    
    /**
     * Draws the intersection, the photoreceptor shapes and the stimulus.
     */
    public void paint(Graphics g) {
        float width = 10.0f;
        float height = 10.0f;

        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, d.width, d.height);
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(width/d.width));
        AffineTransform toScreen = AffineTransform.getScaleInstance(d.width/width, -d.height/height);
        toScreen.concatenate(AffineTransform.getTranslateInstance(width/2.0f, -height/2.0f));
        g2.setTransform(toScreen);

        g2.setPaint(Color.black);
        ListIterator li = setup.getReceptorShapes().listIterator();
        while (li.hasNext()) {
            Object cp = li.next();
            //assert cp instanceof ConvexPolygon;
            g2.draw((ConvexPolygon)cp);
        }
        
        //g2.draw(stimulusShape);
    }

    /**
     * Bootstraps the application.
     */
    public static void main(String s[]) {
        JFrame f = new JFrame("SimulationTest");
        final JPanel panel = new SimulationTest();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ((SimulationTest)panel).setup.getSimulation().stop();
                System.out.println("Stopped.");
                System.exit(0);
            }
        });
        f.getContentPane().add("Center", panel);
        f.pack();
        f.setSize(new Dimension(500,500));
        f.show();
    }

}
