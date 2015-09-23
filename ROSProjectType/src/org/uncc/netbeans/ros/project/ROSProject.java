/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.tools.ant.module.api.support.ActionUtils;
import static org.netbeans.api.project.FileOwnerQuery.getOwner;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.MoveOrRenameOperationImplementation;
import org.netbeans.spi.project.support.ant.AntBasedProjectRegistration;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.modules.ModuleInfo;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author arwillis
 */
@AntBasedProjectRegistration(
        type = ROSProject.TYPE,
        iconResource = ROSProject.ICON_RESOURCE,
        sharedName = ROSProject.NAME_SHARED,
        sharedNamespace = ROSProject.NAME_SPACE_SHARED,
        privateName = ROSProject.NAME_PRIVATE,
        privateNamespace = ROSProject.NAME_SPACE_PRIVATE
)
public class ROSProject implements Project {

    public static String ROS_MAKE_PROPERTYNAME = "make";
    public static String ROS_ROOTFOLDER_PROPERTYNAME = "ros.root";
    public static String ROS_WORKSPACEFOLDER_PROPERTYNAME = "ros.ws";
    public static String ROS_SOURCEFOLDER_PROPERTYNAME = "ros.ws.src";
    public static String ROS_BUILDFOLDER_PROPERTYNAME = "ros.ws.build";
    public static String ROS_DEVELFOLDER_PROPERTYNAME = "ros.ws.devel";
    public static String ROS_INSTALLFOLDER_PROPERTYNAME = "ros.ws.install";

    // Needs to match the <code-name-base> tag from project.xml
    // <code-name-base>org-uncc-netbeans-ros-project</code-name-base>
    // and needs to match the Module line in the Manifest
    // OpenIDE-Module: org.uncc.netbeans.ros.project
    public static final String TYPE = "org.uncc.netbeans.ros.project";
//    public static final String TYPE = "org-ros-project";
    public static final String NAME_SPACE_SHARED = "http://visionlab.uncc.edu/ns/ros-project/1";
    public static final String NAME_SHARED = "data";
    public static final String NAME_PRIVATE = "project-private";
    public static final String NAME_SPACE_PRIVATE = "http://visionlab.uncc.edu/ns/ros-project-private/1";
    public static final String ICON_RESOURCE = "org/uncc/netbeans/ros/project/resources/project_icon.png";
    final AntProjectHelper helper;

    public ROSProject(AntProjectHelper helper) {
        this.helper = helper;
    }

    @Override
    public Lookup getLookup() {
        return Lookups.fixed(new Object[]{
            this,
            helper,
            new Info(),
            new ROSProjectLogicalView(this),
            new AntBasedActionProvider(),
            new AntBasedProjectMoveOrRenameOperation(),
            new AntBasedProjectCopyOperation(),
            new AntBasedProjectDeleteOperation(this),
            new ROSProjectPropertiesLookupProvider(this),
            new ROSProjectProjectProvider(this)
        });
    }

    @Override
    public FileObject getProjectDirectory() {
        return helper.getProjectDirectory();
    }

