/*
 $Id: SystemTimeBaseTest.java,v 1.5 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: SystemTimeBaseTest.java,v $
 Revision 1.5  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.4  2002/10/11 20:38:22  tobi
 commented method to allow javadoc building even in absence of javax.media

 Revision 1.3  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.2  2002/09/30 08:50:11  tobi
 commented out code because it requres javax.media and this is not in default
 java install.  can uncomment when we need to test time base.

 Revision 1.1  2002/09/27 15:25:55  cmarti
 initial version

 */

package ch.unizh.ini.friend.tests;

//import javax.media.SystemTimeBase;

/**
 * Tries to find the resolution of SystemTimeBase.getNanoseconds() (from the Java Media Framwork).
 * @author Christof Marti
 * @version $Revision: 1.5 $
 */
public class SystemTimeBaseTest {
    
    public static void main(String s[]) {
//        SystemTimeBase tb = new SystemTimeBase();
//        long newTime, oldTime;
//        float sum = 0.0f;
//        long nRuns = 10000;
//        
//        for (int i = 0; i < nRuns; i++) {
//            oldTime = tb.getNanoseconds();
//            do {
//               newTime = tb.getNanoseconds();
//            } while (oldTime == newTime);
//            sum += newTime - oldTime;
//        }
//        
//        System.out.println("average = " + sum/nRuns + "ns");
    }
    
}
