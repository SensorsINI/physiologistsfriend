Physiologist's friend simulator   <!-- .style1 {font-family: "Courier New", Courier, mono} -->  <!-- .bold { font-size: medium; font-weight: bold; } -->

Tobi Delbruck

    INI  WWW

[Home](http://www.ini.uzh.ch/%7Etobi)

[Wiki](/%7Etobi/wiki)

[Motivation](/~tobi/motivation.php)

[History](/~tobi/workHistory.php)

[People](/~tobi/people.php)

[Projects](/~tobi/projects.php)

[Publications](/~tobi/publications.php)

[Resources](/~tobi/resources/index.php)

[Fun](/~tobi/fun/index.php)

[Contact](/~tobi/contact.php)

![Icon for PhysioFriend](../webIcon.gif) The Physiologist's Friend Simulator
============================================================================

Downloading, installing, and running PhysioFriend:
--------------------------------------------------

*   [**Download and launch the latest version of PhysioFriend**](friend.jnlp) in one step if you have [Java Web Start](http://java.sun.com/products/javawebstart/) installed.
*   [**Download a Windows installer**](Setup.exe). Run _Setup.exe_ to install PhysioFriend along with Start Menu and Desktop shortcuts.
*   For **other platforms** without Java Web Start but with the JRE (1.4+) installed, download [friend.jar](friend.jar), [jnlp.jar](jnlp.jar), [jh.jar](jh.jar), and [jsearch.jar](jsearch.jar). Save all of them to the same directory. You can then run PhysioFriend with **java -jar friend.jar** from a command line

**What it does**
----------------

**PhysioFriend** lets you plot receptive fields of simulated retinal and cortical cells. It is the software analog to the [Physio Friend chip](../chip/index.php). It is intended for classroom demonstration of cell response properties. You can hear the cell responses as though you were doing a recording from a live animal. You can choose between photoreceptor, horizontal cell, on and off bipolar cells, on and off ganglion cells, and several types of cortical cells. You can use bar, edge, or grating stimuli. You will hear how the cell responds as you use your mouse to move the stimulus. The stimulus orientation, contrast, size, spatial frequency can be easily manipulated through the keyboard.

PhysioFriend is 100% written in Java. All you need is the Java run time environment and a sound card--if you want to hear the cells. (If you don't have a sound card, you can still see the cells responses on an activity meter.)

PhysioFriend does a relatively simple-minded simulation of the cells. The intention is not to simulate the beautiful complexity of the visual system, but just to capture some aspects of how the cells seem to respond.

PhysioFriend source code and documentation
------------------------------------------

The source code is available under the [GPL](http://www.gnu.org/copyleft/gpl.html)

*   [friend.zip](friend.zip) - ZIP archive of everything
*   [friendapi.zip](friendapi.zip) - ZIP archive of generated javadoc for everything

Requirements:
-------------

*   [Java Run Time Environment](http://java.sun.com/downloads/index.html) (JRE) version 1.4+. Available for Windows, Linux, Macintosh and other platforms. 
*   Java compatible sound card. Java seems to know about most sound cards.

Quick start
-----------

![](ScreenShot1.gif)

*   When you start PhysioFriend, you will see a bar stimulus and a blank screen. The simulation will already be running and you will be observing the response of one of the photoreceptors.
*   You won't hear anything because the photoreceptor is not a spiking cell, but you can observe the response of the photoreceptor on the activity bar on the right.
*   To get help on the keyboard shortcuts, use the Help menu or hit F1.
*   To select the cell you want to observe, use the Cell menu.
*   If the cell is a spiking cell, you will hear its spikes and you can see the spike rate on the activity meter. If the cell is a graded cell you will only see the response on the activity meter.
*   To select the stimulus you want to use, use the Stimulus menu.
*   To view the photoreceptor locations, select View.../Photoreceptors
*   Explore the menus and try the popup menu on the tangent screen. There are many keyboard shortcuts that you can pick up from the menus or from the Hot Key help.

Troubleshooting
---------------

*   Java Web Start does the download, install, and launch in one step, and it keeps you automatically up-to-date with the latest version of PhysioFriend. Java Web Start comes by default in Mac OS 10.1. For other platforms, it is installed by default in Java 1.4.1+, and is available as a separate download for other Java versions.
*   If you have Java installed, you may not have Java Web Start as well, so you may need to install the updater to get Web Start.
*   Under **linux**, installing the Java RPM may not install Java Web Start so that the browser will know about it, or the browser may not have the correct file type enabled for the Java Web Start .jnlp suffix.. You still need to go the Java installation directory (typically something like /usr/java/j2re1.4.1) and then unzip the Java Web Start archive and run a separate installer script to get Web Start.
*   If you're having trouble with getting [Mozilla](http://www.mozilla.org/) to recognize the _jnlp_ type, you may need to install [Java Web Start as a helper application to use with files with the _.jnlp_ extension](http://nsdl.sdsc.edu/apps/MozillaWS_help.html).

News
----

*   6.7.03: Added marker hot key to mark stimulus position (Thanks Kevan Martin & Harvey Karten). Changed dynamics of some cells to make them more transient.  
    
*   16.6.03: Recording of spikes from simulation or from microphone (for recording [chip](../chip/index.php) responses) now works.  
    
*   6.6.03: Simulation of color system now works. The help system has been expanded to discuss the color-system simulation.
*   6.6.03: [Source code and documentation](#PhysioFriend_source_code_and) is now available.

Authors
-------

PhysioFriend was originally written by Christof Marti and [Tobi Delbruck](http://www.ini.uzh.ch/%7Etobi) at the [Institute of Neuroinformatics](http://www.ini.uzh.ch/), University and ETH Zurich, Switzerland.  
Johann Gyger, with consultation from [Daniel Kiper](http://www.ini.uzh.ch/%7Ekiper), extended it to model color selective retinal cells.

[Go to PhysioFriend Home](../index.html). [Go to PhysioFriend Chip](../chip/index.php).  

  
_September 20, 2007_

[Home](http://www.ini.uzh.ch/~tobi)

[Motivation](/~tobi/motivation.php)

[History](/~tobi/workHistory.php)

[People](/~tobi/people.php)

[Projects](/~tobi/projects.php)

[Publications](/~tobi/publications.php)

[Resources](/~tobi/resources/index.php)

[Fun](/~tobi/fun/index.php)

[Contact](/~tobi/contact.php)
