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
import org.netbeans.modules.cnd.makeproject.api.MakeProject;
//import org.netbeans.modules.cnd.makeproject.MakeProjectImpl;
import org.uncc.netbeans.ros.pkg.ROSPackageProject;

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

    private Set<Project> loadProjects(FileObject dir) {
        Set<Project> newProjects = new HashSet<>();
        FileObject reportsFolder = dir;
        if (reportsFolder != null) {
            for (FileObject childFolder : reportsFolder.getChildren()) {
                try {
                    if (childFolder.isFolder()) {
                        Project subp = ProjectManager.getDefault().findProject(childFolder);
                        if (subp != null && subp instanceof MakeProject) {
                            newProjects.add((MakeProject) subp);
                        }
                        if (subp != null && subp instanceof ROSPackageProject) {
                            newProjects.add((ROSPackageProject) subp);                            
                        }
                    }
                } catch (IOException | IllegalArgumentException ex) {
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
