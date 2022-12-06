/*
 * $Id: ColorStimulus.java,v 1.2 2003/05/10 17:27:44 jgyger Exp $
 *
 *
 * Copyright 2003 Institute of Neuroinformatics, 
 * University and ETH Zurich, Switzerland
 *
 * This file is part of The Physiologist's Friend.
 *
 * The Physiologist's Friend is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * The Physiologist's Friend is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The Physiologist's Friend; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ch.unizh.ini.friend.stimulus;

import java.awt.Color;

import ch.unizh.ini.friend.graphics.LMSColor;

/**
 * A color stimulus has a foreground and background color.
 * 
 * @author Johann Gyger
 * @version $Revision: 1.2 $
 */
public interface ColorStimulus extends Stimulus {

    /**
     * Sets the foreground excitation color of the stimulus.
     * 
     * @param color the new color
     */
    public void setForeground(Color color);

    /**
     * Returns the foreground excitation color of the stimulus.
     * 
     * @return the color
     */
    public Color getForeground();

    /**
     * Returns the foreground excitation color of the stimulus.
     * 
     * @return the color in LMS color space
     */
    public LMSColor getLMSForeground();

    /**
     * Sets the background excitation color of the stimulus.
     * 
     * @param color the new color
     */
    public void setBackground(Color color);

    /**
     * Returns the background excitation color of the stimulus.
     * 
     * @return the color
     */
    public Color getBackground();

    /**
     * Returns the background excitation color of the stimulus.
     * 
     * @return the color in LMS color space
     */
    public LMSColor getLMSBackground();

}
