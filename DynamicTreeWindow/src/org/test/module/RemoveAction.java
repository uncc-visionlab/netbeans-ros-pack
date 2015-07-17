package org.test.module;


import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.test.module.MyObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author arwillis
 */
public class RemoveAction extends AbstractAction {
    
    private final MyObject bean;
    
    public RemoveAction(final MyObject bean) {
        this.bean = bean;
        this.putValue(AbstractAction.NAME, "Remove Node");
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (bean.hasParent()) {
            final MyObject parent = bean.getParent();
            
            parent.removeChild(bean);
        } else {
            JOptionPane.showMessageDialog(
                    null, "The head node cannot be removed!");
        }
    }   
}