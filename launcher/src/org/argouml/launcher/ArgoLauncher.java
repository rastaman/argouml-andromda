package org.argouml.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.CommandLineImpl;
import org.apache.commons.exec.Execute;
import org.argouml.launcher.utils.JavaEnvUtils;

public class ArgoLauncher {

    private static String DEFAULT_PROFILE="/org/argouml/model/mdr/mof/default-uml14.xmi";
    
    private static String INFO_CONSOLE="org/argouml/resource/info_console.lcf";
    
    private static String USER_PREFS=System.getProperty("user.home")+System.getProperty("file.separator")+"argo.user.properties";
    
    private static String PROFILE_KEY="argo.defaultModel";
    
    private static Properties preferences;

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
        cl.addArgument("-Dargo.defaultModel="+System.getProperty(PROFILE_KEY));
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
            System.setProperty(PROFILE_KEY,theFile.getAbsolutePath());
            if (preferences!=null)
                try {
                    out("Saving profile in user preferences");
                    preferences.put(PROFILE_KEY,theFile.getAbsolutePath());
                    preferences.store(new FileOutputStream(USER_PREFS),"# Modified by ArgoUML Profile launcher");
                } catch (Exception e) {
                    err("Error saving user profile '"+USER_PREFS+"':"+e.getMessage());
                    e.printStackTrace();
                }
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
        preferences = new Properties();
        try {
            preferences.load(new FileInputStream(USER_PREFS));
            List argList = Arrays.asList(args);
            if (preferences.containsKey("argo.defaultModel") && !argList.contains("-checkProfile")) {
                System.setProperty("argo.defaultModel",preferences.getProperty(PROFILE_KEY));
                return false;
            }
        } catch (FileNotFoundException e) {
            //ILB
        } catch (IOException e) {
            err("Error opening user preferences file '"+USER_PREFS+"':"+e.getMessage());
            e.printStackTrace();
        }
        return check;
    }
}
