/*
 * RetintopicLocation.java
 *
 * Created on September 30, 2002, 6:19 PM
 * $Id: RetinotopicLocation.java,v 1.8 2003/07/03 16:54:28 tobi Exp $
 

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
 * $Log: RetinotopicLocation.java,v $
 * Revision 1.8  2003/07/03 16:54:28  tobi
 * fixed a bunch of javadoc errors.
 * made IntegrateFireCell gettter/setter methods for settings timeconstants and used those in simulation setup factory to set complex cell properties better. (need to move this inside complex cell factory method)
 *
 * made lowpass and highpass filters time constants settable.
 *
 * Revision 1.7  2002/10/24 12:05:52  cmarti
 * add GPL header
 *
 * Revision 1.6  2002/10/08 07:48:42  tobi
 *
 * added method to return cell at the correponding location in another map.
 *
 * Revision 1.5  2002/10/05 17:37:16  tobi
 * fixed getneighbor to work now....
 *
 * Revision 1.4  2002/10/04 12:59:00  tobi
 * initial version of HexDirection added to make direction an object.
 * Method forfinding nearest cell in a direction implmented.
 *
 * Revision 1.3  2002/10/01 20:52:48  tobi
 * added method to make a location from a Point2D.
 * this is really just a copy constructor, since RetinotopicLocation is really just a Point2D.
 * oh well.  i could have just used setLocation(Point2D).
 *
 * Revision 1.2  2002/10/01 18:43:23  tobi
 * initial version
 * fixed locaiton to start adding helper methods
 *
 * Revision 1.1  2002/09/30 17:30:21  tobi
 * initial version
 *
 */

package ch.unizh.ini.friend.topology;

import ch.unizh.ini.friend.simulation.SimulationSetup;
import ch.unizh.ini.friend.simulation.SimulationSetupFactory;
import java.awt.geom.Point2D;
import java.lang.Double;
import java.util.*;

/**
 * Represents a location of a retinotopic cell in the 2d image plane. Provides static methods for finding cells topologically.
 <p>
 E.g.
 <pre>
        SimulationSetup setup=SimulationSetupFactory.getSimulationSetup();
        Collection on=setup.getOnGanglions();
        Retinotopic c=RetinotopicLocation.findCenterCell(on);
        Retinotopic x=RetinotopicLocation.findNearestNeighbor(c, on, new HexDirection(HexDirection.E));
</pre>
 
@author  $Author: tobi $
@since $Revision: 1.8 $
 
 */
public class RetinotopicLocation extends Point2D.Float {
    
    /** constructs a <code>RetinotopicLocation</code> from a {@link Point2D}. */
    public RetinotopicLocation(Point2D p){
        setLocation(p);
    }
    
    /** static helper that takes a list of cells, each Retinotopic, and finds the geometrically-center one.
     The center of mass of all the cells is computed, and the cell closest to this point is returned.
     <b>This method is slow, not for runtime use. </b>
     This method is slow because it just does a serial search, but this should be ok for the purposes of establishing cell connections.
     
     @param cells List of cells
     @return center cell
     */
    public static Retinotopic findCenterCell(Collection cells){
        Iterator i=cells.iterator();
        float xsum=0, ysum=0, num=0;
        while(i.hasNext()){
            Retinotopic p=(Retinotopic)i.next();
            xsum+=p.getRetinotopicLocation().x;
            ysum+=p.getRetinotopicLocation().y;
            num++;
        }
        float ymid=ysum/num, xmid=xsum/num;
        
        Point2D.Float com=new Point2D.Float(xmid,ymid);
        
        Retinotopic center=null;
        
        float lastDistance=java.lang.Float.MAX_VALUE;  // for some reason Float.MAX_VALUE doesn't work....  you need to explicitly point to java.lang.Float
        
        i=cells.iterator();
        while(i.hasNext()){
            Retinotopic cell=(Retinotopic)i.next();
            RetinotopicLocation p=cell.getRetinotopicLocation();
            if(com.distanceSq(p)<lastDistance){
                lastDistance=(float)com.distanceSq(p);
                center=cell;
            }
        }
        return center;
    }
    
    /** finds the nearst neighboring cell to me, in collection all, in direction dir.
     <b>This method is slow--not for runtime use. </b>
     @param me starting cell
     @param all cells in which to look for closest one
     @param dir direction to look in
     @return closest cell in {@link HexDirection} <code>dir</code>.  If there is no cell in that direction, returns null.
     */
    public static Retinotopic findNearestNeighbor(Retinotopic me, Collection all, HexDirection dir){
        Retinotopic neighbor=null;
        Iterator i=all.iterator();
        double minDistance=java.lang.Double.MAX_VALUE;
        while(i.hasNext()){
            Retinotopic cell=(Retinotopic)i.next(); // potential neighbor
            if(cell==me) continue; // dont return ourself
            Point2D pme=me.getRetinotopicLocation();  // that's us
//            System.err.println("pme = " + pme );
            Point2D pcell=cell.getRetinotopicLocation(); // thats the potential neighbor
//            System.err.println("pcell = " + pcell );
            
            double angle=Math.atan2(pcell.getY()-pme.getY(),  pcell.getX()-pme.getX()); // get the angle from us to neighbor
//            System.err.println("angle/(PI/6) = " + angle/(Math.PI/6) );
            if(Math.abs(angle-dir.getAngle())>.01) continue;    // if angle is too different, don't chose this cell
            double dist=pme.distanceSq(pcell);  // get the distance
            if(dist<minDistance){   // it it's less than before, replace choice
                minDistance=dist;
                neighbor=cell;
            }
        }
        return neighbor;  // return nearest at correct angle
    }

    /** finds the corresponding cell in another layer at this same retinotopic location 
     <b>This method is slow--not for runtime use. </b>
     @param me the cell in this layer
     @param all the cells in the other layer
     @return the closest cell in the layer <code>all</code> to <code>me</code>
     */
    public static Retinotopic findCorresponding(Retinotopic me, Collection all){
        Retinotopic match=null;
        Iterator i=all.iterator();
        double minDistance=java.lang.Double.MAX_VALUE;
        while(i.hasNext()){
            Retinotopic cell=(Retinotopic)i.next(); // potential neighbor
            Point2D pme=me.getRetinotopicLocation();  // that's us
//            System.err.println("pme = " + pme );
            Point2D pcell=cell.getRetinotopicLocation(); // thats the potential neighbor
//            System.err.println("pcell = " + pcell );
            
            double dist=pme.distanceSq(pcell);  // get the distance
            if(dist<minDistance){   // it it's less than before, replace choice
                minDistance=dist;
                match=cell;
            }
        }
        return match;  // return nearest at correct angle
    }
    
    private static Point2D polarToRectangular(Point2D polar) {
        double phi = polar.getX(); // phase angle
        double rho = polar.getY(); // polar radius
        return new Point2D.Double(rho*Math.cos(phi), rho*Math.sin(phi));
    }
    
    
    private static Point2D rectangularToPolar(Point2D rect) {
        double x = rect.getX();
        double y = rect.getY();
        return new Point2D.Double(Math.atan2(y, x),
        Math.sqrt(x*x + y*y));
    }
    
    public static void main(String[] args){
        SimulationSetup setup=SimulationSetupFactory.getSimulationSetup();
        Collection on=setup.getOnGanglions();
        Retinotopic c=RetinotopicLocation.findCenterCell(setup.getOnGanglions());
        System.err.println("c = " + c );
        Retinotopic x=RetinotopicLocation.findNearestNeighbor(c, on, new HexDirection(HexDirection.E));
        System.err.println("x = " + x );
    }
        
    
}
