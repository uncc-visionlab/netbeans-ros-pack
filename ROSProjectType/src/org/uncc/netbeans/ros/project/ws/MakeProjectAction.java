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
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.loaders.DataFolder.FolderNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;

import org.openide.util.NbBundle.Messages;

//@ActionID(category = "File", id = "org.netbeans.modules.cnd.ROSWorkspaceAction")
@ActionID(category = "File", id = "org.uncc.netbeans.ros.project.ws.ROSWorkspaceFileAction")
@ActionRegistration(displayName = "#CTL_TestAction1")
@ActionReference(path = "Loaders/folder/any/Actions", position = 0)
@Messages("CTL_TestAction1=Test")
public final class MakeProjectAction extends AbstractAction implements ContextAwareAction {

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

        private final Project p=null;

        public ContextAction(Lookup context) {
            DataObject dobj = context.lookup(DataObject.class);
            Node n = dobj.getNodeDelegate();
            if (n != null && n instanceof FolderNode && n.getName().equals("ros_ws")) {
                setEnabled(true);
                System.out.println("HERE");
                putValue(NAME, "Action on Workspace Folder");
            }
            putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        }

        public @Override
        void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "===> running action");
            for (Project p : OpenProjects.getDefault().getOpenProjects()) {
                ROSWorkspaceProjectService s = p.getLookup().lookup(ROSWorkspaceProjectService.class);
                if (s != null) {
                    JOptionPane.showMessageDialog(null, "===> got a service: " + s.m());
                } else {
                    JOptionPane.showMessageDialog(null, "===> nothing for " + p);
                }
            }
        }
    }
}
