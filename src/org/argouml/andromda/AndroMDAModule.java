package org.argouml.andromda;

import java.net.URL;

import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.argouml.application.modules.ModuleLoader;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.modules.container.AbstractModuleContainer;
import org.argouml.modules.container.ModuleContainer;
import org.argouml.modules.context.ArgoUMLContext;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.cmd.GenericArgoMenuBar;

public class AndroMDAModule extends AbstractModuleContainer implements ModuleInterface, ModuleContainer {

    private static final Logger LOG = Logger.getLogger(AndroMDAModule.class);
    
    public final static String VERSION = "0.3";
    
    public final static String NAME = "AndroMDA Module";

    public final static String AUTHORS = "Ludovic Maitre";
    
    public final static String INFO = "This module aims at integrating AndroMDA inside ArgoUML.";
    
    private JMenuItem createProjectMenuItem;
    
    private JMenuItem launchMavenMenuItem;
    
    public AndroMDAModule() { 
        if (instance==null)
            instance=this;
        context = new ArgoUMLContext();
        actionManager = new AndroMDAModuleActionManager( this );
        parentFrame = ProjectBrowser.getInstance();
        initSwingEngine();
        try {
            // Load the menu
            URL uiDef = this.getClass().getResource("descriptor.xml");
            swingEngine.render( uiDef );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#disable()
     */
    public boolean disable() {
        GenericArgoMenuBar menubar =
            (GenericArgoMenuBar) ProjectBrowser.getInstance().getJMenuBar();
        menubar.getTools().remove(createProjectMenuItem);
        menubar.getTools().remove(launchMavenMenuItem);
        return true;
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#enable()
     */
    public boolean enable() {
        try {
            LOG.info("AndroMDA Module being created...");
            createProjectMenuItem = (JMenuItem) swingEngine.find("mi_newProj");
            launchMavenMenuItem = (JMenuItem) swingEngine.find("mi_maven");
            // Register into the Tools menu.
            GenericArgoMenuBar menubar =
            (GenericArgoMenuBar) ProjectBrowser.getInstance().getJMenuBar();
            menubar.getTools().add(createProjectMenuItem);
            menubar.getTools().add(launchMavenMenuItem);
            LOG.info("Add AndroMDA Settings tab");
            ModuleLoader settingsTabLoader = ModuleLoader.getInstance();
            settingsTabLoader.loadClassFromLoader(this.getClass().getClassLoader(),
                    SettingsTabAndroMDA.class.getName(),
                    "org.argouml.andromda.SettingsTabAndroMDA",
                    true);
            LOG.info("AndroMDA Module created!");            
        } catch (Throwable e) {
            LOG.debug("Some problem when adding the module.", e);
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
            return AndroMDAModule.INFO;
        case AUTHOR:
            return AndroMDAModule.AUTHORS;
        case org.argouml.moduleloader.ModuleInterface.VERSION:
            return AndroMDAModule.VERSION;
        default:
            return null;
        }
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#getName()
     */
    public String getName() {
        return AndroMDAModule.NAME;
    }

}
