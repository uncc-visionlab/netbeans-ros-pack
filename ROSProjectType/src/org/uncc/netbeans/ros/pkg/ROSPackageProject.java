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

import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.netbeans.spi.project.ui.support.ProjectSensitiveActions;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.uncc.netbeans.ros.project.ProjectChildrenFactory;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.RunCatkinBuildPackage;
import org.uncc.netbeans.ros.project.RunCatkinCleanBuildPackage;

public class ROSPackageProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;
    final ROSProject project;

    ROSPackageProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
        project = ROSProject.findROSProject(dir);
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                new Info(),
                new ROSPackageProjectLogicalView(this),
                new ROSPackageActionProvider()
            });
        }
        return lkp;
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String CUSTOMER_ICON = "org/uncc/netbeans/ros/pkg/package.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(CUSTOMER_ICON));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change 
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change 
        }

        @Override
        public Project getProject() {
            return ROSPackageProject.this;
        }
    }

    class ROSPackageProjectLogicalView implements LogicalViewProvider {

        @StaticResource()
        public static final String CUSTOMER_ICON = "org/uncc/netbeans/ros/pkg/package.png";
        private final ROSPackageProject project;

        public ROSPackageProjectLogicalView(ROSPackageProject project) {
            this.project = project;
        }

        @Override
        public Node createLogicalView() {
            try {
//Obtain the project directory's node: 
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
//Decorate the project directory's node: 
                return new ProjectNode(nodeOfProjectFolder, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
//Fallback-the directory couldn't be created - 
//read-only filesystem or something evil happened 
                return new AbstractNode(Children.LEAF);
            }
        }

        private final class ProjectNode extends FilterNode {

            final ROSPackageProject project;

            public ProjectNode(Node node, ROSPackageProject project) throws DataObjectNotFoundException {
                super(node,
                        new ProjectChildrenFactory(project, node),
                        //                        new FilterNode.Children(node));
                        new ProxyLookup(new Lookup[]{
                            Lookups.singleton(project),
                            node.getLookup()})
                );
                this.project = project;
            }

            @Override
            public Action[] getActions(boolean arg0) {
                Action[] parentActions = super.getActions(arg0);
                Action[] nodeActions = new Action[]{
                    CommonProjectActions.newFileAction(),
                    //The 'null' indicates that the default icon will be used:
                    ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build", null),
                    ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_REBUILD, "Clean and Build", null),
                    ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_CLEAN, "Clean", null),
                    CommonProjectActions.closeProjectAction(),
                    CommonProjectActions.setProjectConfigurationAction(),};
                Action[] allActions = new Action[nodeActions.length + parentActions.length];
                int idx = 0;
                for (Action a : nodeActions) {
                    allActions[idx++] = a;
                }
                for (Action a : parentActions) {
                    allActions[idx++] = a;
                }
                return allActions;
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(CUSTOMER_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }
        }

        @Override
        public Node findPath(Node root, Object target) {
            //leave unimplemented for now 
            return null;
        }
    }

    private final class ROSPackageActionProvider implements ActionProvider {

        private final String[] supported = new String[]{
            ActionProvider.COMMAND_MOVE,
            ActionProvider.COMMAND_RENAME,
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_BUILD,
            ActionProvider.COMMAND_REBUILD,
            ActionProvider.COMMAND_CLEAN,
            ActionProvider.COMMAND_COMPILE_SINGLE,
            ActionProvider.COMMAND_RUN,
            ActionProvider.COMMAND_RUN_SINGLE
        };

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            ROSProject p = ROSPackageProject.this.project;
            String packageName = getProjectDirectory().getName();
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_RENAME)) {
                DefaultProjectOperations.performDefaultRenameOperation(ROSPackageProject.this, "");
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_MOVE)) {
                DefaultProjectOperations.performDefaultMoveOperation(ROSPackageProject.this);
            }
            if (string.equals(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(ROSPackageProject.this);
            }
            if (string.equals(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(ROSPackageProject.this);
            }
            //Here we find the Ant script and call the target we need!
            if (string.equals(ActionProvider.COMMAND_BUILD)) {
                if (packageName != null) {
                    RunCatkinBuildPackage job = new RunCatkinBuildPackage(project,
                            packageName);
                    job.run(project);
                }
            }
            if (string.equals(ActionProvider.COMMAND_REBUILD)) {
                if (packageName != null) {
                    RunCatkinCleanBuildPackage job = new RunCatkinCleanBuildPackage(project,
                            packageName);
                    job.run(project);
                }
            }
            if (string.equals(ActionProvider.COMMAND_CLEAN)) {
            }
            if (string.equals(ActionProvider.COMMAND_COMPILE_SINGLE)) {
            }
            if (string.equals(ActionProvider.COMMAND_RUN)) {
            }
            if (string.equals(ActionProvider.COMMAND_RUN_SINGLE)) {
            }
        }

        @Override
        public boolean isActionEnabled(String command, Lookup lookup) throws IllegalArgumentException {
            if ((command.equals(ActionProvider.COMMAND_RENAME))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_MOVE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_DELETE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_COPY))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_BUILD))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_REBUILD))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_CLEAN))) {
                return false;
            } else if ((command.equals(ActionProvider.COMMAND_COMPILE_SINGLE))) {
                return false;
            } else if ((command.equals(ActionProvider.COMMAND_RUN))) {
                return false;
            } else if ((command.equals(ActionProvider.COMMAND_RUN_SINGLE))) {
                return false;
            } else {
                throw new IllegalArgumentException(command);
            }
        }
    }

}
