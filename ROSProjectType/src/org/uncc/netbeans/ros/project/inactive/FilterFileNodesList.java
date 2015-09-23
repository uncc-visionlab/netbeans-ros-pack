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
