/**
 * 
 */
package org.argouml.modules.context;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.argouml.modules.actions.ActionManager;
import org.argouml.modules.actions.DefaultActionManager;
import org.argouml.modules.gui.ColorConverter;
import org.argouml.modules.gui.Item;
import org.argouml.modules.gui.ItemList;
import org.argouml.modules.gui.SwixMLUtils;
import org.argouml.ui.ArgoDialog;
import org.swixml.ConverterLibrary;
import org.swixml.SwingEngine;

/**
 * @author lmaitre
 *
 */
public abstract class AbstractModuleContext implements ModuleContext {

    protected Logger LOG = Logger.getLogger(AbstractModuleContext.class);
    
    protected Map componentsToId;
    
    protected Map attributes;
    
    protected ActionManager actionManager;
    
    protected SwixMLUtils swixFacade;
    
    protected SwingEngine swingEngine;

    protected Frame parentFrame;
    
    /**
     * 
     */
    public AbstractModuleContext() {
        super();
        attributes = Collections.synchronizedMap(new HashMap());
        actionManager = new DefaultActionManager();
        componentsToId = new HashMap();
        swingEngine = new SwingEngine( this );
        swingEngine.setClassLoader(this.getClass().getClassLoader());
        swingEngine.getTaglib().registerTag("itemList",ItemList.class);
        swingEngine.getTaglib().registerTag("item",Item.class);        
        ConverterLibrary.getInstance().register(Action.class, actionManager);
        ConverterLibrary.getInstance().register(ColorConverter.TEMPLATE, 
                new ColorConverter());
        swixFacade = new SwixMLUtils(this);
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
     * @param actionManager The actionManager to set.
     */
    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
        ConverterLibrary.getInstance().register(Action.class, actionManager);
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
    public String getComponentId(Object c) {
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

    /**
     * 
     * @param msg
     */
    public void showFeedback(String tit, String msg) {
        final String mess = msg;
        final String title = tit;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ArgoDialog dialog = new ArgoDialog(getParentFrame(),
                        title , 0, true);
                JLabel message = new JLabel(mess);
                message.setBackground((Color)UIManager.get("Label.background"));
                dialog.setContent(message);
                dialog.show();
            }
        });
    }

    /**
     * Show the localized error indicated by the key
     * @param errorKey
     */
    public void showError(String errorKey) {
        showFeedback(localize("error.title"),localize(errorKey));
    }
    
    /**
     * Show the localized error indicated by the key
     * and the arg.
     * @param errorKey
     * @param arg
     */
    public void showError(String errorKey, String arg) {
        showFeedback(localize("error.title"),localize(errorKey)+arg);        
    }

    /**
     * Return the JComponent of other object identified by the id and registered
     * in the SwingEngine.
     * @see org.argouml.modules.context.ModuleContext#find(java.lang.String)
     */
    public Object find(String componentId) {
        return swingEngine.getIdMap().get(componentId);
    }
    
    public Object render(URL ressource) throws Exception {
        if (parentFrame == null && ContextFactory.getInstance().isStandalone()) {
            parentFrame = (Frame) swingEngine.render( ressource );
        } else {
            JPanel buffer = new JPanel();
            insert( ressource , buffer );
        }
        return parentFrame;
    }

    public void insert(URL ressource, Container container) throws Exception {
        swingEngine.insert( ressource , container );
    }
    
    public Map getAllElements(String namespace) {
        return swixFacade.getAllElements(namespace);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key,value);
    }
    
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    public Map getAttributes() {
        return attributes;
    }
    
}
