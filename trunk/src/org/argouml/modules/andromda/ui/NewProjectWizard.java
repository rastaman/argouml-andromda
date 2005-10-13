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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.argouml.modules.andromda.ui.wizards.WizardDialog;

/**
 * A s(a)imple wizard for creating an AndroMDA project inside ArgoUML.
 * @author lmaitre
 */
public class NewProjectWizard extends WizardDialog {

    private Logger LOG = Logger.getLogger(NewProjectWizard.class);
    
	public final static String WIZARD_DESCRIPTOR = "new-project-wizard.xml";

    private Properties projectProperties = new Properties();
    
    private NewProjectWizard wizard;
    
    private JTextField parentFolder;

    private JButton parentFolderBt;
    
	/**
	 * @param wizardDescriptor
	 */
	public NewProjectWizard() {
		super(AndroMDAModule.getArgoUMLParentFrame(),WIZARD_DESCRIPTOR);	
		setValues(projectProperties);
         wizard = this;
         parentFolder = (JTextField) swix.find("parentFolder");
         parentFolderBt = (JButton) swix.find("parentFolderBt");
         parentFolderBt.setAction(chooseParentFolder);
		LOG.info("Wizard is initialized.");	
	}

    public Action chooseParentFolder = new AbstractAction() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JFileChooser chooser =  new JFileChooser();
            chooser.setDialogTitle("Select parent folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retval = chooser.showOpenDialog(wizard);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File theFile = chooser.getSelectedFile();
                try {
                    parentFolder.setText(theFile.getCanonicalPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
    };
    
}
