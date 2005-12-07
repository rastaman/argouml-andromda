package org.argouml.andromda;

import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;
import org.argouml.application.modules.ModuleLoader;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.modules.actions.ActionChooseFolder;
import org.argouml.modules.actions.ActionManager;
import org.argouml.modules.actions.DialogAction;
import org.argouml.modules.context.ContextFactory;
import org.argouml.modules.context.ModuleContext;
import org.argouml.modules.gui.RadioBoxTextAreaUpdaterListener;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.cmd.GenericArgoMenuBar;

public class AndroMDAModule implements ModuleInterface {

    private static final Logger LOG = Logger.getLogger(AndroMDAModule.class);

    public final static String VERSION = "0.5.2";

    public final static String NAME = "AndroMDA Module";

    public final static String AUTHORS = "Ludovic Maitre";

    public final static String INFO = "This module aims at integrating AndroMDA inside ArgoUML.";

    public final static String MENU_ID = "andromda:menu";

    private ModuleContext context;

    public AndroMDAModule() {
        LOG.info("AndroMDA Module being created...");
        context = ContextFactory.getInstance().getContext();
        try {
            // Load the menu
            URL uiDef = this.getClass().getResource("descriptor.xml");
            context.render(uiDef);            
        } catch (Exception e) {
            e.printStackTrace();
        }
        //init all things which have properties localized now, before we load the wizard
        //or other component. Each component must fetch his properties when instantiated,
        //since they can be potentially unloaded later.
        Map buttons = context.getAllElements("maven:goal");
        RadioBoxTextAreaUpdaterListener radioListener = new RadioBoxTextAreaUpdaterListener(context,"maven:launch:goal:help");
        radioListener.setPrefix("maven.launch.");
        Iterator butt = buttons.values().iterator();
        List goalsButtons = new ArrayList();
        Object item;
        while (butt.hasNext()) {
            item = butt.next();
            if (item instanceof JRadioButton) {
                radioListener.register((JRadioButton)item);
                goalsButtons.add(item);
            }
        }
        context.setAttribute("andromda-module:maven:goals",goalsButtons);
        ActionManager actionManager = context.getActionManager();
        ActionLaunchAndroMDA launchAction = new ActionLaunchAndroMDA(context);
        actionManager.addAction("andromda:maven:action:launch",
                launchAction);
        actionManager.addAction("andromda:maven:configuration:edit",
                new ActionConfigAndroMDA(context));
        actionManager.addAction("andromda:maven:project:settings",
                new DialogAction(context,"andromda.project.settings",
                        this.getClass().getResource("project-settings.xml")));
        actionManager.addAction("andromda:createproject",
                new ActionCreateProjectAndroMDA(context));
        actionManager.addAction("andromda:choosehome", new ActionChooseFolder(
                context, "andromda:home", "select.andromda.home"));
        actionManager.addAction("andromda:choosemavenhome",
                new ActionChooseFolder(context, "andromda:mavenhome",
                        "select.maven.home"));
        actionManager.addAction("andromda:projecthome:choose",
                new ActionChooseFolder(context, "andromda:projecthome",
                        "select.project.home"));
        actionManager
                .addAction("andromda:project:chooseparentfolder",
                        new ActionChooseFolder(context,
                                "parentFolder",
                                "select.parent.folder"));        
        LOG.info("AndroMDA Module created!");
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#disable()
     */
    public boolean disable() {
        GenericArgoMenuBar menubar = (GenericArgoMenuBar) ProjectBrowser
                .getInstance().getJMenuBar();
        removeMenuItems(menubar.getTools());
        return true;
    }

    /**
     * @see org.argouml.moduleloader.ModuleInterface#enable()
     */
    public boolean enable() {
        try {
            // Register items for this module into the Tools menu.
            if (!ContextFactory.getInstance().isStandalone()) {
                LOG.info("Add AndroMDA Menu items");
                GenericArgoMenuBar menubar = (GenericArgoMenuBar) ProjectBrowser
                        .getInstance().getJMenuBar();
                addMenuItems(menubar.getTools());
                LOG.info("Add AndroMDA Settings tab");
                ModuleLoader settingsTabLoader = ModuleLoader.getInstance();
                settingsTabLoader.loadClassFromLoader(this.getClass()
                        .getClassLoader(), SettingsTabAndroMDA.class.getName(),
                        "org.argouml.andromda.SettingsTabAndroMDA", true);
            }
        } catch (Throwable e) {
            LOG.debug("Some problem when enabling the module.", e);
            disable();
            return false;
        }
        return true;
    }

    public void addMenuItems(JMenu menu) {
        JMenu holder = (JMenu) context.find(MENU_ID);
        Component[] items = holder.getMenuComponents();
        for (int i=0;i<items.length;i++) {
            menu.add(items[i]); 
        }
    }

    public void removeMenuItems(JMenu menu) {
        JMenu holder = (JMenu) context.find(MENU_ID);
        Component[] items = holder.getMenuComponents();
        for (int i=0;i<items.length;i++) {
            menu.remove(items[i]); 
        }
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
