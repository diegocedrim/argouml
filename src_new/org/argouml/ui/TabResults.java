// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.ui;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.PredicateFind;
import org.argouml.uml.TMResults;
import org.argouml.uml.cognitive.ChildGenRelated;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;

public class TabResults
        extends TabSpawnable
        implements
                Runnable,
                MouseListener,
                ActionListener,
                ListSelectionListener,
                KeyListener
{
    private static final Logger LOG = Logger.getLogger(TabResults.class);

    public static int _numJumpToRelated = 0;

    ////////////////////////////////////////////////////////////////
    // instance variables
    PredicateFind _pred;
    ChildGenerator _cg = null;
    Object _root = null;
    JSplitPane _mainPane;
    Vector _results = new Vector();
    Vector _related = new Vector();
    Vector _diagrams = new Vector();
    boolean _showRelated = false;

    JLabel _resultsLabel = new JLabel("Results:");
    JTable _resultsTable = new JTable(10, 4);
    TMResults _resultsModel = new TMResults();

    JLabel _relatedLabel = new JLabel("Related Elements:");
    JTable _relatedTable = new JTable(4, 4);
    TMResults _relatedModel = new TMResults();

    /**
     * The constructor.
     * 
     */
    public TabResults()
    {
	this(true);
    }

    /**
     * The constructor.
     * 
     * @param showRelated true if related results should be shown
     */
    public TabResults(boolean showRelated)
    {
	super("Results", true);
	_showRelated = showRelated;
	setLayout(new BorderLayout());

	JPanel resultsW = new JPanel();
	JScrollPane resultsSP = new JScrollPane(_resultsTable);
	resultsW.setLayout(new BorderLayout());
	resultsW.add(_resultsLabel, BorderLayout.NORTH);
	resultsW.add(resultsSP, BorderLayout.CENTER);
	_resultsTable.setModel(_resultsModel);
	_resultsTable.addMouseListener(this);
	_resultsTable.addKeyListener(this);
	_resultsTable.getSelectionModel().addListSelectionListener(
								   this);
	_resultsTable.setSelectionMode(
				       ListSelectionModel.SINGLE_SELECTION);
	resultsW.setMinimumSize(new Dimension(100, 100));

	JPanel relatedW = new JPanel();
	if (_showRelated) {
	    JScrollPane relatedSP = new JScrollPane(_relatedTable);
	    relatedW.setLayout(new BorderLayout());
	    relatedW.add(_relatedLabel, BorderLayout.NORTH);
	    relatedW.add(relatedSP, BorderLayout.CENTER);
	    _relatedTable.setModel(_relatedModel);
	    _relatedTable.addMouseListener(this);
	    _relatedTable.addKeyListener(this);
	    relatedW.setMinimumSize(new Dimension(100, 100));
	}

	if (_showRelated) {
	    _mainPane =
		new JSplitPane(JSplitPane.VERTICAL_SPLIT,
			       resultsW,
			       relatedW);
	    _mainPane.setDividerSize(2);
	    add(_mainPane, BorderLayout.CENTER);
	} else {
	    add(resultsW, BorderLayout.CENTER);
	}

    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @param p the predicate for the search
     */
    public void setPredicate(PredicateFind p)
    {
	_pred = p;
    }
    
    /**
     * @param root the root object for the search
     */
    public void setRoot(Object root)
    {
	_root = root;
    }
    
    public void setGenerator(ChildGenerator gen)
    {
	_cg = gen;
    }

    public void setResults(Vector res, Vector dia)
    {
	_results = res;
	_diagrams = dia;
	_resultsLabel.setText("Results: " + _results.size() + " items");
	_resultsModel.setTarget(_results, _diagrams);
	_relatedModel.setTarget(null, null);
	_relatedLabel.setText("Related Elements: ");
    }

    /**
     * @see org.argouml.ui.TabSpawnable#spawn()
     */
    public TabSpawnable spawn()
    {
	TabResults newPanel = (TabResults) super.spawn();
	if (newPanel != null) {
	    newPanel.setResults(_results, _diagrams);
	}
	return newPanel;
    }

    public void doDoubleClick()
    {
	myDoubleClick(_resultsTable);
    }

    public void selectResult(int index)
    {
	if (index < _resultsTable.getRowCount()) {
	    _resultsTable.getSelectionModel().setSelectionInterval(index,
								   index);
	}
    }

    ////////////////////////////////////////////////////////////////
    // ActionListener implementation

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
    }

    ////////////////////////////////////////////////////////////////
    // MouseListener implementation

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me)
    {
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me)
    {
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me)
    {
	if (me.getClickCount() >= 2)
	    myDoubleClick(me.getSource());
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me)
    {
    }
    
    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me)
    {
    }

    public void myDoubleClick(Object src)
    {
	Object sel = null;
	Diagram d = null;
	if (src == _resultsTable) {
	    int row = _resultsTable.getSelectionModel().getMinSelectionIndex();
	    if (row < 0)
		return;
	    sel = _results.elementAt(row);
	    d = (Diagram) _diagrams.elementAt(row);
	} else if (src == _relatedTable) {
	    int row = _relatedTable.getSelectionModel().getMinSelectionIndex();
	    if (row < 0)
		return;
	    _numJumpToRelated++;
	    sel = _related.elementAt(row);
	}

	if (d != null)
	    LOG.debug("go " + sel + " in " + d.getName());
	if (d != null)
	    TargetManager.getInstance().setTarget(d);
	TargetManager.getInstance().setTarget(sel);
    }

    ////////////////////////////////////////////////////////////////
    // KeyListener implementation

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    e.consume();
	    myDoubleClick(e.getSource());
	}
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e)
    {
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e)
    {
    }

    ////////////////////////////////////////////////////////////////
    // ListSelectionListener implementation

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent lse)
    {
	if (lse.getValueIsAdjusting()) {
	    return;
	}
	if (_showRelated) {
	    int row = lse.getFirstIndex();
	    Object sel = _results.elementAt(row);
	    LOG.debug("selected " + sel);
	    _related.removeAllElements();
	    Enumeration elems =
		ChildGenRelated.getSingleton().gen(sel);
	    if (elems != null) {
		while (elems.hasMoreElements()) {
		    _related.addElement(elems.nextElement());
		}
	    }
	    _relatedModel.setTarget(_related, null);
	    _relatedLabel.setText("Related Elements: "
				  + _related.size()
				  + " items");
	}
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
	_resultsLabel.setText("Searching...");
	_results.removeAllElements();
	depthFirst(_root, null);
	setResults(_results, _diagrams);
	_resultsLabel.setText("Results: " + _results.size() + " items");
	_resultsModel.setTarget(_results, _diagrams);
    }

    public void depthFirst(Object node, Diagram lastDiagram)
    {
	if (node instanceof Diagram) {
	    lastDiagram = (Diagram) node;
	    if (!_pred.matchDiagram(lastDiagram))
		return;
	    // diagrams are not placed in search results
	}
	Enumeration elems = _cg.gen(node);
	while (elems.hasMoreElements()) {
	    Object c = elems.nextElement();
	    if (_pred.predicate(c)) {
		_results.addElement(c);
		_diagrams.addElement(lastDiagram);
	    }
	    depthFirst(c, lastDiagram);
	}
    }

} /* end class TabResults */
