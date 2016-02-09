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
package org.uncc.netbeans.ros.filetype.cfg;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.uncc.netbeans.ros.terminal.ROSProjectProperties;
import org.uncc.netbeans.ros.terminal.RunInNetbeansTerminal;

@ActionID(
        category = "File",
        id = "org.uncc.netbeans.ros.filetype.cfg.CfgOpen"
)
@ActionRegistration(
        displayName = "#CTL_CfgOpen"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 225),
    @ActionReference(path = "Loaders/text/x-ros-python/Actions", position = 150, separatorBefore = 125)
})
@NbBundle.Messages("CTL_CfgOpen=Open with rqt_reconfigure")
public final class CfgOpen implements ActionListener {
    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    

    private final CfgDataObject context;
    Project project;
    
    public CfgOpen(CfgDataObject context) {
        this.context = context;
        project = ROSProjectProperties.findProject(context.getPrimaryFile());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
        String rosRootFolder = ROSProjectProperties.getProperty(project, 
                ROSProjectProperties.ROS_ROOTFOLDER_PROPERTYNAME);
        String wsFolder = ROSProjectProperties.getProperty(project,
                ROSProjectProperties.ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String wsDevelFolder = ROSProjectProperties.getProperty(project,
                ROSProjectProperties.ROS_DEVELFOLDER_PROPERTYNAME);
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
        String homeDir = context.getPrimaryFile().getParent().getPath();
        String actionName = "rqt_reconfigure";
        commandList = new String[]{
            "source "+rosRootFolder+"/setup.bash\n",
            "source " + installSetupPath + "\n",
            "cd "+homeDir+"\n",
            "rosrun rqt_reconfigure rqt_reconfigure"+"\n",
            "exit"
        };
        String tabName = actionName + " " + ev.getSource().toString();
        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
    }
}
