package org.argouml.modules.context;

import java.awt.Frame;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.argouml.modules.actions.ActionManager;
import org.swixml.SwingEngine;

/**
 * Context for the operation of the module.
 * Hold configuration informations, allow to run the module from inside argo or as 
 * standalone application.
 * @author lmaitre
 *
 */
public interface ModuleContext {
   
    public Properties getProperties();

    public void setProperty(String key, String value);
    
    public String getProperty(String key);
    
    public void removeProperty(String key);
    
    public String getProjectPath();
    
    public void setAttribute(String key, Object value);
    
    public Object getAttribute(String key);

    public Map getAttributes();

    public ActionManager getActionManager();
    
    public SwingEngine getSwingEngine();
    
    public Object find(String componentId);
    
    public Frame getParentFrame();
    
    public String localize(String key);
    
    public String getComponentId(Object component);
    
    public void setActionManager(ActionManager manager);

    /**
     * @param parentFrame The parentFrame to set.
     */
    public void setParentFrame(Frame parentFrame);

    /**
     * @param swingEngine The swingEngine to set.
     */
    public void setSwingEngine(SwingEngine swingEngine);
    
    /**
     * 
     * @param msg
     */
    public void showFeedback(String title, String msg);

    /**
     * Show the localized error indicated by the key
     * @param errorKey
     */
    public void showError(String errorKey);
    
    /**
     * Show the localized error indicated by the key
     * and the arg.
     * @param errorKey
     * @param arg
     */
    public void showError(String errorKey, String arg);

    /**
     * 
     * @param ressource
     * @return
     * @throws Exception
     */
    public Object render(URL ressource) throws Exception;

    /**
     * Return all elements in this namespace. A namespace in the xml gui descriptor
     * is denotated by &lt;namespacename&gt;:objectid. This method return all elements
     * with a id wich begin with the string argument.
     * @param namespace
     * @return Map Of id/elements which match the namespace.
     */
    public Map getAllElements(String namespace);
}
