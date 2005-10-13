// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.modules.andromda.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.argouml.modules.andromda.exec.MavenLauncher;
import org.argouml.ui.ArgoDialog;
import org.argouml.uml.ui.UMLAction;

/**
 * AndroMDA Module, for launching AndroMDA on the current model.<p>
 * @see <a href="http://forum.java.sun.com/thread.jspa?forumID=57&threadID=382380">Redirecting System.out/err to a JTextArea</a> 
 * @author Ludovic Ma&icirc;tre
 * @since  0.19.6
 */
public final class ActionLaunchAndroMDA extends UMLAction {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionLaunchAndroMDA.class);

    private JTextArea mavenOutput;
    
    private JTextArea mavenError;
    
    private JPanel mavenPanel;

    private ArgoDialog dialog = null;

    private String mavenHome;
    
    private String projectPath;
    
    private Frame parentFrame;

    private SwixMLContainer parent;
    
    /**
     * This is creatable from the module loader.
     */
    public ActionLaunchAndroMDA(SwixMLContainer module) {
        super("AndroMDA Launcher", false);
        parent=module;
    }

    //
    

    
    private String getProjectRoot(String project) {
        String sep = System.getProperty("file.separator");
        return project.substring(0,project.indexOf("mda"+sep+"src"+sep+"uml"));        
    }
    
    ////////////////////////////////////////////////////////////////
    // Main methods.

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     *
     * Just let the tester know that we got executed.
     */
    public void actionPerformed(ActionEvent event) {
        MavenLauncher launcher = new MavenLauncher();
        if (projectPath==null)
            try {
                projectPath= AndroMDAModule.getArgoUMLProjectPath();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        if (mavenHome == null)
            mavenHome = AndroMDAModule.getArgoUMLMavenHome();
        if (parentFrame == null)
            parentFrame = AndroMDAModule.getArgoUMLParentFrame();
        if (dialog==null)
            buildDialog(parentFrame);
        if (mavenHome!=null &&(projectPath != null)) {
            LOG.info("Assume that the model is in $PROJECT/mda/src/uml/");
            try {
                String projectRoot = getProjectRoot(projectPath); 
                launcher.setMavenHome(mavenHome);
                launcher.setProjectRoot(projectRoot);                
                launcher.setStdOut(new TextAreaOutputStream(mavenOutput));          
                launcher.setStdErr(new TextAreaOutputStream(mavenError));                
                //GUI
                dialog.pack();
                dialog.toFront();
                dialog.setVisible(true);
                launcher.start();
            } catch (Exception e) {
                throw new RuntimeException("Problem executing AndroMDA: "+e.getMessage());
            }
        } else {
            throw new RuntimeException("Problem executing AndroMDA: "+ mavenHome +"/"+projectPath);            
        }
    }

    private void buildDialog(Frame owner) {
        dialog = new ArgoDialog(owner, "Maven output", false);
        mavenPanel = (JPanel) parent.getSwingEngine().find("mavenPanel");   
        mavenOutput = (JTextArea) parent.getSwingEngine().find("mavenOutput");
        mavenError = (JTextArea) parent.getSwingEngine().find("mavenError");
        dialog.setContent(mavenPanel);  
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
    public void setMavenHome(String mavenHome) {
        this.mavenHome = mavenHome;
    }

    /**
     * @return Returns the parentFrame.
     */
    public Frame getParentFrame() {
        return parentFrame;
    }

    /**
     * @param parentFrame The parentFrame to set.
     */
    public void setParentFrame(Frame parentFrame) {
        this.parentFrame = parentFrame;
    }

    /**
     * @return Returns the projectPath.
     */
    public String getProjectPath() {
        return projectPath;
    }

    /**
     * @param projectPath The projectPath to set.
     */
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
}
