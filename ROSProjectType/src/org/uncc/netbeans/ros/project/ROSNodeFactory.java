/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.modules.cnd.makeproject.MakeProject;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.uncc.netbeans.ros.project.ws.ROSWorkspaceFilterNode;
import org.uncc.netbeans.ros.project.ws.ROSWorkspaceFilterNode2;

/**
 *
 * @author arwillis
 */
@NodeFactory.Registration(projectType = ROSProject.TYPE, position = 200)
public class ROSNodeFactory implements NodeFactory {

    public ROSNodeFactory() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public NodeList createNodes(Project project) {
        ROSProject p = project.getLookup().lookup(ROSProject.class);
        assert p != null;
        return new FileNodesList(p);
//        try {
////               create default node for all elements
////            AbstractNode nd = new AbstractNode(Children.LEAF);
//            ROSProjectFilterNode nd = new ROSProjectFilterNode(project);
//            return NodeFactorySupport.fixedNodeList(nd);
//        } catch (DataObjectNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        return NodeFactorySupport.fixedNodeList();
    }

    private class FileNodesList implements NodeList<Node> {

        ROSProject project;

        public FileNodesList(ROSProject project) {
            this.project = project;
        }

        @Override
        public List<Node> keys() {
            FileObject textsFolder = project.getProjectDirectory();
            List<Node> result = new ArrayList<Node>();
            if (textsFolder != null) {
                FileObject[] fobjs = textsFolder.getChildren();
                Comparator<FileObject> fileNameComparator = new Comparator<FileObject>() {

                    @Override
                    public int compare(FileObject fileObj1, FileObject fileObj2) {
                        if (fileObj1.isFolder() && !fileObj2.isFolder()) {
                            return -1;
                        }
                        if (!fileObj1.isFolder() && fileObj2.isFolder()) {
                            return 1;
                        }
                        String fileName1 = fileObj1.getName().toUpperCase();
                        String fileName2 = fileObj2.getName().toUpperCase();
                        //ascending order
                        return fileName1.compareTo(fileName2);
                        //descending order
                        //return fruitName2.compareTo(fruitName1);
                    }

                };

                Arrays.sort(fobjs, fileNameComparator);
                for (FileObject folderFile : fobjs) {
                    try {
                        Node fobjNode = DataObject.find(folderFile).getNodeDelegate();
                        if (folderFile.getName().equals("nbproject")) {
                            // do nothing -- do not add to view
                        } else if (folderFile.getName().equals("ros_ws")) {
                            // do nothing -- do not add to view
//                            ROSWorkspaceFilterNode nd = new ROSWorkspaceFilterNode(project);                            
                            ROSWorkspaceFilterNode2 nd = new ROSWorkspaceFilterNode2(project);                            
                            result.add(nd);
                        } else {
                            result.add(DataObject.find(folderFile).getNodeDelegate());
                        }

                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
            return result;
        }

        @Override
        public Node node(Node node) {
            return new FilterNode(node);
        }

        @Override
        public void addNotify() {
        }

        @Override
        public void removeNotify() {
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
        }

    }

}
