
package org.argouml.modules.gui;

import java.awt.Color;

import javax.swing.UIManager;

import org.jdom.Attribute;
import org.swixml.Localizer;

/**
 * The ModuleColorConverter class add the color 'LABEL_BG' and 'LABEL_FG' which 
 * return the color of the JLabel. This is used for the filling the JTextAreas with 
 * an adequate color. 
 * This converter could also be used to apply specific colors holds in the Container.
 * (standalone app or ArgoUML for instance)
 */
public class ColorConverter extends org.swixml.converters.ColorConverter {
  
  /** converter's return type */
  public static final Class TEMPLATE = Color.class;

  /**
    * Returns a <code>java.awt.Color</code> runtime object
    * @param type <code>Class</code> not used
    * @param attr <code>Attribute</code> value needs to provide a String
    * @return runtime type is subclass of <code>java.awt.Color</code>
    */
  public Object convert( final Class type, final Attribute attr, Localizer localizer ) {
    return conv(type,attr);
  }
  /**
   * Returns a <code>java.awt.Color</code> runtime object
   * @param type <code>Class</code> not used
   * @param attr <code>Attribute</code> value needs to provide a String
   * @return runtime type is subclass of <code>java.awt.Color</code>
   */
  public static Object conv( final Class type, final Attribute attr ) {
      if ("LABEL_BG".equals(attr.getValue()))
          return (Color)UIManager.get("Label.background");
      if ("LABEL_FG".equals(attr.getValue()))
          return (Color)UIManager.get("Label.background");
      return org.swixml.converters.ColorConverter.conv(type,attr);
  }


  /**
   * A <code>Converters</code> conversTo method informs about the Class type the converter
   * is returning when its <code>convert</code> method is called
   * @return <code>Class</code> - the Class the converter is returning when its convert method is called
   */
  public Class convertsTo() {
    return TEMPLATE;
  }

}
