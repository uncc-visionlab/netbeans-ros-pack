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
package org.uncc.netbeans.ros.filetype.launch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.netbeans.modules.nativeexecution.api.NativeProcessBuilder;
//import org.netbeans.modules.cnd.api.remote.RemoteProject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.uncc.netbeans.ros.terminal.ROSProjectProperties;
import org.uncc.netbeans.ros.terminal.RunInNetbeansTerminal;

@ActionID(
        category = "File",
        id = "org.uncc.netbeans.ros.filetype.launch.RoslaunchOpenRemote"
)
@ActionRegistration(
        displayName = "#CTL_RoslaunchOpenRemote"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 250),
    @ActionReference(path = "Loaders/text/x-roslaunch+xml/Actions", position = 250)
})
@NbBundle.Messages({"CTL_RoslaunchOpenRemote=Open with roslaunch on remote host"})
public final class RoslaunchOpenRemote implements ActionListener {

    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    

    private final LaunchDataObject context;
    Project project;

    public RoslaunchOpenRemote(LaunchDataObject context) {
        project = ROSProjectProperties.findProject(context.getPrimaryFile());
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String[] commandList;
//        if (project instanceof RemoteProject) {
//            System.out.println("This is a remote project.");
//        }
        String rosRootFolder = ROSProjectProperties.getProperty(project,
                ROSProjectProperties.ROS_ROOTFOLDER_PROPERTYNAME);
        FileObject develFolder = ROSProjectProperties.getDevelFolder(project);
        FileObject installSetup = develFolder.getFileObject("setup.bash");
        if (installSetup == null) {
            // abort the run --> no install directory available
            JOptionPane.showMessageDialog(new JFrame(),
                    "Could not find file setup.bash in devel folder.",
                    "Launch File Run Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String installSetupPath = installSetup.getPath();
        String packageName = ROSProjectProperties.getPackageName(project, context);
        String homeDir = project.getProjectDirectory().getPath();
        ExecutionEnvironment env = RunInNetbeansTerminal.getExecutionEnvironment(true);
        String remoteProjectPath = RunInNetbeansTerminal.getRemoteProjectPath();
        // add an environment variable this way
//        NativeProcessBuilder npb = NativeProcessBuilder.newProcessBuilder(env);
//        npb.getEnvironment().put("SILLY_TEST_VARNAME", "SILLY_TEST_VARVALUE");// NOI18N
        String actionName = "roslaunch";
        homeDir = remoteProjectPath+homeDir;
        String launchfilename = context.getPrimaryFile().getNameExt();
        commandList = new String[]{
            "source " + rosRootFolder + "/setup.bash\n",
            "source " + remoteProjectPath+installSetupPath + "\n",
            "roslaunch " + packageName + " " + launchfilename + "\n",
//            "exit"
        };
        String tabName = actionName + " " + ev.getSource().toString();
        Lookup lookup = context.getLookup();
        FileObject fo = lookup.lookup(FileObject.class);
        RunInNetbeansTerminal.runInNewTerminal( env, actionName, homeDir, commandList);
    }
}
