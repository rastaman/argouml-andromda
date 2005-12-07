package org.argouml.andromda;

import java.io.File;

public class ValidatorAndroMDA {

    public ValidatorAndroMDA() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static boolean validateFile(String filename) {
        if (filename != null) {
            File tmp = new File(filename);
            return tmp.exists() && tmp.isFile(); 
        }
        return false;
    }

    public static boolean validateFolder(String filename) {
        if (filename != null) {
            File tmp = new File(filename);
            return tmp.exists() && tmp.isDirectory(); 
        }
        return false;
    }
    
    public static boolean isNullOrEmpty(String str) {
        return str!=null && !"".equals(str);
    }
}
