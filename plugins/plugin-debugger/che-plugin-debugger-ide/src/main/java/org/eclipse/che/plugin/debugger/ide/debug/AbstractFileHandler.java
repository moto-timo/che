/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.plugin.debugger.ide.debug;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import org.eclipse.che.api.debug.shared.model.Location;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.document.Document;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.project.node.Node;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.ide.part.explorer.project.ProjectExplorerPresenter;
import org.eclipse.che.ide.project.node.FileReferenceNode;

import static org.eclipse.che.ide.api.project.node.HasStorablePath.StorablePath;

/**
 * @author Anatoliy Bazko
 * @author Alexander Andrienko
 */
public abstract class AbstractFileHandler implements ActiveFileHandler {

    private final EditorAgent              editorAgent;
    private final ProjectExplorerPresenter projectExplorer;

    @Inject
    public AbstractFileHandler(EditorAgent editorAgent, ProjectExplorerPresenter projectExplorer) {
        this.editorAgent = editorAgent;
        this.projectExplorer = projectExplorer;
    }

    //todo javadoc
    public void doOpenFile(final Location location, final AsyncCallback<VirtualFile> callback) {
        projectExplorer.getNodeByPath(new StorablePath(location.getTarget()))
                       .then(new Operation<Node>() {
                           @Override
                           public void apply(final Node node) throws OperationException {
                               if (!(node instanceof FileReferenceNode)) {
                                   return;
                               }
                               handleActivateFile((VirtualFile)node, location.getLineNumber(), callback);
                           }
                       })
                       .catchError(new Operation<PromiseError>() {
                           @Override
                           public void apply(PromiseError error) throws OperationException {
                               callback.onFailure(error.getCause());
                           }
                       });
    }

    public void handleActivateFile(final VirtualFile virtualFile, final int debugLine, final AsyncCallback<VirtualFile> callback) {
        final Timer timer = new Timer() {
            @Override
            public void run() {
                scrollEditorToExecutionPoint((TextEditor)editorAgent.getActiveEditor(), debugLine);
                callback.onSuccess(virtualFile);
            }
        };

        editorAgent.openEditor(virtualFile, new EditorAgent.OpenEditorCallback() {
            @Override
            public void onEditorOpened(EditorPartPresenter editor) {
                timer.schedule(300);
            }

            @Override
            public void onEditorActivated(EditorPartPresenter editor) {
                timer.schedule(300);
            }

            @Override
            public void onInitializationFailed() {
                callback.onFailure(null);
            }
        });
    }

    //todo javadoc
    public void scrollEditorToExecutionPoint(TextEditor editor, int lineNumber) {
        Document document = editor.getDocument();

        if (document != null) {
            TextPosition newPosition = new TextPosition(lineNumber, 0);
            document.setCursorPosition(newPosition);
        }
    }

    //todo maybe some methods should be protected
}
