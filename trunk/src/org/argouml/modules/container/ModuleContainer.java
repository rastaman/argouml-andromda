package org.argouml.modules.container;

import java.awt.Frame;

import org.argouml.modules.actions.ActionManager;
import org.argouml.modules.context.ModuleContext;
import org.swixml.SwingEngine;

public interface ModuleContainer {

    public ActionManager getActionManager();
    
    public SwingEngine getSwingEngine();
    
    public Frame getParentFrame();

    public ModuleContext getContext();
    
    public String localize(String key);
    
}
