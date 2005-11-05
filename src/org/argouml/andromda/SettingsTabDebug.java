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

package org.argouml.andromda;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.modules.container.AbstractModuleContainer;
import org.argouml.modules.container.ModuleContainer;


/**
 * Settings tab for the AndroMDA code generator.
 */
public class SettingsTabDebug extends SettingsTabHelper implements SettingsTabPanel {
    
    private static final Logger LOG = Logger.getLogger(SettingsTabDebug.class);

    private JCheckBox cbEventDebugMode;
    
    //private JCheckBox cbDebugMode;

    private JSlider rootLogger;
    
    private JSlider modelImplementationLogger;
    
    private JPanel settingsTab;
    
    private ModuleContainer parent;
    
    /**
     * Creates the widgets but do not initialize them with the values
     * (this is handleSettingsTabRefresh() duty).
     */
    public SettingsTabDebug() {
        super();
        parent = AbstractModuleContainer.getInstance();
        parent.getActionManager().addAction("eventDebugMode", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                refreshCheckBox();
            }  
        });
        setLayout(new BorderLayout());
        LOG.info("SettingsTabDebug being created...");
        settingsTab = (JPanel) parent.getSwingEngine().find("debugSettings");
        add(settingsTab , BorderLayout.NORTH);
        //cbDebugMode = (JCheckBox) parent.getSwingEngine().find("debugMode");
        cbEventDebugMode = (JCheckBox) parent.getSwingEngine().find("cbEventDebugMode");
        rootLogger = (JSlider) parent.getSwingEngine().find("rootLogger");
        rootLogger.setLabelTable(getDebugLevels());
        modelImplementationLogger = (JSlider) parent.getSwingEngine().find("modelImplementationLogger");
        modelImplementationLogger.setLabelTable(getDebugLevels());
        handleSettingsTabRefresh();
        LOG.debug("SettingsTabDebug created!");
    }

    private Dictionary getDebugLevels() {
        Hashtable t = new Hashtable();
        t.put( new Integer( 0 ), new JLabel("Debug") );
        t.put( new Integer( 1 ), new JLabel("Info") );
        t.put( new Integer( 2 ), new JLabel("Warn") );
        t.put( new Integer( 3 ), new JLabel("Error") );
        t.put( new Integer( 4 ), new JLabel("Fatal") );
        t.put( new Integer( 5 ), new JLabel("Off") );
        return t;
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
        refreshCheckBox();
    }

    private void refreshCheckBox() {
        Logger eventPumpLogger = Logger.
            getLogger("org.argouml.model.mdr.ModelEventPumpMDRImpl");
        if (cbEventDebugMode.isSelected()) {
            eventPumpLogger.setLevel(Level.DEBUG);
            eventPumpLogger.debug("Debug mode activated for "+eventPumpLogger.getName());
        } else if (eventPumpLogger.isDebugEnabled()){
            eventPumpLogger.setLevel(Level.INFO);          
            eventPumpLogger.info("Info mode activated for "+eventPumpLogger.getName());
        }
        //eventPumpLogger.
    }
    
    /**
     * Save any fields changed.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave
     */
    public void handleSettingsTabSave() {
        //
    }
    
    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey
     */
    public String getTabKey() { 
        return "Debug"; 
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { 
        return "SettingsTabDebug";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() { 
        return "Debug Settings"; 
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

}
