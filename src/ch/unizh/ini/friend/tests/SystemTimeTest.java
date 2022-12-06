/*
 $Id: SystemTimeTest.java,v 1.4 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: SystemTimeTest.java,v $
 Revision 1.4  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.3  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.2  2002/09/28 08:45:24  tobi
 clarified output print of result

 Revision 1.1  2002/09/27 15:26:28  cmarti
 initial version

 */

package ch.unizh.ini.friend.tests;

/**
 * Tries to find the resolution of System.getCurrentMillis().
 * @author Christof Marti
 * @version $Revision: 1.4 $
 */
public class SystemTimeTest {
    
    public static void main(String s[]) {
        long newTime, oldTime;
        float sum = 0.0f;
        long nRuns = 100;
        
        for (int i = 0; i < nRuns; i++) {
            oldTime = System.currentTimeMillis();
            do {
               newTime = System.currentTimeMillis();
            } while (oldTime == newTime);
            sum += newTime - oldTime;
        }
        
        System.out.println("System.currentTimeMillis has resolution of about = " + sum/nRuns + "ms");
    }
    
}
