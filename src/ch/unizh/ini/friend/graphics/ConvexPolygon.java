/*
 $Id: ConvexPolygon.java,v 1.10 2002/10/24 12:05:48 cmarti Exp $
 

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

 
 $Log: ConvexPolygon.java,v $
 Revision 1.10  2002/10/24 12:05:48  cmarti
 add GPL header

 Revision 1.9  2002/10/07 13:02:07  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.

 Revision 1.8  2002/10/05 17:34:11  tobi
 added some clarifying javadoc

 Revision 1.7  2002/10/01 20:20:43  tobi
 added method getCenter to return the Point2D center of the polygon.,
 this is used to determine the RetinotopicLocation of the photoreceptor.

 Revision 1.6  2002/10/01 16:16:52  cmarti
 change package and import names to new hierarchy

 Revision 1.5  2002/09/16 08:32:05  cmarti
 - 'extends AbstractTransform implements Intersectable, Shape' instead of 'implements Shape, Cloneable' (Cloneable is now extended by Transformable)
 - adapt intersect() signature to use Intersectable
 - remove translate(), rotate(), scale() methods now using the default implementation from AbstractTransformable

 Revision 1.4  2002/09/10 15:59:23  cmarti
 added revision tag

 Revision 1.3  2002/09/07 14:47:49  cmarti
 correct package name

 Revision 1.2  2002/09/07 14:24:20  cmarti
 - handle special cases of intersection
 - add area function
 - add transformation functions (translate, rotate, scale)

 Revision 1.1  2002/09/03 20:44:05  tobi
 initial (re)add to move files to correct hierarchy

 */

package ch.unizh.ini.friend.graphics;

import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;

/**
 * This class implements the notion of a convex polygon. It ensures that
 * the polygon it represents is convex, no three vertices are co-linear
 * and the vertices are in counterclockwise order. The implementaion of the
 * intersection algorithm is based on a note from J. O'Rourke et al.,
 * Computer Graphics and Image Processing 19, 384-391 (1982).
 <p>
 The {@link #points} are stored as linear array are x,y pairs of coordinates.
 * @author Christof Marti
 * @version $Revision: 1.10 $
 */
public class ConvexPolygon extends AbstractTransformable implements Intersectable, Shape {
    
    /**
     * The number of vertices (<code>xpoints</code> and <code>ypoints</code>
     * may have more elements than vertices present).
     */
    public int npoints;
    
    /**
     * The coordinates of the vertices, as x,y pairs.
     */
    public float[] points;
    
    /**
     * Initializes <code>xpoints</code> and <code>ypoints</code> to three elements.
     */
    public ConvexPolygon() {
        this(3);
    }
    
    /** Initializes <code>xpoints</code> and <code>ypoints</code> to <i>n</i> elements.
     * @param n The number of vertices that initially can be stored with this polygon.
     */
    public ConvexPolygon(int n) {
        npoints = 0;
        points = new float[2*n + 2]; //one spare position for the intersection algorithm.
    }
    
    /**
     * Ensures that the represented polygon is convex, no three vertices
     * are co-linear and the vertices are in counterclockwise order. If
     * they do not satisfy this <code>npoints</code> will be set to
     * two.
     * @param points The x,y-coordinates of the vertices.
     * @param npoints The number of initial vertices.
     */
    public ConvexPolygon(float[] points, int npoints) {
        this.npoints = npoints;
        this.points = points;
        
        //assert isCompliant();
    }
    
    /**
     * Creates an instance of <code>ConvexPolygon</code> initalizing it
     * as a regular n-gon with the given radius of the enclosing circle
     * and the given center.
     * @param x x-coordinate of the center.
     * @param y y-coordinate of the center.
     * @param r The radius of the enclosing circle.
     * @param n The number of edges the polygon will have.
     * @return A regular n-gon.
     */
    static public ConvexPolygon getNGonInstance(float x, float y, float r, int n) {
        ConvexPolygon cp = new ConvexPolygon(n);
        double phi;
        for (int i = 0; i < n; i++) {
            phi = 2*i*Math.PI/n;
            cp.addPoint(x + r*(float)Math.cos(phi), y + r*(float)Math.sin(phi));
        };
        return cp;
    }
    
