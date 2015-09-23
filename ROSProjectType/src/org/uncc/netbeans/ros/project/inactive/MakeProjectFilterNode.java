/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.inactive;

import java.awt.Image;
import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.uncc.netbeans.ros.project.ProjectChildrenFactory;
import org.uncc.netbeans.ros.project.ROSProject;

/**
 *
 * @author arwillis
 */
/**
 * This is the node you actually see inside the project node for the project
 */
public class MakeProjectFilterNode extends FilterNode {

    protected static final String IMAGE = "org/netbeans/modules/cnd/makeproject/ui/resources/makeProject.gif";
    private static final Image smallImage = ImageUtilities.loadImage(ROSProject.ICON_RESOURCE); // NOI18N
    Project p;

    public MakeProjectFilterNode(Node node, Project project) throws DataObjectNotFoundException {
        super(node,
                // Default child node handler/constructor
                new ProjectChildrenFactory(project, node),
                //                    new FilterNode.Children(node),
                //                NodeFactorySupport.createCompositeChildren(project,
                //                        MakeProjectNodeFactory.REGISTERED_NODE_LOCATION),
                new ProxyLookup(new Lookup[]{
                    Lookups.singleton(project),
                    node.getLookup()})
        );
        p = project;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Action[] parentActions = super.getActions(arg0);
        Action[] nodeActions = new Action[]{ 
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build", null),
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_REBUILD, "Clean and Build", null),
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_CLEAN, "Clean", null),
        };
        Action[] allActions = new Action[parentActions.length + nodeActions.length];
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
    public String getDisplayName() {
        return "workspace";
    }

    @Override
    public Image getIcon(int type) {
        Image original = ImageUtilities.loadImage(IMAGE);
        return ImageUtilities.mergeImages(original, smallImage, 7, 7);
    }

    @Override
    public Image getOpenedIcon(int type) {
        Image original = ImageUtilities.loadImage(IMAGE);
        return ImageUtilities.mergeImages(original, smallImage, 7, 7);
    }
}
