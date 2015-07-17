/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.nodetree;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author arwillis
 */
class FileFilteredNode extends FilterNode {

    static class FileFilteredChildren extends FilterNode.Children {

        private final FileFilter fileFilter;

        public FileFilteredChildren(Node owner, FileFilter fileFilter) {
            super(owner);
            this.fileFilter = fileFilter;
        }

        @Override
        protected Node copyNode(Node original) {
            return new FileFilteredNode(original, fileFilter);
        }

        @Override
        protected Node[] createNodes(Node object) {
            List<Node> result = new ArrayList<Node>();

            for (Node node : super.createNodes(object)) {
                DataObject dataObject = (DataObject) node.getLookup().lookup(DataObject.class);

                if (dataObject != null) {
                    FileObject fileObject = dataObject.getPrimaryFile();
                    File file = FileUtil.toFile(fileObject);

                    if (fileFilter.accept(file)) {
                        result.add(node);
                    }
                }
            }
            return result.toArray(new Node[result.size()]);
        }
    }

    public FileFilteredNode(Node original, FileFilter fileFilter) {
        super(original, new FileFilteredChildren(original, fileFilter));
    }
}
