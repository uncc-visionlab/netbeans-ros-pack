/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import org.openide.util.Utilities;
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
@NbBundle.Messages({"CTL_RoslaunchOpen=Open with roslaunch/roscore"})
public final class RoslaunchOpen implements ActionListener {

    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    

    private final LaunchDataObject context;
    ROSProject project;

    public RoslaunchOpen(LaunchDataObject context) {
//        MakeProject p1 = Utilities.actionsGlobalContext().lookup(MakeProject.class);
        project = Utilities.actionsGlobalContext().lookup(ROSProject.class);
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
