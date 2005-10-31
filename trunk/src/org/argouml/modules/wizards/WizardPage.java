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
import javax.swing.JTextField;

import org.argouml.modules.container.ModuleContainer;

/**
 * This is a class which is use to distinguish screens in a wizard definitions file.
 * @author lmaitre
 */

public class WizardPage extends JPanel {

    private ModuleContainer moduleContainer;

    /**
     * Return the values on the current page as a hashmap,
     * keyed by swixml components id's and valued with
     * the text of the components. 
     * @return A Map with the content of the fields of the page
     * TODO: Implement and use from WizardDialog
     */
    public Map getValues() {
        Map values = new HashMap();
        Component[] comps = getComponents();
        Component o;
        String id;
        for (int i=0;i<comps.length;i++) {
            id = moduleContainer.getId(comps[i]);
            if (id!=null) {
                if (comps[i] instanceof JTextField) {
                    values.put(id,((JTextField)comps[i]).getText());         
                }
                //TODO: Handle other types of components
            }
        }
        return values;
    }

    /**
     * Assign the values contained in a map to the specified swing components
     * used in the current page. 
     * @param values
     */
    public void setValues(Map values) {
        Iterator k = moduleContainer.getSwingEngine().getDescendants(this);
        Component o = null;
        String id = null;
        while (k.hasNext()) {
            o = (Component)k.next();
            id = moduleContainer.getId(o);
            if ( id!=null && values.containsKey(id) ) {
                if (o instanceof JTextField) {
                    ((JTextField)o).setText((String) values.get(id));
                }
                //TODO: Do other types of components (checkboxes...)
            }
        }   
    }

    public void reset() {
        Iterator k = moduleContainer.getSwingEngine().getDescendants(this);
        Component o = null;
        while (k.hasNext()) {
            o = (Component)k.next();
            if (o instanceof JTextField) {
                    ((JTextField)o).setText("");
            }
            //TODO: Do other types of components (checkboxes...)
        }      
    }

    /**
     * @return Returns the moduleContainer.
     */
    public ModuleContainer getModuleContainer() {
        return moduleContainer;
    }

    /**
     * @param moduleContainer The moduleContainer to set.
     */
    public void setModuleContainer(ModuleContainer moduleContainer) {
        this.moduleContainer = moduleContainer;
    }

}
