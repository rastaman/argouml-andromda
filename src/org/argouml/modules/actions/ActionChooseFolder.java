package org.argouml.modules.actions;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.text.JTextComponent;

import org.argouml.modules.container.ModuleContainer;

public class ActionChooseFolder extends AbstractModuleAction {
    
    private String targetId;
    
    private String label;
    
    public ActionChooseFolder(ModuleContainer p, String target, String label) { 
        super(p); 
        targetId = target;
        this.label = label;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        JFileChooser chooser =  new JFileChooser();
        chooser.setDialogTitle(parent.getSwingEngine().getLocalizer().getString(label));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = chooser.showOpenDialog(parent.getParentFrame());
        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            try {
                JTextComponent target = (JTextComponent) parent.getSwingEngine().
                    find(targetId);
                target.setText(theFile.getCanonicalPath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
    };
};
