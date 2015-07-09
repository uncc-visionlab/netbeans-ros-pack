/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project;

/**
 *
 * @author arwillis
 */
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class ROSProjectPropertiesProvider
        implements ProjectCustomizer.CompositeCategoryProvider {

    private static final String GENERAL = "General";

    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = "org-ros-project", position = 10)
    public static ROSProjectPropertiesProvider createGeneral() {
        return new ROSProjectPropertiesProvider();
    }

    @NbBundle.Messages("LBL_Config_General=General")
    @Override
    public Category createCategory(Lookup lkp) {
        return ProjectCustomizer.Category.create(
                GENERAL,
                Bundle.LBL_Config_General(),
                null);
    }

    @Override
    public JComponent createComponent(Category category, Lookup lkp) {
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(new JLabel("ROS Project Properties"), BorderLayout.CENTER);
        return jPanel1;
    }
}
