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
        id = "org.uncc.netbeans.ros.project.RunRqtGraph"
)
@ActionReference(path = "Projects/Actions")
public class RunRqtGraph extends TerminalDriverAction {

    ROSProject project;
    public static String[] cmds;
    public static String an = "Run rqt_graph";

    public RunRqtGraph(ROSProject project) {
        this.project = project;
        String rosRoot = project.getProperty(ROSProject.ROS_ROOTFOLDER_PROPERTYNAME);
        cmds = new String[]{
            "source "+rosRoot+"/setup.bash\n",
            "rqt_graph\n"
        };
        this.commandList = cmds;
        this.actionName = an;
    }
}