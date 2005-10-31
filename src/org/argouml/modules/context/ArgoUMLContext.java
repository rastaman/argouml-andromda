/**
 * 
 */
package org.argouml.modules.context;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.argouml.andromda.SettingsTabAndroMDA;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

/**
 * @author lmaitre
 *
 */
public class ArgoUMLContext implements ModuleContext {

    private Properties projectProperties;
    
    /**
     * 
     */
    public ArgoUMLContext() {
        super();
        projectProperties = new Properties();
    }

    /**
     * @see org.argouml.modules.context.ModuleContext#getProjectPath()
     */
    public String getProjectPath() {
        String path = null;
        try {
            Project p = ProjectManager.getManager().getCurrentProject();
            if (p.getURL()!=null)
                path = new File(new URI(p.getURL().toExternalForm())).getCanonicalPath();            
        } catch (Exception e) {
            //Should display an error message to the user.
            e.printStackTrace();
        }
        return path;
    }

    /**
     * @see org.argouml.modules.context.ModuleContext#getMavenHome()
     */
    public String getMavenHome() {
        return getProperty(SettingsTabAndroMDA.KEY_MAVEN_HOME);
    }

    /**
     * @see org.argouml.modules.context.ModuleContext#getAndroMDAHome()
     */
    public String getAndroMDAHome() {
        return getProperty(SettingsTabAndroMDA.KEY_ANDROMDA_HOME);
    }

    /**
     * @see org.argouml.modules.context.ModuleContext#getProjectProperties()
     */
    public Properties getProjectProperties() {
        return projectProperties;
    }
    
    public void setProjectProperties(Properties props) {
        projectProperties = props;
    }

    /**
     * @see org.argouml.modules.context.ModuleContext#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        ConfigurationKey theKey = getKey(key);
        return Configuration.getString(theKey);
    }

    public void removeProperty(String key) {
        ConfigurationKey theKey = getKey(key);
        Configuration.removeKey(theKey);
    }
    
    /**
     * @see org.argouml.modules.context.ModuleContext#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(String key, String value) {
        ConfigurationKey theKey = getKey(key);
        Configuration.setString(theKey, value);
    }

    private ConfigurationKey getKey(String key) {
        //TODO: Use tokenizer
        String[] components = key.split(".");
        ConfigurationKey theKey = Configuration.makeKey((components.length>1)?components[0]:key);
        if (components.length>1) {
            for (int i=1;i<components.length;i++)
                theKey = Configuration.makeKey(theKey,components[i]);
        }
        Logger.getRootLogger().info("Return key "+theKey.getKey());
        return theKey;
    }
}
