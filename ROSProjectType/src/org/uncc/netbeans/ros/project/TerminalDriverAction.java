/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.SwingUtilities;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.lib.terminalemulator.Term;
import org.netbeans.modules.dlight.terminal.action.MyTerminalSupportImpl;
import org.netbeans.modules.dlight.terminal.ui.TerminalContainerTopComponent;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironmentFactory;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOContainer;
import org.openide.windows.IOProvider;

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
