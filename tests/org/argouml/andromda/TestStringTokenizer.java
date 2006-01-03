package org.argouml.andromda;

import java.util.StringTokenizer;

import junit.framework.TestCase;

public class TestStringTokenizer extends TestCase {

    private String argt = "mda -Dfilter=spring core deploy \"-Dauthor.name=Ludo is the best\" -verbose";
    
    public static void main(String[] args) {
    }

    private void out(String msg) {
        System.out.println(msg);
    }
    
    public void testTokenizer() {
        StringTokenizer st = new StringTokenizer(argt," ");
        String elem;
        while (st.hasMoreTokens()) {
            elem = st.nextToken();
            if (elem.startsWith("\"")) {
                elem = elem.substring(1,elem.length()) + st.nextToken("\"");
            }
            out(chop(elem));
        }
    }
    
    private String chop(String str) {
        while (str.startsWith(" "))
            str = str.substring(1,str.length());
        while (str.endsWith(" "))
            str = str.substring(0,str.length()-1);
        return str;
    }
}
