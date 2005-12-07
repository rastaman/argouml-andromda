package org.argouml.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CLUtils {

    public CLUtils() {
        super();
    }

    public static List getArguments(String argline) {
        StringTokenizer st = new StringTokenizer(argline," ");
        List args = new ArrayList();
        String elem;
        while (st.hasMoreTokens()) {
            elem = st.nextToken();
            if (elem.startsWith("\"")) {
                elem = elem.substring(1,elem.length()) + st.nextToken("\"");
            }
            args.add(chop(elem));
        }
        return args;
    }
    
    public static String chop(String str) {
        while (str.startsWith(" "))
            str = str.substring(1,str.length());
        while (str.endsWith(" "))
            str = str.substring(0,str.length()-1);
        return str;
    }
}
