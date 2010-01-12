/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    rastaman
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.andromda;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.modules.context.ContextFactory;
import org.argouml.modules.context.ModuleContext;


/**
 * Settings tab for the AndroMDA code generator.
 */
public class SettingsTabAndroMDA extends SettingsTabHelper implements SettingsTabPanel, DocumentListener {
    
    private static final Logger LOG = Logger.getLogger(SettingsTabAndroMDA.class);

    /**
     * Key for maven home.
     */
    public static final String KEY_MAVEN_HOME = "maven.home";

    /**
     * Key for andromda home.
     */
    public static final String KEY_ANDROMDA_HOME = "andromda.home";

    /**
     * Key for profile
     */
    public static final String KEY_PROFILE = "defaultModel";

    private JTextField mavenHome;

    private JTextField andromdaHome;

    private JCheckBox useAndromdaProfile;
    
    private JPanel settingsTab;
    
    private ModuleContext parent;
    
    /**
     * Creates the widgets but do not initialize them with the values
     * (this is handleSettingsTabRefresh() duty).
     */
    public SettingsTabAndroMDA() {
        super();
        parent = ContextFactory.getInstance().getContext();
        setLayout(new BorderLayout());
        LOG.info("SettingsTabAndroMDA being created...");
        settingsTab = (JPanel) parent.find("andromda:settings");
        add(settingsTab , BorderLayout.NORTH);
        mavenHome = (JTextField) parent.find("andromda:mavenhome");
        andromdaHome = (JTextField) parent.find("andromda:home");
        useAndromdaProfile = (JCheckBox) parent.find("andromda:useprofile");
        andromdaHome.getDocument().addDocumentListener(this);
        handleSettingsTabRefresh();
        LOG.debug("SettingsTabAndroMDA created!");
    }

    private String getAndroMdaProfileFolder() {
        return andromdaHome.getText() + File.separator + "andromda" 
        + File.separator + "xml.zips";
    }
    
    private void refreshModulesPath() {
        String andromdaRepo = getAndroMdaProfileFolder();
        //TODO: Use tokenizer        
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

    private boolean isEmptyOrNull(String s) {
        return s==null || "".equals(s);
    }
    
    /**
     * Load or reload field settings.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh
     */
    public void handleSettingsTabRefresh() {
        if (isEmptyOrNull(mavenHome.getText())) {
            if (!isEmptyOrNull(parent.getProperty(KEY_MAVEN_HOME)))
                mavenHome.setText(
                        parent.getProperty(KEY_MAVEN_HOME));
        } else
            parent.setProperty(KEY_MAVEN_HOME, mavenHome.getText());            
            
        if (isEmptyOrNull(andromdaHome.getText())) {
            if (!isEmptyOrNull(parent.getProperty(KEY_ANDROMDA_HOME)))
                andromdaHome.setText(
                        parent.getProperty(KEY_ANDROMDA_HOME));
        } else
            parent.setProperty(KEY_ANDROMDA_HOME, andromdaHome.getText());
            
        refreshCheckBox();
        refreshModulesPath();
    }

    private void refreshCheckBox() {
        //enable/disable default profile checkbox
        if (isEmptyOrNull(andromdaHome.getText())) {
            useAndromdaProfile.setEnabled(false);
            useAndromdaProfile.setSelected(false);
        } else {
            useAndromdaProfile.setEnabled(true);
            String profile = getAndroMdaProfileFolder() + File.separator + 
                findProfile(getAndroMdaProfileFolder(),"andromda-profile");
            if (profile.equals(parent.getProperty(KEY_PROFILE))) {
                useAndromdaProfile.setSelected(true);
            } else {
                useAndromdaProfile.setSelected(false);
            }
        }        
    }
    
    /**
     * Save any fields changed.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave
     */
    public void handleSettingsTabSave() {
        if (!isEmptyOrNull(mavenHome.getText())) {
            parent.setProperty(KEY_MAVEN_HOME, mavenHome.getText());            
        }
        if (!isEmptyOrNull(andromdaHome.getText())) {
            parent.setProperty(KEY_ANDROMDA_HOME, andromdaHome.getText());
            
            String profile = getAndroMdaProfileFolder() + File.separator + 
                findProfile(getAndroMdaProfileFolder(),"andromda-profile");
                        
            if (useAndromdaProfile.isSelected()) {
                File profileFile = new File(profile);
                if (profileFile.exists()) {
                    parent.setProperty(KEY_PROFILE, profile);                    
                } else {
                    parent.showError("error.profile.not.exist",profile);
                }
            } else {
                if (profile.equals(parent.getProperty(KEY_PROFILE))) {
                    parent.removeProperty(KEY_PROFILE);
                }
            }            
        }
        refreshModulesPath();
    }

    public static String findProfile(String folder, String prefix) {
        File f = new File(folder);
        FilenameFilter ff = new ProfileFileFilter(prefix, new String[] {"xml.zip"});
        String[] names = f.list(ff);
        if (names!=null) {
            return names[0];
        }
        return null;
    }
    
    public static class ProfileFileFilter implements FilenameFilter {
        
        private String prefix;
        
        private String[] suffixes;
        
        public ProfileFileFilter(String prefix, String[] suffixs) {
            this.prefix = prefix;
            this.suffixes = suffixs;
        }
        
        public boolean accept(File f, String s) {
            if (s.startsWith(prefix)) {
                for (int i=0;i<suffixes.length;i++)
                    if (s.endsWith(suffixes[i]))
                        return true;
            }
            return false;
        }
        
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
        return getClass().getName(); 
    }

    /**
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
        LOG.info("Receiving "+e);
        refreshCheckBox();
    }

    /**
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
        LOG.info("Receiving "+e);
        refreshCheckBox();
    }

    /**
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        LOG.info("Receiving "+e);
        refreshCheckBox();
    }

}
