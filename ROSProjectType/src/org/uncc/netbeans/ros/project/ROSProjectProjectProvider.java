/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.SubprojectProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.netbeans.modules.cnd.makeproject.MakeProject;
import org.uncc.netbeans.ros.project.ROSProject;

/**
 *
 * @author arwillis
 */

public class ROSProjectProjectProvider implements SubprojectProvider {

    private final ROSProject project;

    public ROSProjectProjectProvider(ROSProject project) {
        this.project = project;
    }

    @Override
    public Set<? extends Project> getSubprojects() {
        return loadProjects(project.getProjectDirectory());
    }

    private Set loadProjects(FileObject dir) {
        Set newProjects = new HashSet();
//        FileObject reportsFolder = dir.getFileObject("ros_ws");
        FileObject reportsFolder = dir;
        if (reportsFolder != null) {
            for (FileObject childFolder : reportsFolder.getChildren()) {
                try {
                    if (childFolder.isFolder()) {
                        Project subp = ProjectManager.getDefault().findProject(childFolder);
                        if (subp != null && subp instanceof MakeProject) {
                            newProjects.add((MakeProject) subp);
                        }
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalArgumentException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return Collections.unmodifiableSet(newProjects);
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }
}
