/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.inactive;

import org.openide.filesystems.FileObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.uncc.netbeans.ros.project.ROSProject;

/**
 *
 * @author arwillis
 */
public class CustomProjectChildrenFactory extends Children.Keys<Object> {

        Children.Array c = new Children.Array();

        public Children Children(Node n) {
            if (c.getNodesCount() == 0) {
                c.add(createNodes(p));
            }
            return c;
        }
        ROSProject p;

        public CustomProjectChildrenFactory(ROSProject p) {
            this.p = p;
        }

        @Override
        protected Node[] createNodes(Object t) {
            FilterFileNodesList fn = new FilterFileNodesList(p) {
//        return new FilterFileNodesList(p) {
                @Override
                public boolean checkAddOK(FileObject folderFile) {
                    if (folderFile.getName().equals("nbproject")) {
                        // do nothing -- do not add to view
                        return false;
                    } else if (folderFile.getName().equals(ROSProject.ROS_WORKSPACE_FOLDER)) {
                        // do nothing -- do not add to view
                        // this node is expanded by the workspace project provider
                        //
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
            return n1;
        }
    }