    /**
     * Creates an instance of <code>ConvexPolygon</code> initalizing it
     * as a regular n-gon with the given radius of the enclosing circle
     * and the center at (0,0).
     * @param r The radius of the enclosing circle.
     * @param n The number of edges the polygon will have.
     * @return A regular n-gon.
     */
    static public ConvexPolygon getNGonInstance(float r, int n) {
        return getNGonInstance(0.0f, 0.0f, r, n);
    }
    
    /**
     * Creates an instance of <code>ConvexPolygon</code> initalizing it
     * as a regular n-gon with radius 1.0 of the enclosing circle and
     * the center at (0,0).
     * @param n The number of edges the polygon will have.
     * @return A regular n-gon.
     */
    static public ConvexPolygon getNGonInstance(int n) {
        return getNGonInstance(0.0f, 0.0f, 1.0f, n);
    }
    
    /**
     * Creates an instance of <code>ConvexPolygon</code> initalizing it
     * as a rectangle.
     * @param x x-coordinate of the vertex with smallest value.
     * @param y y-coordinate of the vertex with smallest value.
     * @param w Width of the rectangle.
     * @param h Height of the rectangle.
     * @return A rectangle.
     */
    static public ConvexPolygon getRectangleInstance(float x, float y, float w, float h) {
        ConvexPolygon cp = new ConvexPolygon(4);
        cp.addPoint(x, y);
        cp.addPoint(x + w, y);
        cp.addPoint(x + w, y + h);
        cp.addPoint(x, y + h);
        return cp;
    }
    
    /**
     * Determines whether the represented polygon is convex, has no three
     * co-linear vertices and the vertices are counterclockwise ordered.
     * @return True iff the instance is compliant to these rules.
     */
    public boolean isCompliant() {
        if (npoints >= 3) {
            for (int i = 4; i < 2*npoints; i += 2) {
                if (signedArea(points[i - 4], points[i - 3], points[i - 2], points[i - 1], points[i], points[i + 1]) <= 0) {
                    return false;
                }
            }
            if (signedArea(points[2*npoints - 4], points[2*npoints - 3], points[2*npoints - 2], points[2*npoints - 1],
                           points[0], points[1]) <= 0 ||
                signedArea(points[2*npoints - 2], points[2*npoints - 1], points[0], points[1], points[2], points[3]) <= 0) {
                return false;
            }
        }
        if (npoints == 2 && points[0] == points[2] && points[1] == points[3]) {
            return false;
        }
        return true;
    }

    /**
     * Calculates the twice the signed area of the  defined by the given
     * three vertices p1, p2 and p3, where the sign is positive iff
     * (p1p2p3) form a counterclockwise cycle.
     * @param x1 x-coordinate of the first vertex.
     * @param y1 y-coordinate of the first vertex.
     * @param x2 x-coordinate of the second vertex.
     * @param y2 y-coordinate of the second vertex.
     * @param x3 x-coordinate of the third vertex.
     * @param y3 y-coordinate of the third vertex.
     * @return Twice the signed area of the triangle definded by the given vertices.
     */
    protected float signedArea(float x1, float y1, float x2, float y2, float x3, float y3) {
        return x1*(y2 - y3) - x2*(y1 - y3) + x3*(y1 - y2);
    }
    
     /**
     * Determines whether the polygon obtained by adding the given point <i>(x,y)</i> at the end
     * would still be convex, have no three co-linear vertices and have the vertices in
     * counterclockwise order.
     */
    public boolean isCompliant(float x, float y) {
        //System.out.println("npoints = " + npoints);
        return npoints == 0 || (npoints == 1 && (points[0] != x || points[1] != y)) || (npoints >= 2 &&
            isStrictlyInHalfPlane(points[2*npoints - 4], points[2*npoints - 3], 
                                   points[2*npoints - 2], points[2*npoints - 1], x, y) &&
            isStrictlyInHalfPlane(points[2*npoints - 2], points[2*npoints - 1],
                                  x, y, points[0], points[1]) &&
            isStrictlyInHalfPlane(x, y, points[0], points[1], points[2], points[3]));
    }

