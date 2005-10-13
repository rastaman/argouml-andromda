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
package org.argouml.modules.andromda.ui.wizards;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;

/**
 * This is a sample and empty implementation of the Wizard.
 * @author lmaitre / Factory Productions
 */
public class DefaultWizardDialog extends WizardDialog {

	/**
	 * @param wizardDescriptor
	 */
	public DefaultWizardDialog(Frame owner,String wizardDescriptor) {
		super(owner,wizardDescriptor);

	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#addListener(fr.factory.gui.wizard.WizardListener)
	 */
	public void addListener(WizardListener wl) {

		super.addListener(wl);
	}

	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#firstPage()
	 */
	public Object firstPage() {

		return super.firstPage();
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#getChildren(java.awt.Container)
	 */
	public Vector getChildren(Container container) {

		return super.getChildren(container);
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#getId(java.awt.Component)
	 */
	public String getId(Component c) {

		return super.getId(c);
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#getPage(int)
	 */
	public Object getPage(int index) {

		return super.getPage(index);
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#getPage(java.lang.String)
	 */
	public JPanel getPage(String id) {

		return super.getPage(id);
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#getValuesForPage(int)
	 */
	public Map getValuesForPage(int pageCounter) {

		return super.getValuesForPage(pageCounter);
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#lastPage()
	 */
	public Object lastPage() {

		return super.lastPage();
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#notifyFinishEvent()
	 */
	public void notifyFinishEvent() {

		super.notifyFinishEvent();
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#notifyNextEvent()
	 */
	public void notifyNextEvent() {

		super.notifyNextEvent();
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#notifyPreviousEvent()
	 */
	public void notifyPreviousEvent() {

		super.notifyPreviousEvent();
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#pageAt(int)
	 */
	public Object pageAt(int index) {

		return super.pageAt(index);
	}
	/* (non-Javadoc)
	 * @see java.awt.Component#setLocation(int, int)
	 */
	public void setLocation(int x, int y) {

		super.setLocation(x, y);
	}
	/* (non-Javadoc)
	 * @see fr.factory.gui.wizard.ExtendedWizardDialog#setPage(int, java.lang.Object)
	 */
	public void setPage(int index, Object page) {

		super.setPage(index, page);
	}
	/* (non-Javadoc)
	 * @see java.awt.Component#setSize(int, int)
	 */
	public void setSize(int w, int h) {

		super.setSize(w, h);
	}
	/* (non-Javadoc)
	 * @see java.awt.Component#show()
	 */
	public void show() {

		super.show();
	}

}
