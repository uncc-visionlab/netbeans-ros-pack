/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.awt.BorderLayout;
import org.netbeans.modules.dlight.terminal.action.MyTerminalSupportImpl;
import org.netbeans.modules.terminal.api.TerminalContainer;
import org.openide.windows.IOContainer;
import org.openide.windows.TopComponent;

/**
 *
 * @author arwillis
 */
public class UNUSEDTerminalTopComponent extends TopComponent {

    private final TerminalContainer tc;
    public MyTerminalSupportImpl mt;
    
    public UNUSEDTerminalTopComponent() {
        setLayout(new BorderLayout());
        tc = TerminalContainer.create(UNUSEDTerminalTopComponent.this, "Local");
        add(tc, BorderLayout.CENTER);
    }

    public IOContainer getIOContainer() {
        return tc.ioContainer();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        tc.componentActivated();
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
        tc.componentDeactivated();
    }

}
