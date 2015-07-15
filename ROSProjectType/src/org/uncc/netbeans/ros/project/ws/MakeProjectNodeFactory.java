/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.ws;

import org.uncc.netbeans.ros.project.*;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.makeproject.MakeProject;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;

/**
 *
 * @author arwillis
 */
@NodeFactory.Registration(projectType = MakeProjectNodeFactory.TYPE, position = 200)
public class MakeProjectNodeFactory implements NodeFactory {
    public static final String TYPE = "org.uncc.netbeans.ros.project.ws";

    public static String REGISTERED_NODE_LOCATION = "Projects/" + TYPE + "/Nodes";

    public MakeProjectNodeFactory() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public NodeList createNodes(Project project) {
        MakeProject p = project.getLookup().lookup(MakeProject.class);
        assert p != null;
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
        return NodeFactorySupport.fixedNodeList(n1);
    }
}
