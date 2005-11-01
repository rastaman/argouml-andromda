/**
 * 
 */
package org.argouml.andromda;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.argouml.modules.container.AbstractModuleContainer;
import org.argouml.modules.container.ModuleContainer;
import org.argouml.modules.context.StandaloneContext;

/**
 * @author lmaitre
 *
 */
public class SampleMdaLauncher extends AbstractModuleContainer implements ModuleContainer {

    private final static String UI_DESCRIPTOR = "org/argouml/andromda/descriptor.xml";

    private final static String LAUNCHER_PROPERTIES = "samplemdalauncher.properties";
    
    private Logger LOG = Logger.getLogger(SampleMdaLauncher.class);
    
    /**
     * 
     */
    public SampleMdaLauncher() {
        if (instance==null)
            instance=this;
        PropertyConfigurator.configure(ClassLoader.getSystemResource("org/argouml/resource/mdr_console.lcf"));
            LOG.info("Initializing GUI...");
            try {
                //
                context = new StandaloneContext();
                context.getProjectProperties().setProperty("project.path",
                        "/Users/lmaitre/apps/andromda-bin-3.1-RC1/samples/"
                        + "car-rental-system/mda/src/uml/CarRentalSystem.xml.zip");
                actionManager = new SampleMdaLauncherActionManager( this );
                initSwingEngine();
                URL uiDef = ClassLoader.getSystemResource(UI_DESCRIPTOR);
                File f = new File(uiDef.getFile());
                parentFrame = (Frame) swingEngine.render(f);
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
                context.getProjectProperties().load(new FileInputStream(propFile));
            } catch (Exception e) {
                e.printStackTrace();
                showFeedback("Error loading properties from '"+propFile.getAbsolutePath()+"'");
            }
        }
    }
    
    public void saveProperties() {
        File propFile = new File(LAUNCHER_PROPERTIES);
        try {
            context.getProjectProperties().store(new FileOutputStream(propFile),"# SampleMdaLauncher properties");
        } catch (Exception e) {
            e.printStackTrace();
            showFeedback("Error storing properties to '"+propFile.getAbsolutePath()+"'");
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SampleMdaLauncher app = new SampleMdaLauncher();
        
    }

}
