/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.uncc.netbeans.ros.project;

import java.awt.Image;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.util.ImageUtilities;

/**
 *
 * @author arwillis
 */
public class AntBasedFilterNode extends FilterNode {

    private static Image smallImage =
            ImageUtilities.loadImage(AntBasedProject.ICON_RESOURCE); // NOI18N

    public AntBasedFilterNode(Project proj) throws DataObjectNotFoundException {
        super(DataObject.find(proj.getProjectDirectory().getFileObject("nbproject")).getNodeDelegate());
    }

    @Override
    public String getDisplayName() {
        return "Important Files";
    }

    //Next, we add icons, for the default state, which is
    //closed, and the opened state; we will make them the same.
    //Icons in project logical views are
    //based on combinations--you must combine the node's own icon
    //with a distinguishing badge that is merged with it. Here we
    //first obtain the icon from a data folder, then we add our
    //badge to it by merging it via a NetBeans API utility method:
    @Override
    public Image getIcon(int type) {
        DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
        Image original = root.getNodeDelegate().getIcon(type);
        return ImageUtilities.mergeImages(original, smallImage, 7, 7);
    }

    @Override
    public Image getOpenedIcon(int type) {
        DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
        Image original = root.getNodeDelegate().getIcon(type);
        return ImageUtilities.mergeImages(original, smallImage, 7, 7);
    }

}