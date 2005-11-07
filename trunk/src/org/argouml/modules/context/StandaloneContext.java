/**
 * 
 */
package org.argouml.modules.context;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.swixml.SwingEngine;

/**
 * @author lmaitre
 *
 */
public class StandaloneContext extends AbstractModuleContext implements ModuleContext {

    private String propertiesFile;
    
    private Properties properties;
    
    public StandaloneContext() {
        super();
        properties = new Properties();
    }
    
    /**
     * 
     */
    public StandaloneContext(String propsFile) {
        super();
        properties = new Properties();
        if (propsFile!=null) {
            propertiesFile = propsFile;
            File props = new File(propsFile);
            if (props.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(propsFile);
                    properties.load(fis);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @see org.argouml.modules.context.ModuleContext#getProjectPath()
     */
    public String getProjectPath() {
        return properties.getProperty("project.path");
    }

    /**
     * @return Returns the properties.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * @param properties The properties to set.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * @return Returns the propertiesFile.
     */
    public String getPropertiesFile() {
        return propertiesFile;
    }

    /**
     * @param propertiesFile The propertiesFile to set.
     */
    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    /**
     * @see java.util.Properties#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * @see java.util.Hashtable#remove(java.lang.Object)
     */
    public void removeProperty(String key) {
        properties.remove(key);
    }

    /**
     * @see org.argouml.modules.context.AbstractModuleContext#getParentFrame()
     */
    public Frame getParentFrame() {
        return SwingEngine.getAppFrame();
    }

}