    public String getPackageName(DataObject context) {
        String pkgName = "";
        Node objNode = context.getNodeDelegate();
        String rosWs = getProperty(ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String rosPkgSrc = getProperty(ROS_SOURCEFOLDER_PROPERTYNAME);
        FileObject packageParent = getProjectDirectory().getFileObject(rosWs).getFileObject(rosPkgSrc);
        FileObject invokingFileObj = context.getPrimaryFile();
        String pathRelToPkgSrc = invokingFileObj.getPath().replace(packageParent.getPath(), "");
        String[] pkgSubFolderNames = pathRelToPkgSrc.split(File.separator);
        boolean foundPackage = false;
        for (String pkgSubFolderName : pkgSubFolderNames) {
            pkgName = pkgSubFolderName;
            packageParent = packageParent.getFileObject(pkgName);
            if (isROSPackageFolder(packageParent)) {
                foundPackage = true;
                break;
            }
        }
        if (!foundPackage) {
            return null;
        }
        return pkgName;
    }

    public static ROSProject findROSProject(FileObject fobj) {
        ROSProject p = null;
        while (fobj.getParent() != null && p == null) {
            fobj = fobj.getParent();
            Project pVal = getOwner(fobj);
            if (pVal instanceof ROSProject) {
                p = (ROSProject) pVal;
            }
        }
        return p;
    }

    public static boolean gradlePluginPresent() {
        Collection<? extends ModuleInfo> modules = Lookup.getDefault().lookupAll(ModuleInfo.class);
        for (ModuleInfo mi : modules) {
            //System.out.println(mi.getDisplayName());
            if (mi.getDisplayName().contains("Gradle Support")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isROSPackageFolder(FileObject folder) {
        if (folder.isFolder()
                && folder.getFileObject("CMakeLists.txt") != null
                && folder.getFileObject("package.xml") != null) {
            if (gradlePluginPresent() && folder.getFileObject("build.gradle") != null) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isROSWorkspaceFolder(FileObject folder) {
        ROSProject project = ROSProject.findROSProject(folder);
        if (project != null) {
            String rosWs = project.getProperty(ROS_WORKSPACEFOLDER_PROPERTYNAME);
            FileObject workspaceParent = project.getProjectDirectory().getFileObject(rosWs);
            return folder.getPath().equals(workspaceParent.getPath());
        }
        return false;
    }

    public String getProperty(String propertyName) {
        FileObject fobj = getProjectDirectory().getFileObject("nbproject").getFileObject("project.properties");
        Properties properties = new Properties();
        try {
            InputStream is = fobj.getInputStream();
            properties.load(is);
            is.close();
        } catch (IOException e) {
            System.out.println("Could not open Config file");
        }
        return properties.getProperty(propertyName);
    }

    private final class Info implements ProjectInformation {

        @Override
        public String getName() {
            return helper.getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(ICON_RESOURCE));
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
        }

        @Override
        public Project getProject() {
            return ROSProject.this;
        }
    }

    private final class AntBasedActionProvider implements ActionProvider {

        private final String[] supported = new String[]{
            ActionProvider.COMMAND_MOVE,
            ActionProvider.COMMAND_RENAME,
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_BUILD,
            ActionProvider.COMMAND_REBUILD,
            ActionProvider.COMMAND_CLEAN,
            ActionProvider.COMMAND_COMPILE_SINGLE,
            //            ActionProvider.COMMAND_RUN,
            ActionProvider.COMMAND_RUN_SINGLE
        };

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_RENAME)) {
                DefaultProjectOperations.performDefaultRenameOperation(ROSProject.this, "");
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_MOVE)) {
                DefaultProjectOperations.performDefaultMoveOperation(ROSProject.this);
            }
            if (string.equals(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(ROSProject.this);
            }
            if (string.equals(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(ROSProject.this);
            }
            //Here we find the Ant script and call the target we need!
            if (string.equals(ActionProvider.COMMAND_BUILD)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"compile"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_REBUILD)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"clean", "compile"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_CLEAN)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"clean"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_COMPILE_SINGLE)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"compile-single"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_RUN)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"run"}, null);

                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_RUN_SINGLE)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"run-single"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
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
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_COMPILE_SINGLE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_RUN))) {
//                return true;
                return false;
            } else if ((command.equals(ActionProvider.COMMAND_RUN_SINGLE))) {
                return true;
            } else {
                throw new IllegalArgumentException(command);
            }
        }
    }

    private final class AntBasedProjectMoveOrRenameOperation implements MoveOrRenameOperationImplementation {

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public List<FileObject> getDataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public void notifyRenaming() throws IOException {
        }

        @Override
        public void notifyRenamed(String nueName) throws IOException {
        }

        @Override
        public void notifyMoving() throws IOException {
        }

        @Override
        public void notifyMoved(Project original, File originalPath, String nueName) throws IOException {
        }
    }

    private final class AntBasedProjectCopyOperation implements CopyOperationImplementation {

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public List<FileObject> getDataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public void notifyCopying() throws IOException {
        }

        @Override
        public void notifyCopied(Project prjct, File file, String string) throws IOException {
        }
    }

    private final class AntBasedProjectDeleteOperation implements DeleteOperationImplementation {

        private final ROSProject project;

        private AntBasedProjectDeleteOperation(ROSProject project) {
            this.project = project;
        }

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public List<FileObject> getDataFiles() {
            List<FileObject> files = new ArrayList<FileObject>();
            FileObject[] projectChildren = project.getProjectDirectory().getChildren();
            for (FileObject fileObject : projectChildren) {
                addFile(project.getProjectDirectory(), fileObject.getNameExt(), files);
            }
            return files;
        }

        private void addFile(FileObject projectDirectory, String fileName, List<FileObject> result) {
            FileObject file = projectDirectory.getFileObject(fileName);
            if (file != null) {
                result.add(file);
            }
        }

        @Override
        public void notifyDeleting() throws IOException {
        }

        @Override
        public void notifyDeleted() throws IOException {
        }
    }
}
