package org.argouml.modules.actions;

import javax.swing.Action;

import org.swixml.Converter;

public interface ActionManager extends Converter {

    public Action getAction(String actionName);
    
    public void addAction(String id, Action action);
    
    public void removeAction(String id);
    
}
