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
package org.uncc.netbeans.ros.filetype.launch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.RunInNetbeansTerminal;

@ActionID(
        category = "File",
        id = "org.uncc.netbeans.ros.filetype.launch.RoslaunchOpen"
)
@ActionRegistration(
        displayName = "#CTL_RoslaunchOpen"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 250),
    @ActionReference(path = "Loaders/text/x-roslaunch+xml/Actions", position = 250)
})
@NbBundle.Messages({"CTL_RoslaunchOpen=Open with roslaunch"})
public final class RoslaunchOpen implements ActionListener {

    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    

    private final LaunchDataObject context;
    ROSProject project;

    public RoslaunchOpen(LaunchDataObject context) {
        project = ROSProject.findROSProject(context.getPrimaryFile());
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
        String rosRootFolder = project.getProperty(ROSProject.ROS_ROOTFOLDER_PROPERTYNAME);
        String wsFolder = project.getProperty(ROSProject.ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String wsDevelFolder = project.getProperty(ROSProject.ROS_DEVELFOLDER_PROPERTYNAME);
        FileObject develFolder = project.getProjectDirectory().getFileObject(wsFolder)
                .getFileObject(wsDevelFolder);
        FileObject installSetup = develFolder.getFileObject("setup.bash");
        if (installSetup == null) {
            // abort the run --> no install directory available
            //installSetupPath = project.getProjectDirectory().getPath() + "/"
            //        + wsFolder + "/" + wsInstallFolder + "/";
            return;
        }
        String installSetupPath = installSetup.getPath();
        String packageName = project.getPackageName(context);
        String homeDir = project.getProjectDirectory().getPath();
        String actionName = "roscore";
        actionName = "roslaunch";
        String launchfilename = context.getPrimaryFile().getNameExt();
        commandList = new String[]{
            "source " + rosRootFolder + "/setup.bash\n",
            "source " + installSetupPath + "\n",
            "roslaunch " + packageName + " " + launchfilename + "\n",
            "exit"
        };
        String tabName = actionName + " " + ev.getSource().toString();
        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
    }
}
