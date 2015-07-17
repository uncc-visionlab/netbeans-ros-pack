/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.test.module;

import java.awt.BorderLayout;
import java.beans.IntrospectionException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author arwillis
 */
@TopComponent.Description(
    preferredID = "ObjectExplorerTopComponent",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.test.module.ObjectExplorerTopComponent")
@ActionReference(path = "Menu/Window")
//@ActionReference(path = "Menu/File")
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_ObjectExplorerAction",
preferredID = "ObjectExplorerTopComponent")
@Messages({
    "CTL_ObjectExplorerAction=ObjectExplorer",
    "CTL_ObjectExplorerTopComponent=ObjectExplorer Window",
    "HINT_ObjectExplorerTopComponent=This is a ObjectExplorer window"
})
public final class ObjectExplorerTopComponent extends TopComponent implements ExplorerManager.Provider {

    private ExplorerManager em = new ExplorerManager();
    private MyObject parent = new MyObject("parent");

    public ObjectExplorerTopComponent() throws IntrospectionException {
//        initComponents();
        setName(Bundle.CTL_ObjectExplorerTopComponent());
        setToolTipText(Bundle.HINT_ObjectExplorerTopComponent());
        setLayout(new BorderLayout());
        parent.addChild(new MyObject("First"));
        em.setRootContext(new ObjectNode(parent));
        add(new BeanTreeView(), BorderLayout.CENTER);
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
    }                                  

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}
