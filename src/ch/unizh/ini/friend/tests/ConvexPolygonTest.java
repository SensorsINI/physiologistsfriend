/*
 $Id: ConvexPolygonTest.java,v 1.7 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: ConvexPolygonTest.java,v $
 Revision 1.7  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.6  2002/10/08 12:12:06  tobi
 commented all assertions, commented Preferences, Logger, and 1.4+ swing methods
 for 1.3 compliance.

 Revision 1.5  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.4  2002/09/16 09:11:46  cmarti
 adapt for new classes in friend.graphics

 Revision 1.3  2002/09/10 15:58:23  cmarti
 added revision tag

 Revision 1.2  2002/09/07 14:48:25  cmarti
 add various special cases of intersections

 Revision 1.1  2002/09/03 20:44:05  tobi
 initial (re)add to move files to correct hierarchy

 */

package ch.unizh.ini.friend.tests;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.ListIterator;
import ch.unizh.ini.friend.graphics.ConvexPolygon;

/**
 * This class tests and demos the function of <coe>ConvexPolygon</code>.
 * Of primariy interrest is the algorithm for intersecting two convex polygons.
 *
 * @author Christof Marti
 * @version $Revision: 1.7 $
 */
public class ConvexPolygonTest extends JPanel {

    /**
     * List of shapes to draw.
     */
    private ArrayList shapes = new ArrayList();
    
    /**
     * List of intersections to fill.
     */
    private ArrayList intersections = new ArrayList();
    
    /**
     * Width of the visible area.
     */
    private float width = 8.0f;
    
    /**
     * Height of the visible area.
     */
    private float height = 8.0f;

    /**
     * Adds two shapes and the shape of their intersection to the lists.
     * @param cp1 First shape.
     * @param cp2 Second shape.
     * @param icp Their intersection.
     */
    private void addTriple(ConvexPolygon cp1, ConvexPolygon cp2, ConvexPolygon icp) {
        float dx = shapes.size()%((int)width) - ((int)width - 2)/2;
        float dy = 2*(shapes.size()/((int)height)) - ((int)height - 2)/2;
        shapes.add(cp1.translate(dx, -dy));
        shapes.add(cp2.translate(dx, -dy));
        if (icp != null) {
            intersections.add(icp.translate(dx, -dy));
        }
    }
    
    /**
     * Initializes the object. Basically creates various cases of intersecting convex polygons.
     */
    public ConvexPolygonTest() {
        ConvexPolygon cp1, cp2, icp;
        
        System.out.println("The 'normal' case.");
        cp1 = ConvexPolygon.getRectangleInstance(-0.5f, -0.25f, 1.0f, 0.5f);
        cp2 = (ConvexPolygon)ConvexPolygon.getNGonInstance(6).scale(0.5f, 0.5f).translate(-0.5f, 0.0f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("A rectangle contained in a hexagon.");
        cp1 = ConvexPolygon.getRectangleInstance(-0.25f, -0.25f, 0.5f, 0.5f);
        cp2 = (ConvexPolygon)ConvexPolygon.getNGonInstance(6).scale(0.5f, 0.5f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("Hexagon and rectangle intersecting in one point (corner of the hexagon).");
        cp1 = ConvexPolygon.getRectangleInstance(0.0f, -0.25f, 0.5f, 0.5f);
        cp2 = (ConvexPolygon)ConvexPolygon.getNGonInstance(6).scale(0.5f, 0.5f).translate(-0.5f, 0.0f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("Two rectangles intersecting in one complete edge.");
        cp1 = ConvexPolygon.getRectangleInstance(-0.5f, -0.25f, 0.5f, 0.5f);
        cp2 = ConvexPolygon.getRectangleInstance(0.0f, -0.25f, 0.5f, 0.5f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("Two rectangles one contained in the other (with three edges of the inner one lying on edges of the outer).");
        cp1 = ConvexPolygon.getRectangleInstance(-0.5f, -0.25f, 1.0f, 0.5f);
        cp2 = ConvexPolygon.getRectangleInstance(-0.5f, -0.25f, 0.5f, 0.5f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("Two rectangles intersecting with co-linear edges.");
        cp1 = ConvexPolygon.getRectangleInstance(-0.5f, -0.25f, 1.0f, 0.5f);
        cp2 = ConvexPolygon.getRectangleInstance(0.0f, -0.25f, 1.0f, 0.5f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("Two irregular pentagons intersecting twice at corners");
        cp1 = new ConvexPolygon(5);
        cp1.addPoint(0.25f, 0.0f); cp1.addPoint(0.0f, 0.25f); cp1.addPoint(-0.5f, 0.25f); cp1.addPoint(-0.5f, -0.25f); cp1.addPoint(0.0f, -0.25f);
        cp2 = new ConvexPolygon(5);
        cp2.addPoint(-0.25f, 0.0f); cp2.addPoint(0.0f, -0.25f); cp2.addPoint(0.5f, -0.25f); cp2.addPoint(0.5f, 0.25f); cp2.addPoint(0.0f, 0.25f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("A rectangle contained in a rectangle with all corners intersecting with lines.");
        cp1 = ConvexPolygon.getRectangleInstance(-0.5f, -0.5f, 1.0f, 1.0f);
        cp2 = new ConvexPolygon(4);
        cp2.addPoint(0.5f, 0.0f); cp2.addPoint(0.0f, 0.5f); cp2.addPoint(-0.5f, 0.0f); cp2.addPoint(0.0f, -0.5f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("A rectangle contained in a rectangle with all corners intersecting with lines (exchanged).");
        cp1 = ConvexPolygon.getRectangleInstance(-0.5f, -0.5f, 1.0f, 1.0f);
        cp2 = new ConvexPolygon(4);
        cp2.addPoint(0.5f, 0.0f); cp2.addPoint(0.0f, 0.5f); cp2.addPoint(-0.5f, 0.0f); cp2.addPoint(0.0f, -0.5f);
        icp = (ConvexPolygon)cp1.intersect(cp2);
        addTriple(cp1, cp2, icp);

        System.out.println("Two non-intersecting rectangles.");
        cp1 = ConvexPolygon.getRectangleInstance(-0.75f, -0.25f, 0.5f, 0.5f);
        cp2 = ConvexPolygon.getRectangleInstance(0.25f, -0.25f, 0.5f, 0.5f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);

        System.out.println("Two equal rectangles.");
        cp1 = ConvexPolygon.getRectangleInstance(-0.5f, -0.5f, 1.0f, 1.0f);
        cp2 = ConvexPolygon.getRectangleInstance(-0.5f, -0.5f, 1.0f, 1.0f);
        icp = (ConvexPolygon)cp2.intersect(cp1);
        addTriple(cp1, cp2, icp);
    }
    
    /**
     * Paints all shapes and intersections to a <code>Graphics2D</code> instance.
     * @param g The graphics context.
     */
    public void paint(Graphics g) {
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
    }

    /**
     * Gets the whole thing started.
     */
    public static void main(String s[]) {
        JFrame f = new JFrame("Rourke");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        JPanel panel = new ConvexPolygonTest();
        f.getContentPane().add("Center", panel);
        f.pack();
        f.setSize(new Dimension(500,500));
        f.show();
    }

}
