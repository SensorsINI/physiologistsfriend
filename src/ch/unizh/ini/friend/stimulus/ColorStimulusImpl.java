/*
 * $Id: ColorStimulusImpl.java,v 1.3 2003/06/23 11:30:16 tobi Exp $
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
import ch.unizh.ini.friend.graphics.ShapeList;

/**
 * Default implementation of the ColorStimulus interface.
 *
 * @author Johann Gyger
 * @version $Revision: 1.3 $
 */
public class ColorStimulusImpl
extends ConcreteStimulus
implements ColorStimulus, Cloneable{
    
    /** The foreground color. */
    protected LMSColor foreground;
    
    /** The background color. */
    protected LMSColor background;
    
    // used to implement setVisible/isVisible
    protected LMSColor lastForeground;
    
    /**
     * Creates a new color stimulus.
     */
    public ColorStimulusImpl() {
        setForeground(Color.yellow);
        setBackground(Color.darkGray);
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.ColorStimulus#getForeground()
     */
    public Color getForeground() {
        return foreground.getColor();
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.ColorStimulus#getBackground()
     */
    public Color getBackground() {
        return background.getColor();
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.ColorStimulus#setForeground(java.awt.Color)
     */
    public void setForeground(Color color) {
        foreground = new LMSColor(color);
        lastForeground = foreground;
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.ColorStimulus#setBackground(java.awt.Color)
     */
    public void setBackground(Color color) {
        background = new LMSColor(color);
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#brightenBackground()
     */
    public void brightenBackground() {
        setBackground(getBackground().brighter());
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#brightenForeground()
     */
    public void brightenForeground() {
        setForeground(getForeground().brighter());
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#darkenBackground()
     */
    public void darkenBackground() {
        setBackground(getBackground().darker());
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#darkenForeground()
     */
    public void darkenForeground() {
        setForeground(getForeground().darker());
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#getBackgroundExcitationDensity()
     */
    public float getBackgroundExcitationDensity() {
        LMSColor c = background;
        return (c.getL() + c.getM() + c.getS()) / 3.0f;
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#getForegroundExcitationDensity()
     */
    public float getForegroundExcitationDensity() {
        LMSColor c = foreground;
        return (c.getL() + c.getM() + c.getS()) / 3.0f;
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#flipContrast()
     */
    public void flipContrast() {
        LMSColor tmp = foreground;
        foreground = background;
        lastForeground = background;
        background = tmp;
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#isVisible()
     */
    public boolean isVisible() {
        return foreground != background;
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.Stimulus#setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        if (visible) {
            foreground = lastForeground;
        } else {
            lastForeground = foreground;
            foreground = background;
        }
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.ColorStimulus#getLMSBackground()
     */
    public LMSColor getLMSBackground() {
        return background;
    }
    
    /**
     * @see ch.unizh.ini.friend.stimulus.ColorStimulus#getLMSForeground()
     */
    public LMSColor getLMSForeground() {
        return foreground;
    }
    
    /** clones the foreground color, background color, and the tranformed ShapeList */
    public Object clone(){
        ColorStimulus s=new ColorStimulusImpl();
        s.setForeground(getForeground());
        s.setBackground(getBackground());
        s.setShapes((ShapeList)getTransformedShapes().clone());
        return s;
    }
    
}
