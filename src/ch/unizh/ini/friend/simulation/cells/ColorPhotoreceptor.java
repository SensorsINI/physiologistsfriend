/*
 * $Id: ColorPhotoreceptor.java,v 1.2 2003/05/10 17:27:44 jgyger Exp $
 *
 *
 * Copyright 2003 Institute of Neuroinformatics, 
 * University and ETH Zurich, Switzerland
 *
 * This file is part of The Physiologist's Friend.
 *
 * The Physiologist's Friend is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * The Physiologist's Friend is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The Physiologist's Friend; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ch.unizh.ini.friend.simulation.cells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ch.unizh.ini.friend.graphics.ConvexPolygon;
import ch.unizh.ini.friend.graphics.Intersectable;
import ch.unizh.ini.friend.graphics.ShapeList;
import ch.unizh.ini.friend.graphics.Transformable;
import ch.unizh.ini.friend.stimulus.ColorStimulus;
import ch.unizh.ini.friend.topology.RetinotopicLocation;

/**
 * A color-selective photoreceptor. The receptor is either sensitive to L, M, 
 * or S stimuli depending on it's cone type.
 * 
 * @author Johann Gyger
 * @version $Revision: 1.2 $
 */
public class ColorPhotoreceptor extends Photoreceptor {

    /** Red selective (L cone) photoreceptor type. */
    public static final int L_CONE = 0;

    /** Green selective (M cone) photoreceptor type. */
    public static final int M_CONE = 1;

    /** Blue selective (S cone) photoreceptor type. */
    public static final int S_CONE = 2;

    /** Total number of cone types. */
    public static final int NUM_CONES = 3;

    private final int coneType;

    /**
     * Creates a new color-selective photoreceptor.
     * 
     * @param shape the shape of this photoreceptor
     * @param stimulus the stimulus used to stimulate this photoreceptor
     * @param type the cone type.
     */
    public ColorPhotoreceptor(
        ConvexPolygon shape,
        ColorStimulus stimulus,
        int type) {
        super(shape, stimulus);
        coneType = type;
    }

    /**
     * Overridden to support color-specific excitation.
     * 
     * @return the excitation
     */
    public float excitation() {
        ColorStimulus cs = (ColorStimulus) stimulus;
        float fg, bg;
        switch (coneType) {
            case ColorPhotoreceptor.L_CONE :
                fg = cs.getLMSForeground().getL();
                bg = cs.getLMSBackground().getL();
                break;
            case ColorPhotoreceptor.M_CONE :
                fg = cs.getLMSForeground().getM();
                bg = cs.getLMSBackground().getM();
                break;
            case ColorPhotoreceptor.S_CONE :
                fg = cs.getLMSForeground().getS();
                bg = cs.getLMSBackground().getS();
                break;
            default :
                String msg = "ColorStimulus#excitation: invalid cone type";
                throw new RuntimeException(msg);
        }

        float receptorArea = shape.area();

        ShapeList transformedShapes = stimulus.getTransformedShapes();
        Intersectable intersection = transformedShapes.intersect(shape);
        float interArea = intersection.area();

        float excit = fg * interArea + bg * (receptorArea - interArea);
        excit = excit / receptorArea;

        return excit;
    }

    /* (non-Javadoc)
     * This method does pretty the same as Photoreceptor#addHexCol. 
     * Instead of adding only one Photoreceptor it simply creates 
     * a ColorPhotoreceptor for each cone type.
     */
    private static void addHexCol(
        ArrayList[] prs,
        float x,
        float y,
        float r,
        float dy,
        int n,
        ColorStimulus stimulus,
        Collection shapes) {
        for (int i = 0; i < n; i++, y += dy) {
            ConvexPolygon cp = ConvexPolygon.getNGonInstance(x, y, r, 6);
            for (int j = 0; j < NUM_CONES; j++)
                prs[j].add(new ColorPhotoreceptor(cp, stimulus, j));
            if (shapes != null) {
                boolean res = shapes.add(cp);
                //assert res;
            }
        }
    }

    /**
     * This method does almost the same as 
     * Photoreceptor#getHexagonalArrayListInstance. But instead of returning
     * an ArrayList it returns an array of ArrayLists, one for each cone type.
     * For each shape, a color-selective photoreceptor of each cone type is 
     * created which means that they overlap.
     * 
     * @param a the number of shapes making one side 
     * @param r the radius of the enclosing circle arround one photoreceptor
     * @param spacing the additional distance between the photoreceptors
     * @param stimulus the stimulus to attach to the photoreceptors
     * @param shapes a collection the shapes of the photoreceptors are added to
     * @return an array of lists with color-selective photoreceptors
     */
    public static ArrayList[] getHexagonalArrayLists(
        int a,
        float r,
        float spacing,
        ColorStimulus stimulus,
        Collection shapes) {
        //assert a > 0;
        //assert r > 0.0f;

        int n = 1 + 3 * a * (a - 1);
        ArrayList[] al = new ArrayList[NUM_CONES];
        for (int i = 0; i < NUM_CONES; i++)
            al[i] = new ArrayList(n);

        float dx = (2.0f * r + spacing) * (float) Math.cos(Math.PI / 6.0);
        float dy = 2 * r + spacing;
        addHexCol(al, 0.0f, - (a - 1) * dy, r, dy, 2 * a - 1, stimulus, shapes);
        for (int i = 1; i < a; i++) {
            float x = i * dx;
            float y = - (a - 1 - i / 2.0f) * dy;
            int n2 = 2 * a - 1 - i;
            addHexCol(al, -x, y, r, dy, n2, stimulus, shapes);
            addHexCol(al, x, y, r, dy, n2, stimulus, shapes);
        }

        // why is the array rotated here?
        // it is rotated to give arrangement with x axis as principal
        Iterator li = shapes.iterator();
        while (li.hasNext()) {
            Object current = li.next();
            //assert current instanceof Transformable;
             ((Transformable) current).rotate((float) Math.PI / 2.0f);
        }

        // we must fix up the locations of the photoreceptors since they have 
        // been transformed...
        for (int i = 0; i < NUM_CONES; i++) {
            li = al[i].iterator();
            while (li.hasNext()) {
                Photoreceptor p = (Photoreceptor) li.next();
                p.setRetinotopicLocation(
                    new RetinotopicLocation(p.shape.getCenter()));
            }
        }

        return al;
    }

}
