/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.test.module;

import java.util.concurrent.Callable;
import org.openide.nodes.Children;

/**
 *
 * @author arwillis
 */
public class ObjectCallable implements Callable {

    private final MyObject key;
    
    public ObjectCallable(final MyObject key) {
        this.key = key;
    }
    
    @Override
    public Children call() throws Exception {
        if (!key.hasChildren()) {
            return Children.LEAF;
        } else {
            return Children.create(new ObjectChildFactory(key), true);
        }
    }
    
}
