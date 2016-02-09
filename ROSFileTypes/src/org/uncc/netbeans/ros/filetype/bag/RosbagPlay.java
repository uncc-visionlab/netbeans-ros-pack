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
package org.uncc.netbeans.ros.filetype.bag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import org.uncc.netbeans.ros.terminal.ROSProjectProperties;
import org.uncc.netbeans.ros.terminal.RunInNetbeansTerminal;

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
    Project project;
    
    public RosbagPlay(BagDataObject context) {
        this.context = context;
        project = Utilities.actionsGlobalContext().lookup(Project.class);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
        String rosRootFolder = ROSProjectProperties.getProperty(project, 
                ROSProjectProperties.ROS_ROOTFOLDER_PROPERTYNAME);
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
