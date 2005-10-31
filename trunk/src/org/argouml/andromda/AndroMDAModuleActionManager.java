package org.argouml.andromda;

import org.argouml.modules.actions.AbstractActionManager;
import org.argouml.modules.actions.ActionChooseFolder;
import org.argouml.modules.actions.ActionManager;
import org.argouml.modules.container.ModuleContainer;

public class AndroMDAModuleActionManager extends AbstractActionManager implements ActionManager {

    private ModuleContainer parent;
    
    public AndroMDAModuleActionManager(ModuleContainer p) {
        super();
        parent = p;
        addAction("launchMavenAction",new ActionLaunchAndroMDA(parent));
        addAction("createProjectAction",new ActionCreateProjectAndroMDA(parent));
        addAction("chooseAndroMdaHomeAction",new ActionChooseFolder(parent, "andromdaHome", "select.andromda.home"));
        addAction("chooseMavenHomeAction",new ActionChooseFolder(parent, "mavenHome", "select.maven.home"));
        addAction("chooseParentFolderAction",new ActionChooseFolder(parent, "parentFolder", "select.parent.folder"));
    }
   
}
