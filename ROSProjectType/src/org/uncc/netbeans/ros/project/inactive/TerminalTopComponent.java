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
package org.uncc.netbeans.ros.project.inactive;

import java.awt.BorderLayout;
import org.netbeans.modules.dlight.terminal.action.MyTerminalSupportImpl;
import org.netbeans.modules.terminal.api.TerminalContainer;
import org.openide.windows.IOContainer;
import org.openide.windows.TopComponent;

/**
 *
 * @author arwillis
 */
public class TerminalTopComponent extends TopComponent {

    private final TerminalContainer tc;
    public MyTerminalSupportImpl mt;
    
    public TerminalTopComponent() {
        setLayout(new BorderLayout());
        tc = TerminalContainer.create(TerminalTopComponent.this, "Local");
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
