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
import java.io.FileInputStream;
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

//public class ROSProjectPropertiesProvider
//        implements ProjectCustomizer.CompositeCategoryProvider {
//
//    private static final String GENERAL = "General";
//
//    @ProjectCustomizer.CompositeCategoryProvider.Registration(
//            projectType = ROSProject.TYPE, position = 10)
//    public static ROSProjectPropertiesProvider createGeneral() {
//        return new ROSProjectPropertiesProvider();
//    }
//
//    @NbBundle.Messages("LBL_Config_General=General")
//    @Override
//    public Category createCategory(Lookup lkp) {
//        return ProjectCustomizer.Category.create(
//                GENERAL,
//                Bundle.LBL_Config_General(),
//                null);
//    }
//
//    @Override
//    public JComponent createComponent(Category category, Lookup lkp) {
//        JPanel jPanel1 = new JPanel();
//        jPanel1.setLayout(new BorderLayout());
//        jPanel1.add(new JLabel("ROS Project Properties"), BorderLayout.CENTER);
//        return jPanel1;
//    }
//}
@NbBundle.Messages({
    "LBL_Config_Murphy1=Murphy 1",
    "LBL_Config_Murphy2=Murphy 2 "
})
public class ROSProjectPropertiesProvider
        implements ProjectCustomizer.CompositeCategoryProvider {

    private String name;

//    @ProjectCustomizer.CompositeCategoryProvider.Registration(projectType = ROSProjectPropertiesLookupProvider.CUSTOMIZER_FOLDER_PATH, position = 100)
    @ProjectCustomizer.CompositeCategoryProvider.Registration(projectType = ROSProject.TYPE, position = 100)
//    @ProjectCustomizer.CompositeCategoryProvider.Registration(projectType = "org-netbeans-modules-cnd-makeproject", position = 100)
    public static ROSProjectPropertiesProvider createMurphy1() {
        return new ROSProjectPropertiesProvider(Bundle.LBL_Config_Murphy1());
    }

    @ProjectCustomizer.CompositeCategoryProvider.Registration(projectType = ROSProject.TYPE, position = 110)
    public static ROSProjectPropertiesProvider createMurphy2() {
        return new ROSProjectPropertiesProvider(Bundle.LBL_Config_Murphy2());
    }

    private ROSProjectPropertiesProvider(String name) {
        this.name = name;
    }

//    @Override
//    public Category createCategory(Lookup lkp) {
//        ProjectCustomizer.Category toReturn = null;
//        if (Bundle.LBL_Config_Murphy1().equals(name)) {
//            toReturn = ProjectCustomizer.Category.create(
//                    Bundle.LBL_Config_Murphy1(),
//                    Bundle.LBL_Config_Murphy1(),
//                    null);
//        } else {
//            toReturn = ProjectCustomizer.Category.create(
//                    Bundle.LBL_Config_Murphy2(),
//                    Bundle.LBL_Config_Murphy2(), // NOI18N 
//                    null);
//        }
//        return toReturn;
//    }
//
//    @Override
//    public JComponent createComponent(Category category, Lookup lkp) {
//        String nm = category.getName();
//        if (Bundle.LBL_Config_Murphy1().equals(nm)) {
//            JPanel jPanel1 = new JPanel();
//            jPanel1.setLayout(new BorderLayout());
//            jPanel1.add(new JLabel(Bundle.LBL_Config_Murphy1()), BorderLayout.CENTER);
//            return jPanel1;
//        } else {
//            JPanel jPanel2 = new JPanel();
//            jPanel2.setLayout(new BorderLayout());
//            jPanel2.add(new JLabel(Bundle.LBL_Config_Murphy2()), BorderLayout.CENTER);
//            return jPanel2;
//        }
//    }
//    private ImportantFilesCustomizerTab(String name) {
//        this.name = name;
//    }
    @Override
    public Category createCategory(Lookup lkp) {
        ProjectCustomizer.Category toReturn = null;
        if (Bundle.LBL_Config1().equals(name)) {
            toReturn = ProjectCustomizer.Category.create(Bundle.LBL_Config1(), Bundle.LBL_Config1(), null);
        } else {
            toReturn = ProjectCustomizer.Category.create(Bundle.LBL_Config2(), Bundle.LBL_Config2(), null);
        }
        return toReturn;
    }

    @Override
    public JComponent createComponent(Category category, Lookup lkp) {
        ROSProject proj = lkp.lookup(ROSProject.class);
        FileObject fobj = proj.getProjectDirectory().getFileObject("nbproject").getFileObject("project.properties");
        Properties properties = new Properties();
        try {
            properties.load(fobj.getInputStream());
//            properties.load(new FileInputStream("nbproject/project.properties"));
        } catch (IOException e) {
            System.out.println("Could not open Config file");
        }

        String outDir = properties.getProperty("outDir");
        String logFile = properties.getProperty("logFile");
        String nm = category.getName();
        if (name.equals(nm)) {
            JPanel jPanel1 = new JPanel();
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(new JLabel(name), BorderLayout.CENTER);
            
            return jPanel1;
        } else {
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            jPanel2.add(new JLabel(name), BorderLayout.CENTER);
            return jPanel2;
        }
    }

    @NbBundle.Messages({"LBL_Config1=ConfigurationPart1"})
    @ProjectCustomizer.CompositeCategoryProvider.Registration(projectType = ROSProject.TYPE, position = 10)
    public static ROSProjectPropertiesProvider createMyDemoConfigurationTab1() {
        return new ROSProjectPropertiesProvider(Bundle.LBL_Config1());
    }

    @NbBundle.Messages({"LBL_Config2=ConfigurationPart2"})
    @ProjectCustomizer.CompositeCategoryProvider.Registration(projectType = ROSProject.TYPE, position = 20)
    public static ROSProjectPropertiesProvider createMyDemoConfigurationTab2() {
        return new ROSProjectPropertiesProvider(Bundle.LBL_Config2());
    }
}
