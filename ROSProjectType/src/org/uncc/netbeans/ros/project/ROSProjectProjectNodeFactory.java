/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

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
import org.uncc.netbeans.ros.project.ws.MakeProjectFilterNode;

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
                if (n instanceof FolderNode && //false &&
                        n.getName().equals(ROSNodeFactory.ROS_WORKSPACE_FOLDER)) {
                    fn = new MakeProjectFilterNode(n, node);
                } else {
                    fn = new FilterNode(n);
                }
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
