package org.argouml.modules.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JTextField;

import org.argouml.modules.context.ModuleContext;
import org.argouml.modules.gui.SwixMLUtils;

public class DialogAction extends AbstractModuleAction {

    private JDialog dialog;

    private URL dialogDescriptor;
    
    private String name;
    
    public DialogAction(ModuleContext p, String name, URL dialogDescriptor) {
        super(p, name);
        this.name = name;
        this.dialogDescriptor = dialogDescriptor;
    }

    public void actionPerformed(ActionEvent e) {        
        if (dialog==null) {
            try {
                dialog = new JDialog(parent.getParentFrame());
                parent.getSwingEngine().insert(dialogDescriptor, dialog);
                registerStandardActions();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource()==parent.find("dialog:close"))
            close();
        else if (e.getSource()==parent.find("dialog:apply"))
            apply();
        else {
            SwixMLUtils.centerOnParent(dialog);
            dialog.toFront();
            dialog.setVisible(true);            
        }
    }

    private void registerStandardActions() {
        parent.getActionManager().addAction("dialog:action:close",this);
        parent.getActionManager().addAction("dialog:action:apply",this);
    }
    
    public void close() {
        dialog.hide();
    }
    
    public void apply() {
        Iterator it = parent.getSwingEngine().getDescendants(dialog);
        Component comp;
        String id;
        while (it.hasNext()) {
            comp = (Component) it.next();
            id = parent.getComponentId(comp);
            if (id!=null) {
                if (comp instanceof JTextField) {
                    parent.setAttribute(id,((JTextField)comp).getText());         
                }
                //TODO: Handle other types of components
            }
        }
    }

}
