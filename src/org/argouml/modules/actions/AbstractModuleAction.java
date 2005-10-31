/**
 * 
 */
package org.argouml.modules.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.argouml.modules.container.ModuleContainer;

/**
 * @author lmaitre
 *
 */
public abstract class AbstractModuleAction extends AbstractAction {

    protected ModuleContainer parent;
    
    /**
     * 
     */
    public AbstractModuleAction(ModuleContainer p) {
        super();
        parent = p;
    }

    /**
     * @param name
     */
    public AbstractModuleAction(ModuleContainer p, String name) {
        super(name);
        parent = p;
    }

    /**
     * @param name
     * @param icon
     */
    public AbstractModuleAction(ModuleContainer p, String name, Icon icon) {
        super(name, icon);
        parent = p;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public abstract void actionPerformed(ActionEvent e);

}
