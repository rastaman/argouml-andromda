package org.argouml.andromda;

import java.io.File;

/**
 * Contains methods for validation.
 * @author lmaitre
 */
public class ValidatorAndroMDA {

	/**
	 * Return true if the filename exist and is a file
	 * @param filename
	 * @return
	 */
    public static boolean validateFile(String filename) {
        if (filename != null) {
            File tmp = new File(filename);
            return tmp.exists() && tmp.isFile(); 
        }
        return false;
    }

    /**
     * Return true if the filename exist and is a folder
     * @param filename
     * @return
     */
    public static boolean validateFolder(String filename) {
        if (filename != null) {
            File tmp = new File(filename);
            return tmp.exists() && tmp.isDirectory(); 
        }
        return false;
    }
    
    /**
     * Return true if the string is null or empty
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str==null || "".equals(str);
    }
}
