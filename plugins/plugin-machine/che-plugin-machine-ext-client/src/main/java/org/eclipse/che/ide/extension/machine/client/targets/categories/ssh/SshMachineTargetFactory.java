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
package org.eclipse.che.ide.extension.machine.client.targets.categories.ssh;

import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.targets.Target;
import org.eclipse.che.ide.extension.machine.client.targets.TargetFactory;

import javax.validation.constraints.NotNull;

/**
 * Factory for {@link SshMachineTarget} instances.
 *
 * @author Oleksii Orel
 */
public class SshMachineTargetFactory extends TargetFactory {
    private final MachineLocalizationConstant machineLocale;
    private final SshView.ActionDelegate delegate;
    private final SshView view;

    protected SshMachineTargetFactory(@NotNull MachineLocalizationConstant machineLocale,
                                      @NotNull SshView view,
                                      @NotNull SshView.ActionDelegate delegate) {
        this.machineLocale = machineLocale;
        this.delegate = delegate;
        this.view = view;
    }


    @NotNull
    @Override
    public SshMachineTarget createTarget(@NotNull String name) {
        final SshMachineTarget target = new SshMachineTarget();

        target.setName(name);
        target.setCategory(this.machineLocale.targetsViewCategorySsh());
        target.setView(view);
        target.setDirty(false);

        return target;
    }

    @Override
    public SshMachineTarget createDefaultTarget() {
        final SshMachineTarget target = new SshMachineTarget();

        target.setName("new_target");
        target.setCategory(this.machineLocale.targetsViewCategorySsh());
        target.setView(view);
        target.setHost("");
        target.setPort("22");
        target.setDirty(true);
        target.setUserName("root");
        target.setPassword("");
        target.setConnected(false);

        return target;
    }

    @Override
    public void deleteTarget(Target target) {
        this.delegate.onDeleteTarget(target);
    }

}
