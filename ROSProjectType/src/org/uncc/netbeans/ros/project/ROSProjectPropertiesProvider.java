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
import java.io.IOException;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.uncc.netbeans.ros.project.ui.CustomizerPanelGeneral;

@NbBundle.Messages({"LBL_Config_General=General"})
public class ROSProjectPropertiesProvider
        implements ProjectCustomizer.CompositeCategoryProvider {

    private String name;

    private ROSProjectPropertiesProvider(String name) {
        this.name = name;
    }

    @Override
    public Category createCategory(Lookup lkp) {
        ProjectCustomizer.Category toReturn = null;
        if (Bundle.LBL_Config_General().equals(name)) {
            toReturn = ProjectCustomizer.Category.create(
                    Bundle.LBL_Config_General(),
                    Bundle.LBL_Config_General(),
                    null);
        }
        return toReturn;
    }

    @Override
    public JComponent createComponent(Category category, Lookup lkp) {
        String nm = category.getName();
        if (Bundle.LBL_Config_General().equals(nm)) {
            ROSProject proj = lkp.lookup(ROSProject.class);
            Properties properties = lkp.lookup(Properties.class);
            CustomizerPanelGeneral n = new CustomizerPanelGeneral(
                    proj.getProjectDirectory().getPath(),
                    properties);
            return n;
        } else {
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            jPanel2.add(new JLabel("Unknown"), BorderLayout.CENTER);
            return jPanel2;
        }
    }

    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = ROSProject.TYPE, position = 10)
    public static ROSProjectPropertiesProvider createGeneral() {
        return new ROSProjectPropertiesProvider(Bundle.LBL_Config_General());
    }
}
