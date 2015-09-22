/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.filetype.rviz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.RunInNetbeansTerminal;

@ActionID(
        category = "File",
        id = "org.uncc.netbeans.ros.filetype.rviz.RVizConfigOpen"
)
@ActionRegistration(
        displayName = "#CTL_RVizConfigOpen"
)
@ActionReference(path = "Loaders/text/x-ros-rviz/Actions", position = 150, separatorBefore = 125)
@Messages("CTL_RVizConfigOpen=Open RViz Config File")
public final class RVizConfigOpen implements ActionListener {

    private final DataObject context;
    ROSProject project;

    public RVizConfigOpen(DataObject context) {
        this.context = context;
        project = ROSProject.findROSProject(context.getPrimaryFile());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
        String rosRootFolder = project.getProperty(ROSProject.ROS_ROOTFOLDER_PROPERTYNAME);
        String homeDir = context.getPrimaryFile().getParent().getPath();
        String actionName = "roscore";
        actionName = "rqt_bag";
        String rvizfilename = context.getPrimaryFile().getNameExt();
        commandList = new String[]{
            "source "+rosRootFolder+"/setup.bash\n",
            "cd "+homeDir+"\n",
            "rosrun rviz rviz -d "+rvizfilename+"\n",
            "exit"
        };
        String tabName = actionName + " " + ev.getSource().toString();
        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
    }
}
