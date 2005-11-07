/**
 * 
 */
package org.argouml.modules.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.swixml.Converter;
import org.swixml.Localizer;

/**
 * @author lmaitre
 *
 */
public class DefaultActionManager implements ActionManager, Converter {

    private Logger LOG = Logger.getLogger(DefaultActionManager.class);
    
    protected Map actionsByName;
    
    /**
     * 
     */
    public DefaultActionManager() {
        super();
        actionsByName = new HashMap();
    }

    public void addAction(String id, Action action) {
        //LOG.info("Add action '"+id+"' with value '"+action+"'");
        actionsByName.put(id,action);
    }
    
    public void removeAction(String id) {
        //
        actionsByName.remove(id);
    }
    
    /**
     * @see org.argouml.modules.actions.ActionManager#getAction(java.lang.String)
     */
    public Action getAction(String actionName) {
        return (Action) actionsByName.get(actionName);
    }

    public Collection getActions() {
        return actionsByName.values();
    }
    
    public Map getActionsMap() {
        return actionsByName;
    }
    
    /**
     * @see org.swixml.Converter#convert(java.lang.Class, org.jdom.Attribute, org.swixml.Localizer)
     */
    public Object convert(Class arg0, Attribute arg1, Localizer arg2) throws Exception {
        //LOG.info("Return action for "+arg1.getValue()+":"+getAction(arg1.getValue()));
        if (actionsByName.containsKey(arg1.getValue()))
            return getAction(arg1.getValue());
        else
            return new ManagedAction(this, arg1.getValue());
    }

    /**
     * @see org.swixml.Converter#convertsTo()
     */
    public Class convertsTo() {
        return Action.class;
    }

}
