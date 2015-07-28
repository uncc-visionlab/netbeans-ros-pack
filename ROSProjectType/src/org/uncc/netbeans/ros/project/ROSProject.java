/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.tools.ant.module.api.support.ActionUtils;
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
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
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
public class ROSProject implements Project, NodeListener {
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
    // TODO Constant moved to project customizer
//    public static final String PROJECT_ROS_SRCDIR = "ros_ws";
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

    public FileObject getSubFolder(String foldername, boolean create) {
        FileObject result = getProjectDirectory().getFileObject(foldername);
        if (result == null && create) {
            try {
                result = getProjectDirectory().createFolder(foldername);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }
        }
        return result;
    }

    public String getPackageName(DataObject context) {
        String pkgName="";
        Node objNode = context.getNodeDelegate();
        String rosWs = getProperty(ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String rosPkgSrc = getProperty(ROS_SOURCEFOLDER_PROPERTYNAME);
        FileObject packageParent = getProjectDirectory().getFileObject(rosWs).getFileObject(rosPkgSrc);
        FileObject invokingFileObj = context.getPrimaryFile();
        String pathRelToPkgSrc = invokingFileObj.getPath().replace(packageParent.getPath(),"");
        String[] pkgSubFolderNames = pathRelToPkgSrc.split(File.separator);
        for (String pkgSubFolderName : pkgSubFolderNames) {
            if (pkgSubFolderName.length() > 0) {
                pkgName = pkgSubFolderName;
                break;
            }
        }
//        FileObject fobj = findPackageThatOwnsNode(objNode);
        return pkgName;
    }
    
/*        private static FileObject findPackageThatOwnsNode(Node node) {
        if (node != null) {
            Project project = node.getLookup().lookup(Project.class);
            if (project == null) {
                DataObject dataObject = node.getLookup().lookup(DataObject.class);
                if (dataObject != null) {
                    project = FileOwnerQuery.getOwner(dataObject.getPrimaryFile());
                }
            }
            return (project == null) ? findProjectThatOwnsNode(node.getParentNode()) : project;
        } else {
            return null;
        }
    }
  */  
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

    @Override
    public void childrenAdded(NodeMemberEvent nme) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        Node[] nodeArray = nme.getDelta();        
//        for (Node n : nodeArray) {
//            n.addNodeListener(this);
//        }
    }

    @Override
    public void childrenRemoved(NodeMemberEvent nme) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void childrenReordered(NodeReorderEvent nre) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nodeDestroyed(NodeEvent ne) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                    ActionUtils.runTarget(buildImpl, new String[]{"clean","compile"}, null);
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

//        @Override
//        public List<FileObject> getDataFiles() {
//            return new ArrayList<FileObject>();
//        }
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
