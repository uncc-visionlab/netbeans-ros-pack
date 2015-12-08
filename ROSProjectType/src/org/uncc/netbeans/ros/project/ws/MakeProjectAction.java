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
package org.uncc.netbeans.ros.project.ws;

/**
 *
 * @author arwillis
 */
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;

import org.openide.util.NbBundle.Messages;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.RunCatkinCreatePackage;

//@ActionID(category = "File", id = "org.netbeans.modules.cnd.ROSWorkspaceAction")
@ActionID(category = "File", id = "org.uncc.netbeans.ros.project.ws.MakeProjectAction")
@ActionRegistration(displayName = "#CTL_MakeProjectAction", lazy = true)
@ActionReference(path = "Loaders/folder/any/Actions", position = 0)
@Messages("CTL_MakeProjectAction=Create ROS Package")
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

        private final ROSProject project;

        public ContextAction(Lookup context) {
            DataObject dobj = context.lookup(DataObject.class);
            //Node n = dobj.getNodeDelegate();
            project = ROSProject.findROSProject(dobj.getPrimaryFile());
            if (ROSProject.isROSWorkspaceFolder(dobj.getPrimaryFile())) {
                setEnabled(true);
                putValue(NAME, "Create a new package");
            }
            putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        }

        public @Override
        void actionPerformed(ActionEvent e) {
//            JOptionPane.showMessageDialog(null, "===> running action");
            CreateROSPackageDialog d = new CreateROSPackageDialog(null, "test");
            d.setVisible(true);
            String packageName = d.getPackageName();
            String dependenciesStr = d.getDependencies();
            if (packageName != null) {
                dependenciesStr = (dependenciesStr == null) ? "" : dependenciesStr;
                System.out.println("Creating package " + packageName
                        + " with dependencies " + dependenciesStr);
                RunCatkinCreatePackage job = new RunCatkinCreatePackage(project,
                        packageName, dependenciesStr);
                job.run(project);
            }
//            for (Project p : OpenProjects.getDefault().getOpenProjects()) {                
//                ROSWorkspaceProjectService s = p.getLookup().lookup(ROSWorkspaceProjectService.class);
//                if (s != null) {
////                    JOptionPane.showMessageDialog(null, "===> got a service: " + s.m());
//                } else {
////                    JOptionPane.showMessageDialog(null, "===> nothing for " + p);
//                }
//            }
        }
    }
}
