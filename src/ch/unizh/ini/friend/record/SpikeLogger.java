/*
 * $Id: SpikeLogger.java,v 1.1 2003/06/12 06:28:47 tobi Exp $
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
 * Created on June 10, 2003, 11:34 AM
 */

package ch.unizh.ini.friend.record;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Logs {@link SpikeEvent}'s to a {@link java.io.File}. The spikes times are logged along with the string representation of an artibtray object that
 *is passed on construction. To use this class, create a {@link SpikeLogger}, {@link #open} it, and {@link SpikeReporter#addSpikeListener add}
 * the <code>SpikeLogger</code> as a {@link SpikeListener} to the reporter.
 *<p>
 *<b>File format</b>
 *<p>
 *Comments have '#' in first character of line. Header line has date of creation and creator class. 
 Data is logged as 1 spike/line. If an object is passed to append to spikes times, spikes times
 *are followed by ',' and object.
 *For example
 *<pre>
 *# SpikeLogger ...
 *320
 *450
 *</pre>
 *or
 *<pre>
 *# SpikeLogger ...
 *32,16,43
 *1500,54,879
 *# SpikeLogger ...
 *</pre>
 *
 *
 * @author  $Author: tobi $
 *@version $Revision: 1.1 $
 */
public class SpikeLogger implements SpikeListener {
    
    File file=null;
    Object object=null;
    PrintWriter pw=null;
    
    //    /** Creates a new instance of SpikeLogger */
    //    public SpikeLogger() {
    //    }
    
    public SpikeLogger(File f){
        file=f;
    }
    
    public SpikeLogger(File f, Object o){
        this(f);
        object=o;
    }
    
    /** open the file for output.
     *@throws IOException
     */
    public void open() throws IOException{
        pw=new PrintWriter(new BufferedWriter(new FileWriter(file)));
        pw.println("# SpikeLogger opened "+new Date());
        if(pw.checkError()) System.err.println("writer error");
    }
    
    /** closes the file.
     *@throws IOException
     */
    public void close() throws IOException {
        pw.println("# SpikeLogger closed "+new Date());
        pw.close();
    }
    
    /** called by spike source ({@link SpikeReporter}) when a spike is detected
     *      @param e the spike event
     *
     */
    public void spikeOccurred(SpikeEvent e) {
        if(object!=null) pw.println(e+","+object);
        else pw.println(e);
    }
    
}

/*
 *$Log: SpikeLogger.java,v $
 *Revision 1.1  2003/06/12 06:28:47  tobi
 *added microphone recording for hooking up friend chip. not fully functional yet.
 *
 */
