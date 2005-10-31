package org.argouml.modules.context;

import java.util.Properties;

/**
 * Context for the operation of the module.
 * Hold configuration informations, allow to run the module from inside argo or as 
 * standalone application.
 * @author lmaitre
 *
 */
public interface ModuleContext {

    public String getProjectPath();
    
    public String getMavenHome();
    
    public String getAndroMDAHome();
    
    public Properties getProjectProperties();

    public void setProperty(String key, String value);
    
    public String getProperty(String key);
    
    public void removeProperty(String key);
    
}
