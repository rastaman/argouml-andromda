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
            args.add(elem.trim());
        }
        return args;
    }

}
