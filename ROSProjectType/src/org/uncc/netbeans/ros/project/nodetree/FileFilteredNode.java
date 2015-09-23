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
