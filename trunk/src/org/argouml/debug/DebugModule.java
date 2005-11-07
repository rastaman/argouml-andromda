/**
 * 
 */
package org.argouml.debug;

import java.net.URL;

import org.apache.log4j.Logger;
import org.argouml.application.modules.ModuleLoader;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.modules.context.ContextFactory;
import org.argouml.modules.context.ModuleContext;

/**
 * @author lmaitre
 *
 */
public class DebugModule implements ModuleInterface {

    private static final Logger LOG = Logger.getLogger(DebugModule.class);
    
    public final static String VERSION = "0.1";
    
    public final static String NAME = "Debug Module";

    public final static String AUTHORS = "Ludovic Maitre";
    
    public final static String INFO = "This module allow to set Log4J configuration at runtime in ArgoUML.";

    public DebugModule() {
        LOG.info("Debug Module being created...");
        ModuleContext context = ContextFactory.getInstance().getContext();
        try {
            // Load the module ui definition
            URL uiDef = this.getClass().getResource("descriptor.xml");
            context.render( uiDef );
        } catch (Exception e) {
            e.printStackTrace();
        }      
        LOG.info("Debug Module created!");            
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#disable()
     */
    public boolean disable() {
        return true;
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#enable()
     */
    public boolean enable() {
        try {
            if (!ContextFactory.getInstance().isStandalone()) {
                LOG.info("Register Debug Settings tab");
                ModuleLoader settingsTabLoader = ModuleLoader.getInstance();
                settingsTabLoader.loadClassFromLoader(this.getClass().getClassLoader(),
                        SettingsTabDebug.class.getName(),
                        "org.argouml.debug.SettingsTabDebug",
                        true);                
            }
        } catch (Throwable e) {
            LOG.debug("Some problem when enabling the module.", e);
            disable();
            return false;
        }
        return true;        
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#getInfo(int)
     */
    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return DebugModule.INFO;
        case AUTHOR:
            return DebugModule.AUTHORS;
        case org.argouml.moduleloader.ModuleInterface.VERSION:
            return DebugModule.VERSION;
        default:
            return null;
        }
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#getName()
     */
    public String getName() {
        return DebugModule.NAME;
    }
}
