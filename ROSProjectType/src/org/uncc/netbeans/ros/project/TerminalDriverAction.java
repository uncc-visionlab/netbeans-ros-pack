/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;

/**
 *
 * @author arwillis
 */
public class TerminalDriverAction extends AbstractAction implements ContextAwareAction {

    String[] commandList = null;
    String actionName = "Default";

    public @Override
    void actionPerformed(ActionEvent e) {
        assert false;
    }

    public @Override
    Action createContextAwareInstance(Lookup context) {
        return new TerminalDriverAction.TerminalContextAction(context);
    }

    public String[] getCommandList() {
        return commandList;
    }
    
    public String getName() {
        return actionName;
    }
    
    public void run(ROSProject project) {
        String homeDir = project.getProjectDirectory().getPath();
        RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    private final class TerminalContextAction extends AbstractAction {

        private final Project p;

        public TerminalContextAction(Lookup context) {
            p = context.lookup(Project.class);
            String name = ProjectUtils.getInformation(p).getDisplayName();
            if (p instanceof ROSProject) {
                setEnabled(true);
                putValue(NAME, actionName);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String homeDir = p.getProjectDirectory().getPath();
            RunInNetbeansTerminal.runInNewTerminal(actionName, homeDir, commandList);
        }
    }
}
