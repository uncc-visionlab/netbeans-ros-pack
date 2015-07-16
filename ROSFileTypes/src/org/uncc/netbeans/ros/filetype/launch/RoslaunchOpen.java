/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.filetype.launch;

import org.uncc.netbeans.ros.filetype.bag.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
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
    @ActionReference(path = "Menu/File", position = 0),
    @ActionReference(path = "Loaders/text/x-roslaunch+xml/Actions", position = 0)
})
@NbBundle.Messages({"CTL_RoslaunchOpen=Open with roslaunch/roscore"})
public final class RoslaunchOpen implements ActionListener {
    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    

    private final LaunchDataObject context;    

    public RoslaunchOpen(LaunchDataObject context) {
        ROSProject proj = context.getLookup().lookup(ROSProject.class);
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
        String homeDir = context.getPrimaryFile().getParent().getPath();
        String actionName = "roscore";
//        commandList = new String[]{
//            "source /opt/ros/indigo/setup.bash\n",
//            "roscore\n",
//            "exit"
//        };
//        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        actionName = "roslaunch";
        String launchfilename = context.getPrimaryFile().getNameExt();
        commandList = new String[]{
            "source /opt/ros/indigo/setup.bash\n",
            "cd "+homeDir+"\n",
            "roslaunch "+launchfilename+"\n",
            "exit"
        };
        String tabName = actionName + " " + ev.getSource().toString();
        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
    }
}
