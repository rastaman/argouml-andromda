/**
 * 
 */
package org.argouml.modules.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpinnerListModel;

import org.apache.log4j.Level;

/**
 * @author lmaitre
 *
 */
public class SpinnerModel extends SpinnerListModel {
    
    /**
     * @param values
     */
    public SpinnerModel(List values) {
        super(values);
    }

    /**
     * @param values
     */
    public SpinnerModel(Object[] values) {
        super(values);
    }

    /**
     * 
     */
    public SpinnerModel() {
        super();
        List spinnerModel = new ArrayList();
        spinnerModel.add("DEBUG");
        spinnerModel.add("INFO");
        spinnerModel.add("WARN");
        spinnerModel.add("ERROR");
        spinnerModel.add("FATAL");
        spinnerModel.add("OFF");
        setList(spinnerModel);
        Level l = Level.DEBUG;
    }

}
