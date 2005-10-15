// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.modules.andromda.ui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.application.api.PluggableSettingsTab;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.swixml.SwingEngine;


/**
 * Settings tab for the AndroMDA code generator.
 */
public class SettingsTabAndroMDA extends SettingsTabHelper implements SettingsTabPanel, PluggableSettingsTab
{
    private static final Logger LOG = Logger.getLogger(SettingsTabAndroMDA.class);

    /**
     * Key for maven home.
     */
    public static final ConfigurationKey KEY_MAVEN_HOME =
    Configuration.makeKey("andromda", "maven", "home");
    
    private JTextField mavenHome;

    /**
     * Key for andromda home.
     */
    public static final ConfigurationKey KEY_ANDROMDA_HOME =
    Configuration.makeKey("andromda", "home");

    private JTextField andromdaHome;

    private JPanel settingsTab;

    private SwingEngine swingEngine;
    
    public Action chooseMavenHomeFolder = new AbstractAction() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JFileChooser chooser =  new JFileChooser();
            chooser.setDialogTitle("Select Maven Home");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retval = chooser.showOpenDialog(settingsTab);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File theFile = chooser.getSelectedFile();
                try {
                    mavenHome.setText(theFile.getCanonicalPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
    };

    public Action chooseAndroMdaHomeFolder = new AbstractAction() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JFileChooser chooser =  new JFileChooser();
            chooser.setDialogTitle("Select AndroMDA Home");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retval = chooser.showOpenDialog(settingsTab);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File theFile = chooser.getSelectedFile();
                try {
                    andromdaHome.setText(theFile.getCanonicalPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
    };
    
    /**
     * Creates the widgets but do not initialize them with the values
     * (this is handleSettingsTabRefresh() duty).
     */
    public SettingsTabAndroMDA() {
        super();
        
        setLayout(new BorderLayout());
        swingEngine = new SwingEngine( this );
        URL uiDef = this.getClass().getResource("descriptor.xml");
        swingEngine.setClassLoader(this.getClass().getClassLoader());        
        LOG.info("SettingsTabAndroMDA being created...");
        try {
            swingEngine.render( uiDef );
        } catch (Exception e) {
            e.printStackTrace();
        }
        settingsTab = (JPanel) swingEngine.find("andromdaSettings");
        add(settingsTab , BorderLayout.NORTH);
        handleSettingsTabRefresh();
        LOG.debug("SettingsTabAndroMDA created!");
    }

    /**
     * Save any fields changed.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave
     */
    public void handleSettingsTabSave() {
        Configuration.setString(KEY_MAVEN_HOME, mavenHome.getText());
        Configuration.setString(KEY_ANDROMDA_HOME, andromdaHome.getText());
        //refresh the system property of the modules search paths of MDR
        refreshModulesPath();
    }

    private void refreshModulesPath() {
        LOG.info("Assume that the profiles are in $ANDROMDA_HOME/andromda/xml.zips");        
        String sep = System.getProperty("file.separator");
        String andromdaRepo = andromdaHome.getText() + sep + "andromda" + sep + "xml.zips";
        String path = System.getProperty("org.argouml.model.modules_search_path");
        if (path!=null) {
            boolean alreadyIn = false;
            String[] paths = path.split(",");
            for (int i=0;i<paths.length;i++) {
                if (paths[i].equals(andromdaRepo))
                    alreadyIn=true;
            }
            if (!alreadyIn) {
                if (paths.length>0)
                    path += "," + andromdaRepo;
                else
                    path = andromdaRepo;
            }
        } else
            path = andromdaRepo;
        System.setProperty("org.argouml.model.modules_search_path",path);
        
    }
    /**
     * Cancel any changes.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabCancel
     */
    public void handleSettingsTabCancel() {
       //TODO: Do something useful here.
    }

    /**
     * Load or reload field settings.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh
     */
    public void handleSettingsTabRefresh() {
        String keyMavenHome = Configuration.getString(SettingsTabAndroMDA.KEY_MAVEN_HOME);
        if (keyMavenHome!=null)
            mavenHome.setText(keyMavenHome);
        String keyAndromdaHome = Configuration.getString(SettingsTabAndroMDA.KEY_ANDROMDA_HOME);
        if (keyAndromdaHome!=null)
            andromdaHome.setText(keyAndromdaHome);
        refreshModulesPath();
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey
     */
    public String getTabKey() { 
        return "AndroMDA"; 
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { 
        return "SettingsTabAndroMDA";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() { 
        return "AndroMDA Settings"; 
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { 
        return ArgoVersion.getVersion(); 
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { 
        return "Ludovic Maitre"; 
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { 
        return "andromda.module"; 
    }
    
}
