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
package org.argouml.modules.wizards;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.modules.context.ModuleContext;
import org.swixml.SwingEngine;
import org.tigris.swidgets.Dialog;

/**
 * This is the class which define a Wizard.
 * @author lmaitre
 */
public class WizardDialog extends Dialog {
    
    private Logger LOG = Logger.getLogger(WizardDialog.class);

	protected Vector wizardListener = new Vector();
	
	protected Vector pages = new Vector();
	
	protected JPanel buttonsPanel = new JPanel();
	
	protected JPanel jp1 = new JPanel();
	
	protected JPanel jp2;

	protected JButton next;
	
	protected JButton previous;
	
	protected JButton finish;

	protected int pageCounter = 0;

	protected boolean nextStatus = true;
	
	protected boolean previousStatus = true;
	
	protected boolean finishStatus = false;

	protected String _title;

    private ModuleContext parent;

    private WizardDialog wizard;
    
	public JPanel getPage(String id) {
		try {
			return (JPanel) parent.find(id);
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return null;
	}
	
	public WizardDialog(ModuleContext p, String title, String id, URL wizardDescriptor) {
        super(p.getParentFrame(),title,0,true);
        parent = p;
        SwingEngine swingEngine = parent.getSwingEngine();
        if (swingEngine.getTaglib().getFactory(WizardPage.class)==null) {
            swingEngine.getTaglib().registerTag("wizardpage",WizardPage.class);                
        }
		try {
             parent.render(wizardDescriptor);
             _title=parent.localize(title);
			Iterator i = swingEngine.getDescendants(swingEngine.find(id));
			while (i.hasNext()) {
				Component c = (Component) i.next();
				if (c instanceof WizardPage) {
                     LOG.info("Add "+c.getName());
					pages.addElement(c);
                    ((WizardPage)c).setContext(parent);
                }
			}
            //Add buttons
            next = (JButton)swingEngine.find("wizard:next");
            previous = (JButton)swingEngine.find("wizard:previous");
            finish = (JButton)swingEngine.find("wizard:finish");     
            buttonsPanel = (JPanel)swingEngine.find("wizard:buttons");
            this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
            this.getContentPane().add(jp1, BorderLayout.CENTER);
            next.addMouseListener(new NextPage());
            previous.addMouseListener(new PreviousPage());
            finish.addMouseListener(new FinishPage());            
            LOG.info("Go to page "+pageCounter);
            goToPage(pageCounter);
		} catch (Exception e) {
			LOG.error("Error while loading wizard pages from '"+wizardDescriptor+"'");
			e.printStackTrace();
		}
        centerOnParent();
        this.pack();
		wizard = this;
	}

	private WizardListener m_wiz;
	
	public void addListener(WizardListener wl) {
		wizardListener.add(wl);
	}
	
	public void notifyNextEvent() {
		Enumeration en = wizardListener.elements();
		while(en.hasMoreElements()) {
			m_wiz = (WizardListener)en.nextElement();
			m_wiz.nextEvent(new WizardEvent(this));
		}
	}

	public void notifyPreviousEvent() {
		Enumeration en = wizardListener.elements();
		while(en.hasMoreElements()) {
			m_wiz = (WizardListener)en.nextElement();
			m_wiz.previousEvent(new WizardEvent(this));
		}
		
	}
	
	public void notifyFinishEvent() {
		Enumeration en = wizardListener.elements();
		while(en.hasMoreElements()) {
			m_wiz = (WizardListener)en.nextElement();
			m_wiz.finishEvent(new WizardEvent(this));
		}		
	}

	public void show() {
		super.show();
	}
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
	}
	public void setSize(int w, int h) {
		super.setSize(w, h);
	}
	public void setPage(int index, WizardPage page) {
		pages.set(index, page);
	}

	public WizardPage firstPage() {
		return (WizardPage) pages.firstElement();
	}
	public WizardPage lastPage() {
		return (WizardPage) pages.lastElement();
	}
	public WizardPage pageAt(int index) {
		return (WizardPage) pages.elementAt(index);
	}
	
	public Vector getChildren(Container container) {		
		return getChildrenOfType(container,null);
	}

	public Vector getChildrenOfType(Container container,Class type) {
		Vector children = new Vector();
		Component[] childs = container.getComponents();
		for (int i=0;i<childs.length;i++) {
			if (type==null||type.equals(childs[i].getClass()))
				children.add(childs[i]);
			if (childs[i] instanceof Container)
				children.add(getChildren((Container)childs[i]));
		}
		return children;
	}
	
