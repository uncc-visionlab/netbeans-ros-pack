/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.nodetree;

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author arwillis
 */
class ProxyChildren extends FilterNode.Children {

    public ProxyChildren(Node owner) {
        super(owner);
    }

    @Override
    protected Node copyNode(Node original) {
        return new NodeProxy(original);
    }

    @Override
    protected Node[] createNodes(Node object) {
        List<Node> result = new ArrayList<Node>();

        for (Node node : super.createNodes(object)) {
            if (accept(node)) {
                result.add(node);
            }
        }

        return result.toArray(new Node[0]);
    }

    private boolean accept(Node node) {
        return true;
        // ...
    }
}
