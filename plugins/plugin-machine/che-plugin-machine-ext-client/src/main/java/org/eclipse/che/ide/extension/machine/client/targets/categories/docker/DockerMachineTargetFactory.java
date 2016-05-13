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

import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.targets.Target;
import org.eclipse.che.ide.extension.machine.client.targets.TargetFactory;

import javax.validation.constraints.NotNull;

/**
 * Factory for {@link DockerMachineTarget} instances.
 *
 * @author Oleksii Orel
 */
public class DockerMachineTargetFactory extends TargetFactory {
    private final MachineLocalizationConstant machineLocale;
    private final DockerView.ActionDelegate delegate;
    private final DockerView view;

    protected DockerMachineTargetFactory(@NotNull MachineLocalizationConstant machineLocale,
                                         @NotNull DockerView view,
                                         @NotNull DockerView.ActionDelegate delegate) {
        this.machineLocale = machineLocale;
        this.delegate = delegate;
        this.view = view;
    }


    @NotNull
    @Override
    public DockerMachineTarget createTarget(@NotNull String name) {
        final DockerMachineTarget target = new DockerMachineTarget();

        target.setName(name);
        target.setCategory(this.machineLocale.targetsViewCategoryDocker());
        target.setView(view);
        target.setDirty(false);

        return target;
    }

    @Override
    public DockerMachineTarget createDefaultTarget() {
        final DockerMachineTarget target = new DockerMachineTarget();

        target.setName("new_target");
        target.setCategory(this.machineLocale.targetsViewCategoryDocker());
        target.setView(view);

        return target;
    }

    @Override
    public void deleteTarget(Target target) {
        this.delegate.onDeleteTarget(target);
    }

}
