package org.argouml.andromda;

import java.net.URL;

import junit.framework.TestCase;

import org.swixml.SwingEngine;

public class TestSwixML extends TestCase {

    private static String descriptor = "desc.xml";
    
    public static void main(String[] args) {
        SwingEngine se = new SwingEngine();
        try {
            URL u = TestSwixML.class.getResource(descriptor);
            se.render(u);
            se.test();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
