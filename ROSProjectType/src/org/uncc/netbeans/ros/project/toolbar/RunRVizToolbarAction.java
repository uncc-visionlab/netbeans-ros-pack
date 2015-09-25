/* 
 * Copyright (C) 2015 Andrew Willis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.uncc.netbeans.ros.project.toolbar;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
//import org.netbeans.api.debugger.DebuggerManager;
//import org.netbeans.api.debugger.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
//import org.netbeans.spi.debugger.ui.AttachType;
//import org.netbeans.spi.debugger.ui.Controller;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.support.MainProjectSensitiveActions;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.Actions;
import org.openide.awt.DropDownButtonFactory;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.WeakSet;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Martin Entlicher
 * Derived from:
 * org.netbeans.modules.debugger.ui.actions.DebugMainProjectAction
 */
@ActionID(
        category = "Project",
        id = "org.uncc.netbeans.ros.project.toolbar.RunRVizToolbarAction"
)
@ActionRegistration( displayName = "#CTL_RunRVizToolbarAction", lazy = true)
@ActionReference(path = "Toolbars/Memory", position = 2100)
@NbBundle.Messages("CTL_RunRVizToolbarAction=Run RViz with Drop-down Options")

public class RunRVizToolbarAction implements Action, Presenter.Toolbar, PopupMenuListener {

    private static WeakSet<AttachHistorySupport> ahs = null;

    private Action delegate;
    private AttachHistorySupport attachHistorySupport;

    /**
     * Creates a new instance of DebugMainProjectAction
     */
    public RunRVizToolbarAction() {
        delegate = MainProjectSensitiveActions.mainProjectCommandAction(
                ActionProvider.COMMAND_DEBUG,
//                NbBundle.getMessage(DebugMainProjectAction.class, "LBL_DebugMainProjectAction_Name"), ImageUtilities.loadImageIcon("org/uncc/netbeans/ros/project/toolbar/rviz.png", false)); // NOI18N
//        delegate.putValue("iconBase", "org/netbeans/modules/debugger/resources/debugProject.png"); //NOI18N
        NbBundle.getMessage(RunRVizToolbarAction.class, "CTL_RunRVizToolbarAction"), ImageUtilities.loadImageIcon("org/uncc/netbeans/ros/project/toolbar/rviz.png", false)); // NOI18N
        delegate.putValue("iconBase", "org/uncc/netbeans/ros/project/toolbar/rviz.png"); //NOI18N
        attachHistorySupport = new AttachHistorySupport();
    }

    @Override
    public Object getValue(String arg0) {
        return delegate.getValue(arg0);
    }

    @Override
    public void putValue(String arg0, Object arg1) {
        delegate.putValue(arg0, arg1);
    }

    @Override
    public void setEnabled(boolean arg0) {
        delegate.setEnabled(arg0);
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener arg0) {
        delegate.addPropertyChangeListener(arg0);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener arg0) {
        delegate.removePropertyChangeListener(arg0);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        Project p = OpenProjects.getDefault().getMainProject();
//        GestureSubmitter.logDebugProject(p);
        delegate.actionPerformed(arg0);
    }

