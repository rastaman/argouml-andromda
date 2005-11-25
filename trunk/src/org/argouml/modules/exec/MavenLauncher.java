package org.argouml.modules.exec;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * A class which launch Maven. Incidentally this is used here to launch AndroMDA.
 * @see <a href="http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html">When Runtime.exec() won't</a>
 */
public class MavenLauncher extends Thread {
    
    private String projectRoot;
    
    private String mavenHome;
    
    private OutputStream stdOut;
    
    private OutputStream stdErr;

    private List goals = new Vector();
    
    private Map properties = new HashMap();
    
    private String sep;
    
    private String ext;

    public MavenLauncher() {
        sep = System.getProperty("file.separator");
        ext = System.getProperty("os.name").indexOf("Windows") > -1 ? ".bat" : "";
    }

    public void run() {
        try
        {
            File mavenExec = new File(mavenHome + sep + "bin" + sep + "maven" + ext );
            File workDir = new File(projectRoot);
            PrintWriter out = new PrintWriter(stdOut);            
            out.println("[ARGOUML] Execute '"+mavenExec.getCanonicalPath()+"' in '"+workDir.getCanonicalPath());
            out.flush();
            Runtime rt = Runtime.getRuntime();
            Vector commands = new Vector();
            //ISSUE #3681
            String OS = System.getProperty("os.name").toLowerCase();
            
            if (OS.indexOf("windows 9") > -1) {
                commands.add("command.com");
                commands.add("/c");
            } else if ( (OS.indexOf("nt") > -1)
                    || (OS.indexOf("windows 2000") > -1 )
                   || (OS.indexOf("windows 2003") > -1 )
                   || (OS.indexOf("windows xp") > -1) ) {
                commands.add("cmd.exe");
                commands.add("/c");
            }
            commands.add(mavenExec.getCanonicalPath());
            Iterator i = properties.keySet().iterator();
            while (i.hasNext()) {
                String propertyName = (String) i.next();
                String value = (String) properties.get(propertyName);
                commands.add("-D"+propertyName+"="+value);
            }
            commands.addAll(goals);
            Process proc = rt.exec((String[])commands.toArray(new String[commands.size()]),
                    null,//env,
                    workDir);
            // any error message?
            StreamGobbler errorGobbler = new
                StreamGobbler(proc.getErrorStream(), stdErr);            
            // any output?
            StreamGobbler outputGobbler = new
                StreamGobbler(proc.getInputStream(), stdOut);
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
            // any error???
            int exitVal = proc.waitFor();
            out.println("[ARGOUML] AndroMDA/Maven run on model completed ["+exitVal+"]");
            out.flush();
        } catch (Throwable t)
          {
            t.printStackTrace();
          }        
    }
    
    /* (non-Javadoc)
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean addGoal(Object o) {
        return goals.add(o);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean removeGoal(Object o) {
        return goals.remove(o);
    }

    /**
     * @return Returns the mavenHome.
     */
    public String getMavenHome() {
        return mavenHome;
    }

    /**
     * @param mavenHome The mavenHome to set.
     */
    public void setMavenHome(String mavenhome) {
        mavenHome = mavenhome;
    }

    /**
     * @return Returns the projectRoot.
     */
    public String getProjectRoot() {
        return projectRoot;
    }

    /**
     * @param projectRoot The projectRoot to set.
     */
    public void setProjectRoot(String projectRoot) {
        this.projectRoot = projectRoot;
    }

    /**
     * @return Returns the stdErr.
     */
    public OutputStream getStdErr() {
        return stdErr;
    }

    /**
     * @param stdErr The stdErr to set.
     */
    public void setStdErr(OutputStream stdErr) {
        this.stdErr = stdErr;
    }

    /**
     * @return Returns the stdOut.
     */
    public OutputStream getStdOut() {
        return stdOut;
    }

    /**
     * @param stdOut The stdOut to set.
     */
    public void setStdOut(OutputStream stdOut) {
        this.stdOut = stdOut;
    }

    /**
     * @return Returns the properties.
     */
    public Map getProperties() {
        return properties;
    }

    /**
     * @param properties The properties to set.
     */
    public void setProperties(Map properties) {
        this.properties = properties;
    }
    
}
