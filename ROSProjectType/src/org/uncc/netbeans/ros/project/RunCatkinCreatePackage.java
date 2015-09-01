/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;

@ActionID(
        category = "Project",
        id = "org.uncc.netbeans.ros.project.RunROSCore"
)
@ActionReference(path = "Projects/Actions")
public class RunCatkinCreatePackage extends TerminalDriverAction {

    ROSProject project;
    public static String[] cmds = null;
    public static String an = "run roscore";

    public RunCatkinCreatePackage(ROSProject project, String packageName,
            String dependenciesStr) {
        this.project = project;
        String rosRoot = project.getProperty(ROSProject.ROS_ROOTFOLDER_PROPERTYNAME);
        String ros_ws = project.getProperty(ROSProject.ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String ros_src = project.getProperty(ROSProject.ROS_SOURCEFOLDER_PROPERTYNAME);

        cmds = new String[]{
            "source " + rosRoot + "/setup.bash\n",
            "cd " + ros_ws + "/" + ros_src + "\n",
            "catkin_create_pkg " + packageName + " " + dependenciesStr + "\n",
            "sleep 1\n",
            "exit\n"
        };
        this.commandList = cmds;
        this.actionName = an;
    }

}
