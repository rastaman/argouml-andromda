/**
 * 
 */
package org.argouml.modules.exec;

import java.awt.Rectangle;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

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
        //See http://www.jguru.com/faq/view.jsp?EID=16674
        //http://java.sun.com/docs/books/tutorial/uiswing/components/textarea.html
        area.setCaretPosition(area.getDocument().getLength());
    }
    
}
