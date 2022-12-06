/*
 * Direction.java
 *
 * Created on October 4, 2002, 2:02 PM
 * $Id: HexDirection.java,v 1.4 2002/10/24 12:05:52 cmarti Exp $
 

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
 * $Log: HexDirection.java,v $
 * Revision 1.4  2002/10/24 12:05:52  cmarti
 * add GPL header
 *
 * Revision 1.3  2002/10/05 17:36:41  tobi
 * made hex direction use BigInteger for true nonnegative direction.
 * fixed angle to be -pi to pi
 *
 * Revision 1.2  2002/10/05 15:46:50  tobi
 * it is now truely mod 6 non negative number, and angle is from -PI to PI
 *
 * Revision 1.1  2002/10/04 12:59:00  tobi
 * initial version of HexDirection added to make direction an object.
 * Method forfinding nearest cell in a direction implmented.
 *
 */

package ch.unizh.ini.friend.topology;

import java.math.BigInteger;

/**
 * Represents a topological direction in the hexagonal arrangement of cells in the retina.
 A direction is represented in the hex arrangement as shown in this image:
 <p>
 <img src="doc-files/hex.gif">
 
 * @author  $Author: cmarti $
 @since $Revision: 1.4 $
 
 */
public class HexDirection {
    
    protected int dir=0;
    /** a hex direction */
    public static final int E=0,NE=1,NW=2,W=3,SW=4,SE=5;
    
    /** Creates a new instance of Direction with direction set to -1. */
    public HexDirection() {
        dir=-1;
    }
    
    /** Creates new instance with given direction. */
    public HexDirection(int i){
        BigInteger b=BigInteger.valueOf(i);  // we use BigInteger for unsigned modulus.....
        BigInteger b2=b.mod(BigInteger.valueOf(6));
        //System.out.println("i="+i+" b="+b+" b2="+b2);
        dir=b2.intValue();
    }
    
    /** gets the direction as a int, 0-5 CCW from east, non negative. */
    public int get(){ return dir;}
    /** sets the direction */
    public void set(int i){dir=i;}
    
    /** get angle in radians. Positive x is angle 0.  Increases to Math.PI for CCW rotation, decreases to -Math.PI in CW direction. 
     @return angle in radians.*/
    public double getAngle(){
        int i=get();
        if (i<3) return i*Math.PI/3;
        else return (i-6)*Math.PI/3;
    }
    
    /** tests HexDirection */
    public static void main(String[] args){
        for(int i=-8;i<15;i++){
//        System.out.println("dir("+i+")= "+new HexDirection(i).get()+" angle="+new HexDirection(i).getAngle());
        }
    }
    
    /** returns int direction, 0-5 CCW from east and non negative */
    public String toString(){
        return "HexDirection "+Integer.toString(get());
    }
    
}
