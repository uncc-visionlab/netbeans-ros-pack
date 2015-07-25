/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.modules.git.ui.actions.ContextHolder;
import org.netbeans.modules.git.ui.clone.CloneAction;
import org.netbeans.modules.versioning.spi.VCSContext;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;

@ActionID(
        category = "Project",
        id = "org.uncc.netbeans.ros.project.RunCloneGitRepository"
)
@ActionReference(path = "Projects/Actions")
public class RunCloneGitRepository extends AbstractAction implements ContextAwareAction {

    ROSProject project;
    String actionName = "Clone Git Repository";
    public static String[] cmds;

    public RunCloneGitRepository(ROSProject project) {
        this.project = project;
        String rosRoot = project.getProperty(ROSProject.ROS_ROOTFOLDER_PROPERTYNAME);
        String rosWorkspace = project.getProperty(ROSProject.ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String rosSource = project.getProperty(ROSProject.ROS_SOURCEFOLDER_PROPERTYNAME);
        cmds = new String[]{
            "source " + rosRoot + "/setup.bash\n",
            "cd " + rosWorkspace + File.separator + rosSource + "\n",
            "wstool update\n",
            "exit"
        };
    }

    public @Override
    void actionPerformed(ActionEvent e) {
        assert false;
    }

    public @Override
    Action createContextAwareInstance(Lookup context) {
        return new RunCloneGitRepository.GitContextAction(context);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    private final class GitContextAction extends AbstractAction {

        private final Project p;

        public GitContextAction(Lookup context) {
            p = context.lookup(Project.class);
            String name = ProjectUtils.getInformation(p).getDisplayName();
            if (p instanceof ROSProject) {
                setEnabled(true);
                putValue(NAME, actionName);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new CloneAction(new ContextHolder(VCSContext.EMPTY)).actionPerformed(e);
//            UNUSEDRunGetGitRepository runws = new UNUSEDRunGetGitRepository();
//            String homeDir = p.getProjectDirectory().getPath();
//            RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, cmds);
        }
    }
}
