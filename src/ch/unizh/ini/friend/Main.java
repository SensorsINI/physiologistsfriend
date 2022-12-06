/*
 * Main.java
 *
 * Created on September 14, 2002, 3:54 AM
 
 $Id: Main.java,v 1.32 2003/07/08 15:20:58 tobi Exp $
 
 
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
 
 
 
 */

package ch.unizh.ini.friend;
import ch.unizh.ini.friend.gui.*;
import ch.unizh.ini.friend.stimulus.Stimulus;
import ch.unizh.ini.friend.stimulus.*;
import ch.unizh.ini.friend.graphics.*;
import ch.unizh.ini.friend.simulation.*;
import ch.unizh.ini.friend.simulation.cells.*;
import ch.unizh.ini.friend.stimulus.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.UIManager;

//import java.util.logging.*;


/**
 * Main class for the Physiologist's Friend program.
 *
 * @author  tobi
 * @since $Revision: 1.32 $
 */
public class Main {
    
    /** location of splash screen image for original (monochrome) version of PhysioFriend relative to class root: {@value}
     * @see #SPLASH_IMAGE_COLOR
     */
    public static final String SPLASH_IMAGE="ch/unizh/ini/friend/SplashScreen.gif";
    
    /** location of splash screen image for color version of PhysioFriend relative to class root: {@value}
     * @see #SPLASH_IMAGE
     */
    public static final String SPLASH_IMAGE_COLOR="ch/unizh/ini/friend/SplashScreen-color.gif";
    
    /** time to show splash screen in ms: {@value} */
    public static final long SPLASH_DURATION=5500;
    
    /** has color been enabled */
    private boolean colorVersionEnabled=false;
    
    /** is the color version of PhysioFriend running */
    public boolean isColorVersionEnabled(){ return colorVersionEnabled; }
    
    /** top level logger that anyone can use to log messages.  For example:
     * <pre>
     * Main.log.info("on cell");
     * </pre>
     */
    //    public static Logger log=Logger.getLogger("friend");
    
    /** run friend by instantiating a new Main:
     * <pre>
     * new Main()
     * <pre>
     */
    public Main(String[] args) {
        showSplash();
        SimulationSetup setup;
        if (args.length > 0 && args[0].equals("-color")){
            colorVersionEnabled=true;
            setup = SimulationSetupFactory.getColorSimulationSetup();
        }else{
            colorVersionEnabled=false;
            setup = SimulationSetupFactory.getSimulationSetup();
        }
        Stimulus stimulus = setup.getStimulus();
        FriendGUI gui=new FriendGUI(setup);
        gui.show();
        gui.startSimulation();
        gui.requestFocus();
    }
    
    private void showSplash(){
        Thread splashThread=new Thread(){
            public void run(){
                //                if(!isColorVersionEnabled()){
                //                    try{
                //                        ClassLoader cl=this.getClass().getClassLoader();
                //                        URL splashImageURL=cl.getResource(SPLASH_IMAGE);
                //                        //            System.out.println("splashImageURL="+splashImageURL);
                //                        Image splashImage=Toolkit.getDefaultToolkit().getImage(splashImageURL);
                //                        //            int h=splashImage.getHeight(); // cant get height for abstract image???
                //                        //            int w=splashImage.getWidth();
                //                        GifViewerWindow splash=GifViewerWindow.showGifFile(splashImage,212,400);
                //                        try{Thread.currentThread().sleep(SPLASH_DURATION);}catch(InterruptedException e){}
                //                        GifViewerWindow.hideGifFile(splash);
                //                    }catch(Exception io){
                //                        System.err.println("can't show splash image "+SPLASH_IMAGE);
                //                        io.printStackTrace();
                //                        //            System.exit(1);
                //                    }
                //                }else{ // color version
                try{
                    ClassLoader cl=this.getClass().getClassLoader();
                    URL splashImageURL=cl.getResource(SPLASH_IMAGE_COLOR);
                    //            System.out.println("splashImageURL="+splashImageURL);
                    Image splashImage=Toolkit.getDefaultToolkit().getImage(splashImageURL);
                    //            int h=splashImage.getHeight(); // cant get height for abstract image???
                    //            int w=splashImage.getWidth();
                    GifViewerWindow splash=GifViewerWindow.showGifFile(splashImage,358,400);
                    try{Thread.currentThread().sleep(SPLASH_DURATION);}catch(InterruptedException e){}
                    GifViewerWindow.hideGifFile(splash);
                }catch(Exception io){
                    System.err.println("can't show splash image "+SPLASH_IMAGE_COLOR);
                    io.printStackTrace();
                    //            System.exit(1);
                }
                //                }
            }
        };
        splashThread.start();
    }
    
