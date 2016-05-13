/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.plugin.jdb.ide.debug;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.api.debug.shared.model.Location;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.event.FileEvent;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.ext.java.client.project.node.JavaNodeManager;
import org.eclipse.che.ide.ext.java.client.project.node.jar.JarFileNode;
import org.eclipse.che.ide.ext.java.shared.JarEntry;
import org.eclipse.che.ide.part.explorer.project.ProjectExplorerPresenter;
import org.eclipse.che.plugin.debugger.ide.debug.AbstractFileHandler;

import static org.eclipse.che.ide.api.event.FileEvent.FileOperation.OPEN;

/**
 * Responsible to open files in editor when debugger stopped at breakpoint.
 *
 * @author Anatoliy Bazko
 */
public class JavaDebuggerFileHandler extends AbstractFileHandler {

    private final DtoFactory      dtoFactory;
    private final AppContext      appContext;
    private final EventBus        eventBus;
    private final JavaNodeManager javaNodeManager;

    @Inject
    public JavaDebuggerFileHandler(EditorAgent editorAgent,
                                   DtoFactory dtoFactory,
                                   AppContext appContext,
                                   EventBus eventBus,
                                   JavaNodeManager javaNodeManager,
                                   ProjectExplorerPresenter projectExplorer) {
        super(editorAgent, projectExplorer);
        this.dtoFactory = dtoFactory;
        this.appContext = appContext;
        this.eventBus = eventBus;
        this.javaNodeManager = javaNodeManager;
    }

    @Override
    public void openFile(Location location, AsyncCallback<VirtualFile> callback) {
//        if (debuggerManager.getActiveDebugger() != debuggerManager.getDebugger(JavaDebugger.ID)) {
//            callback.onFailure(null);
//            return;
//        }
//
//        VirtualFile activeFile = null;
//        final EditorPartPresenter activeEditor = editorAgent.getActiveEditor();
//        if (activeEditor != null) {
//            activeFile = activeEditor.getEditorInput().getFile();
//        }

//        if (activeFile == null || !filePaths.contains(activeFile.getPath())) {
//            openFile(className, filePaths, 0, new AsyncCallback<VirtualFile>() {
//                @Override
//                public void onSuccess(VirtualFile result) {
//                    scrollEditorToExecutionPoint((TextEditorPresenter)editorAgent.getActiveEditor(), lineNumber);
//                    callback.onSuccess(result);
//                }
//
//                @Override
//                public void onFailure(Throwable caught) {
//                    callback.onFailure(caught);
//                }
//            });
//        } else {
//            scrollEditorToExecutionPoint((TextEditorPresenter)activeEditor, lineNumber);
//            callback.onSuccess(activeFile);
//        }
    }

    private void openExternalResource(Location location, final AsyncCallback<VirtualFile> callback) {
        String className = location.getTarget();
        JarEntry jarEntry = dtoFactory.createDto(JarEntry.class);
        jarEntry.setPath(className);//what hell?
        jarEntry.setName(className.substring(className.lastIndexOf(".") + 1) + ".class");
        jarEntry.setType(JarEntry.JarEntryType.CLASS_FILE);

        JarFileNode jarFileNode = javaNodeManager.getJavaNodeFactory()
                                                 .newJarFileNode(jarEntry,
                                                                 null,
                                                                 appContext.getCurrentProject().getProjectConfig(),
                                                                 javaNodeManager.getJavaSettingsProvider().getSettings());

        handleActivateFile(jarFileNode, location.getLineNumber(), callback);
        eventBus.fireEvent(new FileEvent(jarFileNode, OPEN)); //todo we have a bug here!!!!!!!
    }

    private String prepareFQN(String fqn) {//todo maybe move to util class?
        //handle fqn in case nested classes
        if (fqn.contains("$")) {
            return fqn.substring(0, fqn.indexOf("$"));
        }

        //handle fqn in case lambda expression
        if (fqn.contains("$$")) {//todo lyambda maybe $* ? we need check it
            return fqn.substring(0, fqn.indexOf("$$"));
        }
        return fqn;
    }
}
