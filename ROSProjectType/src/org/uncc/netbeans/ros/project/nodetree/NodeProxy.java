/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.nodetree;

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author arwillis
 */
class NodeProxy extends FilterNode {

    public NodeProxy(Node original) {
        super(original, new ProxyChildren(original));
    }

    // add your specialized behavior here...
}
