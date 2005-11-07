package org.argouml.modules.actions;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.argouml.modules.context.ModuleContext;

public class ActionChooseFolder extends AbstractModuleAction {
    
    private String targetId;

    private String label;
    
    private JFileChooser chooser;
    
    public ActionChooseFolder(ModuleContext p, String target, String lbl) { 
        super(p); 
        targetId = target;
        label = lbl;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (chooser==null) {
            chooser =  new JFileChooser();
            chooser.setDialogTitle(parent.localize(label));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);            
        }
        int retval = chooser.showOpenDialog(parent.getParentFrame());
        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            try {
                JTextField target = (JTextField) parent.find(targetId);
                target.setText(theFile.getCanonicalPath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
    };
};
