$Id: README.txt,v 1.5 2002/10/20 16:22:12 tobi Exp $

directory structure for PhysioFriend


src/ root of source tree
src/ch/unizh/ini/friend/  all java files rooted here to obey package root ch.unizh.ini.friend
src/ch/unizh/ini/friend/Main.java   Main class, execute this to run friend
build/ final jar file, jar build script, installer build script, other stuff for deployment. installer build xml file for GhostInstaller (www.ginstall.com)
help/  help system.  uses HelpBreeze java help authoring system to build
3rdPartyJars/  3rd party jars used in friend, such as jnlp.jar (java web start) and jh.jar jsearch.jar (javahelp)
classes/  compiled classes go here
docs/ javadoc goes here
FriendAnt.xml Ant build scripts for building all stuff related to Java

$Log: README.txt,v $
Revision 1.5  2002/10/20 16:22:12  tobi
fixed directories


