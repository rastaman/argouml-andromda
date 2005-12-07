/**
 * 
 */
package org.argouml.modules.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JRadioButton;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.argouml.modules.context.ModuleContext;

/**
 * @author lmaitre
 *
 */
public class RadioBoxTextAreaUpdaterListener implements MouseListener {

    private JTextComponent target;
    
    private ModuleContext context;

    private String prefix = "";
    
    private Map translations = new HashMap();
    
    /**
     * 
     */
    public RadioBoxTextAreaUpdaterListener(ModuleContext ctxt, String targetid) {
        super();
        context = ctxt;
        target = (JTextComponent) context.find(targetid);
    }

    public void register(JRadioButton button) {
        String text = context.localize(prefix + button.getText());
        //Logger.getRootLogger().info("Put '"+text+"' for "+button.getText());
        translations.put(button.getText(),text);
        button.addMouseListener(this);
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        if (target!=null && e.getSource() instanceof JRadioButton) {
            String key = ((JRadioButton)e.getSource()).getText();
            //Logger.getRootLogger().info("Localizing "+key+", "+context.getSwingEngine().getLocalizer().isUsable());
            target.setText((String)translations.get(key));
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @return Returns the prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix The prefix to set.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
