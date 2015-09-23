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
package org.uncc.netbeans.ros.project;

import java.awt.Image;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.modules.cnd.makeproject.MakeProject;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.uncc.netbeans.ros.pkg.ROSPackageProject;
import org.uncc.netbeans.ros.project.inactive.MakeProjectFilterNode;
import org.uncc.netbeans.ros.project.inactive.ROSPkgFilterNode;

/**
 *
 * @author arwillis
 */
public class ProjectChildrenFactory extends FilterNode.Children {

    Project p;

    public ProjectChildrenFactory(Node or) {
        super(or);
    }

    public ProjectChildrenFactory(Project p, Node or) {
        this(or);
        this.p = p;
    }

    @Override
    protected void addNotify() {
        super.addNotify();
    }

    @Override
    protected void filterChildrenAdded(NodeMemberEvent nme) {
        super.filterChildrenAdded(nme);
    }

    @Override
    protected void filterChildrenRemoved(NodeMemberEvent nme) {
        super.filterChildrenRemoved(nme);
    }

    @Override
    protected void filterChildrenReordered(NodeReorderEvent nre) {
        super.filterChildrenReordered(nre);
    }

    @Override
    protected Node copyNode(Node n) {
        DataObject dobj = n.getLookup().lookup(DataObject.class);
        if (dobj != null && dobj instanceof DataFolder) {
            FileObject fobj = dobj.getPrimaryFile();
            // see if a project type owns this node
            if (ProjectManager.getDefault().isProject(fobj)) {
                Project p = FileOwnerQuery.getOwner(fobj);
                if (p != null) {
//                    if (!(p instanceof ROSPackageProject) &&
//                        !(p instanceof MakeProject)) {
                    LogicalViewProvider projLogicalViewProvider = p.getLookup().lookup(LogicalViewProvider.class);
                    final Image icon = projLogicalViewProvider.createLogicalView().getIcon(0);
                    return new FilterNode(n, new ProjectChildrenFactory(p, n),
                            new ProxyLookup(new Lookup[]{
                                Lookups.singleton(p),
                                n.getLookup()})) {
                                @Override
                                public Image getIcon(int type) {
                                    return icon;
                                }

                                @Override
                                public Image getOpenedIcon(int type) {
                                    return icon;
                                }
                            };
//                    if (projLogicalViewProvider != null) {
//                        Node projNode = projLogicalViewProvider.createLogicalView();
//                        if (projNode != null) {
//                            return projNode;
//                        }
//                    }
                }
            }
            // ROS-specific nodes (workspace C++ project / ROS package project)
            ROSProject project = ROSProject.findROSProject(fobj);
            FileObject nbFolder = project.getProjectDirectory().getFileObject("nbproject");
            if (fobj.getPath().equals(nbFolder.getPath())) {
                //  hide the netbeans config folder
            }
        }
//            else if (ROSProject.isROSWorkspaceFolder(fobj)) {
//                try {
//                    FilterNode fn = new MakeProjectFilterNode(n, p);
//                    return fn;
//                } catch (DataObjectNotFoundException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            } else if (ROSProject.isROSPackageFolder(fobj)) {
//                try {
//                    FilterNode fn = new ROSPkgFilterNode(n, p);
//                    return fn;
//                } catch (DataObjectNotFoundException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
//        } else {
//            FilterNode fn = new FilterNode(n, new ProjectChildrenFactory(p, n),
//                    new ProxyLookup(new Lookup[]{
//                        Lookups.singleton(p),
//                        n.getLookup()}));
//            return fn;
//        }
        FilterNode fn = new FilterNode(n, new ProjectChildrenFactory(p, n),
                new ProxyLookup(new Lookup[]{
                    Lookups.singleton(p),
                    n.getLookup()}));
        return fn;
//        return n.cloneNode();
    }

    @Override
    protected Node[] createNodes(Node n) {
        Node[] nodeArray = super.createNodes(n);
        if (nodeArray[0].getName().equals("nbproject")) {
            return null;
        }
        return nodeArray;
    }
}
