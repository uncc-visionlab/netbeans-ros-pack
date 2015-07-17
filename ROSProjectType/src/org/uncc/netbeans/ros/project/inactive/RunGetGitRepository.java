/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.inactive;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.uncc.netbeans.ros.project.TerminalDriverAction;

@ActionID(
        category = "Project",
        id = "org.uncc.netbeans.ros.project.RunGetGitRepository"
)
@ActionReference(path = "Projects/Actions")
public class RunGetGitRepository extends TerminalDriverAction {

    public RunGetGitRepository() {
        String[] cmds = {"source /opt/ros/indigo/setup.bash\n",
            "cd ros_ws/src\n",
//            "echo \"y\" | wstool set ros_control --git \n",
            "wstool update\n",
            "exit"};
//        this.commandList = cmds;
//        this.actionName = "Get Git Repo";
    }
}
