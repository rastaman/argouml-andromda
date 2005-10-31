/**
 * 
 */
package org.argouml.andromda;

import java.awt.Frame;
import java.io.File;
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

    private final static String MAVEN_HOME = "/Users/lmaitre/apache/maven-1.0.2";
    
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
                context.getProjectProperties().setProperty("project.path",MAVEN_HOME);
                actionManager = new SampleMdaLauncherActionManager( this );
                initSwingEngine();
                URL uiDef = ClassLoader.getSystemResource(UI_DESCRIPTOR);
                File f = new File(uiDef.getFile());
                parentFrame = (Frame) swingEngine.render(f);
                parentFrame.setVisible(true);
                parentFrame.pack();
                parentFrame.show(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOG.info("Application is started.");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SampleMdaLauncher app = new SampleMdaLauncher();
        
    }

}
