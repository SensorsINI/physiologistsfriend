/*
 * CurrentTimeMillisBenchMark.java
 *
 * Created on September 10, 2002, 4:44 PM
 * $Id: CurrentTimeMillisBenchMark.java,v 1.3 2002/10/24 12:05:52 cmarti Exp $
 

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
 * <hr>
 * <pre>
 * $Log: CurrentTimeMillisBenchMark.java,v $
 * Revision 1.3  2002/10/24 12:05:52  cmarti
 * add GPL header
 *
 * Revision 1.2  2002/10/01 16:16:54  cmarti
 * change package and import names to new hierarchy
 *
 * Revision 1.1  2002/09/10 15:01:23  tobi
 * tests currentTimeMillis call time.  turns out about 100us.
 *
 * </pre>
 * @author $Author: cmarti $
 */

package ch.unizh.ini.friend.tests;

import java.util.Random;

/**
 *
 * @author  tobi
 @since $Revision: 1.3 $
 
 */
public class CurrentTimeMillisBenchMark {
    
    /** Creates a new instance of CurrentTimeMillisBenchMark */
    public CurrentTimeMillisBenchMark() {
    }
    
    public static void main(String[] args){
        int numIterations=1000000;
        int numRepeats=10;
        long[] durations=new long[numRepeats];
        for(int i=0;i<numRepeats;i++){
            long startTime=System.currentTimeMillis();
            for(int j=0;j<numIterations;j++){
                long sample=System.currentTimeMillis();
            }
            long stopTime=System.currentTimeMillis();
            durations[i]=stopTime-startTime;
        }
        System.out.println("currentTimeMillis() time sec/iteration");
        for(int i=0;i<numRepeats;i++){
            System.out.println((float)durations[i]/numIterations+" ");
        }
        System.out.println("");

        Random r=new Random();
        
        for(int i=0;i<numRepeats;i++){
            long startTime=System.currentTimeMillis();
            for(int j=0;j<numIterations;j++){
                int sample=r.nextInt();
            }
            long stopTime=System.currentTimeMillis();
            durations[i]=stopTime-startTime;
        }
        System.out.println("Math.rand() time sec/iteration");
        for(int i=0;i<numRepeats;i++){
            System.out.println((float)durations[i]/numIterations);
        }
        System.out.println("");
    }
    
}
