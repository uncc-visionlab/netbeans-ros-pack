/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
