/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
