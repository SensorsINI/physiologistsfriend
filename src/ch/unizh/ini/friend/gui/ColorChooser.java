/*
 * $Id: ColorChooser.java,v 1.2 2003/05/10 17:27:42 jgyger Exp $
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

package ch.unizh.ini.friend.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Adaptor for JColorChooser. The preview panel is changed and shows the LMS
 * values of the currently selected color.
 * 
 * @author Johann Gyger
 * @version $Revision: 1.2 $
 */
public class ColorChooser {

    /** Color chooser used to show the dialog */
    protected JColorChooser chooser;

    /** Preview panel in color chooser dialog */
    LMSColorPreviewPanel previewPanel = new LMSColorPreviewPanel();

    /** Used by #showDialog: The OK action listener cannot access locals. */
    private Color newColor;

    /**
     * Create a new instance.
     */
    public ColorChooser() {
        chooser = new JColorChooser();
        chooser.getSelectionModel().addChangeListener(previewPanel);
        chooser.setPreviewPanel(previewPanel);
    }

    /**
     * Show a modal color-chooser dialog.
     *
     * @param component  Parent component
     * @param title  Title of dialog
     * @param initialColor  Inital color
     * @return Selected or previous color if the user opted out
     */
    public Color showDialog(Component component, String title, 
        Color initialColor) {
        newColor = null;
        chooser.setColor(initialColor);
        previewPanel.refresh(initialColor);
        
        ActionListener ok = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newColor = chooser.getColor();
            }
        };
        
        JDialog dialog = 
            JColorChooser.createDialog(component, title, true, chooser, ok, null);
        dialog.show();
        
        return newColor;
    }

}
