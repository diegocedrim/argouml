// Copyright (c) 1996-99 The Regents of the University of California. All
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




package uci.uml.ui.style;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.plaf.basic.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import uci.ui.*;
import uci.gef.*;
import uci.uml.ui.*;


public class StylePanelFigClass extends StylePanelFig {

  ////////////////////////////////////////////////////////////////
  // constants

  ////////////////////////////////////////////////////////////////
  // instance vars

  ////////////////////////////////////////////////////////////////
  // contructors

  public StylePanelFigClass() {
    super();
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.ipadx = 0; c.ipady = 0;

  }


  ////////////////////////////////////////////////////////////////
  // accessors

//   public void refresh() {
//     super.refresh();
//   }


  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
//     Document bboxDoc = _bboxField.getDocument();
//     if (e.getDocument() == bboxDoc) setTargetBBox();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }



  public void itemStateChanged(ItemEvent e) {
    //    Object src = e.getSource();
    //     if (src == _fillField) setTargetFill();
    //     else if (src == _lineField) setTargetLine();
    //     else if (src == _dashedField) setTargetDashed();
    // else
    super.itemStateChanged(e);
  }


} /* end class StylePanelFigClass */

