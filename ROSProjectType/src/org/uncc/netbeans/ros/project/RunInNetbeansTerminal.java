/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
