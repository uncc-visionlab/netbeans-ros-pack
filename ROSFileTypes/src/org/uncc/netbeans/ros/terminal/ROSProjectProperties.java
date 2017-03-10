/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.terminal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.netbeans.api.project.FileOwnerQuery.getOwner;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.modules.ModuleInfo;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author arwillis
 */
public class ROSProjectProperties {

    public static String ROS_MAKE_PROPERTYNAME = "make";
    public static String ROS_ROOTFOLDER_PROPERTYNAME = "ros.root";
    public static String ROS_WORKSPACEFOLDER_PROPERTYNAME = "ros.ws";
    public static String ROS_SOURCEFOLDER_PROPERTYNAME = "ros.ws.src";
    public static String ROS_BUILDFOLDER_PROPERTYNAME = "ros.ws.build";
    public static String ROS_DEVELFOLDER_PROPERTYNAME = "ros.ws.devel";
    public static String ROS_INSTALLFOLDER_PROPERTYNAME = "ros.ws.install";

    public static boolean isMakeProjectFolder(FileObject fobj) {
        return (fobj.getFileObject("ros_ws")==null);
    }

    public static boolean isROSProjectFolder(FileObject fobj) {
        return (fobj.getFileObject("ros_ws")!=null);
    }
    
    public static FileObject getDevelFolder(Project project) {
        FileObject develFolder = null;
        String wsFolder = getProperty(project,
                ROSProjectProperties.ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String wsDevelFolder = getProperty(project,
                ROSProjectProperties.ROS_DEVELFOLDER_PROPERTYNAME);
        if (isROSProjectFolder(project.getProjectDirectory())) {
            develFolder = project.getProjectDirectory().getFileObject(wsFolder)
                    .getFileObject(wsDevelFolder);
        } else if (isMakeProjectFolder(project.getProjectDirectory())) {
            develFolder = project.getProjectDirectory().getFileObject(wsDevelFolder);
        }        
        return develFolder;
    }
    
    public static String getPackageName(Project p, DataObject context) {
        String pkgName = null, xml_pkgName = null;
        String rosWs = ROSProjectProperties.getProperty(p, ROS_WORKSPACEFOLDER_PROPERTYNAME);
        String rosPkgSrc = ROSProjectProperties.getProperty(p, ROS_SOURCEFOLDER_PROPERTYNAME);
        FileObject packageParent = null;
        if (isROSProjectFolder(p.getProjectDirectory())) {
                packageParent = p.getProjectDirectory().getFileObject(rosWs).getFileObject(rosPkgSrc);
        } else if (isMakeProjectFolder(p.getProjectDirectory())) {
                packageParent = p.getProjectDirectory().getFileObject(rosPkgSrc);            
        }
        FileObject invokingFileObj = context.getPrimaryFile();
        String pathRelToPkgSrc = invokingFileObj.getPath().replace(packageParent.getPath(), "");
        String[] pkgSubFolderNames = pathRelToPkgSrc.split(File.separator);
        for (String pkgSubFolderName : pkgSubFolderNames) {
            pkgName = pkgSubFolderName;
            packageParent = packageParent.getFileObject(pkgName);
            if (isValidROSPackageFolder(packageParent)) {
                break;//return pkgName;
            }
        }
        FileObject package_XML = packageParent.getFileObject("package.xml");
        if (package_XML == null) {
            return pkgName;
        } else {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            Document doc = null;
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(package_XML.getInputStream());
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            if (doc != null) {
                doc.getDocumentElement().normalize();
                //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList nList = doc.getDocumentElement().getChildNodes();
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    org.w3c.dom.Node nNode = nList.item(temp);
                    //System.out.println("\nCurrent Element :" + nNode.getNodeName());
//                    if (nNode.getNodeType() == org.w3c.dom.Node.TEXT_NODE) {
//                        System.out.println(nNode.getNodeValue());
//                    }
                    if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (nNode.getNodeName().equals("name")) {
                            xml_pkgName = eElement.getFirstChild().getNodeValue();
                            break;
                        }
                    }
                }
            }
        }
        if (xml_pkgName != null) {
            pkgName = xml_pkgName;
        }
        return pkgName;
    }

    public static Project findProject(FileObject fobj) {
        Project p = null;
        while (fobj != null && p == null) {
            Project pVal = getOwner(fobj);
            // Get a parent project that is a netbeans project 
            // this will be either class ROSProject or MakeProject but not
            // class ROSPackageProject
            if (pVal instanceof Project && 
                    pVal.getProjectDirectory().getFileObject("nbproject") != null) {
                p = pVal;
            }
            fobj = fobj.getParent();
        }
        return p;
    }

    public static boolean gradlePluginPresent() {
        Collection<? extends ModuleInfo> modules = Lookup.getDefault().lookupAll(ModuleInfo.class
        );
        for (ModuleInfo mi : modules) {
            //System.out.println(mi.getDisplayName());
            if (mi.getDisplayName().contains("Gradle Support")) {
                return true;
            }
        }

        return false;
    }

    public static boolean isValidROSPackageFolder(FileObject folder) {
        return isROSPackageFolderPriv(folder, false);
    }

    public static boolean isROSPackageFolder(FileObject folder) {
        return isROSPackageFolderPriv(folder, true);
    }

    private static boolean isROSPackageFolderPriv(FileObject folder,
            boolean checkForGradle) {
        if (folder.isFolder()
                && folder.getFileObject("CMakeLists.txt") != null
                && folder.getFileObject("package.xml") != null) {
            if (checkForGradle
                    && gradlePluginPresent()
                    && folder.getFileObject("build.gradle") != null) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isROSWorkspaceFolder(FileObject folder) {
        Project project = findProject(folder);
        if (project != null) {
            String rosWs = getProperty(project, ROS_WORKSPACEFOLDER_PROPERTYNAME);
            FileObject workspaceParent = project.getProjectDirectory().getFileObject(rosWs);
            return folder.getPath().equals(workspaceParent.getPath());
        }
        return false;
    }

    public static String getProperty(Project p, String propertyName) {
        FileObject fobj = null;
        try {
            fobj = p.getProjectDirectory().getFileObject("ros_ws").getFileObject("nbproject").getFileObject("ros.project.properties");
        } catch (Exception e) {
            System.out.println("Could not open config file ros.project.properties in ros_ws/nbproject folder");
        }

        if (fobj == null) {
            fobj = p.getProjectDirectory().getFileObject("nbproject").getFileObject("ros.project.properties");
        }
        if (fobj == null) 
            return null;
        Properties properties = new Properties();
        try {
            InputStream is = fobj.getInputStream();
            properties.load(is);
            is.close();
        } catch (IOException e) {
            System.out.println("Could not open config file: ros.project.properties");
        }
        return properties.getProperty(propertyName);
    }    
}
