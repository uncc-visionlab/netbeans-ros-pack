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
package org.uncc.netbeans.ros.project.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.RunInNetbeansTerminal;
import org.uncc.netbeans.ros.project.RunRViz;

@ActionID(
        category = "Project",
        id = "org.uncc.netbeans.ros.project.toolbar.RunRVizToolbarButton"
)
@ActionRegistration(
        iconBase = "org/uncc/netbeans/ros/project/toolbar/rviz.png",
        displayName = "#CTL_RunRVizToolbarButton"
)
@ActionReference(path = "Toolbars/Memory", position = 2100)
@Messages("CTL_RunRVizToolbarButton=Run RViz")
// Notes on customizing the default toolbar
// https://anchialas.wordpress.com/2010/08/19/custom-toolbar-configuration/
public final class RunRVizToolbarButton implements ActionListener {

    private final Project context;

    public RunRVizToolbarButton(Project context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
        if (context instanceof ROSProject) {
            ROSProject p = (ROSProject)context;
            RunRViz rvizDelegator = new RunRViz(p);
            String homeDir = p.getProjectDirectory().getPath();
            RunInNetbeansTerminal.runInNewTerminal(rvizDelegator.getName(), 
                    homeDir, rvizDelegator.getCommandList());
        }
    }
}
