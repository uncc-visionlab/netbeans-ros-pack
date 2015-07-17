/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.test.module;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arwillis
 */
public class MyObject {
    public static final String ADD_CHILD = "ADD";
    public static final String CHILDREN_TYPE = "CHILDREN";
    public static final String LABEL_TYPE = "LABEL";
    public static final String REMOVE_CHILD = "REMOVE";
    
    private transient final PropertyChangeSupport propertyChangeSupport =
            new PropertyChangeSupport(this);

    private final MyObject parent;

    private String label;
    private List children = new ArrayList();
    
    
    public MyObject(final MyObject parent, final String label) {
        this.parent = parent;
        this.label = label;
    }
    
    public MyObject(final String label) {
        this(null, label);
    }
    
    

    public void addChild(final MyObject child) {
        final boolean oldState = this.hasChildren();
        final int oldChildren = children.size();
        
        children.add(child);
        propertyChangeSupport.firePropertyChange(
                CHILDREN_TYPE, oldState, this.hasChildren());
        propertyChangeSupport.firePropertyChange(
                ADD_CHILD, oldChildren, children.size());
    }

    
    public List getChildren() {
        return children;
    }
    
    public MyObject getParent() {
        return parent;
    }

    public String getLabel() {
        return label;
    }
    
    public boolean hasChildren() {
        return !children.isEmpty();
    }
    
    public boolean hasParent() {
        return parent != null;
    }
    
    public void removeChild(final MyObject child) {
        final boolean oldState = this.hasChildren();
        final int oldChildren = children.size();
        
        children.remove(child);
        propertyChangeSupport.firePropertyChange(
                CHILDREN_TYPE, oldState, this.hasChildren());
        propertyChangeSupport.firePropertyChange(
                REMOVE_CHILD, oldChildren, children.size());
    }

    public void setLabel(String label) {
        final String oldLabel = this.label;
        
        this.label = label;
        
        propertyChangeSupport.firePropertyChange(LABEL_TYPE, oldLabel, label);
    }

    public void addPropertyChangeListener(
            final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}