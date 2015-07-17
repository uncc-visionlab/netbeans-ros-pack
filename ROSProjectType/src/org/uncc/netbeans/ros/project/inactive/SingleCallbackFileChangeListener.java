/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uncc.netbeans.ros.project.inactive;

import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileRenameEvent;

/**
 *
 * @author arwillis
 */
public abstract class SingleCallbackFileChangeListener implements FileChangeListener {

    public abstract void callback(FileEvent fe);
    
    @Override
    public void fileFolderCreated(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        callback(fe);
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        callback(fe);
    }

    @Override
    public void fileChanged(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        callback(fe);
    }

    @Override
    public void fileDeleted(FileEvent fe) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        callback(fe);
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        callback(fre);
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        callback(fae);
    }
    
}
