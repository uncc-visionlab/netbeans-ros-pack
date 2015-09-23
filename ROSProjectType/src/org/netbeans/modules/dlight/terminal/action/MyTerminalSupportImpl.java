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
package org.netbeans.modules.dlight.terminal.action;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.lib.terminalemulator.Term;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.netbeans.modules.nativeexecution.api.HostInfo;
import org.netbeans.modules.nativeexecution.api.NativeProcess;
import org.netbeans.modules.nativeexecution.api.NativeProcessBuilder;
import org.netbeans.modules.nativeexecution.api.execution.NativeExecutionDescriptor;
import org.netbeans.modules.nativeexecution.api.execution.NativeExecutionService;
import org.netbeans.modules.nativeexecution.api.pty.PtySupport;
import org.netbeans.modules.nativeexecution.api.util.ConnectionManager;
import org.netbeans.modules.nativeexecution.api.util.ConnectionManager.CancellationException;
import org.netbeans.modules.nativeexecution.api.util.HostInfoUtils;
import org.netbeans.modules.terminal.api.IONotifier;
import org.netbeans.modules.terminal.api.IOTerm;
import org.netbeans.modules.terminal.api.IOVisibility;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.WeakListeners;
import org.openide.windows.IOContainer;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author Vladimir Voskresensky
 */
public class MyTerminalSupportImpl {

    private static final RequestProcessor RP = new RequestProcessor("Terminal Action RP", 100); // NOI18N
    public Term gterm;

    public MyTerminalSupportImpl() {
    }

    public Term getTerminal() {
        return gterm;
    }

    public static Component getToolbarPresenter(Action action) {
        JButton button = new JButton(action);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setText(null);
        button.putClientProperty("hideActionText", Boolean.TRUE); // NOI18N
        Object icon = action.getValue(Action.SMALL_ICON);
        if (icon == null) {
            icon = ImageUtilities.loadImageIcon("org/netbeans/modules/dlight/terminal/action/local_term.png", false);// NOI18N
        }
        if (!(icon instanceof Icon)) {
            throw new IllegalStateException("No icon provided for " + action); // NOI18N
        }
        button.setDisabledIcon(ImageUtilities.createDisabledIcon((Icon) icon));
        return button;
    }

    public void openTerminalImpl(
            final IOContainer ioContainer,
            final String tabTitle,
            final ExecutionEnvironment env,
            final String dir,
            final boolean silentMode,
            final boolean pwdFlag) {
        final IOProvider term = IOProvider.get("Terminal"); // NOI18N
        if (term != null) {
            final AtomicBoolean destroyed = new AtomicBoolean(false);
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (SwingUtilities.isEventDispatchThread()) {
                        ioContainer.requestActive();
                    } else {
                        doWork();
                    }
                }

                private void doWork() {
                    if (!ConnectionManager.getInstance().isConnectedTo(env)) {
                        try {
                            ConnectionManager.getInstance().connectTo(env);
                        } catch (IOException ex) {
                            if (!destroyed.get()) {
                                String error = ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage();
                                String msg = NbBundle.getMessage(MyTerminalSupportImpl.class, "TerminalAction.FailedToStart.text", error); // NOI18N
                                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE));
                            }
                            return;
                        } catch (CancellationException ex) {
                            return;
                        }
                    }

