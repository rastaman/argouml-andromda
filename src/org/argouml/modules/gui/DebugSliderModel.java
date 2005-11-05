/**
 * 
 */
package org.argouml.modules.gui;

import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author lmaitre
 *
 */
public class DebugSliderModel extends DefaultBoundedRangeModel implements BoundedRangeModel {

    private ArrayList levels;

    private Logger theTargetLogger;
    
    /**
     * 
     */
    public DebugSliderModel(String categoryName) {
        super();
        Object[] objLevels = new Object[] {
                Level.DEBUG,
                Level.INFO,
                Level.WARN,
                Level.ERROR,
                Level.FATAL,
                Level.OFF
        };
        levels = new ArrayList();
        for (int i=0;i<objLevels.length;i++) {
            levels.add(objLevels[i]);
        }
        setMinimum(0);
        setMaximum(levels.size()-1);
        // Special case for the root logger
        if ("root".equals(categoryName)) {
            theTargetLogger = Logger.getRootLogger();
        } else {
            theTargetLogger = Logger.getLogger(categoryName);
        }
        int initialValue = levels.indexOf(theTargetLogger.getLevel());
        setValue(initialValue);
    }

    /**
     * @see javax.swing.DefaultBoundedRangeModel#setValue(int)
     */
    public void setValue(int n) {
        super.setValue(n);
        if (getValueIsAdjusting()) {
            Level l = (Level) levels.get(n);
            if (theTargetLogger.getLevel()!=l) {
                System.err.println("Set value from "+theTargetLogger.getLevel()+" to "+l);
                theTargetLogger.setLevel(l);
            }
        }
    }

}
