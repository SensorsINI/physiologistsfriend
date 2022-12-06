/*
 * $Id: LMSColorPreviewPanel.java,v 1.2 2003/05/10 17:27:42 jgyger Exp $
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
import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.unizh.ini.friend.graphics.LMSColor;

/**
 * This panel can be used to replace the default preview panel of
 * {@link javax.swing.JColorChooser}:
 * 
 * <code>
 * JColorChooser chooser = new JColorChooser();
 * JPanel preview = new LMSColorPreviewPanel();
 * chooser.getSelectionModel().addChangeListener(previewPanel);
 * chooser.setPreviewPanel(previewPanel);
 * </code>
 * 
 <pre>

Issue: JColorChooser is somewhat buggy and the preview window might not
work with Java <= 1.4.2 [1]. On my machine it works (I have 1.4.2).
See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/colorchooser.html#previewpanel">http://java.sun.com/docs/books/tutorial/uiswing/components/colorchooser.html#previewpanel</a> for a note about
 buggy replacement of the panel.

 * @author Johann Gyger
 * @version $Revision: 1.2 $
 */
public class LMSColorPreviewPanel extends JPanel implements ChangeListener {

    JPanel colorPanel = new JPanel();
    JLabel lValueLabel = new JLabel();
    JLabel mValueLabel = new JLabel();
    JLabel sValueLabel = new JLabel();
    JProgressBar lProgressBar = new JProgressBar();
    JProgressBar mProgressBar = new JProgressBar();
    JProgressBar sProgressBar = new JProgressBar();

    NumberFormat lmsFormat = NumberFormat.getInstance();

    /**
     * Create a default instance.
     */
    public LMSColorPreviewPanel() {
        initGUI();
        setSize(getPreferredSize());

        lmsFormat.setMinimumFractionDigits(2);
        lmsFormat.setMaximumFractionDigits(2);
    }

    /**
     * Listen if the color changes.
     */
    public void stateChanged(ChangeEvent e) {
        ColorSelectionModel m = (ColorSelectionModel) e.getSource();
        refresh(m.getSelectedColor());
    }

    /**
     * Refresh view.
     * 
     * @param c New color
     */
    public void refresh(Color c) {
        colorPanel.setBackground(c);

        LMSColor lms = new LMSColor(c);
        lValueLabel.setText(lmsFormat.format(lms.getL()));
        mValueLabel.setText(lmsFormat.format(lms.getM()));
        sValueLabel.setText(lmsFormat.format(lms.getS()));

        lProgressBar.setValue(Math.round(100 * lms.getL()));
        mProgressBar.setValue(Math.round(100 * lms.getM()));
        sProgressBar.setValue(Math.round(100 * lms.getS()));        
    }

    private void initGUI() {
        Box mainBox = Box.createHorizontalBox();
        Box lmsLabelBox = Box.createVerticalBox();
        Box lmsProgressBox = Box.createVerticalBox();
        Box lmsValueBox = Box.createVerticalBox();

        JLabel lLabel = new JLabel();
        JLabel mLabel = new JLabel();
        JLabel sLabel = new JLabel();

        lLabel.setText("L");
        mLabel.setText("M");
        sLabel.setText("S");

        lValueLabel.setText("0.00");
        mValueLabel.setText("0.00");
        sValueLabel.setText("0.00");

        colorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        colorPanel.setPreferredSize(new Dimension(60, 40));

        lmsLabelBox.add(lLabel, null);
        lmsLabelBox.add(Box.createVerticalStrut(5), null);
        lmsLabelBox.add(mLabel, null);
        lmsLabelBox.add(Box.createVerticalStrut(5), null);
        lmsLabelBox.add(sLabel, null);

        lmsProgressBox.add(lProgressBar, null);
        lmsProgressBox.add(Box.createVerticalStrut(5), null);
        lmsProgressBox.add(mProgressBar, null);
        lmsProgressBox.add(Box.createVerticalStrut(5), null);
        lmsProgressBox.add(sProgressBar, null);

        lmsValueBox.add(lValueLabel, null);
        lmsValueBox.add(Box.createVerticalStrut(5), null);
        lmsValueBox.add(mValueLabel, null);
        lmsValueBox.add(Box.createVerticalStrut(5), null);
        lmsValueBox.add(sValueLabel, null);

        mainBox.add(colorPanel, null);
        mainBox.add(Box.createHorizontalStrut(15), null);
        mainBox.add(lmsLabelBox, null);
        mainBox.add(Box.createHorizontalStrut(5), null);
        mainBox.add(lmsProgressBox, null);
        mainBox.add(Box.createHorizontalStrut(5), null);
        mainBox.add(lmsValueBox, null);

        add(mainBox, null);
    }

}