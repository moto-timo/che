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
package org.eclipse.che.plugin.gdb.ide;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import org.eclipse.che.api.debug.shared.model.Location;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.plugin.debugger.ide.debug.AbstractFileHandler;
import org.eclipse.che.ide.part.explorer.project.ProjectExplorerPresenter;

/**
 * Responsible to open files in editor when debugger stopped at breakpoint.
 *
 * @author Anatoliy Bazko
 */
public class GdbDebuggerFileHandler extends AbstractFileHandler {

    private final EditorAgent editorAgent;

    @Inject
    public GdbDebuggerFileHandler(EditorAgent editorAgent,
                                  ProjectExplorerPresenter projectExplorer) {
        super(editorAgent, projectExplorer);
        this.editorAgent = editorAgent;
    }

    @Override
    public void openFile(Location location, AsyncCallback<VirtualFile> callback) {
        EditorPartPresenter activeEditor = editorAgent.getActiveEditor();
        if (activeEditor == null || !activeEditor.getEditorInput().getFile().getPath().equals(location.getTarget())) {
            doOpenFile(location, callback);
        } else {
            scrollEditorToExecutionPoint((TextEditor)activeEditor, location.getLineNumber());
            callback.onSuccess(activeEditor.getEditorInput().getFile());
        }
    }
}
