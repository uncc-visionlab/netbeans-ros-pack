/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.awt.Image;
import javax.swing.Action;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
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

/**
 *
 * @author arwillis
 */
public class ROSWorkspaceLogicalView implements LogicalViewProvider {

    private final ROSProject project;

    public ROSWorkspaceLogicalView(ROSProject project) {
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        try {
            //Get the Text directory, creating if deleted
//            FileObject text = project.getProjectDirectory();
            FileObject text = project.getSubFolder("ros_ws", true);

            //Get the DataObject that represents it
            DataFolder textDataObject = DataFolder.findFolder(text);

            //Get its default node-we'll wrap our node around it to change the
            //display name, icon, etc
            Node realTextFolderNode = textDataObject.getNodeDelegate();

            //This FilterNode will be our project node
            return new ProjectNode(realTextFolderNode, project);
        } catch (DataObjectNotFoundException donfe) {
            Exceptions.printStackTrace(donfe);
            //Fallback-the directory couldn't be created -
            //read-only filesystem or something evil happened
            return new AbstractNode(Children.LEAF);
        }
    }

    /**
     * This is the node you actually see in the project tab for the project
     */
    private static final class ProjectNode extends FilterNode {

        final ROSProject project;

        public ProjectNode(Node node, ROSProject project) throws DataObjectNotFoundException {
            super(node,
                    // Default child node handler/constructor
                    //new FilterNode.Children(node),
                    // Custom child node handler/constructor 
                    NodeFactorySupport.createCompositeChildren(project,
                            RootNode.REGISTERED_NODE_LOCATION),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup(new Lookup[]{
                        Lookups.singleton(project),
                        node.getLookup()
                    })
            );
            this.project = project;
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] nodeActions = new Action[]{
//                CommonProjectActions.newFileAction(),
//                //The 'null' indicates that the default icon will be used:
//                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build", null),
//                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_REBUILD, "Clean and Build", null),
//                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_CLEAN, "Clean", null),
//                CommonProjectActions.moveProjectAction(),
//                CommonProjectActions.renameProjectAction(),
//                CommonProjectActions.copyProjectAction(),
//                CommonProjectActions.deleteProjectAction(),
                //                CommonProjectActions.setAsMainProjectAction(),
                new RunROSCore(),
                new RunRViz(),
                new RunCloneGitRepository(),
//                CommonProjectActions.closeProjectAction(),
//                CommonProjectActions.setProjectConfigurationAction(),
//                CommonProjectActions.customizeProjectAction()
            };
            return nodeActions;
        }

//        @Override
//        public Action[] getActions(boolean arg0) {
//            return new Action[]{
//                CommonProjectActions.newFileAction(),
//                CommonProjectActions.copyProjectAction(),
//                CommonProjectActions.deleteProjectAction(),
//                CommonProjectActions.closeProjectAction()
//            };
//        }
        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage(ROSProject.ICON_RESOURCE);
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

//    @Override
//    public org.openide.nodes.Node createLogicalView() {
//        return new RootNode(project);
//    }
    private static final class RootNode extends AbstractNode {

        public static final String REGISTERED_NODE_LOCATION
                = "Projects/"+ROSProject.TYPE+"/Nodes";
//        public static final String REGISTERED_NODE_LOCATION
//                = "Projects/"+ROSProject.TYPE+"/Nodes";
        final ROSProject project;

        public RootNode(ROSProject project) {
            super(NodeFactorySupport.createCompositeChildren(project, 
                    ROSWorkspaceLogicalView.RootNode.REGISTERED_NODE_LOCATION), 
                    Lookups.singleton(project));
            this.project = project;
            setIconBaseWithExtension(ROSProject.ICON_RESOURCE);
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] nodeActions = new Action[8];
            nodeActions[0] = CommonProjectActions.newFileAction();
            //The 'null' indicates that the default icon will be used:
            nodeActions[1] = ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build", null);
            nodeActions[2] = ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_REBUILD, "Clean and Build", null);
            nodeActions[3] = ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_CLEAN, "Clean", null);
            nodeActions[4] = CommonProjectActions.copyProjectAction();
            nodeActions[5] = CommonProjectActions.deleteProjectAction();
            nodeActions[6] = CommonProjectActions.setAsMainProjectAction();
            nodeActions[7] = CommonProjectActions.closeProjectAction();
            return nodeActions;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage(ROSProject.ICON_RESOURCE);
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
