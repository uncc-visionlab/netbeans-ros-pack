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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.netbeans.lib.terminalemulator.Term;
import org.netbeans.modules.dlight.api.terminal.TerminalSupport;
import org.netbeans.modules.dlight.terminal.action.MyRemoteTerminalAction;
import org.netbeans.modules.dlight.terminal.action.MyTerminalSupportImpl;
import org.netbeans.modules.dlight.terminal.action.RemoteTerminalAction;
import org.netbeans.modules.dlight.terminal.action.TerminalSupportImpl;
import org.netbeans.modules.dlight.terminal.ui.TerminalContainerTopComponent;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironmentFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileSystem;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOContainer;
import org.openide.windows.IOProvider;

/**
 *
 * @author arwillis
 */
public class RunInNetbeansTerminal {

    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N    
    public static String lastPath = "";

    public static void runInNewTerminal(FileObject fo,
            final String tabName, String homeDir,
            String[] commandList,
            boolean useRemotehost) {
        String fullPath = homeDir;
        final TerminalContainerTopComponent emulator = TerminalContainerTopComponent.findInstance();
        if (!emulator.isOpened()) {
            //emulator.open();
            emulator.requestActive();
        }
        final IOContainer ioContainer = emulator.getIOContainer();
        final IOProvider term = IOProvider.get("Terminal"); // NOI18N
        // TerminalContainerTopComponent.SILENT_MODE_COMMAND.equals(e.getActionCommand())            
        if (term != null) {
            final String path = (fo.isFolder()) ? fo.getPath() : fo.getParent().getPath();
            ExecutionEnvironment env = null;
            try {
                FileSystem fileSystem = fo.getFileSystem();
                Method declaredMethod = fileSystem.getClass().getDeclaredMethod("getExecutionEnvironment"); // NOI18N
                if (declaredMethod != null) {
                    declaredMethod.setAccessible(true);
                    Object invoke = declaredMethod.invoke(fileSystem);
                    if (invoke instanceof ExecutionEnvironment) {
                        env = (ExecutionEnvironment) invoke;
                    }
                }
            } catch (FileStateInvalidException | IllegalAccessException |
                    IllegalArgumentException | InvocationTargetException |
                    NoSuchMethodException | SecurityException ex) {
            }
            if (useRemotehost) {

                try {
                    env = new MyRemoteTerminalAction().getEnvironment();
                    if (env == null) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Could not create remote terminal. Check your connection and path.",
                                "Terminal Creation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Construct a remote instance string from the provided env
                    // /home/turtlebot/.netbeans/remote/192.168.0.30/visionlab16-pc-Linux-x86_64"
                    String arch = System.getProperty("os.arch");
                    arch = (arch.equals("amd64")) ? "x86_64" : arch;
                    lastPath = "/home/" + env.getUser() + "/.netbeans/remote/"
                            + env.getHost() + "/"
                            + InetAddress.getLocalHost().getHostName() + "-"
                            + System.getProperty("os.name") + "-" + arch;
                    // prompt the user to enter their name
                    String remoteDir = JOptionPane.showInputDialog(new JFrame("Remote Project Path"),
                            "Path:", lastPath);
                    lastPath = remoteDir;
                    commandList[1]=remoteDir+commandList[1];
                    fullPath = lastPath + homeDir;
                } catch (UnknownHostException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (env == null) {
                env = ExecutionEnvironmentFactory.getLocal();
            }

            final ExecutionEnvironment envFinal = env;
            final String pathFinal = fullPath;
            final String[] commandListFinal = commandList;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final MyTerminalSupportImpl mt = new MyTerminalSupportImpl();
                    mt.openTerminalImpl(ioContainer, tabName, envFinal, pathFinal, false, true, 0);
                    RunInNetbeansTerminal.RunCommandsInTerminal runnable = new RunInNetbeansTerminal.RunCommandsInTerminal(
                            commandListFinal, ioContainer, mt);
                    RP.post(runnable);
                }
            });
        }
    }

    public static void runInNewTerminal(String tabName, String homeDir, String[] commandList) {
        final TerminalContainerTopComponent emulator = TerminalContainerTopComponent.findInstance();
        if (!emulator.isOpened()) {
            //emulator.open();
            emulator.requestActive();
        }
        final IOContainer ioContainer = emulator.getIOContainer();
        final IOProvider term = IOProvider.get("Terminal"); // NOI18N
        // TerminalContainerTopComponent.SILENT_MODE_COMMAND.equals(e.getActionCommand())            
        if (term != null) {
//            ExecutionEnvironment env = new MyRemoteTerminalAction().getEnvironment();
            ExecutionEnvironment env = ExecutionEnvironmentFactory.getLocal();
            if (env != null) {
//                    System.out.println("homeDir = "+homeDir);                    
                final MyTerminalSupportImpl mt = new MyTerminalSupportImpl();
//                mt.openTerminalImpl(ioContainer, tabName, env, "/home", false, true, 0);
                mt.openTerminalImpl(ioContainer, tabName, env, homeDir, false, true, 0);
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
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
            Term t = myTerminalImpl.getTerminal();
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
