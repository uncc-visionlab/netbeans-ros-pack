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

import java.awt.Image;
import java.util.ArrayList;
import javax.swing.Action;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.ProjectSensitiveActions;
import org.openide.actions.*;
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
public class ROSProjectLogicalView implements LogicalViewProvider {

    public static final String REGISTERED_NODE_LOCATION
            = "Projects/" + ROSProject.TYPE + "/Nodes";
    private final ROSProject project;

    public ROSProjectLogicalView(ROSProject project) {
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        try {
            //Get the Text directory, creating if deleted
            FileObject text = project.getProjectDirectory();
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
                    //                    new ROSProjectChildrenFactory(project).Children(node),
                    new ProjectChildrenFactory(project, node),
                    //                    new FilterNode.Children(node),
                    // Custom child node handler/constructor 
                    //                    NodeFactorySupport.createCompositeChildren(project,
                    //                            REGISTERED_NODE_LOCATION),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup(new Lookup[]{
                node.getLookup(),
                Lookups.singleton(project)})
            );
            this.project = project;
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] parentActions = super.getActions(arg0);
            Action[] nodeActions = new Action[]{
                //The 'null' indicates that the default icon will be used:
                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build", null),
                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_REBUILD, "Clean and Build", null),
                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_CLEAN, "Clean", null),
                CommonProjectActions.closeProjectAction(),
                new RunROSCore(project),
                new RunRViz(project),
                new RunRqtGraph(project),
                //new RunCloneGitRepository(project),
                CommonProjectActions.setProjectConfigurationAction(),
                CommonProjectActions.customizeProjectAction()
            };
            ArrayList<Action> actionList = new ArrayList<Action>();
            int idx = 0;
            for (Action a : nodeActions) {
                if (a != null) {
                    actionList.add(a);
                }
            }
            for (Action a : parentActions) {
                if (a != null) {
                    System.out.println(a.getClass().toString());
                    if (!(a instanceof org.openide.actions.PropertiesAction)) {
                        actionList.add(a);
                    }
                }
            }
            return actionList.toArray(new Action[0]);
        }

        @Override
        public Image getIcon(int type
        ) {
            return ImageUtilities.loadImage(ROSProject.ICON_RESOURCE);
        }

        @Override
        public Image getOpenedIcon(int type
        ) {
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
