package org.argouml.modules.gui;

import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;

import org.argouml.modules.context.ContextFactory;
import org.argouml.modules.context.ModuleContext;
import org.swixml.SwingEngine;

/**
 * Combobox Model for swixml (+patch) enabled argouml modules.
 */
public class ComboModel extends DefaultComboBoxModel {

  public static SwingEngine engine;
  
  private ItemList model;
  
  /**
   * Constructs a DefaultComboBoxModel object.
   */
  public ComboModel() {
    super();
  }
  
  /**
   * Constructor. Take a string which must be the id of a previously registered
   * itemList in the swixml descriptor.
   * @param arg The id of the itemList from which retrieve the name of the elements
   */
  public ComboModel(String arg) {
      super();
      if (engine == null) {
          ModuleContext context = ContextFactory.getInstance().getContext();
          engine = context.getSwingEngine();
      }
      model = (ItemList) engine.getIdMap().get(arg);
      Iterator it = model.iterator();
      Item i;
      while (it.hasNext()) {
          i = (Item) it.next();
          if (getSelectedItem()==null)
              setSelectedItem(i.getName());
          addElement(i.getName());
      }
  }

}

