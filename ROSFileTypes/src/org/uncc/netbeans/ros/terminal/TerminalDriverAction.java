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
    
    public void run(Project project) {
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
            if (p instanceof Project) {
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
