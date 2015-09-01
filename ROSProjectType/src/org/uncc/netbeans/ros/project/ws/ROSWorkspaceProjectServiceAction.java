/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.ws;

/**
 *
 * @author arwillis
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;

import org.openide.util.NbBundle.Messages;
import org.uncc.netbeans.ros.project.ROSProject;

//@ActionID(category = "File", id = "org.netbeans.modules.cnd.ROSWorkspaceAction")
@ActionID(category = "File", id = "org.uncc.netbeans.ros.project.ws.ROSWorkspaceProjectAction")
@ActionRegistration(displayName = "#CTL_TestAction")
@ActionReference(path = "Project/Actions", position = 0)
@Messages("CTL_TestAction=Test")
public final class ROSWorkspaceProjectServiceAction extends AbstractAction implements ContextAwareAction {

    public @Override
    void actionPerformed(ActionEvent e) {
        assert false;
    }

    public @Override
    Action createContextAwareInstance(Lookup context) {
        return new ContextAction(context);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    private static final class ContextAction extends AbstractAction {

        private final Project p;

        public ContextAction(Lookup context) {
            p = context.lookup(Project.class);
            String name = ProjectUtils.getInformation(p).getDisplayName();
            if (p instanceof org.netbeans.modules.cnd.makeproject.MakeProject) {
                setEnabled(true);
                putValue(NAME, "Test");
            }
//            putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
            putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, false);
            // TODO menu item label with optional mnemonics
            //putValue(NAME, "&Info on " + name);
        }

        public @Override
        void actionPerformed(ActionEvent e) {
//            JOptionPane.showMessageDialog(null, "===> running action");
            for (Project p : OpenProjects.getDefault().getOpenProjects()) {
                ROSWorkspaceProjectService s = p.getLookup().lookup(ROSWorkspaceProjectService.class);
                if (s != null) {
//                    JOptionPane.showMessageDialog(null, "===> got a service: " + s.m());
                } else {
//                    JOptionPane.showMessageDialog(null, "===> nothing for " + p);
                }
            }
        }
    }
}
