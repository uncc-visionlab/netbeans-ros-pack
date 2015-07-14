/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.ws;

/**
 *
 * @author arwillis
 */
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataObjectNotFoundException;

import org.openide.util.Exceptions;
import org.uncc.netbeans.ros.project.ROSProject;

@NodeFactory.Registration(projectType = ROSProject.TYPE+"_disable")
//@NodeFactory.Registration(projectType = ROSProject.TYPE)
public class ROSWorkspaceNodeFactory implements NodeFactory {

    @Override
    public NodeList createNodes(Project project) {
        //Optionally, only return a new node 
        //if some item is in the project's lookup: 
        //MyCoolLookupItem item = project.getLookup().lookup(MyCoolLookupItem.class);
        //if (item != null) {
        try {
            ROSWorkspaceNode nd = new ROSWorkspaceNode(project);
            return NodeFactorySupport.fixedNodeList(nd);
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        //} 
        //If the above try/catch fails, e.g., 
        //our item isn't in the lookup, 
        //then return an empty list of nodes: 
        return NodeFactorySupport.fixedNodeList();
    }
}
