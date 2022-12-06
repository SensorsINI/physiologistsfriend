/*
 * SpikeSoundTest.java
 *
 * Created on September 13, 2002, 9:22 PM
 
 $Id: SpikeSoundTest.java,v 1.5 2002/10/24 12:05:52 cmarti Exp $
 

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

 
 $Log: SpikeSoundTest.java,v $
 Revision 1.5  2002/10/24 12:05:52  cmarti
 add GPL header

 Revision 1.4  2002/10/06 10:51:18  tobi
 fixed javadoc links

 Revision 1.3  2002/10/01 16:16:54  cmarti
 change package and import names to new hierarchy

 Revision 1.2  2002/09/23 14:58:25  tobi
 removed .... prints

 Revision 1.1  2002/09/13 20:48:38  tobi
 inital add

 
 */

package ch.unizh.ini.friend.tests;

import ch.unizh.ini.friend.gui.SpikeSound;
import java.util.Random;

/**
 * Tests {@link ch.unizh.ini.friend.gui.SpikeSound} by generating a Poisson spike sequence controlled by a slider.
 
 * @author  tobi
 @version $Revision: 1.5 $
 */
public class SpikeSoundTest {
    
    
    public static float spikeRate=20f;
    
    /** run this to play a randomly genrated spike train */
    public static void main(String[] args){
        SpikeSound s=new SpikeSound();
        SpikeSoundTestGUI gui=new SpikeSoundTestGUI();
        gui.show();
        Random r=new Random();
        long t=System.currentTimeMillis();
        while(true){
            long tnow=System.currentTimeMillis();
            long dt=tnow-t;
            t=tnow;
            if( r.nextFloat() < dt/1000f*spikeRate ){
               s.play();
               //System.out.print(".");
            }
            try{
                Thread.currentThread().sleep(3);
            }catch(InterruptedException e){}
        }
    }
    
}
