/*
 * $Id: LMSColor.java,v 1.2 2003/05/10 17:27:42 jgyger Exp $
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

package ch.unizh.ini.friend.graphics;

import java.awt.Color;

/**
 * Represents a color in the LMS color space. The {@link java.awt.Color} class
 * itself supports color spaces, however, this class does not use this feature.
 * It's merely a convenience class to keep things simple.
 * 
 * @author Johann Gyger
 * @version $Revision: 1.2 $
 */
public class LMSColor {

    private final Color color;

    private final float l;

    private final float m;

    private final float s;

    /**
     * Creates a new color instance in the LMS color space.
     * 
     * @param color the color in the sRGB color space
     */
    public LMSColor(Color color) {
        this.color = color;

        float[] rgb = color.getRGBColorComponents(null);

        // compute linear sRGB values
        float[] linrgb = new float[3];
        for (int i = 0; i < 3; i++) {
            linrgb[i] =
                rgb[i] <= 0.03928
                    ? rgb[i] / 12.92f
                    : (float) Math.pow((rgb[i] + 0.055) / 1.055, 2.4);
        }

        // linear transformation into LMS color space
        // see http://www.computer.org/cga/cg2001/g5034abs.htm
        l = 0.3811f * linrgb[0] + 0.5783f * linrgb[1] + 0.0402f * linrgb[2];
        m = 0.1967f * linrgb[0] + 0.7244f * linrgb[1] + 0.0782f * linrgb[2];
        s = 0.0241f * linrgb[0] + 0.1288f * linrgb[1] + 0.8444f * linrgb[2];
    }

    /**
     * Returns the long-wavelength (red) component.
     * 
     * @return float the L color component
     */
    public float getL() {
        return l;
    }

    /**
     * Returns the middle-wavelength (green) component.
     * 
     * @return float the M color component
     */
    public float getM() {
        return m;
    }

    /**
     * Returns the short-wavelength (blue) component.
     * 
     * @return float the S color component
     */
    public float getS() {
        return s;
    }

    /**
     * Returns the color in the sRGB color space.
     * 
     * @return Color the color in the sRGB space
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns a string representation of this <code>LMSColor</code>. This
     * method is intended to be used only for debugging purposes. The
     * content and format of the returned string might vary between
     * implementations. The returned string might be empty but cannot 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>LMSColor</code>.
     */
    public String toString() {
        return "LMSColor[L="
            + getL()
            + ",M="
            + getM()
            + ",S="
            + getS()
            + ",color="
            + getColor()
            + "]";
    }

}
