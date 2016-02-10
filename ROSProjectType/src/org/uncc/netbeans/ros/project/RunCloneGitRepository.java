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
package org.uncc.netbeans.ros.project;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
//import org.netbeans.modules.git.ui.actions.ContextHolder;
//import org.netbeans.modules.git.ui.clone.CloneAction;
//import org.netbeans.modules.versioning.spi.VCSContext;
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
            if (p instanceof ROSProject && false) {
                setEnabled(true);
                putValue(NAME, actionName);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            new CloneAction(new ContextHolder(VCSContext.EMPTY)).actionPerformed(e);
//            UNUSEDRunGetGitRepository runws = new UNUSEDRunGetGitRepository();
//            String homeDir = p.getProjectDirectory().getPath();
//            RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, cmds);
        }
    }
}
