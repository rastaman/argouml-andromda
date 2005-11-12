// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
package org.argouml.andromda;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.argouml.modules.context.ModuleContext;
import org.argouml.modules.exec.MavenLauncher;
import org.argouml.modules.exec.TextAreaOutputStream;
import org.argouml.modules.wizards.WizardDialog;
import org.argouml.modules.wizards.WizardEvent;
import org.argouml.modules.wizards.WizardListener;
import org.argouml.ui.ArgoDialog;
import org.argouml.uml.ui.UMLAction;

/**
 * This action create a new project.
 * @author lmaitre
 *
 */
public class ActionCreateProjectAndroMDA  extends UMLAction 
    implements WizardListener {
   
    public final static String WIZARD_DESCRIPTOR = "new-project-wizard.xml";

    public final static String WIZARD_TITLE_KEY = "project.title";

    public final static String WIZARD_ID = "andromda:wizards:new-project";

    private Logger LOG = Logger.getLogger(ActionCreateProjectAndroMDA.class);

    private ModuleContext parent;
    
    private JTextArea mavenOutput;
    
    private JTextArea mavenError;
    
    private JPanel mavenPanel;

    private ArgoDialog dialog = null;

    private String mavenHome;
    
    private Frame parentFrame;
        
    private WizardDialog wizard;
    
    /**
     * This is creatable from the module loader.
     */
    public ActionCreateProjectAndroMDA(ModuleContext module) {
        super("AndroMDA Create Project Module", false);
        parent = module;
        URL wizardDescriptor = this.getClass().getResource(WIZARD_DESCRIPTOR);
        wizard = new WizardDialog(parent,
                WIZARD_TITLE_KEY , WIZARD_ID, wizardDescriptor);
        wizard.addListener(this);
        LOG.debug("Wizard is initialized");            
    }
    
    /**
     * @see org.argouml.modules.wizards.WizardListener#finishEvent(org.argouml.modules.wizards.WizardEvent)
     */
    public void finishEvent(WizardEvent evt) {
        LOG.debug("Finished completing wizard!");
        WizardDialog w = evt.getWizard();
        Map properties = w.getAllValues();
        Iterator it = properties.keySet().iterator();
        while (it.hasNext()) {
            String propertyName = (String)it.next();
            String value = (String) properties.get(propertyName);
            LOG.debug(propertyName+"="+value);
        }
        String projectPath=(String)properties.remove("parentFolder");
        w.setVisible(false);
        w.toBack();
        MavenLauncher launcher = new MavenLauncher();
        launcher.addGoal("andromdapp:generate");
        launcher.setProperties(properties);
        if (projectPath==null) {
            parent.showError("error.project.not.exist");
            return;
        }
        if (mavenHome == null)
            mavenHome = parent.getProperty(SettingsTabAndroMDA.KEY_MAVEN_HOME);
        if (mavenHome == null) {
            parent.showError("error.maven.not.set");
            return;
        }
        
        if (!new File(mavenHome).exists()) {
            parent.showError("error.maven.not.exist");            
        }
        
        if (parentFrame == null)
            parentFrame = parent.getParentFrame();
        if (dialog==null)
            buildDialog(parentFrame);
        if (mavenHome!=null &&(parentFrame != null)) {
            //LOG.info("Assume that the model is in $PROJECT/mda/src/uml/");
            try {
                String projectRoot = projectPath; 
                launcher.setMavenHome(mavenHome);
                launcher.setProjectRoot(projectRoot);                
                launcher.setStdOut(new TextAreaOutputStream(mavenOutput));          
                launcher.setStdErr(new TextAreaOutputStream(mavenError));                
                //GUI
                dialog.pack();
                dialog.toFront();
                dialog.setVisible(true);
                launcher.start();
            } catch (Exception e) {
                parent.showError("error.maven.runtime",e.getMessage());
            }
        } else {
            parent.showError("error.maven.runtime","Project is "+projectPath+", Maven home is "+mavenHome);
        }
    }

    private void buildDialog(Frame owner) {
        dialog = new ArgoDialog(owner, parent.localize("maven.output"), false);
        mavenPanel = (JPanel) parent.find("andromda:maven");
        mavenOutput = (JTextArea) parent.find("andromda:mavenoutput");
        mavenError = (JTextArea) parent.find("andromda:mavenerror");
        dialog.setContent(mavenPanel);  
    }
    
    /**
     * @see org.argouml.modules.wizards.WizardListener#nextEvent(org.argouml.modules.wizards.WizardEvent)
     */
    public void nextEvent(WizardEvent evt) {
        // NI
    }

    /**
     * @see org.argouml.modules.wizards.WizardListener#previousEvent(org.argouml.modules.wizards.WizardEvent)
     */
    public void previousEvent(WizardEvent evt) {
        // NI
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        wizard.centerOnParent();
        wizard.reset();
        wizard.pack();
        wizard.toFront();
        wizard.setVisible(true);        
    }

}
