package org.argouml.modules.andromda.ui;

import java.awt.Frame;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.swixml.SwingEngine;

public class AndroMDAModule implements org.argouml.moduleloader.ModuleInterface, SwixMLContainer {

    private static final Logger LOG = Logger.getLogger(AndroMDAModule.class);
    
    public final static String VERSION = "0.3";
    
    public final static String NAME = "AndroMDA Module";

    public final static String AUTHORS = "Ludovic Maitre";
    
    public final static String INFO = "This module aims at integrating AndroMDA inside ArgoUML.";
    
    private SwingEngine swingEngine;
    
    public Action createProject = new ActionCreateProjectAndroMDA(this);
    
    public Action launchMaven = new ActionLaunchAndroMDA(this);
    
    private JMenuItem createProjectMenuItem;
    
    private JMenuItem launchMavenMenuItem;

    public AndroMDAModule() {
        swingEngine = new SwingEngine( this );
        swingEngine.setClassLoader(this.getClass().getClassLoader());
        try {
            // Load the menu
            URL uiDef = this.getClass().getResource("descriptor.xml");
            swingEngine.render( uiDef );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static String getArgoUMLProjectPath() throws Exception {
        Project p = ProjectManager.getManager().getCurrentProject();
        return new File(new URI(p.getURL().toExternalForm())).getCanonicalPath();
    }

    static Frame getArgoUMLParentFrame() {
        return ProjectBrowser.getInstance();        
    }

    static String getArgoUMLMavenHome() {
        return Configuration.getString(SettingsTabAndroMDA.KEY_MAVEN_HOME);
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
            LOG.debug("AndroMDA Module created!");            
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

    public SwingEngine getSwingEngine() {
        return swingEngine;
    }
}
