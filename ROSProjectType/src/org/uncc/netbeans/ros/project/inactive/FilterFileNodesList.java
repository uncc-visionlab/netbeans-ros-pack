/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.inactive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author arwillis
 */
public class FilterFileNodesList implements NodeList<Node> {

    Project project;

    public Node createNode(FileObject fobj, Node fobjNode) {
        return fobjNode;
    }

    public boolean checkAddOK(FileObject folderFile) {
        return true;
    }

    public FilterFileNodesList(Project project) {
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
                    if (checkAddOK(folderFile)) {
                        result.add(createNode(folderFile, fobjNode));
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
