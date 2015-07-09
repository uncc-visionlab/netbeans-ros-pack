/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.uncc.netbeans.ros.project;

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author arwillis
 */
@NodeFactory.Registration(projectType=AntBasedProject.TYPE, position=200)
public class AntBasedNodeFactory implements NodeFactory {

    public AntBasedNodeFactory() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public NodeList<AbstractNode> createNodes(Project proj) {
//        try {
            AbstractNode nd = new AbstractNode(Children.LEAF);
            nd.setDisplayName("Hello World!");
            return (NodeList<AbstractNode>) NodeFactorySupport.fixedNodeList(nd);
//        } catch (DataObjectNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return NodeFactorySupport.fixedNodeList();
    }

//    @Override
//    public NodeList createNodes(Project proj) {
//        try {
//            AntBasedFilterNode nd = new AntBasedFilterNode(proj);
//            return NodeFactorySupport.fixedNodeList(nd);
//        } catch (DataObjectNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return NodeFactorySupport.fixedNodeList();
//    }    
    
}