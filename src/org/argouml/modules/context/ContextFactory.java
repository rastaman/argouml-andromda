package org.argouml.modules.context;

public class ContextFactory {

    private static ContextFactory instance;
    
    private boolean standalone;
    
    private ModuleContext context;
    
    private ContextFactory() {
        super();
    }

    public static ContextFactory getInstance() {
        if (instance==null) {
            instance = new ContextFactory();
        }
        return instance;
    }
    
    public ModuleContext getContext() {
        if (context==null) {
            if (standalone) {
                context = new StandaloneContext();
            } else {
                context = new ArgoUMLContext();
            }
        }
        return context;
    }
    
    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }
    
    public boolean isStandalone() {
        return standalone;
    }
    
    public ModuleContext createContext(String propertiesFile) {
        context = new StandaloneContext(propertiesFile);
        standalone = true;
        return context;
    }
}