    /** run this to run Friend */
    public static void main(String[] args){
        
        
        //        try{
        //            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        //        }catch(Exception e){
        //            e.printStackTrace();
        //        }
        try {
            UIManager.setLookAndFeel(  UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("couldn't set java look and feel");}
        new Main(args);
    }
    
}

/*
 *$Log: Main.java,v $
 *Revision 1.32  2003/07/08 15:20:58  tobi
 *added back splash screen
 *
 Revision 1.31  2003/06/23 11:30:15  tobi
 greatly improved recording display speed, capability

 added full screen exclusive display

 Revision 1.30  2003/05/10 17:27:41  jgyger
 Merge from color-branch
 
 Revision 1.29.2.3  2003/05/08 17:13:19  tobi
 splash screen unified.
 
 Revision 1.29.2.2  2003/05/05 11:08:51  tobi
 added color gif to startup and added general method to determine if color version is running
 
 Revision 1.29.2.1  2003/03/16 16:31:34  jgyger
 add command line flag '-color' to switch between
 normal and color simulation
 
 Revision 1.29  2002/10/27 17:05:10  tobi
 now showing splash after gui starts, to make sure its on top.
 
 Revision 1.28  2002/10/25 17:30:02  tobi
 made splash a thread so startup is faster
 
 Revision 1.27  2002/10/25 08:43:47  tobi
 splash screen size changed
 
 Revision 1.26  2002/10/24 12:05:48  cmarti
 add GPL header
 
 Revision 1.25  2002/10/15 19:26:55  tobi
 lots of javadoc added,
 mouse wheel enabled.
 
 Revision 1.24  2002/10/15 10:34:11  tobi
 
 set look and feel explitley to java look and feel
 
 Revision 1.23  2002/10/15 09:43:22  tobi
 set java look and feel to be default
 
 Revision 1.22  2002/10/13 16:29:09  tobi
 many small changes from tuebingen trip.
 
 Revision 1.21  2002/10/07 13:02:07  tobi
 commented  assert's and all other 1.4+ java things like Preferences, Logger,
 and setFocusable, setFocusCycleRoot. overrode isFocusable in TangentScreen to receive
 keyboard presses.
 It all runs under 1.3 sdk now.
 
 Revision 1.20  2002/10/06 10:51:18  tobi
 fixed javadoc links
 
 Revision 1.19  2002/10/05 21:28:44  tobi
 made logger public so all classes can use it.
 
 Revision 1.18  2002/10/01 13:45:52  tobi
 changed package and import to fit new hierarchy
 
 Revision 1.17  2002/09/29 19:07:43  tobi
 removed log comment
 
 Revision 1.16  2002/09/28 13:57:40  tobi
 tried using a logger, but nothing interesting
 
 Revision 1.15  2002/09/26 16:06:33  tobi
 simplified even more to use built in defualts for simulation setup.
 
 Revision 1.14  2002/09/25 17:05:41  tobi
 no change
 
 Revision 1.13  2002/09/25 17:03:36  tobi
 simplified because all setup now done in simulationSetup. Main now
 just make stimulus, simulation, and GUI and that's it.
 
 Revision 1.12  2002/09/25 08:41:26  tobi
 fixed javadoc so no errors generated and added package descriptions.
 
 Revision 1.11  2002/09/24 20:45:50  cmarti
 adapt the 'bipolars', 'bipolarsRisingOutput' and 'bipolarsFallingOutput'
 to the new implementation of friend.simulation.cells.Bipolar. this fix
 allows other code to work as before. it is provisional.
 
 Revision 1.10  2002/09/24 04:32:21  tobi
 made photoreceptorShapes public so gui could get at them.
 
 Revision 1.9  2002/09/23 15:18:26  tobi
 added some more fiducial cell references.
 
 Revision 1.8  2002/09/23 10:37:30  tobi
 moved more initialization to GUI
 
 Revision 1.7  2002/09/23 06:47:41  tobi
 made cells public so GUI can reference them
 
 Revision 1.6  2002/09/22 19:34:58  tobi
 now uses new stimulus BarStimulus
 
 Revision 1.5  2002/09/19 06:52:38  tobi
 Main now sets up tangent screen and shows reasonable bar stimulus that can be controlled
 in a usable manner.
 
 Revision 1.4  2002/09/18 06:40:53  tobi
 yay! stimulus now moves around...
 
 Revision 1.3  2002/09/17 14:44:11  tobi
 latest attempt to build a working program, but is not buildable.
 
 Revision 1.2  2002/09/15 21:44:30  tobi
 slight mods to main()
 
 Revision 1.1  2002/09/15 08:02:21  tobi
 initial verison, nothing sensible yet.
 
 */
