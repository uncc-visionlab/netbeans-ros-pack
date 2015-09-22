/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.pkg;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;
import org.uncc.netbeans.ros.project.ROSProject;

@ServiceProvider(service = ProjectFactory.class)
public class ROSPackageProjectFactory implements ProjectFactory {

    //Specifies when a project is a project, i.e., 
    //if "CMakeLists.txt" and "package.xml" is present in a folder: 
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return ROSProject.isROSPackageFolder(projectDirectory);
    }

    //Specifies when the project will be opened, i.e., if the project exists: 
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new ROSPackageProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        // leave unimplemented for the moment 
    }
}
