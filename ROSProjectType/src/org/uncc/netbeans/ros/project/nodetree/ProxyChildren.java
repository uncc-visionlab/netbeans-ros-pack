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
