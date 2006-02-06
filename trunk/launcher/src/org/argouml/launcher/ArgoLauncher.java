package org.argouml.launcher;

import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.CommandLineImpl;
import org.apache.commons.exec.Execute;
import org.argouml.launcher.utils.JavaEnvUtils;

public class ArgoLauncher {

    private static String DEFAULT_PROFILE="/org/argouml/model/mdr/mof/default-uml14.xmi";
    
    private static String INFO_CONSOLE="org/argouml/resource/info_console.lcf";
    
    public ArgoLauncher() {
        super();
        
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (checkForProfile(args)) {
            askForProfile();
        }
        CommandLine cl = new CommandLineImpl();
        cl.setExecutable(JavaEnvUtils.getJreExecutable("java"));
        cl.addArgument("-Dlog4j.configuration="+INFO_CONSOLE);
        cl.addArgument("-Dargo.defaultModel="+System.getProperty("argo.defaultModel"));
        cl.addArgument("-jar");
        cl.addArgument("argouml.jar");
        cl.addArguments(args);
        Execute exe = new Execute();
        exe.setWorkingDirectory(getWorkingDir());
        exe.setNewEnvironment(false);
        exe.setCommandline(cl);
        try {
            exe.spawn();
        } catch (Throwable e) {
            System.err.println("Cannot launch class: "+e.getMessage());
            e.printStackTrace();
            System.err.flush();
            System.exit(10);
        } 
        System.exit(0);
    }

    private static File getWorkingDir() {
        URL loc = ArgoLauncher.class.getResource("/org/argouml/launcher/ArgoLauncher.class");
        String jar = loc.getPath().substring(loc.getProtocol().length()+2,
                loc.getPath().indexOf("!"));
        return new File(jar).getParentFile();
    }
    
    private static void askForProfile() {
        JFrame parent = new JFrame();
        JFileChooser chooser =  new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retval = chooser.showOpenDialog(parent);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            System.setProperty("argo.defaultModel",theFile.getAbsolutePath());
        }
        parent.dispose();
    }
    
    private static void out(String s) {
        System.out.println(s);
    }

    private static void err(String s) {
        System.err.println(s);
    }

    private static boolean checkForProfile(String[] args) {
        boolean check = true;
        //ArrayList argsList = new ArrayList(Array.toList()); 
        return check;
    }
}
