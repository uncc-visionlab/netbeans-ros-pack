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
package org.uncc.netbeans.ros.project.inactive;

import java.awt.Image;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.uncc.netbeans.ros.project.ProjectChildrenFactory;
import org.uncc.netbeans.ros.project.ROSProject;

/**
 *
 * @author arwillis
 */
/**
 * This is the node you actually see inside the project node for the project
 */
public class ROSPkgFilterNode extends FilterNode {

    @StaticResource()
    protected static final String IMAGE = "org/uncc/netbeans/ros/pkg/package.png";
    private static final Image smallImage = ImageUtilities.loadImage(ROSProject.ICON_RESOURCE); // NOI18N
    Project p;

    public ROSPkgFilterNode(Node node, Project project) throws DataObjectNotFoundException {
        super(node,
                // Default child node handler/constructor
                //                new ProjectChildrenFactory(project, node),
                new ProjectChildrenFactory(project, node));
//                new FilterNode.Children(node),
        //                NodeFactorySupport.createCompositeChildren(project,
        //                        MakeProjectNodeFactory.REGISTERED_NODE_LOCATION),
//                new ProxyLookup(new Lookup[]{
//                    Lookups.singleton(project),
//                    node.getLookup(),})
//        );
        p = project;
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Action[] parentActions = super.getActions(arg0);
        Action[] nodeActions = new Action[]{ //                //The 'null' indicates that the default icon will be used:
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build", null),
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_REBUILD, "Clean and Build", null),
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_CLEAN, "Clean", null),
        //            new RunROSCore(),
        };
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
    public String getDisplayName() {
        return super.getDisplayName();
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
