/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.awt.Component;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.makeproject.api.ui.LogicalViewNodeProvider;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author arwillis
 */
// Use this class to override the nodes returned within a netbeans MakeProject
//@ServiceProvider(service = org.netbeans.modules.cnd.makeproject.api.ui.LogicalViewNodeProvider.class)
public class ROS_MakeProjectLogicalViewNodeProvider implements LogicalViewNodeProvider {

    @Override
    public AbstractNode getLogicalViewNode(Project prjct) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
