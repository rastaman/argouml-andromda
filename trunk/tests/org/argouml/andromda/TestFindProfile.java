package org.argouml.andromda;

import java.io.File;
import java.io.FilenameFilter;

import junit.framework.TestCase;

public class TestFindProfile extends TestCase {

    private String mdaHome = "/Users/lmaitre/apps/andromda-bin-3.1-RC1/andromda/xml.zips";
    
    private String prefix = "andromda-profile";
    
    private String andromdaProfile = "andromda-profile-3.1-RC1.xml.zip";
    
    public void testFindProfile() {
        String profile = findProfile(mdaHome,prefix);
        assertNotNull(profile);
        assertTrue("Profile '"+profile+"' doesn't is '"+andromdaProfile+"'",
                profile.equals(andromdaProfile));
    }
    
    public String findProfile(String folder, String prefix) {
        File f = new File(folder);
        FilenameFilter ff = new ProfileFileFilter(prefix, new String[] {"xml.zip"});
        String[] names = f.list(ff);
        if (names!=null) {
            return names[0];
        }
        return null;
    }
    
    public class ProfileFileFilter implements FilenameFilter {
        
        private String prefix;
        
        private String[] suffixes;
        
        public ProfileFileFilter(String prefix, String[] suffixs) {
            this.prefix = prefix;
            this.suffixes = suffixs;
        }
        
        public boolean accept(File f, String s) {
            if (s.startsWith(prefix)) {
                for (int i=0;i<suffixes.length;i++)
                    if (s.endsWith(suffixes[i]))
                        return true;
            }
            return false;
        }
        
    }
}
