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
package org.uncc.netbeans.ros.project.ws;

/**
 *
 * @author arwillis
 */
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;

import org.netbeans.spi.project.ProjectServiceProvider;

@ProjectServiceProvider(service = ROSWorkspaceProjectService.class,
        projectType = "org-netbeans-modules-cnd-makeproject")
public class ROSWorkspaceProjectServiceImpl extends ROSWorkspaceProjectService {

    static {
//        JOptionPane.showMessageDialog(null, "===> loading ServiceImpl");
    }
    private final Project p;

    public ROSWorkspaceProjectServiceImpl(Project p) {
        this.p = p;
//        JOptionPane.showMessageDialog(null, "===> new ServiceImpl on " + p);
    }

    @Override
    public String m() {
        return ProjectUtils.getInformation(p).getDisplayName();
    }
}
