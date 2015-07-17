/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.test.module;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author arwillis
 */
public class AddAction extends AbstractAction {

    private final MyObject bean;

    public AddAction(final MyObject bean) {
        this.bean = bean;
        this.putValue(AbstractAction.NAME, "Add Node");
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        bean.addChild(
                new MyObject(bean, String.valueOf(System.currentTimeMillis())));
    }
}
