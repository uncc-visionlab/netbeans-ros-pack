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
