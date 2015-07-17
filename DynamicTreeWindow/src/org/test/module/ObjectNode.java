/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.test.module;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author arwillis
 */
public class ObjectNode extends BeanNode implements PropertyChangeListener {

    private MyObject bean;
    
    public ObjectNode(MyObject bean) throws IntrospectionException {
        super(bean, Children.createLazy(new ObjectCallable(bean)),
                Lookups.singleton(bean));
        this.bean = bean;

        this.setDisplayName(bean.getLabel());
        bean.addPropertyChangeListener(this);
    }
    
    public final void checkChildren(final Object eventObject) {
        if (eventObject == Boolean.TRUE) {
            this.setChildren(Children.create(
                    new ObjectChildFactory(bean), false));
        } else if (eventObject == Boolean.FALSE) {
            this.setChildren(Children.LEAF);
        }
    }
    
    @Override
    public Action[] getActions(final boolean popup) {
        final Action[] returnActions = new Action[2];
        returnActions[0] = new AddAction(bean);
        returnActions[1] = new RemoveAction(bean);
        
        return returnActions;
    }
    
    @Override
    public Action getPreferredAction() {
        return new AddAction(bean);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(MyObject.LABEL_TYPE)){
            setDisplayName(bean.getLabel());
        }
        
        // We need to see if we have to update our children.
        else if (MyObject.CHILDREN_TYPE.equals(evt.getPropertyName())) {
            this.checkChildren(evt.getNewValue());
        }
    }
} 