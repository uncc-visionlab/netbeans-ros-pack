/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.filetype.bag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.RunInNetbeansTerminal;

@ActionID(
        category = "File",
        id = "org.uncc.netbeans.ros.filetype.bag.RosbagPlay"
)
@ActionRegistration(
        displayName = "#CTL_RosbagPlay"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 0),
    @ActionReference(path = "Loaders/application/x-ros-rqt-bag/Actions", position = 20)
})
@NbBundle.Messages("CTL_RosbagPlay=Open with rosbag play --clock")
public final class RosbagPlay implements ActionListener {
    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    

    private final BagDataObject context;
    ROSProject project;
    
    public RosbagPlay(BagDataObject context) {
        this.context = context;
        project = Utilities.actionsGlobalContext().lookup(ROSProject.class);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
        String rosRootFolder = project.getProperty(ROSProject.ROS_ROOTFOLDER_PROPERTYNAME);
        String homeDir = context.getPrimaryFile().getParent().getPath();
        String actionName = "rosbag";
        String bagfilename = context.getPrimaryFile().getNameExt();
        commandList = new String[]{
            "source "+rosRootFolder+"/setup.bash\n",
            "cd "+homeDir+"\n",
            "rosbag play -r 1 --clock "+bagfilename+"\n",
            "exit"
        };
        String tabName = actionName + " " + ev.getSource().toString();
        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
    }
}