    /**
     * Adds the given vertex at the end of the allready added vertices iff the resulting
     * polygon is still convex, no three vertices co-linear and in counterclockwise order.
     * @param x x-coordinate of the vertex to add.
     * @param y y-coordinate of the vertex to add.
     */
    public void addPoint(float x, float y) {
        //System.out.println("x = " + x + "; y = " + y);
        //assert isCompliant(x, y);
        
        if (2*npoints == points.length) {
            float[] tmp = points;
            points = new float[4*npoints];
            System.arraycopy(tmp, 0, points, 0, 2*npoints);
        }
        
        points[2*npoints] = x;
        points[2*npoints + 1] = y;
        npoints++;
    }
    
    /**
     * Adds the given vertex at the end of the allready added vertices. The
     * number of vertices <code>npoints</code> will <b>not</b> be increased
     * and no checks regarding the conformance are made.
     * @param x x-coordinate of the vertex to add.
     * @param y y-coordinate of the vertex to add.
     */
    protected void addPointSilently(float x, float y) {
        if (2*npoints == points.length) {
            float[] tmp = points;
            points = new float[4*npoints];
            System.arraycopy(tmp, 0, points, 0, 2*npoints);
        }
        
        points[2*npoints] = x;
        points[2*npoints + 1] = y;
    }
    
