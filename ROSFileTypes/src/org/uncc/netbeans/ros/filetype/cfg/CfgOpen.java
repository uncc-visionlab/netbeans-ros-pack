/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.filetype.cfg;


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
    ROSProject project;
    
    public CfgOpen(CfgDataObject context) {
        this.context = context;
        project = ROSProject.findROSProject(context.getPrimaryFile());
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
