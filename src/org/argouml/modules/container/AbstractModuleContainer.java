/**
 * 
 */
package org.argouml.modules.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.argouml.modules.actions.ActionManager;
import org.argouml.modules.context.ModuleContext;
import org.argouml.ui.ArgoDialog;
import org.swixml.ConverterLibrary;
import org.swixml.SwingEngine;

/**
 * @author lmaitre
 *
 */
public abstract class AbstractModuleContainer implements ModuleContainer {

    protected Map componentsToId;
    
    protected ActionManager actionManager;
    
    protected SwingEngine swingEngine;

    protected Frame parentFrame;

    protected ModuleContext context;
    
    protected static ModuleContainer instance;
    
    public static ModuleContainer getInstance() {
        return instance;
    }
    
    /**
     * 
     */
    public AbstractModuleContainer() {
        super();
    }

    protected void initSwingEngine() {
        swingEngine = new SwingEngine( this );
        swingEngine.setClassLoader(this.getClass().getClassLoader());
        //The action manager must be set by the child class instance before init
        ConverterLibrary.getInstance().register(Action.class, actionManager);
        ConverterLibrary.getInstance().register(ModuleColorConverter.TEMPLATE, 
                new ModuleColorConverter());
        componentsToId = new HashMap();
    }
    
    /**
     * @see org.argouml.modules.container.ModuleContainer#getActionManager()
     */
    public ActionManager getActionManager() {
        return actionManager;
    }

    /**
     * @see org.argouml.modules.container.ModuleContainer#getSwingEngine()
     */
    public SwingEngine getSwingEngine() {
        return swingEngine;
    }

    /**
     * @see org.argouml.modules.container.ModuleContainer#getParentFrame()
     */
    public Frame getParentFrame() {
        return parentFrame;
    }

    /**
     * @see org.argouml.modules.container.ModuleContainer#getContext()
     */
    public ModuleContext getContext() {
        return context;
    }

    /**
     * @param actionManager The actionManager to set.
     */
    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    /**
     * @param context The context to set.
     */
    public void setContext(ModuleContext context) {
        this.context = context;
    }

    /**
     * @param parentFrame The parentFrame to set.
     */
    public void setParentFrame(Frame parentFrame) {
        this.parentFrame = parentFrame;
    }

    /**
     * @param swingEngine The swingEngine to set.
     */
    public void setSwingEngine(SwingEngine swingEngine) {
        this.swingEngine = swingEngine;
    }

    /**
     * @param key The key to localize
     * @return String localized
     */
    public String localize(String key) {
        return swingEngine.getLocalizer().getString(key);
    }

    /**
     * Return the id of a Swing component.
     * Use a Map to improve time of lookup (but this is more long when the component
     * isn't registered)
     * @param c
     * @return String the id of the component
     */
    public String getId(Component c) {
        String result = (String) componentsToId.get(c);
        if (result == null) {
            Map ids = getSwingEngine().getIdMap();
            Iterator i = ids.keySet().iterator();
            while (i.hasNext()) {
                String id = (String) i.next();
                Object idtmp = ids.get(id);
                if (idtmp.equals(c)) {
                    result=id;
                    componentsToId.put(c,id);
                }
            }
        }
        return result;
    }
    
    public void showFeedback(String msg) {
        final String mess = msg;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ArgoDialog dialog = new ArgoDialog(getParentFrame(),
                        getName(), 0, true);
                JLabel message = new JLabel(mess);
                message.setBackground((Color)UIManager.get("Label.background"));
                dialog.setContent(message);
                dialog.show();
            }
        });
    }

    public void showError(String errorKey) {
        showFeedback(localize(errorKey));
    }

    public void showError(String errorKey, String arg) {
        showFeedback(localize(errorKey)+arg);        
    }
    
    abstract public String getName();
    
}
