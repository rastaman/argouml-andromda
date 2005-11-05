package org.argouml.modules.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

public class ManagedAction extends AbstractAction {

    private Logger LOG = Logger.getLogger(ManagedAction.class);
    
    private ActionManager manager;
    
    private String name;
    
    public ManagedAction(ActionManager manager, String name) {
        super();
        this.manager = manager;
        this.name = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (manager.getAction(name)!=null) {
            manager.getAction(name).actionPerformed(e);
        } else
            LOG.warn("No action '"+name+"'.");
    }
}
