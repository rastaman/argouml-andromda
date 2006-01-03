package org.argouml.andromda;

import javax.swing.JFrame;

import junit.framework.TestCase;

import org.argouml.modules.gui.ComboModel;
import org.argouml.modules.gui.Item;
import org.argouml.modules.gui.ItemList;
import org.swixml.SwingEngine;

public class ItemTest extends TestCase {

    public static void main(String[] args) {
    }

    public void testItem() {
        SwingEngine engine = new SwingEngine();
        engine.getTaglib().registerTag("itemList",ItemList.class);
        engine.getTaglib().registerTag("item",Item.class);
        ComboModel.engine = engine;
        try {
            JFrame parentFrame = (JFrame) engine.render(this.getClass().getResource("item.xml"));
            engine.test();          
            while(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail("Error: "+e.getMessage());
        }
    }
}
