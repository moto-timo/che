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
package org.eclipse.che.ide.extension.machine.client.targets.categories.development;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import org.eclipse.che.api.machine.shared.dto.MachineDto;
import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.targets.CategoryPage;
import org.eclipse.che.ide.extension.machine.client.targets.TargetFactory;
import org.eclipse.che.ide.extension.machine.client.targets.TargetsView;

/**
 * Development type page presenter.
 *
 * @author Oleksii Orel
 */
public class DevelopmentCategoryPresenter implements CategoryPage, DevelopmentView.ActionDelegate {
    private final DevelopmentView                 developmentView;
    private final MachineLocalizationConstant     machineLocale;
    private final DevelopmentMachineTargetFactory targetFactory;

    private TargetsView.ActionDelegate delegate;

    @Inject
    public DevelopmentCategoryPresenter(DevelopmentView developmentView,
                                        MachineLocalizationConstant machineLocale) {
        this.developmentView = developmentView;
        this.machineLocale = machineLocale;

        this.targetFactory = new DevelopmentMachineTargetFactory(machineLocale, developmentView);

        developmentView.setDelegate(this);
    }

    @Override
    public void setDelegate(TargetsView.ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getCategory() {
        return this.machineLocale.targetsViewCategoryDevelopment();
    }

    @Override
    public TargetFactory getTargetFactory() {
        return this.targetFactory;
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(developmentView);
    }

    private MachineDto getMachineByName(String machineName) {
        return this.delegate != null ? this.delegate.getMachineByName(machineName) : null;
    }


    @Override
    public boolean onRestoreTargetFields(DevelopmentMachineTarget target) {
        if (target == null) {
            return false;
        }

        final MachineDto machine = this.getMachineByName(target.getName());
        if (machine == null) {
            return false;
        }

        target.setOwner(machine.getOwner());
        target.setType(machine.getConfig().getType());
        target.setSourceType(machine.getConfig().getSource().getType());
        target.setSourceUrl(machine.getConfig().getSource().getLocation());

        return true;
    }
}
