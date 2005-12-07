package org.argouml.andromda;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.modules.context.ModuleContext;
import org.argouml.modules.gui.SwixMLUtils;
import org.argouml.ui.ArgoDialog;
import org.argouml.uml.ui.UMLAction;
import org.jdom.transform.JDOMResult;
import org.xml.sax.InputSource;

public class ActionConfigAndroMDA extends UMLAction {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionLaunchAndroMDA.class);

    private ModuleContext parent;

    /**
     * This is creatable from the module loader.
     */
    public ActionConfigAndroMDA(ModuleContext module) {
        super("AndroMDA Config", false);
        parent=module;
    }

    /**
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String andromdaConfig = (String) parent.getAttribute("andromda:projecthome")
            + "/mda/conf/andromda.xml";
        if (!ValidatorAndroMDA.validateFile(andromdaConfig)) {
            parent.showError("file.not.exist",andromdaConfig);
            return;
        }
        try {
            InputSource input = new InputSource(new FileInputStream(andromdaConfig));
            SAXSource myInput = new SAXSource(input);
            SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory
                    .newInstance();

            // Set up source for style sheet
            String xsltFileName = "config2swing.xsl";
            URL xsltUrl = getClass().getResource(xsltFileName);
            if (xsltUrl == null) {
                throw new Exception("Error opening XSLT style sheet : "
                        + xsltFileName);
            }
            StreamSource xsltStreamSource = new StreamSource(xsltUrl.openStream());
            xsltStreamSource.setSystemId(xsltUrl.toExternalForm());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JDOMResult result = new JDOMResult();
            // Create transformer and do transformation
            Transformer transformer = stf.newTransformer(xsltStreamSource);
            transformer.transform(myInput, result);
            ArgoDialog configDialog = new ArgoDialog(parent.getParentFrame(),"AndroMDA Configuration",ArgoDialog.OK_CANCEL_OPTION,true);
            parent.getSwingEngine().insert(result.getDocument(),configDialog);
            SwixMLUtils.centerOnParent(configDialog);
            configDialog.setVisible(true);
        } catch (Exception e1) {
            parent.showError("error",e1.getMessage());
            e1.printStackTrace();
        }
    }


}
