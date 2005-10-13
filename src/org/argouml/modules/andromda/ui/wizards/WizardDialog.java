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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
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
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.swixml.ConverterLibrary;
import org.swixml.SwingEngine;

/**
 * This is the class which define a Wizard.
 * @author lmaitre
 */
public abstract class WizardDialog extends JDialog {
    
    private Logger LOG = Logger.getLogger(WizardDialog.class);

	protected SwingEngine swix = new SwingEngine(this);

	protected Vector wizardListener = new Vector();
	
	protected Vector pages = new Vector();
	
	protected static JPanel jp = new JPanel();
	
	protected static JPanel jp1 = new JPanel();
	
	protected JPanel jp2;

	protected JButton next;
	
	protected JButton previous;
	
	protected JButton finish;

	protected int pageCounter = 0;
	
	protected static WizardDialog wm;
	
	protected boolean nextStatus = true;
	
	protected boolean previousStatus = true;
	
	protected boolean finishStatus = false;

	protected String _title;
	
	public JPanel getPage(String id) {
		try {
			return (JPanel) swix.find(id);
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return null;
	}
	
	public WizardDialog(Frame owner,String wizardDescriptor) {
        super(owner);
		setModal(true);
		next = new JButton("Next");
		previous = new JButton("Previous");
		finish = new JButton("Finish");
		jp.add(next);
		jp.add(previous);
		jp.add(finish);
		this.getContentPane().add(jp, BorderLayout.SOUTH);
		this.getContentPane().add(jp1, BorderLayout.CENTER);
		next.addMouseListener(new NextPage());
		previous.addMouseListener(new PreviousPage());
		finish.addMouseListener(new FinishPage());
		try {
            ConverterLibrary.getInstance().register(WizardColorConverter.TEMPLATE, 
                    new WizardColorConverter());
			swix.getTaglib().registerTag("wizardpage",WizardPage.class);
			swix.cleanup();
            URL descriptor = this.getClass().getResource(wizardDescriptor);
			swix.insert(descriptor,this);
			_title=getName();
			Iterator i = swix.getAllComponentItertor();
			while (i.hasNext()) {
				Component c = (Component) i.next();
				if (c instanceof WizardPage)
					pages.addElement(c);
			}
            goToPage(pageCounter);
		} catch (Exception e) {
			System.err.println("Error while loading wizard pages from '"+wizardDescriptor+"'");
			e.printStackTrace();
		}
        centerOnParent();
        this.pack();
		wm = this;
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
	public void setPage(int index, Object page) {
		pages.set(index, page);
	}
	public Object getPage(int index) {
		return pages.get(index);
	}
	public Object firstPage() {
		return pages.firstElement();
	}
	public Object lastPage() {
		return pages.lastElement();
	}
	public Object pageAt(int index) {
		return pages.elementAt(index);
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

	public String getId(Component c) {
		String result = null;
		Map ids = swix.getIdMap();
		Iterator i = ids.keySet().iterator();
		while (i.hasNext()) {
			String id = (String) i.next();
			Object idtmp = ids.get(id);
			if (idtmp.equals(c))
				result=id;
		}
		return result;
	}
	
    /**
     * Return the values for all the pages
     * @return Map of all the properties which have been entered.
     */
    public Map getAllValues() {
        Map values = new HashMap();
        for (int i=0;i<pages.size();i++) {
            Map pageMap = getValuesForPage(i);
            Iterator keys = pageMap.keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                values.put(key,pageMap.get(key));
            }
        }
        return values;
    }
	/**
	 * Return the values on the current page as a hashmap,
	 * keyed by swixml components id's and valued with
	 * the text of the components. 
	 * @param pageCounter
	 * @return
	 */
	public Map getValues() {
		Map values = new HashMap();
		Iterator k = swix.getDescendants((JPanel) pageAt(pageCounter));
		Component o;
		String id;
		while (k.hasNext()) {
			o = (Component)k.next();
			id = getId(o);
			if (id !=null &&(o instanceof JTextComponent))				
				values.put(id,((JTextComponent)o).getText());
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
		Map values = new HashMap();
		Iterator k = swix.getDescendants((JPanel) pageAt(pageCounter));
		Component o;
		String id;
		while (k.hasNext()) {
			o = (Component)k.next();
			id = getId(o);
			if (id !=null &&(o instanceof JTextComponent))				
				values.put(id,((JTextComponent)o).getText());
		}
		return values;
	}

	/**
	 * Assign the values contained in a map to the specified swing components
	 * used in the current page. 
	 * @param pageCounter
	 * @param values
	 */
	public void setValues(Map values) {
		Iterator k = swix.getDescendants((JPanel) pageAt(pageCounter));
		Component o = null;
		String id = null;
		while (k.hasNext()) {
			o = (Component)k.next();
			id = getId(o);
			if ((id!=null)&&(o instanceof JTextComponent)&&(values.containsKey(id))) {
					((JTextComponent)o).setText((String) values.get(id));
					LOG.info("Value for id '"+id+"' is '"+(String) values.get(id)+"'");
			}
		}	
	}
    
	/**
	 * Assign the values contained in a map to the specified swing components
	 * used in the page. 
	 * @param pageCounter
	 * @param values
	 */
	public void setValuesForPage(int pageCounter,Map values) {
		Iterator k = swix.getDescendants((JPanel) pageAt(pageCounter));
		Component o = null;
		String id = null;
		while (k.hasNext()) {
			o = (Component)k.next();
			id = getId(o);
			if ((id!=null)&&(o instanceof JTextComponent)&&(values.containsKey(id)))
					((JTextComponent)o).setText((String) values.get(id));
		}	
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
        this.getContentPane().remove(1);
        jp2 = (JPanel) pageAt(pageCounter);
        if (jp2!=null)
            jp2.setVisible(true);
        this.getContentPane().add(jp2, BorderLayout.CENTER);
        this.setTitle(_title+" - "+(pageCounter+1)+"/"+pages.size());
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
				wm.notifyNextEvent();
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
				wm.notifyPreviousEvent();
			}
		}
	}
	
	protected class FinishPage extends MouseAdapter implements MouseListener {

		public FinishPage() {
		}

		public void mouseClicked(MouseEvent me) {
			if (me.getSource() == finish && finishStatus) {
				wm.notifyFinishEvent();
			}
		}
    }
    
    /**
     * Moves the dialog to be centered on its parent's location on the screen.
     **/
    private void centerOnParent() {
        Dimension size = getSize();
        Dimension p = getParent().getSize();
        int x = (getParent().getX() - size.width)
        + (int) ((size.width + p.width) / 2d);
        int y = (getParent().getY() - size.height)
        + (int) ((size.height + p.height) / 2d);
        setLocation(x, y);
    }    
}