    /**
     * Determines whether the given rectangle is completely contained
     * by this instance of <code>ConvexPolygon</code>.
     * @param rectangle2D The rectangle to check.
     * @return true iff the given rectangle is contained by the polygon.
     */
    public boolean contains(Rectangle2D rectangle2D) {
        int type;
        double[] coords = new double[6];
        PathIterator pi = rectangle2D.getPathIterator(null);
        while (!pi.isDone()) {
            type = pi.currentSegment(coords);
            if ((type == pi.SEG_LINETO || type == pi.SEG_MOVETO) && !contains(coords[0], coords[1])) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Determines whether the given point is contained by this instance
     * of <code>ConvexPolygon</code>.
     * @param point2D The point to check.
     * @return true iff the given point is contained by the polygon.
     */
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }
    
    /**
     * Determines whether the given point <i>(x, y)</i> is contained by this
     * instance of <code>ConvexPolygon</code>.
     * @param x x-coordinate of the point to check.
     * @param y y-coordinate of the point to check.
     * @return True iff the point is contained in the polygon.
     */
    public boolean contains(double x, double y) {
        if (npoints < 3) {
            return false;
        }
        
        for (int i = 2; i < 2*npoints; i += 2) {
            if (!isInHalfPlane(points[i - 2], points[i - 1], points[i], points[i + 1],
                               (float)x, (float)y)) {
                return false;
            }
        }
        return isInHalfPlane(points[2*npoints - 2], points[2*npoints - 2], points[0], points[0],
                             (float)x, (float)y);
    }
    
    /**
     * Determines whether the given rectangle is completely contained
     * by this instance of <code>ConvexPolygon</code>.
     * @param x x-coordinate of the vertex with the smallest value.
     * @param y y-coordinate of the vertex with the smallest value.
     * @param w Width of the rectangle.
     * @param h Height of the rectangle.
     * @return True iff the given rectangle is contained by the polygon.
     */
    public boolean contains(double x, double y, double w, double h) {
        return contains(x, y) && contains(x + w, y) && contains(x, y + h) && contains(x + w, y + h);
    }
    
    /**
     * Computes a bounding box around this instance of <code>ConvexPolygon</code>.
     * @return The bounding box as an instance of <code>Rectangle</code>.
     */
    public Rectangle getBounds() {
        int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;
        
        for (int i = 0; i < 2*npoints; i += 2) {
            minx = Math.min(minx, (int)Math.floor(points[i]));
            miny = Math.min(miny, (int)Math.floor(points[i + 1]));
            maxx = Math.max(maxx, (int)Math.ceil(points[i]));
            maxy = Math.max(maxy, (int)Math.ceil(points[i + 1]));
        }
        
        return new Rectangle(minx, miny, maxx - minx, maxy - miny);
    }
    
    /**
     * Computes a bounding box around this instance of <code>ConvexPolygon</code>.
     * @return The bounding box as an instance of <code>Rectangle2D</code>.
     */
    public Rectangle2D getBounds2D() {
        float minx = Float.POSITIVE_INFINITY, miny = Float.POSITIVE_INFINITY;
        float maxx = Float.NEGATIVE_INFINITY, maxy = Float.NEGATIVE_INFINITY;
        
        for (int i = 0; i < 2*npoints; i += 2) {
            minx = Math.min(minx, points[i]);
            miny = Math.min(miny, points[i + 1]);
            maxx = Math.max(maxx, points[i]);
            maxy = Math.max(maxy, points[i + 1]);
        }
        
        return new Rectangle2D.Float(minx, miny, maxx - minx, maxy - miny);
    }
    
    /**
     Computes the center of this <code>ConvexPolygon</code>.  
     This is the average location of all the points the make up the polygon.
     @return the center point. 
     */
    public Point2D getCenter(){
//        Rectangle bound=getBounds();
//        float midX=bound.x+bound.width/2;
//        float midY=bound.y+bound.height/2;
        float sumX=0, sumY=0;
        for(int i=0;i<npoints;i++){
            sumX+=points[2*i]; 
            sumY+=points[2*i+1];
        }
        float midX=sumX/npoints, midY=sumY/npoints;
        Point2D p=new Point2D.Float(midX, midY);
//        System.out.println("getCenter()="+p+" of "+this);
        return p;
    }
    
    /**
     * Returns a <code>PathIterator</code> for this polygon, applying the
     * transform if not <code>null</code>.
     * @see <code>PathIterator</code>
     * @param at The transform to apply.
     * @return The <code>PathIterator</code>
     */
    public PathIterator getPathIterator(AffineTransform at) {
        if (at == null) {
            return new ConvexPolygonIterator(this);
        }
        
        ConvexPolygon cp = new ConvexPolygon(npoints);
        at.transform(points, 0, cp.points, 0, npoints);
        cp.npoints = npoints;
        //assert cp.isCompliant(); Orientation changes during rendering!!!!!!
        return new ConvexPolygonIterator(cp);
    }
    
    /**
     * Returns a <code>PathIterator</code> for this polygon, applying the
     * transform if not <code>null</code>.
     * @see <code>PathIterator</code>
     * @param at The transform to apply.
     * @param flatness Has no meaning because the polygon is flat by nature.
     * @return The <code>PathIterator</code>
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }
    
    /**
     * Clones the polygon by allocating a new polygon and copying the array of coordinates.
     */
    public Object clone() {
        ConvexPolygon cp = new ConvexPolygon(npoints);
        System.arraycopy(points, 0, cp.points, 0, 2*npoints);
        cp.npoints = npoints;
        return cp;
    }
    
    /**
     * Determines whether the given rectangle intersects with this polygon.
     * @param rect The rectangle to check against.
     * @return True iff they intersect.
     */
    public boolean intersects(Rectangle2D rect) {
        return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
    
    /**
     * Determines whether the given rectangle intersects with this polygon.
     * @param x x-coordinate of the vertex with the smallest value.
     * @param y y-coordinate of the vertex with the smallest value.
     * @param w width of the rectangle.
     * @param h height of the rectangle.
     * @return True iff they intersect.
     */
    public boolean intersects(double x, double y, double w, double h) {
        ConvexPolygon cp = new ConvexPolygon(4);
        cp.addPoint((float)x, (float)y);
        cp.addPoint((float)(x + w), (float)y);
        cp.addPoint((float)x, (float)(y + h));
        cp.addPoint((float)(x + w), (float)(y + h));
        return intersect(cp) != null;
    }

    /**
     * Computes the dot product of the two given vectors.
     * @param x1 x-coordinate of the first vector.
     * @param y1 y-coordinate of the first vector.
     * @param x2 x-coordinate of the second vector.
     * @param y2 y-coordinate of the second vector.
     * @return The dot product of the two given vectors.
     */
    protected float dotProd(float x1, float y1, float x2, float y2) {
        return x1*x2 + y1*y2;
    }

    /**
     * Determines whether <i>(x,y)</i> is on the left, right of or on the straight line
     * defined by <i>(x1,y1)</i> and <i>(x2, y2)</i> (looking from the first to the second),
     * including the line itself.
     * @param x1 x-coordinate of the first point defining the half-plane.
     * @param y1 y-coordinate of the first point defining the half-plane.
     * @param x2 x-coordinate of the second point defining the half-plane.
     * @param y2 y-coordinate of the second point defining the half-plane.
     * @param x x-coordinate of the point to check.
     * @param y y-coordinate of the point to check.
     * @return Positive for 'left', zero for 'on', negative for 'right'.
     */
    protected float pointInHalfPlane(float x1, float y1, float x2, float y2, float x, float y) {
        return dotProd(y1-y2, x2-x1, x-x1, y-y1);
    }
    
    /**
     * Determines whether <i>(x,y)</i> is on the left of the straight line defined
     * by <i>(x1,y1)</i> and <i>(x2, y2)</i> (looking from the first to the second),
     * including the line itself.
     * @param x1 x-coordinate of the first point defining the half-plane.
     * @param y1 y-coordinate of the first point defining the half-plane.
     * @param x2 x-coordinate of the second point defining the half-plane.
     * @param y2 y-coordinate of the second point defining the half-plane.
     * @param x x-coordinate of the point to check.
     * @param y y-coordinate of the point to check.
     * @return True iff <i>(x,y)</i> is on the left of the straight line defined
     * by <i>(x1,y1)</i> and <i>(x2, y2)</i>.
     */
    protected boolean isInHalfPlane(float x1, float y1, float x2, float y2, float x, float y) {
        return dotProd(y1-y2, x2-x1, x-x1, y-y1) >= 0;
    }

    /**
     * Determines whether <i>(x,y)</i> is on the left of the straight line defined
     * by <i>(x1,y1)</i> and <i>(x2, y2)</i> (looking from the first to the second),
     * excluding the line itself.
     * @param x1 x-coordinate of the first point defining the half-plane.
     * @param y1 y-coordinate of the first point defining the half-plane.
     * @param x2 x-coordinate of the second point defining the half-plane.
     * @param y2 y-coordinate of the second point defining the half-plane.
     * @param x x-coordinate of the point to check.
     * @param y y-coordinate of the point to check.
     * @return True iff <i>(x,y)</i> is on the left of the straight line defined
     * by <i>(x1,y1)</i> and <i>(x2, y2)</i>.
     */
    protected boolean isStrictlyInHalfPlane(float x1, float y1, float x2, float y2, float x, float y) {
        return dotProd(y1-y2, x2-x1, x-x1, y-y1) > 0;
    }

    /**
     * Determines whether the two given lines intersect. Adapted
     * for special cases of the algorithm for intersecting two polygons.
     * <b>This isn't done accurately yet.</b>
     * @param x11 x-coordinate of the first point of the first line.
     * @param y11 y-coordinate of the first point of the first line.
     * @param x12 x-coordinate of the second point of the first line.
     * @param y12 y-coordinate of the second point of the first line.
     * @param x21 x-coordinate of the first point of the second line.
     * @param y21 y-coordinate of the first point of the second line.
     * @param x22 x-coordinate of the second point of the second line.
     * @param y22 y-coordinate of the second point of the second line.
     * @return True iff they intersect.
     */
    protected boolean doIntersect(float x11, float y11, float x12, float y12,
                        float x21, float y21, float x22, float y22) {
        //System.out.println("doIntersect: p11 = (" + x11 + "," + y11 + ") p12 = (" + x12 + "," + y12 + ") p21 = (" + x21 + "," + y21 + ") p22 = (" + x22 + "," + y22 + ")");
        //System.out.println("doIntersect: " + dotProd(y11-y12, x12-x11, x22-x21, y22-y21));
        if (dotProd(y11-y12, x12-x11, x22-x21, y22-y21) == 0) {
            return false;
        }
        float p11 = pointInHalfPlane(x21, y21, x22, y22, x11, y11);
        float p12 = pointInHalfPlane(x21, y21, x22, y22, x12, y12);
        float p21 = pointInHalfPlane(x11, y11, x12, y12, x21, y21);
        float p22 = pointInHalfPlane(x11, y11, x12, y12, x22, y22);
        //System.out.println("doIntersect: p11 = " + p11 + ", p12 = " + p12 + ", p21 = " + p21 + ", p22 = " + p22);
        return ((p11 >= 0 && p12 <= 0) || (p11 <= 0 && p12 >= 0)) && ((p21 >= 0 && p22 <= 0) || (p21 <= 0 && p22 >= 0));
    }
    
    /**
     * Intersects the two given lines and stores the intersection point in the given array.
     * <b>This isn't done accurately yet.</b>
     * @param x11 x-coordinate of the first point of the first line.
     * @param y11 y-coordinate of the first point of the first line.
     * @param x12 x-coordinate of the second point of the first line.
     * @param y12 y-coordinate of the second point of the first line.
     * @param x21 x-coordinate of the first point of the second line.
     * @param y21 y-coordinate of the first point of the second line.
     * @param x22 x-coordinate of the second point of the second line.
     * @param y22 y-coordinate of the second point of the second line.
     * @param coords The array the intersection point is stored in.
     */
    protected void intersect(float x11, float y11, float x12, float y12,
                   float x21, float y21, float x22, float y22,
                   float [] coords) {
        float dx1 = x12-x11, dy1 = y12-y11, dx2 = x22-x21, dy2 = y22-y21;
        //assert dx1*dy2 != dx2*dy1 : "Co-linear lines!";
        if (dx1 != 0.0f) {
            float m = dy1/dx1;
            float s = (y11 + (x21 - x11)*m - y21)/(dy2 - dx2*m);
            coords[0] = x21 + s*dx2;
            coords[1] = y21 + s*dy2;
        } else {
            float m = dy2/dx2;
            float t = (y21 + (x11 - x21)*m - y11)/(dy1 - dx1*m);
            coords[0] = x11 + t*dx1;
            coords[1] = y11 + t*dy1;
        }
    }

    /**
     * Adds a point to the polygon iff it <code>isCompliant(x, y)</code>.
     */
    protected void addPointOfIntersection(float x, float y) {
        if (isCompliant(x, y)) {
            addPoint(x, y);
        }
    }
    
    /**
     * Determines whether a given point is in the given polygon.
     * @param x x-coordinate of the  point.
     * @param y y-coordinate of the  point.
     * @param P the polygon to check against.
     * @return true iff the point is within the polygon.
     */
    protected boolean isInPolygon(float x, float y, ConvexPolygon P) {
        for (int p = 2; p < 2*P.npoints; p += 2) {
            if (!isInHalfPlane(P.points[p-2], P.points[p-1], P.points[p], P.points[p+1], x, y)) {
                return false;
            }
        }
        return isInHalfPlane(P.points[2*npoints-2], P.points[2*npoints-1], P.points[0], P.points[1], x, y);
    }
    
    /**
     * Intersects this convex polygon with n vertices with the given convex polygon with m vertices
     * and returns the intersection which is again a convex polygon with at most n+m vertices.
     * The algorithm runs in O(n + m) and is due to J. O'Rourke et al., Computer Graphics and Image
     * Processing 19, 384-391 (1982).
     * @param other The polygon to intersect with.
     * @return The intersection.
     */
    public Intersectable intersect(Intersectable other) {
        //assert other instanceof ConvexPolygon : "Unimplemented intersection algorithm";
        ConvexPolygon P = this, Q = (ConvexPolygon)other;
        //assert P.npoints >= 3;
        //assert Q.npoints >= 3;
        P.addPointSilently(P.points[0], P.points[1]);
        Q.addPointSilently(Q.points[0], Q.points[1]);
        ConvexPolygon PQ = new ConvexPolygon(P.npoints + Q.npoints);
        
        float [] coords = new float [2];
        int i = 0, p = 2, q = 2, first_p = -1, first_q = -1;
        boolean first = true, insideP = true;
        while (i <= 2*(P.npoints + Q.npoints)) {
            //System.out.println("p = " + p + ", q = " + q);
            //System.out.println("(" + P.points[p] + "," + P.points[p+1] + ") (" + Q.points[q] + "," + Q.points[q+1] + ")");
            if (doIntersect(P.points[p-2], P.points[p-1], P.points[p], P.points[p+1],
                            Q.points[q-2], Q.points[q-1], Q.points[q], Q.points[q+1])) {
                if (!first && p == first_p && q == first_q) {
                    if (PQ.npoints >= 3) {
                        return PQ;
                    } else {
                        return null;
                    }
                }
                intersect(P.points[p-2], P.points[p-1], P.points[p], P.points[p+1],
                          Q.points[q-2], Q.points[q-1], Q.points[q], Q.points[q+1], coords);
                //System.out.println("intersection at: (" + coords[0] + "," + coords[1] + ")");
                if (first) {
                    first = false;
                    first_p = p;
                    first_q = q;
                }
                PQ.addPointOfIntersection(coords[0], coords[1]);
                if (isInHalfPlane(Q.points[q-2], Q.points[q-1], Q.points[q], Q.points[q+1], P.points[p], P.points[p+1])) {
                    insideP = true;
                }
                else {
                    insideP = false;
                }
            }
            if (dotProd(Q.points[q-1] - Q.points[q+1], Q.points[q] - Q.points[q-2],
                P.points[p] - P.points[p-2], P.points[p+1] - P.points[p-1]) >= 0) {
                //System.out.println("qq x pp >= 0");
                if (isInHalfPlane(Q.points[q-2], Q.points[q-1], Q.points[q], Q.points[q+1], P.points[p], P.points[p+1])) {
                    if (!first && !insideP) {
                        PQ.addPointOfIntersection(Q.points[q], Q.points[q+1]);
                    }
                    q = q%(2*Q.npoints) + 2;
                }
                else {
                    if (!first && insideP) {
                        PQ.addPointOfIntersection(P.points[p], P.points[p+1]);
                    }
                    p = p%(2*P.npoints) + 2;
                }
            }
            else {
                //System.out.println("qq x pp < 0");
                if (isInHalfPlane(P.points[p-2], P.points[p-1], P.points[p], P.points[p+1], Q.points[q], Q.points[q+1])) {
                    if (!first && insideP) {
                        PQ.addPointOfIntersection(P.points[p], P.points[p+1]);
                    }
                    p = p%(2*P.npoints) + 2;
                }
                else {
                    if (!first && !insideP) {
                        PQ.addPointOfIntersection(Q.points[q], Q.points[q+1]);
                    }
                    q = q%(2*Q.npoints) + 2;
                }
                    
            }
            i++;
        }
        if (isInPolygon(P.points[0], P.points[1], Q)) { return (ConvexPolygon)P.clone(); }
        if (isInPolygon(Q.points[0], Q.points[1], P)) { return (ConvexPolygon)Q.clone(); }
        return null;
    }
    
    /**
     * Calculates the area of this polygon.
     */
    public float area() {
        float a = 0.0f;
        for (int i = 4; i < 2*npoints; i += 2) {
            a += Math.abs(signedArea(points[0], points[1], points[i-2], points[i-1], points[i], points[i+1]));
        }
        return a/2.0f;
    }
    
    /**
     * Returns a <code>String</code>-representation of the polygon.
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < 2*npoints; i += 2) {
            s = s.concat("(" + points[i] + "," + points[i+1] + ") ");
        }
        return s;
    }

    /**
     * Applies the given transformation to the vertices of the polygon.  This applies the transformation to all the points
     and leaves them in the transformed state.
     
     * @param at The affine transformation to apply.
     * @return this - for easy concatenation.
     */
    public Transformable apply(AffineTransform at) {
        at.transform(points, 0, points, 0, npoints);
        return this;
    }

}
