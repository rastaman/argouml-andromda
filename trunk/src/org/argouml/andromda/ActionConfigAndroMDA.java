package org.argouml.andromda;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.argouml.modules.context.ModuleContext;
import org.argouml.modules.gui.ComboModel;
import org.argouml.modules.gui.DOMWriter;
import org.argouml.modules.gui.SwixMLUtils;
import org.argouml.ui.ArgoDialog;
import org.argouml.uml.ui.UMLAction;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.jdom.Document;
import org.jdom.transform.JDOMResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class ActionConfigAndroMDA extends UMLAction {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ActionLaunchAndroMDA.class);

    private ModuleContext parent;

    private Document configForm;

    private org.w3c.dom.Document configXml;
    
    private String configLocation;

    private ArgoDialog dialog;

    /**
     * This is creatable from the module loader.
     */
    public ActionConfigAndroMDA(ModuleContext module) {
        super("AndroMDA Config", false);
        parent = module;
        parent.getActionManager().addAction("andromda:config:action:close",
                this);
        parent.getActionManager().addAction("andromda:config:action:apply",
                this);
        parent.getActionManager().addAction("andromda:config:action:select-namespace",
                this);
    }

    public void buildDialog() {
        if (dialog == null) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                configXml = builder.parse(new File(configLocation));
                //TODO: Use the DOM document instead of re-parsing the file
                configForm = getDocument(configLocation);
                dialog = new ArgoDialog(parent.getParentFrame(),
                        "AndroMDA Configuration", 0,
                        true);
                JPanel contents = new JPanel();
                parent.getSwingEngine().insert(configForm, contents);
                dialog.setContent(contents);
                //update buttons
                Iterator it = parent.getSwingEngine().getDescendants(
                        (Component)parent.find("andromda:config:buttons"));
                Object button;
                while (it.hasNext()) {
                    button = it.next();
                    if (button instanceof JButton)
                        dialog.addButton((JButton)button);
                }
                SwixMLUtils.centerOnParent(dialog);
            } catch (Exception e) {
                e.printStackTrace();
                parent.showError("error", e.getMessage());
            }
        }
    }

    /**
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String id = parent.getComponentId(e.getSource());
        if ("andromda:configuration".equals(id)) {
            display("default");
            return;
        }
        ComboModel model = (ComboModel) ((JComboBox) parent
                .find("andromda:config:select-namespace")).getModel();
        String currentNamespace = model.getSelectedItem().toString();
        if ("andromda:config:apply".equals(id)) {
            apply(currentNamespace);
            saveConfig();
        } else if ("andromda:config:close".equals(id)) {
            close();
        } else if ("andromda:config:select-namespace".equals(id)) {
            display(currentNamespace);
        }
    }

    public void display(String namespace) {
        String andromdaConfig = AndroMDAModule.getProjectRoot(parent.getProjectPath())
                + "/mda/conf/andromda.xml";
        if (!ValidatorAndroMDA.validateFile(andromdaConfig)) {
            parent.showError("file.not.exist", andromdaConfig);
            return;
        }
        try {
            if (andromdaConfig != configLocation) {
                // TODO: Manage change of file/project
                configLocation = andromdaConfig;
                buildDialog();
            }
            //manage namespaces
            setVisibleNamespace(namespace);
            dialog.validate();
            dialog.setVisible(true);
        } catch (Exception e1) {
            parent.showError("error", e1.getMessage());
            e1.printStackTrace();
        }
    }

    public void setVisibleNamespace(String namespace) {
        Iterator it = parent.getAllElements("namespaces:properties").keySet().iterator();
        Object namespaceVbox;
        String name;
        while (it.hasNext()) {
            name = it.next().toString();
            namespaceVbox = parent.find(name);
            if (namespaceVbox instanceof Component) {
                if (("namespaces:properties:"+namespace).equals(name))
                    ((Component)namespaceVbox).setVisible(true);
                else
                    ((Component)namespaceVbox).setVisible(false);
            }
        }
    }
    
    public void saveConfig() {
        DOMWriter dw = new DOMWriter();
        File f = new File(configLocation);
        File backup = new File(configLocation + ".bak");
        f.renameTo(backup);
        f = new File(configLocation);
        LOG.info("Writing document to file '" + f.getAbsolutePath() + "'");
        // FileOutputStream fos = new FileOutputStream(f);
        // dw.setOutput(fos,"iso-8859-1");
        // dw.setCanonical(true);
        try {
            FileWriter fw = new FileWriter(f);
            dw.setOutput(fw);
            dw.setEncoding("ISO-8859-1");
            dw.write(configXml);
        } catch (Exception e) {
            e.printStackTrace();
            parent.showError("error", e.getMessage());
        }
    }

    public void apply(String namespace) {
        // Apply XPaths
        Component namespaceToUpdate = (Component) parent
                .find("namespaces:properties:" + namespace);
        Iterator it = parent.getSwingEngine().getDescendants(namespaceToUpdate);
        String name;
        String value;
        Object swing;
        try {
            while (it.hasNext()) {
                swing = it.next();// parent.find(name);
                name = parent.getComponentId(swing);// it.next().toString();
                if (swing instanceof JTextField) {
                    value = ((JTextField) swing).getText();
                    LOG.info("Update '"
                            + name.trim().replaceAll("\r", "").replaceAll("\n",
                                    "").replaceAll("\t", "")
                                    .replaceAll(" ", "") + "' with value '"
                            + value + "'");
                    updateXPath(configXml, name, value);
                }/*
                     * else { LOG.info("Don't know how to update '"+name+"'
                     * ["+swing+"]"); }
                     */
            }
        } catch (Exception e) {
            e.printStackTrace();
            parent.showError("error", e.getMessage());
        }
    }

    public void updateXPath(org.w3c.dom.Document domDoc, String xpath,
            String value) throws Exception {

         // Create an XPath evaluator and pass in the document.
		 XPath path = new DOMXPath(xpath);
         Node n = (Node) path.selectSingleNode(domDoc.getDocumentElement());
        if (n != null) {
            LOG.info("Set " + xpath + " from " + n.getNodeValue()
                    + " to value " + value);
            // debugNode(n);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                // n.setNodeValue(value);
                Element e = (Element) n;
                Node newNode = domDoc.createTextNode(value);
                e.replaceChild(newNode, e.getFirstChild());
            }
        } else
            LOG.info("Cannot update " + xpath);
    }

    public void close() {

    }

    private Document getDocument(String configFile) throws Exception {
        InputSource input = new InputSource(new FileInputStream(configFile));
        SAXSource myInput = new SAXSource(input);
        SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory
                .newInstance();

        // Set up source for style sheet
        String xsltFileName = "config2swing.xsl";
        URL xsltUrl = getClass().getResource(xsltFileName);
        if (xsltUrl == null) {
            throw new Exception("Cannot open XSL style sheet : " + xsltFileName);
        }
        StreamSource xsltStreamSource = new StreamSource(xsltUrl.openStream());
        xsltStreamSource.setSystemId(xsltUrl.toExternalForm());
        JDOMResult result = new JDOMResult();
        // Create transformer and do transformation
        Transformer transformer = stf.newTransformer(xsltStreamSource);
        transformer.transform(myInput, result);
        return result.getDocument();
    }
}
