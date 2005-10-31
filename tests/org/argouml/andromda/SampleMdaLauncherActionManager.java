/**
 * 
 */
package org.argouml.andromda;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.argouml.andromda.AndroMDAModuleActionManager;
import org.argouml.andromda.SettingsTabAndroMDA;
import org.argouml.application.api.PluggableSettingsTab;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.i18n.Translator;
import org.argouml.modules.actions.AbstractModuleAction;
import org.argouml.modules.container.ModuleContainer;
import org.argouml.ui.ArgoDialog;

/**
 * @author lmaitre
 *
 */
public class SampleMdaLauncherActionManager extends AndroMDAModuleActionManager {

    private static Logger LOG = Logger.getLogger(SampleMdaLauncherActionManager.class);
    
    /**
     * @param p
     */
    public SampleMdaLauncherActionManager(ModuleContainer p) {
        super(p);
        addAction("quitAction",new QuitAction(p));        
        addAction("openAction",new OpenAction(p));        
        addAction("settingsAction",new SettingsAction(p));        
    }

    class QuitAction extends AbstractModuleAction {

        public QuitAction(ModuleContainer p) { super(p); }
        
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    class OpenAction extends AbstractModuleAction {
        
        public OpenAction(ModuleContainer p) { super(p); }
        
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JFileChooser chooser =  new JFileChooser();
            chooser.setDialogTitle("Select project file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int retval = chooser.showOpenDialog(parent.getParentFrame());
            if (retval == JFileChooser.APPROVE_OPTION) {
                File project = chooser.getSelectedFile();
                try {
                    LOG.info("Project path set to "+project.getCanonicalPath());
                    parent.getContext().getProjectProperties().setProperty("project.path",project.getCanonicalPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
    };

    class SettingsAction extends AbstractModuleAction {
       
        private ArgoDialog dialog;
        
        private JTabbedPane tabs;
        
        public SettingsAction(ModuleContainer p) { super(p); }
        
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (dialog == null) {
                    try {
                        dialog =
                            new ArgoDialog(parent.getParentFrame(), Translator.localize("dialog.settings"),
                                ArgoDialog.OK_CANCEL_OPTION, true) {

                            public void actionPerformed(ActionEvent ev) {
                                super.actionPerformed(ev);
                                if (ev.getSource() == getOkButton()) {
                                    handleSave();
                                } else if (ev.getSource() == getCancelButton()) {
                                    handleCancel();
                                }
                            }
                        };

                        tabs = new JTabbedPane();

                        JButton applyButton = new JButton(Translator.localize("button.apply"));
                        String mnemonic = Translator.localize("button.apply.mnemonic");
                        if (mnemonic != null && mnemonic.length() > 0) {
                            applyButton.setMnemonic(mnemonic.charAt(0));
                        }
                        applyButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                handleSave();
                            }
                        });
                        dialog.addButton(applyButton);

                        ArrayList list = new ArrayList();
                        list.add(new SettingsTabAndroMDA());
                        ListIterator iterator = list.listIterator();
                        while (iterator.hasNext()) {
                            Object o = iterator.next();
                            SettingsTabPanel stp =
                                ((PluggableSettingsTab) o).getSettingsTabPanel();

                            tabs.addTab(
                                    Translator.localize(stp.getTabKey()),
                                    stp.getTabPanel());
                            LOG.info("Added '"+Translator.localize(stp.getTabKey())+"' with contents "+stp.getTabPanel());
                        }

                        // Increase width to accommodate all tabs on one row.
                        // (temporary solution until tabs are replaced with tree)
                        final int minimumWidth = 480;
                        tabs.setPreferredSize(
                                new Dimension(Math.max(tabs.getPreferredSize().width,
                                                   minimumWidth),
                                              tabs.getPreferredSize().height));

                        tabs.setTabPlacement(SwingConstants.LEFT);
                        dialog.setContent(tabs);
                    } catch (Exception exception) {
                        LOG.error("got an Exception in ActionSettings", exception);
                    }
                }

                handleRefresh();
                dialog.toFront();
                dialog.setVisible(true);            
        };
        
        /**
         * @see org.argouml.application.events.ArgoModuleEventListener#moduleDisabled(org.argouml.application.events.ArgoModuleEvent)
         */
        public void moduleDisabled(ArgoModuleEvent event) {
        }

        /**
         * Called when the user has pressed Save. Performs "Save" in all Tabs.
         */
        private void handleSave() {
            for (int i = 0; i < tabs.getComponentCount(); i++) {
                Object o = tabs.getComponent(i);
                if (o instanceof SettingsTabPanel) {
                    ((SettingsTabPanel) o).handleSettingsTabSave();
                }
            }
        }

        /**
         * Called when the user has pressed Cancel. Performs "Cancel" in all Tabs.
         */
        private void handleCancel() {
            for (int i = 0; i < tabs.getComponentCount(); i++) {
                Object o = tabs.getComponent(i);
                if (o instanceof SettingsTabPanel) {
                    ((SettingsTabPanel) o).handleSettingsTabCancel();
                }
            }
        }

        /**
         * Called when the user has pressed Refresh. Performs "Refresh" in all Tabs.
         */
        private void handleRefresh() {
            for (int i = 0; i < tabs.getComponentCount(); i++) {
                Object o = tabs.getComponent(i);
                if (o instanceof SettingsTabPanel) {
                    ((SettingsTabPanel) o).handleSettingsTabRefresh();
                }
            }
        }        
    };
}
