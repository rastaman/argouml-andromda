/**
 * 
 */
package org.argouml.andromda;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import javax.swing.JMenu;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.argouml.debug.DebugModule;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.modules.context.ContextFactory;
import org.argouml.modules.context.ModuleContext;

/**
 * @author lmaitre
 * 
 */
public class SampleMdaLauncher {

    private final static String UI_DESCRIPTOR = "app.xml";

    private final static String LAUNCHER_PROPERTIES = "samplemdalauncher.properties";

    private Logger LOG = Logger.getLogger(SampleMdaLauncher.class);

    private ModuleContext context;

    /**
     * 
     */
    public SampleMdaLauncher() {
        PropertyConfigurator.configure(ClassLoader
                .getSystemResource("org/argouml/resource/mdr_console.lcf"));
        LOG.info("Initializing GUI...");
        try {
            context = ContextFactory.getInstance().createContext(
                    LAUNCHER_PROPERTIES);
            context.setProperty("project.path",
                            "/Users/lmaitre/apps/andromda-bin-3.1-RC1/samples/"
                          + "car-rental-system/mda/src/uml/CarRentalSystem.xml.zip");
            SampleMdaLauncherActionManager manager = new SampleMdaLauncherActionManager(context);
            context.setActionManager(manager);
            manager.addAction("app:settings", manager.new SettingsAction(context, this));
            URL uiDef = this.getClass().getResource(UI_DESCRIPTOR);
            context.render(uiDef);
            AndroMDAModule mdaModule = new AndroMDAModule();
            ModuleInterface[] modules = new ModuleInterface[] {
                    mdaModule,
                    new DebugModule()
            };
            for (int i = 0; i < modules.length; i++)
                modules[i].enable();
            JMenu tools = (JMenu) context.find("app:tools");
            mdaModule.addMenuItems(tools);      
            Frame parentFrame = (Frame) context.getParentFrame();
            loadProperties();
            parentFrame.setVisible(true);
            parentFrame.pack();
            parentFrame.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Application is started.");
    }

    public String getName() {
        return "Sample MDA Launcher";
    }

    public void loadProperties() {
        File propFile = new File(LAUNCHER_PROPERTIES);
        if (propFile.exists()) {
            try {
                context.getProperties().load(new FileInputStream(propFile));
            } catch (Exception e) {
                e.printStackTrace();
                context.showError("Error loading properties from '"
                        + propFile.getAbsolutePath() + "'");
            }
        }
    }

    public void saveProperties() {
        File propFile = new File(LAUNCHER_PROPERTIES);
        try {
            context.getProperties().store(new FileOutputStream(propFile),
                    "# SampleMdaLauncher properties");
        } catch (Exception e) {
            e.printStackTrace();
            context.showError("Error storing properties to '"
                    + propFile.getAbsolutePath() + "'");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SampleMdaLauncher app = new SampleMdaLauncher();
    }

}
