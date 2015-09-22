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
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.RunCatkinCleanBuildPackage;

//@ActionID(category = "File", id = "org.netbeans.modules.cnd.ROSWorkspaceAction")
@ActionID(category = "File", id = "org.uncc.netbeans.ros.project.ws.ROSPkgCleanBuildAction")
@ActionRegistration(displayName = "#CTL_ROSPkgCleanBuildAction")
@ActionReference(path = "Loaders/folder/any/Actions", position = 0)
@Messages("CTL_ROSPkgCleanBuildAction=Clean Build this package with catkin_make")
public final class ROSPkgCleanBuildAction extends AbstractAction implements ContextAwareAction {
// cd ${project.dir}/${ros.ws}
// catkin_make --pkg ${package_folder_name} --make-args clean

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
        String packageName;

        public ContextAction(Lookup context) {
            DataObject dobj = context.lookup(DataObject.class);
            //Node n = dobj.getNodeDelegate();
            project = ROSProject.findROSProject(dobj.getPrimaryFile());
            if (ROSProject.isROSPackageFolder(dobj.getPrimaryFile())) {
                packageName = dobj.getName();
                setEnabled(true);
                putValue(NAME, "Clean and Build this package with catkin_make");
            }
            putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
        }

        public @Override
        void actionPerformed(ActionEvent e) {
            if (packageName != null) {
                RunCatkinCleanBuildPackage job = new RunCatkinCleanBuildPackage(project,
                        packageName);
                job.run(project);
            }
        }
    }
}
