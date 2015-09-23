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

import org.uncc.netbeans.ros.project.ROSProjectProjectProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataFolder.FolderNode;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.uncc.netbeans.ros.project.ROSProject;

/**
 *
 * @author arwillis
 */
@NodeFactory.Registration(projectType = ROSProject.TYPE, position = 20)
public class ROSProjectProjectNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        ROSProjectProjectProvider rsp = project.getLookup().lookup(ROSProjectProjectProvider.class);
        assert rsp != null;
        return new ReportsNodeList(rsp.getSubprojects());
    }

    private class ReportsNodeList implements NodeList<Project> {

        Set<? extends Project> subprojects;

        public ReportsNodeList(Set<? extends Project> subprojects) {
            this.subprojects = subprojects;
        }

        @Override
        public List<Project> keys() {
            List<Project> result = new ArrayList<Project>();
            for (Project oneReportSubProject : subprojects) {
                result.add(oneReportSubProject);
            }
            return result;
        }

        @Override
        public Node node(final Project node) {
            FilterNode fn = null;
            try {
                DataObject d = DataObject.find(node.getProjectDirectory());
//                d.addPropertyChangeListener(this);
                Node n = d.getNodeDelegate();
//                n.addNodeListener(ROSProjectProjectNodeFactory.this);
//                if (n instanceof FolderNode && //false &&
//                        n.getName().equals(ROSProject.ROS_WORKSPACE_FOLDER)) {
//                    fn = new MakeProjectFilterNode(n, node);
//                } else {
                    fn = new FilterNode(n);
//                }
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
            return fn;
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
