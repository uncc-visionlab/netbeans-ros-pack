/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.inactive;

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.uncc.netbeans.ros.project.ProjectChildrenFactory;
import org.uncc.netbeans.ros.project.ROSProject;

/**
 *
 * @author arwillis
 */
@NodeFactory.Registration(projectType = ROSProject.TYPE, position = 200)
public class ROSNodeFactory implements NodeFactory {

    Project proj;
    ProjectChildrenFactory childrenFactory;  
    public ROSNodeFactory() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public NodeList createNodes(Project project) {
        ROSProject p = project.getLookup().lookup(ROSProject.class);
        assert p != null;
        FilterFileNodesList fn = new FilterFileNodesList(p) {
//        return new FilterFileNodesList(p) {
            @Override
            public boolean checkAddOK(FileObject folderFile) {
                if (folderFile.getName().equals("nbproject")) {
                    // do nothing -- do not add to view
                    return false;
//                } else if (folderFile.getName().equals(ROSProject.ROS_WORKSPACE_FOLDER)) {
                    // do nothing -- do not add to view
                    // this node is expanded by the workspace project provider
                    //
//                    return false;
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
//        try {
//               create default node for all elements
//            AbstractNode nd = new AbstractNode(Children.LEAF);
//            ROSWorkspaceFilterNode nd = new ROSWorkspaceFilterNode(project);
//            Node[] n2 = new Node[n1.length+1];
//            int i=0;
//            while(i < n1.length) {
//                n2[i] = n1[i];
//                i++;
//            }
//            n2[i]=nd;
//            return NodeFactorySupport.fixedNodeList(n2);
//        } catch (DataObjectNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return NodeFactorySupport.fixedNodeList(n1);
        return NodeFactorySupport.fixedNodeList();
    }
}
