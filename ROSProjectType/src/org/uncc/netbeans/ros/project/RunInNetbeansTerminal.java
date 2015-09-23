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
package org.uncc.netbeans.ros.project;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.SwingUtilities;
import org.netbeans.lib.terminalemulator.Term;
import org.netbeans.modules.dlight.terminal.action.MyTerminalSupportImpl;
import org.netbeans.modules.dlight.terminal.ui.TerminalContainerTopComponent;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironmentFactory;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOContainer;
import org.openide.windows.IOProvider;

/**
 *
 * @author arwillis
 */
public class RunInNetbeansTerminal {
    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    
    
    public static void runInNewTerminal(String tabName, String homeDir, String[] commandList) {
            final TerminalContainerTopComponent emulator = TerminalContainerTopComponent.findInstance();
//            WindowManager.getDefault().findMode("editor").dockInto(emulator);
            if (!emulator.isOpened()) {
                //emulator.open();
                emulator.requestActive();
            }
            final IOContainer ioContainer = emulator.getIOContainer();
            final IOProvider term = IOProvider.get("Terminal"); // NOI18N
            // TerminalContainerTopComponent.SILENT_MODE_COMMAND.equals(e.getActionCommand())            
            if (term != null) {
                ExecutionEnvironment env = ExecutionEnvironmentFactory.getLocal();
                if (env != null) {
//                    System.out.println("homeDir = "+homeDir);                    
                    final MyTerminalSupportImpl mt = new MyTerminalSupportImpl();
                    mt.openTerminalImpl(ioContainer, tabName, env, homeDir, false, false);
//                final TopComponent[] tcs = WindowManager.getDefault().findMode("editor").getTopComponents();
                    RunInNetbeansTerminal.RunCommandsInTerminal runnable = new RunInNetbeansTerminal.RunCommandsInTerminal(
                            commandList, ioContainer, mt);
                    RP.post(runnable);
                }
            }
    }
    static class RunCommandsInTerminal implements Runnable {

        String[] commandList = null;
        IOContainer ioContainer;
        MyTerminalSupportImpl myTerminalImpl;

        public RunCommandsInTerminal(String[] commandList,
                IOContainer ioContainer,
                MyTerminalSupportImpl myTerminalImpl) {
            this.commandList = commandList;
            this.ioContainer = ioContainer;
            this.myTerminalImpl = myTerminalImpl;
        }

        @Override
        public void run() {
            if (SwingUtilities.isEventDispatchThread()) {
                ioContainer.requestActive();
            } else {
                doWork();
            }
        }

        private void doWork() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
            Term t = myTerminalImpl.gterm;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            for (String cmd : commandList) {
                clipboard.setContents(new StringSelection(cmd), null);
                t.pasteFromClipboard();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }
    
}