    /**
     * Return the values for all the pages
     * @return Map of all the properties which have been entered.
     */
    public Map getAllValues() {
        Map values = new HashMap();
        Iterator it = pages.iterator();
        WizardPage page;
        while (it.hasNext()) {
            page = (WizardPage)it.next();
            LOG.debug("Get values for '"+page.getName()+"'");
            Map pageMap = page.getValues();
            Iterator keys = pageMap.keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                LOG.debug("Put '"+pageMap.get(key)+"' under key '"+key+"'");
                values.put(key,pageMap.get(key));
            }            
        }
        return values;
    }
	
	/**
	 * Return the values on the given page as a hashmap,
	 * keyed by swixml components id's and valued with
	 * the text of the components. 
	 * @param pageCounter
	 * @return
	 */
	public Map getValuesForPage(int pageCounter) {
        return pageAt(pageCounter).getValues();
	}
    
	/**
	 * Assign the values contained in a map to the specified swing components
	 * used in the page. 
	 * @param pageCounter
	 * @param values
	 */
	public void setValuesForPage(int pageCounter,Map values) {
	    pageAt(pageCounter).setValues(values);
	}
    
	/**
	*	Validating which button to show and which one to disable.
	*
	*/
	public void goToPage(int pageCounter) {
		if (pageCounter == 0) {
			previous.setEnabled(false);
			previousStatus = false;
			finish.setEnabled(false);
			finishStatus = false;
		} else {
			previous.setEnabled(true);
			previousStatus = true;
		}
		if (pageCounter == pages.size() - 1) {
			next.setEnabled(false);
			nextStatus = false;
			finish.setEnabled(true);
			finishStatus = true;
		} else {
			next.setEnabled(true);
			nextStatus = true;
			finish.setEnabled(false);
			finishStatus = false;
        }	
        if (jp2!=null)
            jp2.setVisible(false);
        this.getContentPane().removeAll();
        this.getContentPane().add(buttonsPanel,BorderLayout.SOUTH);
        jp2 = (JPanel) pageAt(pageCounter);
        if (jp2!=null)
            jp2.setVisible(true);
        this.getContentPane().add(jp2, BorderLayout.CENTER);
        this.setTitle(_title+" - "+(pageCounter+1)+"/"+pages.size());
        if (jp2.getGraphics()!=null)
        		this.getContentPane().update(jp2.getGraphics());        
	}

	protected class NextPage extends MouseAdapter implements MouseListener {

		public NextPage() {
		}

		public void mouseClicked(MouseEvent me) {
			if (me.getSource() == next && nextStatus) {
				pageCounter++;
				goToPage(pageCounter);
				getValuesForPage(pageCounter);
				wizard.notifyNextEvent();
			}
		}
	}

	protected class PreviousPage extends MouseAdapter implements MouseListener {

		public PreviousPage() {
		}

		public void mouseClicked(MouseEvent me) {
			if (me.getSource() == previous && previousStatus) {
				pageCounter--;
				goToPage(pageCounter);
				getValuesForPage(pageCounter);
				wizard.notifyPreviousEvent();
			}
		}
	}
	
	protected class FinishPage extends MouseAdapter implements MouseListener {

		public FinishPage() {
		}

		public void mouseClicked(MouseEvent me) {
			if (me.getSource() == finish && finishStatus) {
				wizard.notifyFinishEvent();
			}
		}
    }
    
    /**
     * Moves the dialog to be centered on its parent's location on the screen.
     **/
    public void centerOnParent() {
        Dimension size = getSize();
        Dimension p = getParent().getSize();
        int x = (getParent().getX() - size.width)
        + (int) ((size.width + p.width) / 2d);
        int y = (getParent().getY() - size.height)
        + (int) ((size.height + p.height) / 2d);
        setLocation(x, y);
    }

    /**
     * @see org.tigris.swidgets.Dialog#nameButtons()
     */
    protected void nameButtons() {
        //      
    }    

    public void reset() {
        Iterator it = pages.iterator();
        while (it.hasNext()) {
            ((WizardPage)it.next()).reset();
        }
        pageCounter = 0;
        goToPage(pageCounter);
    }
}