                    final HostInfo hostInfo;
                    try {
                        HostInfoUtils.isHostInfoAvailable(env);
                        hostInfo = HostInfoUtils.getHostInfo(env);
                        boolean isSupported = PtySupport.isSupportedFor(env);
                        if (!isSupported) {
                            if (!silentMode) {
                                String message;

                                if (hostInfo.getOSFamily() == HostInfo.OSFamily.WINDOWS) {
                                    message = NbBundle.getMessage(MyTerminalSupportImpl.class, "LocalTerminalNotSupported.error.nocygwin"); // NOI18N
                                } else {
                                    message = NbBundle.getMessage(MyTerminalSupportImpl.class, "LocalTerminalNotSupported.error"); // NOI18N
                                }

                                NotifyDescriptor nd = new NotifyDescriptor.Message(message, NotifyDescriptor.INFORMATION_MESSAGE);
                                DialogDisplayer.getDefault().notify(nd);
                            }
                            return;
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        return;
                    } catch (CancellationException ex) {
                        Exceptions.printStackTrace(ex);
                        return;
                    }

                    final AtomicReference<InputOutput> ioRef = new AtomicReference<InputOutput>();
                    try {
                        InputOutput io = term.getIO(tabTitle, null, ioContainer);
                        ioRef.set(io);

//                        Term term = IOTerm.term(io);
                        gterm = IOTerm.term(io);
                        // TODO: this is a temporary solution.

                        // Right now xterm emulation is not fully supported. (NB7.4)
                        // Still it has a very desired functionality - is recognises
                        // \ESC]%d;%sBEL escape sequences.
                        // Although \ESC]0;%sBEL is not implemented yet and window title
                        // is not set, it, at least, can skip the whole %s.
                        // This makes command prompt look better when this sequence is used
                        // in PS1 (ex. cygwin set this by default).
                        //
                        gterm.setEmulation("xterm"); // NOI18N

                        NativeProcessBuilder npb = NativeProcessBuilder.newProcessBuilder(env);
                        npb.addNativeProcessListener(new NativeProcessListener(ioRef.get(), destroyed));

                        if (pwdFlag) {
                            final String promptCommand = "echo -n \"\033]0;" + tabTitle + " `pwd`\007\"";   // NOI18N
                            final String commandName = "PROMPT_COMMAND";                                    // NOI18N
                            String usrPrompt = npb.getEnvironment().get(commandName);
                            npb.getEnvironment().put(commandName,
                                    (usrPrompt == null)
                                            ? promptCommand
                                            : promptCommand + ';' + usrPrompt
                            );
                        }

                        String shell = hostInfo.getLoginShell();
                        if (dir != null) {
                            npb.setWorkingDirectory(dir);
                        }
//                            npb.setWorkingDirectory("${HOME}");
                        npb.setExecutable(shell);
                        if (shell.endsWith("bash") || shell.endsWith("bash.exe")) { // NOI18N
                            npb.setArguments("--login"); // NOI18N
                        }
                        NativeExecutionDescriptor descr;
                        descr = new NativeExecutionDescriptor().controllable(true).frontWindow(true).inputVisible(true).inputOutput(ioRef.get());
                        descr.postExecution(new Runnable() {

                            @Override
                            public void run() {
                                ioRef.get().closeInputOutput();
                            }
                        });
                        NativeExecutionService es = NativeExecutionService.newService(npb, descr, "Terminal Emulator"); // NOI18N
                        Future<Integer> result = es.run();
                        // ask terminal to become active
                        SwingUtilities.invokeLater(this);

                        try {
                            // if terminal can not be started then ExecutionException should be thrown
                            // wait one second to see if terminal can not be started. otherwise it's OK to exit by TimeOut
                            result.get(1, TimeUnit.SECONDS);
                        } catch (TimeoutException ex) {
                            // we should be there
                        } catch (InterruptedException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (ExecutionException ex) {
                            if (!destroyed.get()) {
                                String error = ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage();
                                String msg = NbBundle.getMessage(MyTerminalSupportImpl.class, "TerminalAction.FailedToStart.text", error); // NOI18N
                                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE));
                            }
                        }
                    } catch (java.util.concurrent.CancellationException ex) { // VK: don't quite understand who can throw it?
                        Exceptions.printStackTrace(ex);
                        reportInIO(ioRef.get(), ex);
                    }
                }

                private void reportInIO(InputOutput io, Exception ex) {
                    if (io != null && ex != null) {
                        io.getErr().print(ex.getLocalizedMessage());
                    }
                }
            };
            RP.post(runnable);
        }
    }

    private final static class NativeProcessListener implements ChangeListener, PropertyChangeListener {

        private final AtomicReference<NativeProcess> processRef;
        private final AtomicBoolean destroyed;

        public NativeProcessListener(InputOutput io, AtomicBoolean destroyed) {
            assert destroyed != null;
            this.destroyed = destroyed;
            this.processRef = new AtomicReference<NativeProcess>();
            IONotifier.addPropertyChangeListener(io, WeakListeners.propertyChange(NativeProcessListener.this, io));
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            NativeProcess process = processRef.get();
            if (process == null && e.getSource() instanceof NativeProcess) {
                processRef.compareAndSet(null, (NativeProcess) e.getSource());
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (IOVisibility.PROP_VISIBILITY.equals(evt.getPropertyName()) && Boolean.FALSE.equals(evt.getNewValue())) {
                if (destroyed.compareAndSet(false, true)) {
                    // term is closing => destroy process
                    final NativeProcess proc = processRef.get();
                    if (proc != null) {
                        RP.submit(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    proc.destroy();
                                } catch (Throwable th) {
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
