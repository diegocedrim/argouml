// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

/*
 * ArrowButton.java
 */
package org.argouml.swingext;

import javax.swing.border.Border;
import java.awt.*;

/**
 * A metal look and feel arrow button that can be created to point to
 * a compass point.
 *
 * @author Bob Tarling
 */
public class ArrowButton extends javax.swing.JButton {

    /** Construct an ArrowButton pointing in the given direction
     *
     * @param direction the direction the arrow will point, this being
     * one of the constants NORTH, SOUTH, EAST, WEST
     * @param border the border for the arrow
     */
    public ArrowButton(int direction, Border border) {
        this(direction);
        setBorder(border);
    }

    /** Construct an ArrowButton pointing in the given direction
     *
     * @param direction the direction the arrow will point, this being
     * one of the constants NORTH, SOUTH, EAST, WEST
     */    
    public ArrowButton(int direction) {
        super();
        ArrowIcon arrowIcon = new ArrowIcon(direction);
        setIcon(arrowIcon);
        super.setFocusPainted(false);
        setPreferredSize(new Dimension(arrowIcon.getIconWidth(),
				       arrowIcon.getIconHeight()));
        setMinimumSize(new Dimension(arrowIcon.getIconWidth(),
				     arrowIcon.getIconHeight()));
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * @see java.awt.Component#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        return new Dimension(getIcon().getIconWidth(),
			     getIcon().getIconHeight());
    }
}
