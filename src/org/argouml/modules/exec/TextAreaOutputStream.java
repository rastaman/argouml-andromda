/**
 * 
 */
package org.argouml.modules.exec;

import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JTextArea;

/**
 * @author lmaitre
 *
 */
public class TextAreaOutputStream extends OutputStream {
   
    private JTextArea area;

    private ArrayList byteList = new ArrayList ();
    
    /**
     * 
     */
    public TextAreaOutputStream(JTextArea target) {
        super();
        area = target;    
    }
    
    public void write (int b) {
        byteList.add (new Byte ((byte) b));
    }

    public void flush () {
        byte[] bytes = new byte[byteList.size ()];

        for (int i = 0; i < bytes.length; i ++) {
            bytes[i] = ((Byte) byteList.get (i)).byteValue ();
        }

        byteList.clear();
        area.append(new String(bytes));
    }
    
}
