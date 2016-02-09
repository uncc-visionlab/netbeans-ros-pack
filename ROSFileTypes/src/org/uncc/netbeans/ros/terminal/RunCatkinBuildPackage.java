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
package org.uncc.netbeans.ros.terminal;

import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;

@ActionID(
        category = "Project",
        id = "org.uncc.netbeans.ros.project.RunROSCore"
)
@ActionReference(path = "Projects/Actions")
public class RunCatkinBuildPackage extends TerminalDriverAction {

    Project project;
    public static String[] cmds = null;
    public static String an = "run roscore";

    public RunCatkinBuildPackage(Project project, String packageName) {
            
        this.project = project;
//        String rosRoot = project.getProperty(ROSProjectProperties.ROS_ROOTFOLDER_PROPERTYNAME);
//        String ros_ws = project.getProperty(ROSProjectProperties.ROS_WORKSPACEFOLDER_PROPERTYNAME);
//        String ros_src = project.getProperty(ROSProjectProperties.ROS_SOURCEFOLDER_PROPERTYNAME);

        cmds = new String[]{
//            "source " + rosRoot + "/setup.bash\n",
//            "cd " + ros_ws + "\n",
//            "catkin_make --pkg "+packageName+"\n",
            //"exit\n"
        };
        this.commandList = cmds;
        this.actionName = an;
    }

}
