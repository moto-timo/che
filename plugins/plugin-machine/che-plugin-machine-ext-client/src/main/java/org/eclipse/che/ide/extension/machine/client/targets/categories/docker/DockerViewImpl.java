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
package org.eclipse.che.ide.extension.machine.client.targets.categories.docker;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.ui.zeroclipboard.ClipboardButtonBuilder;

/**
 * The implementation of {@link DockerView}.
 *
 * @author Oleksii Orel
 */
@Singleton
public class DockerViewImpl implements DockerView {

    private static final SshViewImplUiBinder UI_BINDER = GWT.create(SshViewImplUiBinder.class);

    private final FlowPanel rootElement;

    private ActionDelegate delegate;

    @UiField
    TextBox targetName;

    @UiField
    TextBox owner;

    @UiField
    TextBox type;

    @UiField
    TextBox sourceType;

    @UiField
    TextBox sourceUrl;


    @Inject
    public DockerViewImpl(ClipboardButtonBuilder buttonBuilder) {
        this.rootElement = UI_BINDER.createAndBindUi(this);
        this.rootElement.setVisible(true);

        buttonBuilder.withResourceWidget(sourceUrl).build();
    }


    @Override
    public Widget asWidget() {
        return rootElement;
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setTargetName(String targetName) {
        this.targetName.setValue(targetName);
    }

    @Override
    public void setOwner(String owner) {
        this.owner.setValue(owner);
    }

    @Override
    public void setType(String type) {
        this.type.setValue(type);
    }

    @Override
    public void setSourceType(String sourceType){
        this.sourceType.setValue(sourceType);
    }

    @Override
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl.setValue(sourceUrl);
    }

    public void updateTargetView(DockerMachineTarget target) {
        this.setTargetName(target.getName());
        this.setOwner(target.getOwner());
        this.setType(target.getType());
        this.setSourceType(target.getSourceType());
        this.setSourceUrl(target.getSourceUrl());
    }

    @Override
    public boolean restoreTargetFields(DockerMachineTarget target) {
        return this.delegate != null && this.delegate.onRestoreTargetFields(target);
    }


    interface SshViewImplUiBinder extends UiBinder<FlowPanel, DockerViewImpl> {
    }
}
