/*
 $Id: PhotoreceptorTest.java,v 1.6 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: PhotoreceptorTest.java,v $
 Revision 1.6  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.5  2002/10/08 12:12:06  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.4  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.3  2002/09/24 04:47:17  tobi
 changed to 7 receptors for ease of understanding output.

 Revision 1.2  2002/09/23 14:54:23  tobi
 adapted to new unified stimuli

 Revision 1.1  2002/09/17 12:44:01  cmarti
 renaming PhotoReceptor to Photoreceptor...
 
 Revision 1.4  2002/09/16 08:47:58  cmarti
 adapt for new classes in friend.stimulus and friend.graphics
 
 Revision 1.3  2002/09/13 12:07:22  cmarti
 use new ConvexPolygonStimulus for stimulus representation
 
 Revision 1.2  2002/09/10 18:10:07  cmarti
 removed Compound
 
 Revision 1.1  2002/09/10 15:55:58  cmarti
 initial version
 
 */

package ch.unizh.ini.friend.tests;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.ListIterator;
import ch.unizh.ini.friend.graphics.*;
import ch.unizh.ini.friend.simulation.cells.Photoreceptor;
import ch.unizh.ini.friend.stimulus.*;
import java.util.Iterator;

/**
 * This class tests and demos the function of <code>Photoreceptor</code>.
 * Of primary interrest are the intersection and area computations and
 * the use of the photoreceptor class.
 * @author  Christof Marti
 * @version $Revision: 1.6 $
 */
public class PhotoreceptorTest extends JPanel {
    
    //    /** The shape of the stimulus. */
    //    private ConvexPolygon stimulusShape = (ConvexPolygon)ConvexPolygon.getRectangleInstance(-4.0f, -1.0f, 8.0f, 2.0f).rotate((float)Math.PI/7.0f);
    //
    //    /** The shape with wrappers. */
    //    private SeparateTransforms stimulusShapeBuffered = new SynchronizedSeparateTransforms(new ConcreteSeparateTransforms(stimulusShape));
    //
    //    /** The stimulus. */
    //    private Stimulus stimulus = new ConcreteStimulus(stimulusShapeBuffered);
    
    // the stimulus to test
    private Stimulus stimulus=new BarStimulus();
    
    /** Shapes of the photo receptors. */
    private ArrayList shapes = new ArrayList();
    
    /** Intersections between the shapes of the photoreceptors and the stimulus. */
    private ArrayList intersections = new ArrayList();
    
    /** The photoreceptors. */
    private ArrayList receptors = Photoreceptor.getHexagonalArrayListInstance(2, 0.5f, 0.01f, stimulus, shapes);
    
    /** Computes the intersections of the stimulus and the photoreceptors and displays the resulting area. */
    public PhotoreceptorTest() {
        ShapeList stimulusShapes=stimulus.getTransformedShapes();   // the shape(s) of the stimulus (1 for bar/edge, many for grating)
        ListIterator li = shapes.listIterator();                    // the list of photoreceptor shapes
        while (li.hasNext()) {
            
            Object cp = li.next();
            //assert cp instanceof ConvexPolygon;
            ShapeList intersectionsWithOnePhotorecptor=(ShapeList)stimulusShapes.intersect((ConvexPolygon)cp);
            for(Iterator i=intersectionsWithOnePhotorecptor.iterator();i.hasNext();){
                Intersectable s=(Intersectable)i.next();
                if (s != null) {
                    intersections.add(s);
                    System.out.println("fraction of photoreceptor covered " + s.area()/((ConvexPolygon)cp).area());
                }
            }
        }
    }
    
    /**
     * Paints all graphical components.
     * @param g The Graphics context.
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
        
        g2.setPaint(Color.green);
        ListIterator li = intersections.listIterator();
        while (li.hasNext()) {
            Object cp = li.next();
            //assert cp instanceof ConvexPolygon;
            g2.fill((ConvexPolygon)cp);
        }
        
        g2.setPaint(Color.black);
        li = shapes.listIterator();
        while (li.hasNext()) {
            Object cp = li.next();
            //assert cp instanceof ConvexPolygon;
            g2.draw((ConvexPolygon)cp);
        }
        
        ShapeList l=stimulus.getTransformedShapes();
        for(Iterator i=l.iterator();i.hasNext();){
            Shape s=(Shape)i.next();
            g2.draw(s);
        }
        //        g2.draw(stimulus.getTransformedShapes());
    }
    
    /**
     * Bootstraps the application.
     * @param s Command-line arguments.
     */
    public static void main(String s[]) {
        JFrame f = new JFrame("PhotoreceptorTest");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        JPanel panel = new PhotoreceptorTest();
        f.getContentPane().add("Center", panel);
        f.pack();
        f.setSize(new Dimension(500,500));
        f.show();
    }
    
}
