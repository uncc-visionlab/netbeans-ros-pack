/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.ws;

import java.awt.Image;
import java.util.Enumeration;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.uncc.netbeans.ros.project.FilterFileNodesList;
import org.uncc.netbeans.ros.project.ROSProject;
import org.uncc.netbeans.ros.project.ROSProjectLogicalView;
import org.uncc.netbeans.ros.project.RunCloneGitRepository;
import org.uncc.netbeans.ros.project.RunROSCore;
import org.uncc.netbeans.ros.project.RunRViz;

/**
 *
 * @author arwillis
 */
/**
 * This is the node you actually see inside the project node for the project
 */
public class MakeProjectFilterNode extends FilterNode implements FileChangeListener {

    protected static final String IMAGE = "org/netbeans/modules/cnd/makeproject/ui/resources/makeProject.gif";
    private static Image smallImage = ImageUtilities.loadImage(ROSProject.ICON_RESOURCE); // NOI18N
    Project p;
    FileObject fobj = null;

//    public MakeProjectFilterNode(Node node) {
//        super(node);
//    }
    public MakeProjectFilterNode(Node node, Project project) throws DataObjectNotFoundException {
        super(node,
                // Default child node handler/constructor
                //                                        new FilterNode.Children(node),
                NodeFactorySupport.createCompositeChildren(project,
                        MakeProjectNodeFactory.REGISTERED_NODE_LOCATION),
                new ProxyLookup(new Lookup[]{
                    Lookups.singleton(project),
                    node.getLookup()})
        );
        p = project;
        if (fobj == null) {
            fobj = p.getProjectDirectory();
            fobj.addFileChangeListener(this);
        }
    }

    @Override
    public Action[] getActions(boolean arg0) {
        Action[] parentActions = super.getActions(arg0);
        Action[] nodeActions = new Action[]{ //                CommonProjectActions.newFileAction(),
        //                //The 'null' indicates that the default icon will be used:
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build", null),
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_REBUILD, "Clean and Build", null),
        //                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_CLEAN, "Clean", null),
        //                CommonProjectActions.moveProjectAction(),
        //                CommonProjectActions.renameProjectAction(),
        //                CommonProjectActions.copyProjectAction(),
        //                CommonProjectActions.deleteProjectAction(),
        //                CommonProjectActions.setAsMainProjectAction(),
        //            new RunROSCore(),
        //            new RunRViz(),
        //            new RunCloneGitRepository(), //                CommonProjectActions.closeProjectAction(),
        //                CommonProjectActions.setProjectConfigurationAction(),
        //                CommonProjectActions.customizeProjectAction()
        };
        Action[] allActions = new Action[parentActions.length + nodeActions.length];
        int i = 0;
        while (i < parentActions.length) {
            allActions[i] = parentActions[i];
            i++;
        }
        int j = 0;
        while (j < nodeActions.length) {
            allActions[i] = nodeActions[j];
            i++;
            j++;
        }
        return allActions;
    }

//    @Override
//    public Action[] getActions(boolean arg0) {
//        return new Action[]{ //            CommonProjectActions.newFileAction(),
//        //            CommonProjectActions.copyProjectAction(),
//        //            CommonProjectActions.deleteProjectAction(),
//        //            CommonProjectActions.closeProjectAction()
//        };
//    }
    @Override
    public String getDisplayName() {
        return "workspace";
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
//        DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
//        Image original = root.getNodeDelegate().getIcon(type);
        Image original = ImageUtilities.loadImage(IMAGE);
        return ImageUtilities.mergeImages(original, smallImage, 7, 7);
    }

    @Override
    public Image getOpenedIcon(int type) {
//        DataFolder root = DataFolder.findFolder(FileUtil.getConfigRoot());
//        Image original = root.getNodeDelegate().getIcon(type);
        Image original = ImageUtilities.loadImage(IMAGE);
        return ImageUtilities.mergeImages(original, smallImage, 7, 7);
    }

    void setNodes() {
        org.openide.nodes.Children c = this.getChildren();
        FilterFileNodesList fn = new FilterFileNodesList(p) {
            @Override
            public boolean checkAddOK(FileObject folderFile) {
                if (folderFile.getName().equals("nbproject")) {
                    // do nothing -- do not add to view
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public Node createNode(FileObject fobj, Node fobjNode) {
                return fobjNode;
            }
        };
        Node[] n1;
        n1 = fn.keys().toArray(new Node[0]);
        List<Node> nl = fn.keys();
        boolean addNode = true;
        for (Node newNode : nl) {
            for (int i = 0; i < c.getNodesCount(); i++) {
                if (c.getNodeAt(i).equals(newNode)) {
                    addNode = false;
                    break;
                }
            }
            if (addNode) {
                c.add(new Node[]{newNode});
            }
        }
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setNodes();
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setNodes();
    }

    @Override
    public void fileChanged(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setNodes();
    }

    @Override
    public void fileDeleted(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setNodes();
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setNodes();
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setNodes();
    }
}
