package org.argouml.modules.gui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.argouml.modules.context.ModuleContext;
import org.swixml.SwingEngine;

public class SwixMLUtils {

    private ModuleContext context;
    
    public SwixMLUtils(ModuleContext ctxt) {
        super();
        context = ctxt;
    }

    public Map getAllElements(String namespace) {
        SwingEngine se = context.getSwingEngine();
        Iterator it = se.getIdMap().keySet().iterator();
        HashMap results = new HashMap();
        String id;
        while (it.hasNext()) {
            id = (String) it.next();
            if (id.startsWith(namespace)) {
                results.put(id,se.find(id));
            }
        }
        return results;
    }

    /**
     * Moves the dialog to be centered on its parent's location on the screen.
     **/
    public static void centerOnParent(Dialog d) {
        Dimension size = d.getSize();
        Dimension p = d.getParent().getSize();
        int x = (d.getParent().getX() - size.width)
        + (int) ((size.width + p.width) / 2d);
        int y = (d.getParent().getY() - size.height)
        + (int) ((size.height + p.height) / 2d);
        d.setLocation(x, y);
    }

}
