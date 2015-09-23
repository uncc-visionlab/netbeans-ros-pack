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