    @Override
    public Component getToolbarPresenter() {
        JPopupMenu menu = new JPopupMenu();
        JButton button = DropDownButtonFactory.createDropDownButton(
                new ImageIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB)), menu);
        final JMenuItem item = new JMenuItem(Actions.cutAmpersand((String) delegate.getValue("menuText")));
        item.setEnabled(delegate.isEnabled());

        delegate.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propName = evt.getPropertyName();
                if ("enabled".equals(propName)) {
                    item.setEnabled((Boolean) evt.getNewValue());
                } else if ("menuText".equals(propName)) {
                    item.setText(Actions.cutAmpersand((String) evt.getNewValue()));
                }
            }
        });

        menu.add(item);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                DebugMainProjectAction.this.actionPerformed(e);
            }
        });
        try {
            Action ca = Actions.forID("Debug", "org.netbeans.modules.debugger.ui.actions.ConnectAction");
            JMenuItem item2 = new JMenuItem(Actions.cutAmpersand((String) ca.getValue(NAME)));
            Actions.connect(item2, ca);
            menu.add(item2);
        } catch (Exception nsee) {
            Exceptions.printStackTrace(nsee);
        }

        menu.addPopupMenuListener(this);

        Actions.connect(button, this);
        return button;
    }

    static synchronized void attachHistoryChanged() {
        if (ahs == null) {
            return;
        }
        for (AttachHistorySupport support : ahs) {
            support.computeItems();
        }
    }

    private static synchronized void addAttachHistorySupport(AttachHistorySupport support) {
        if (ahs == null) {
            ahs = new WeakSet<AttachHistorySupport>();
        }
        ahs.add(support);
    }

    // PopupMenuListener ........................................................
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        JPopupMenu menu = (JPopupMenu) e.getSource();
        attachHistorySupport.init(menu);
        menu.removePopupMenuListener(this);
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    // AttachHistorySupport .....................................................
    static class AttachHistorySupport implements ActionListener {

        private JPopupMenu menu;
        private JMenuItem[] items = new JMenuItem[0];
        private JSeparator separator = new JPopupMenu.Separator();
        private static final RequestProcessor RP = new RequestProcessor(AttachHistorySupport.class.getName());

        public void init(JPopupMenu menu) {
            this.menu = menu;
            addAttachHistorySupport(this);
            computeItems();
        }

        public void computeItems() {
            menu.remove(separator);
            for (int x = 0; x < items.length; x++) {
                menu.remove(items[x]);
            } // for
//            Properties props = Properties.getDefault().getProperties("debugger").getProperties("last_attaches");
//            Integer[] usedSlots = (Integer[]) props.getArray("used_slots", new Integer[0]);
//            if (usedSlots.length > 0) {
//                menu.add(separator);
//            }
//            items = new JMenuItem[usedSlots.length];
//            for (int x = 0; x < usedSlots.length; x++) {
//                String dispName = props.getProperties("slot_" + usedSlots[x]).getString("display_name", "<???>"); // NOI18N
//                items[x] = new JMenuItem(dispName);
//                items[x].addActionListener(this);
//                menu.add(items[x]);
//            } // for
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();
            int index = -1;
            for (int x = 0; x < items.length; x++) {
                if (items[x] == item) {
                    index = x;
                    break;
                }
            }
            if (index == -1) {
                return;
            } // should not occure
            final int findex = index;
            RP.post(new Runnable() {
                @Override
                public void run() {
                    perform(findex);
                }
            });
        }

        private void perform(int index) {
//            Properties props = Properties.getDefault().getProperties("debugger").getProperties("last_attaches");
//            Integer[] usedSlots = (Integer[]) props.getArray("used_slots", new Integer[0]);
//            String attachTypeName = props.getProperties("slot_" + usedSlots[index]).getString("attach_type", "???");
//            List types = DebuggerManager.getDebuggerManager().lookup(null, AttachType.class);
//            AttachType att = null;
//            for (Object t : types) {
//                AttachType at = (AttachType) t;
//                if (attachTypeName.equals(at.getTypeDisplayName())) {
//                    att = at;
//                    break;
//                }
//            } // for
//            if (att != null) {
//                final AttachType attachType = att;
//                final Controller[] controllerPtr = new Controller[]{null};
//                try {
//                    SwingUtilities.invokeAndWait(new Runnable() {
//                        @Override
//                        public void run() {
//                            JComponent customizer = attachType.getCustomizer();
//                            Controller controller = attachType.getController();
//                            if (controller == null && (customizer instanceof Controller)) {
//                                Exceptions.printStackTrace(new IllegalStateException("FIXME: JComponent " + customizer + " must not implement Controller interface!"));
//                                controller = (Controller) customizer;
//                            }
//                            controllerPtr[0] = controller;
//                        }
//                    });
//                } catch (InterruptedException ex) {
//                    Exceptions.printStackTrace(ex);
//                    return;
//                } catch (InvocationTargetException ex) {
//                    Exceptions.printStackTrace(ex);
//                    return;
//                }
//                final Controller controller = controllerPtr[0];
//                Method loadMethod = null;
//                try {
//                    loadMethod = controller.getClass().getMethod("load", Properties.class);
//                } catch (NoSuchMethodException ex) {
//                } catch (SecurityException ex) {
//                }
//                if (loadMethod == null) {
//                    return;
//                }
//                try {
//                    Boolean result = (Boolean) loadMethod.invoke(controller, props.getProperties("slot_" + usedSlots[index]).getProperties("values"));
//                    if (!result) {
//                        return; // [TODO] not loaded, cannot be used to attach
//                    }
//                } catch (IllegalAccessException ex) {
//                } catch (IllegalArgumentException ex) {
//                } catch (InvocationTargetException ex) {
//                }
//                final boolean[] passedPtr = new boolean[]{false};
//                try {
//                    SwingUtilities.invokeAndWait(new Runnable() {
//                        @Override
//                        public void run() {
//                            passedPtr[0] = controller.ok();
//                        }
//                    });
//                } catch (InterruptedException ex) {
//                    Exceptions.printStackTrace(ex);
//                    return;
//                } catch (InvocationTargetException ex) {
//                    Exceptions.printStackTrace(ex);
//                    return;
//                }
//                if (passedPtr[0]) {
//                    makeFirst(index);
////                    GestureSubmitter.logAttach(attachTypeName);
//                }
//                //return;
//            } else {
//                // report failure - attach type not found
//                StatusDisplayer.getDefault().setStatusText(NbBundle.getMessage(DebugMainProjectAction.class, "CTL_Attach_Type_Not_Found"));
//            }
        }

        private void makeFirst(int index) {
//            if (index == 0) {
//                return;  // nothing to do
//            }
//            Properties props = Properties.getDefault().getProperties("debugger").getProperties("last_attaches");
//            Integer[] usedSlots = (Integer[]) props.getArray("used_slots", new Integer[0]);
//            int temp = usedSlots[index];
//            for (int x = index; x > 0; x--) {
//                usedSlots[x] = usedSlots[x - 1];
//            }
//            usedSlots[0] = temp;
//            props.setArray("used_slots", usedSlots);
//            attachHistoryChanged();
        }

    } // AttachHistorySupport

}
