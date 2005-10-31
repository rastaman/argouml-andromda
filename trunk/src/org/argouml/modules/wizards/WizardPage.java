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
package org.argouml.modules.wizards;

import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.argouml.modules.container.ModuleContainer;

/**
 * This is a class which is use to distinguish screens in a wizard definitions file.
 * @author lmaitre
 */

public class WizardPage extends JPanel {

    private Map values = new HashMap();

    private ModuleContainer moduleContainer;

    /**
     * Return the values on the current page as a hashmap,
     * keyed by swixml components id's and valued with
     * the text of the components. 
     * @param pageCounter
     * @return
     * TODO: Implement and use from WizardDialog
     */
    public Map getValues() {
        Component[] comps = getComponents();
        Component o;
        String id;
        for (int i=0;i<comps.length;i++) {
            if (comps[i] instanceof JTextComponent) {
                //id = SwingEngine..getId(o);
                values.put(null/*id*/,((JTextComponent)null).getText());         
            }
        }
        Iterator k = null;//parent.getSwingEngine().getDescendants((JPanel) pageAt(pageCounter));
        while (k.hasNext()) {
            o = (Component)k.next();
            id = null;//getId(o);
            if (id !=null &&(o instanceof JTextComponent))              
                values.put(id,((JTextComponent)o).getText());
        }
        return values;
    }

    /**
     * Assign the values contained in a map to the specified swing components
     * used in the current page. 
     * @param pageCounter
     * @param values
     * TODO: Implement and use from WizardDialog
     */
    public void setValues(Map values) {
        Iterator k = null;//parent.getSwingEngine().getDescendants((JPanel) pageAt(pageCounter));
        Component o = null;
        String id = null;
        while (k.hasNext()) {
            o = (Component)k.next();
            id = null;//getId(o);
            if ((id!=null)&&(o instanceof JTextComponent)&&(values.containsKey(id))) {
                    ((JTextComponent)o).setText((String) values.get(id));
                    //LOG.info("Value for id '"+id+"' is '"+(String) values.get(id)+"'");
            }
        }   
    }

}
