/**
 * 
 */
package org.argouml.modules.andromda.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.swixml.SwingEngine;

/**
 * @author lmaitre
 *
 */
public class SampleMdaLauncher implements SwixMLContainer {

    private final static String UI_DESCRIPTOR = "org/argouml/modules/andromda/ui/descriptor.xml";

    private final static String MAVEN_HOME = "/Users/lmaitre/apache/maven-1.0.2";
    
    private Logger LOG = Logger.getLogger(SampleMdaLauncher.class);
    
    private SwingEngine swix;

    private Frame root;
    
    private File project;
    
    public Action quitAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    public Action openAction = new AbstractAction() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JFileChooser chooser =  new JFileChooser();
            chooser.setDialogTitle("Select project file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int retval = chooser.showOpenDialog(root);
            if (retval == JFileChooser.APPROVE_OPTION) {
                project = chooser.getSelectedFile();
                try {
                    launchMavenAction.setProjectPath(project.getCanonicalPath());
                    LOG.info("Project path set to "+project.getCanonicalPath());
                    
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
    };
    
    public ActionLaunchAndroMDA launchMavenAction = new ActionLaunchAndroMDA(this);

    public ActionCreateProjectAndroMDA createProjectAction = new ActionCreateProjectAndroMDA(this);
    
    /**
     * 
     */
    public SampleMdaLauncher() {
        PropertyConfigurator.configure(ClassLoader.getSystemResource("org/argouml/resource/info_console.lcf"));
            LOG.info("Initializing GUI...");
            try {
                //
                swix = new SwingEngine(this);
                //swix.getTaglib().registerTag("filetree", FileTree.class);
                URL uiDef = ClassLoader.getSystemResource(UI_DESCRIPTOR);
                File f = new File(uiDef.getFile());
                swix.render(f);
                root = (Frame) swix.getRootComponent();
                project = new File("/Users/lmaitre/apps/andromda-bin-3.1-RC1/samples/car-rental-system/mda/src/uml/CarRentalSystem.xml.zip");
                launchMavenAction.setParentFrame(root);
                launchMavenAction.setMavenHome(MAVEN_HOME);
                launchMavenAction.setProjectPath(project.getCanonicalPath());
                root.setVisible(true);
                root.pack();
                root.show();    
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

    /**
     * @return Returns the swix.
     */
    public SwingEngine getSwingEngine() {
        return swix;
    }

}
