// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggablePropertyPanel;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.events.ArgoModuleEventListener;
import org.argouml.model.Model;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.AbstractArgoJPanel;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.PropPanelDiagram;
import org.argouml.uml.diagram.ui.PropPanelString;
import org.argouml.uml.diagram.ui.PropPanelUMLActivityDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLClassDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLCollaborationDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLDeploymentDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLSequenceDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLStateDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLUseCaseDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.argouml.uml.ui.foundation.core.PropPanelClass;
import org.argouml.util.ConfigLoader;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;

/**
 * This is the tab on the details panel (DetailsPane) that holds the property
 * panel. On change of target, the property panel in TabProps is changed. <p>
 *
 * With the introduction of the TargetManager,
 * this class holds its original power
 * of controlling its target. The property panels (subclasses of PropPanel) for
 * which this class is the container are being registered as TargetListeners in
 * the setTarget method of this class.
 * They are not registered with TargetManager
 * but with this class to prevent race-conditions while firing TargetEvents from
 * TargetManager.
 */
public class TabProps
    extends AbstractArgoJPanel
    implements TabModelTarget, ArgoModuleEventListener {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TabProps.class);
    ////////////////////////////////////////////////////////////////
    // instance variables
    private boolean shouldBeEnabled = false;
    private JPanel blankPanel = new JPanel();
    private Hashtable panels = new Hashtable();
    private JPanel lastPanel = null;
    private String panelClassBaseName = "";

    private Object target;

    /**
     * The list with targetlisteners, these are the property panels
     * managed by TabProps.
     * It should only contain one listener at a time.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * The constructor.
     *
     */
    public TabProps() {
        this("tab.properties", "ui.PropPanel");
    }

    /**
     * The constructor.
     *
     * @param tabName the name of the tab
     * @param panelClassBase the panel class base
     */
    public TabProps(String tabName, String panelClassBase) {
        super(tabName);
        TargetManager.getInstance().addTarget(this);
        setOrientation(ConfigLoader.getTabPropsOrientation());
        panelClassBaseName = panelClassBase;
        setLayout(new BorderLayout());
        //setFont(new Font("Dialog", Font.PLAIN, 10));

        ArrayList list = Argo.getPlugins(PluggablePropertyPanel.class);
        ListIterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            PluggablePropertyPanel ppp = (PluggablePropertyPanel) o;
            panels.put(ppp.getClassForPanel(), ppp.getPropertyPanel());
        }

        ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);

        initPanels();
    }

    /**
     * @see java.lang.Object#finalize()
     */
    public void finalize() throws Throwable {
        super.finalize();
        ArgoEventPump.removeListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
    }

    /**
     * Set the orientation of the property panel.
     *
     * @param orientation the new orientation for this property panel
     *
     * @see org.tigris.swidgets.Orientable#setOrientation(org.tigris.swidgets.Orientation)
     */
    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
        Enumeration pps = panels.elements();
        while (pps.hasMoreElements()) {
            Object o = pps.nextElement();
            if (o instanceof Orientable) {
                Orientable orientable = (Orientable) o;
                orientable.setOrientation(orientation);
            }
        }
    }

    /**
     * Preload property panels that are commonly used within the first
     * few seconds after the tool is launched.
     */
    protected void initPanels() {

        panels.put(Model.getMetaTypes().getUMLClass(), new PropPanelClass());
        panels.put(ArgoDiagram.class, new PropPanelDiagram());

        // Put all the diagram PropPanels here explicitly. They would eventually
        // pick up their superclass ArgoDiagram, but in the meantime
        // panelClassFor() would moan that they can't be found.

        // Note that state digrams do actually have a diagram property panel!

        panels.put(UMLActivityDiagram.class,
                new PropPanelUMLActivityDiagram());
        panels.put(UMLClassDiagram.class,
                new PropPanelUMLClassDiagram());
        panels.put(UMLCollaborationDiagram.class,
                new PropPanelUMLCollaborationDiagram());
        panels.put(UMLDeploymentDiagram.class,
                new PropPanelUMLDeploymentDiagram());
        panels.put(UMLSequenceDiagram.class,
                new PropPanelUMLSequenceDiagram());
        panels.put(UMLStateDiagram.class,
                new PropPanelUMLStateDiagram());
        panels.put(UMLUseCaseDiagram.class,
                new PropPanelUMLUseCaseDiagram());

        // FigText has no owner, so we do it directly
        panels.put(FigText.class, new PropPanelString());
        // now a plugin
        // panels.put(MModelImpl.class, new PropPanelModel());
        // panels.put((Class)Model.getFacade().USE_CASE/*MUseCaseImpl.class*/,
        //                 new PropPanelUseCase());
    }

    /**
     * Adds a property panel to the internal list. This allows a plugin to
     * add / register a new property panel at run-time.
     * This property panel will then
     * be displayed in the detatils pane whenever an element
     * of the given metaclass is selected.
     *
     * @param c the metaclass whose details show be displayed
     *          in the property panel p
     * @param p an instance of the property panel for the metaclass m
     *
     */
    public void addPanel(Class c, PropPanel p) {
        panels.put(c, p);
    }


    ////////////////////////////////////////////////////////////////
    // accessors
    /**
     * Sets the target of the property panel. The given target t
     * may either be a Diagram or a modelelement. If the target
     * given is a Fig, a check is made if the fig has an owning
     * modelelement and occurs on the current diagram.
     * If so, that modelelement is the target.
     *
     * @deprecated As of ArgoUml version 0.13.5,
     *         the visibility of this method will change in the future,
     *         replaced by {@link org.argouml.ui.targetmanager.TargetManager}.
     *
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        // targets ought to be modelelements or diagrams
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        if (!(t == null || Model.getFacade().isABase(t)
                || t instanceof ArgoDiagram)) {
            return;
        }

        if (lastPanel != null) {
            remove(lastPanel);
            if (lastPanel instanceof TargetListener) {
                removeTargetListener((TargetListener) lastPanel);
            }
        }
        target = t;
        if (t == null) {
            add(blankPanel, BorderLayout.CENTER);
            shouldBeEnabled = false;
            lastPanel = blankPanel;
        } else {
            shouldBeEnabled = true;
            TabModelTarget newPanel = null;
            Class targetClass = t.getClass();
            while (newPanel == null) {
                newPanel = findPanelFor(targetClass);
                targetClass = targetClass.getSuperclass();
                if (targetClass == java.lang.Object.class) {
                    break;
                }
            }
            if (newPanel != null) {
                addTargetListener(newPanel);
            }
            if (newPanel instanceof JPanel) {
                add((JPanel) newPanel, BorderLayout.CENTER);
                shouldBeEnabled = true;
                lastPanel = (JPanel) newPanel;
            } else {
                add(blankPanel, BorderLayout.CENTER);
                shouldBeEnabled = false;
                lastPanel = blankPanel;
            }

        }
    }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(TargetManager.getInstance().getTarget());
    }

    /**
     * @param targetClass the target class
     * @return the tab model target
     */
    public TabModelTarget findPanelFor(Class targetClass) {
        TabModelTarget p = (TabModelTarget) panels.get(targetClass);
        LOG.info("Getting prop panel for:" + targetClass + ", "
                + "found (in cache?) " + p);
        if (p == null) {
            Class panelClass = panelClassFor(targetClass);
            if (panelClass == null) {
                return null;
            }
            LOG.info("panelClass for found: " + panelClass);
            try {
                // if a class is abstract we do not need to try
                // to instantiate it.
                if (Modifier.isAbstract(panelClass.getModifiers())) {
                    return null;
                }
                p = (TabModelTarget) panelClass.newInstance();
                // moved next line inside try block to avoid filling
                // the hashmap with bogus values.
                panels.put(targetClass, p);
            } catch (IllegalAccessException ignore) {
                // doubtfull if this must be ignored.
                LOG.error(ignore);
                return null;
            } catch (InstantiationException ignore) {
                // doubtfull if this must be ignored.
                LOG.error(ignore);
                return null;
            }

        } else {
            LOG.info("found props for " + targetClass.getName());
        }
        return p;
    }

    /**
     * Locate the panel for the given class.
     *
     * @param targetClass the given class
     * @return the properties panel for the given class, or null if not found
     */
    public Class panelClassFor(Class targetClass) {
        String panelClassName = "";
        String pack = "org.argouml.uml";
        String base = "";

        String targetClassName = targetClass.getName();
        LOG.info("Trying to locate panel for: " + targetClassName);
        int lastDot = targetClassName.lastIndexOf(".");

        if (targetClassName.startsWith("ru.novosoft.uml.")) {
            //remove "ru.novosoft.uml."
            if (lastDot > 0) {
                base = targetClassName.substring(16, lastDot + 1);
            } else {
                base = targetClassName.substring(16);
            }
            
            if (lastDot > 0) {
                targetClassName = targetClassName.substring(lastDot + 1);
            }

            if (targetClassName.startsWith("M")) {
                targetClassName = targetClassName.substring(1); //remove M
            }
            if (targetClassName.endsWith("Impl")) {
                targetClassName =
                    targetClassName.substring(0, targetClassName.length() - 4);
            }
        } else {
            //remove "org.omg.uml."
            if (lastDot > 0) {
                base = targetClassName.substring(12, lastDot + 1);
            } else {
                base = targetClassName.substring(12);
            }
            
            if (lastDot > 0) {
                targetClassName = targetClassName.substring(lastDot + 1);
            }

            if (targetClassName.startsWith("Uml")) {
                targetClassName = targetClassName.substring(3); //remove Uml
            }
            if (targetClassName.indexOf('$') > 0) {
                targetClassName =
                    targetClassName.substring(0, targetClassName.indexOf('$'));
            }
        }

        // This doesn't work for panel property tabs - they are being put in the
        // wrong place. Really we should have defined these are preloaded them
        // along with ArgoDiagram in initPanels above.

        try {
            panelClassName =
                pack + ".ui." + base + "PropPanel" + targetClassName;
            LOG.info("Looking for: " + panelClassName);
            return Class.forName(panelClassName);
        } catch (ClassNotFoundException ignore) {
            LOG.error(
		      "Class " + panelClassName + " for Panel not found!",
		      ignore);
        }
        return null;
    }

    /**
     * @return the name
     */
    protected String getClassBaseName() {
        return panelClassBaseName;
    }

    /**
     * Returns the current target.
     * @deprecated As of ArgoUml version 0.13.5,
     * the visibility of this method will change in the future, replaced by
     * {@link org.argouml.ui.targetmanager.TargetManager#getTarget()
     * TargetManager.getInstance().getTarget()}.
     *
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Determines if the property panel should be enabled. Returns true if it
     * should be enabled. The property panel should allways be enabled if the
     * target is an instance of a modelelement or an argodiagram.
     * If the target given is a Fig, a check is made if the fig
     * has an owning modelelement and occurs on
     * the current diagram. If so, that modelelement is the target.
     *
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(Object)
     */
    public boolean shouldBeEnabled(Object t) {
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        if (t instanceof Diagram || Model.getFacade().isABase(t)) {
            shouldBeEnabled = true;
        } else {
            shouldBeEnabled = false;
        }

        return shouldBeEnabled;
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleLoaded(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleLoaded(ArgoModuleEvent event) {
        if (event.getSource() instanceof PluggablePropertyPanel) {
            PluggablePropertyPanel p =
                (PluggablePropertyPanel) event.getSource();
            panels.put(p.getClassForPanel(), p.getPropertyPanel());

        }
    }
    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleUnloaded(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleUnloaded(ArgoModuleEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleEnabled(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleEnabled(ArgoModuleEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleDisabled(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleDisabled(ArgoModuleEvent event) {
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
        fireTargetAdded(e);
        if (listenerList.getListenerCount() > 0) {
            validate();
            repaint();
        }

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        //setTarget(e.getNewTarget());
        fireTargetRemoved(e);
        //validate();
        //repaint();
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
        fireTargetSet(e);
        validate();
        repaint();
    }

    /**
     * Adds a listener.
     * @param listener the listener to add
     */
    private void addTargetListener(TargetListener listener) {
        listenerList.add(TargetListener.class, listener);
    }

    /**
     * Removes a target listener.
     * @param listener the listener to remove
     */
    private void removeTargetListener(TargetListener listener) {
        listenerList.remove(TargetListener.class, listener);
    }

    private void fireTargetSet(TargetEvent targetEvent) {
        //      Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
		((TargetListener) listeners[i + 1]).targetSet(targetEvent);
            }
        }
    }

    private void fireTargetAdded(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
		((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
            }
        }
    }

    private void fireTargetRemoved(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
                ((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
            }
        }
    }

} /* end class TabProps */

