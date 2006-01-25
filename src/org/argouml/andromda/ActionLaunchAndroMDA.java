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

package org.argouml.andromda;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.argouml.modules.actions.AbstractModuleAction;
import org.argouml.modules.context.ModuleContext;
import org.argouml.modules.exec.MavenLauncher;
import org.argouml.modules.exec.TextAreaOutputStream;
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

    private ModuleContext parent;
    
    private JPanel goalsPanel;
    
    private JTextArea mavenOutput;
    
    private JTextArea mavenError;
    
    private JPanel mavenPanel;

    private ArgoDialog dialog = null;

    private ArgoDialog goalsDialog = null;

    private Frame parentFrame;
    
    /**
     * This is creatable from the module loader.
     */
    public ActionLaunchAndroMDA(ModuleContext module) {
        super("AndroMDA Launcher", false);
        parent=module;
    }

    //
        
    ////////////////////////////////////////////////////////////////
    // Main methods.

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     *
     * Just let the tester know that we got executed.
     */
    public void actionPerformed(ActionEvent event) {
        if (parentFrame == null)
            parentFrame = parent.getParentFrame();
        if (goalsDialog==null)
            buildGoalsDialog(parentFrame);
        if (dialog==null)
            buildDialog(parentFrame);
        //LOG.info("Assume that the model is in $PROJECT/mda/src/uml/");
        goalsDialog.pack();
        goalsDialog.toFront();
        goalsDialog.setVisible(true);
    }
    
    private void buildDialog(Frame owner) {
        dialog = new ArgoDialog(owner, parent.localize("maven.output"), false);
        mavenPanel = (JPanel) parent.find("andromda:maven");   
        mavenOutput = (JTextArea) parent.find("andromda:mavenoutput");
        mavenError = (JTextArea) parent.find("andromda:mavenerror");
        parent.getActionManager().addAction("andromda:maven:action:clear-console",
                new ClearAction(new JTextArea[] { mavenOutput, mavenError }));
        JButton clear = (JButton) parent.find("andromda:clear");
        dialog.addButton(clear);
        dialog.setContent(mavenPanel);
    }

    private void buildGoalsDialog(Frame owner) {
        goalsDialog = new ArgoDialog(owner, parent.localize("maven.launch"), false);
        parent.getActionManager().addAction("andromda:maven:action:run",
                new LaunchAction(parent));
        goalsPanel = (JPanel) parent.find("andromda:maven:panels:launch");   
        goalsDialog.setContent(goalsPanel);
        JButton launch = (JButton) parent.find("andromda:maven:launch");
        goalsDialog.addButton(launch);
        goalsDialog.pack();
    }

    private List getGoals() {
        List goalsButtons = (List) parent.getAttribute("andromda-module:maven:goals");
        List goals = new ArrayList();
        Iterator it = goalsButtons.iterator();
        JRadioButton tmp;
        while (it.hasNext()) {
        		tmp = (JRadioButton) it.next();
        		if (tmp.isSelected())
        			goals.add(tmp.getText());        			
        }
        JTextField freeGoals = (JTextField) parent.find("maven:goal:free");
        if (!ValidatorAndroMDA.isNullOrEmpty(freeGoals.getText())) {
            goals.addAll(getArguments(freeGoals.getText()));
        }
        return goals;
    }
    
    /**
     * @return Returns the mavenHome.
     */
    public String getMavenHome() {
        return parent.getProperty(SettingsTabAndroMDA.KEY_MAVEN_HOME);
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

    public List getArguments(String argline) {
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
    
    /**
     * @return Returns the projectPath.
     */
    public String getProjectPath() {
    		return parent.getProjectPath();
    }
    
    class ClearAction extends AbstractAction {
        
        private JTextArea[] textAreas;
        
        public ClearAction(JTextArea[] tas) {
            textAreas = tas;
        }
        
        public void actionPerformed(ActionEvent e) {
            for (int i=0;i<textAreas.length;i++)
                textAreas[i].setText("");
        }        
    }
    
    class LaunchAction extends AbstractModuleAction {
                
        /**
         * @param p
         */
        public LaunchAction(ModuleContext p) {
            super(p);
            // TODO Auto-generated constructor stub
        }

        public void actionPerformed(ActionEvent e) {
            if (!ValidatorAndroMDA.validateFile(getProjectPath())) {
                parent.showError("error.project.not.exist",getProjectPath());
                return;
            }
            if (!ValidatorAndroMDA.validateFolder(getMavenHome())) {
                parent.showError("error.maven.not.set");
                return;
            }
            try {
                String projectRoot = AndroMDAModule.getProjectRoot(getProjectPath()); 
                if (!ValidatorAndroMDA.validateFile(projectRoot+"project.xml")) {
                    parent.showError("error.maven.project.not.exist",projectRoot+"project.xml");
                    return;
                }
                MavenLauncher launcher = new MavenLauncher();
                launcher.addGoals(getGoals());
                launcher.setMavenHome(getMavenHome());
                launcher.setProjectRoot(projectRoot);                
                launcher.setStdOut(new TextAreaOutputStream(mavenOutput));          
                launcher.setStdErr(new TextAreaOutputStream(mavenError));                
                //GUI
                dialog.pack();
                dialog.toFront();
                dialog.setVisible(true);
                launcher.start();
            } catch (Exception ex) {
                parent.showError("error.maven.runtime",ex.getMessage());
                ex.printStackTrace();
            }               
        };
        
    }
}
