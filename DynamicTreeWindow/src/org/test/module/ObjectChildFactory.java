/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.test.module;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.*;

/**
 *
 * @author arwillis
 */
public class ObjectChildFactory extends ChildFactory.Detachable<MyObject>
        implements PropertyChangeListener {

    private MyObject key;

    public ObjectChildFactory(final MyObject key) {
        this.key = key;
        key.addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List toPopulate) {
        final List<MyObject> children = key.getChildren();

        for (MyObject child : children) {
            toPopulate.add(child);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(MyObject key) {
        ObjectNode node = null;
        try {
            node = new ObjectNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (MyObject.ADD_CHILD.equals(evt.getPropertyName())) {
            this.refresh(true);
        } else if (MyObject.REMOVE_CHILD.equals(evt.getPropertyName())) {
            this.refresh(true);
        }
    }
}