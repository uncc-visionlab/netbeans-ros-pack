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
public class RunROSCore extends TerminalDriverAction {

    public static String[] cmds = {
        "source /opt/ros/indigo/setup.bash\n",
        "roscore\n"
    };
    public static String an = "run roscore";

    public RunROSCore() {
        this.commandList = cmds;
        this.actionName = an;
    }
}
