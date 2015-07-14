/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.ws;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.uncc.netbeans.ros.project.ROSProject;

/**
 *
 * @author arwillis
 */
//@NodeFactory.Registration(projectType = "org-netbeans-modules-cnd-makeproject", position = 20)
@NodeFactory.Registration(projectType = ROSProject.TYPE, position = 20)
public class ROSWorkspaceProjectNodeFactory implements NodeFactory {

//    @StaticResource()
//    public static final String SUB_ICON = "org/customer/project/sub/icon.png";
    @Override
    public NodeList<?> createNodes(Project project) {
        ROSWorkspaceProjectProvider rsp = project.getLookup().lookup(ROSWorkspaceProjectProvider.class);
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
        public Node node(Project node) {
            FilterNode fn = null;
            try {
                fn = new ROSWorkspaceFilterNode(node);
                fn = new FilterNode(DataObject.find(node.getProjectDirectory()).getNodeDelegate()) {
                    @Override
                    public Image getIcon(int type) {
                        return ImageUtilities.loadImage(ROSWorkspaceNode.IMAGE);
                    }

                    @Override
                    public Image getOpenedIcon(int type) {
                        return ImageUtilities.loadImage(ROSWorkspaceNode.IMAGE);
                    }
                };
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
