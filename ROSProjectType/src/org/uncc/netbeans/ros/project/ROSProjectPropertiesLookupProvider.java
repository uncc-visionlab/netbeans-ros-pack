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
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;

public class ROSProjectPropertiesLookupProvider implements CustomizerProvider {

    public final ROSProject project;
    public static final String CUSTOMIZER_FOLDER_PATH = "Projects/" + ROSProject.TYPE + "/Customizer";
//    public static final String CUSTOMIZER_FOLDER_PATH = "Projects/org-netbeans-modules-cnd-makeproject/Customizer";
    Properties properties;

    public ROSProjectPropertiesLookupProvider(ROSProject project) {
        this.project = project;
    }

    @Override
    public void showCustomizer() {
        FileObject fobj = project.getProjectDirectory().getFileObject("nbproject").getFileObject("project.properties");
        properties = new Properties();
        try {
            InputStream is = fobj.getInputStream();
            properties.load(is);
            is.close();
        } catch (IOException e) {
            System.out.println("Could not open Config file");
        }
        Dialog dialog = ProjectCustomizer.createCustomizerDialog(
                //Path to layer folder: 
                CUSTOMIZER_FOLDER_PATH,
                //Lookup, which must contain, at least, the Project: 
                Lookups.fixed(project, properties),
                //Preselected category: 
                "",
                //OK button listener: 
                new OKOptionListener(),
                null,
                //HelpCtx for Help button of dialog: 
                null);
        dialog.setTitle(ProjectUtils.getInformation(project).getDisplayName());
        dialog.setVisible(true);
    }

    private class OKOptionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StatusDisplayer.getDefault().setStatusText("Settings for "
                    + project.getProjectDirectory().getName() + " stored.");
            FileObject fobj = project.getProjectDirectory().getFileObject("nbproject").getFileObject("project.properties");
            try {
                OutputStream os = fobj.getOutputStream();
                properties.store(os,null);
                os.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
