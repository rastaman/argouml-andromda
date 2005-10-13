/**
 * 
 */
package org.argouml.modules.andromda.ui.wizards;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.argouml.modules.andromda.ui.ActionLaunchAndroMDA;
import org.argouml.modules.andromda.ui.SwixMLContainer;
import org.swixml.SwingEngine;

/**
 * @author lmaitre
 *
 */
public class SampleWizardTester implements SwixMLContainer {

    private final static String WIZARD_DESCRIPTOR = "org/argouml/modules/andromda/ui/new-project-wizard.xml";

    private final static String MAVEN_HOME = "/Users/lmaitre/apache/maven-1.0.2";
    
    private Logger LOG = Logger.getLogger(SampleWizardTester.class);
    
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
    
    /**
     * 
     */
    public SampleWizardTester() {
        PropertyConfigurator.configure(ClassLoader.getSystemResource("org/argouml/resource/info_console.lcf"));
            LOG.info("Initializing GUI...");
            try {
                root = new WizardFrame(WIZARD_DESCRIPTOR);
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
        SampleWizardTester app = new SampleWizardTester();
        
    }

    /**
     * @return Returns the swix.
     */
    public SwingEngine getSwingEngine() {
        return swix;
    }    
}
