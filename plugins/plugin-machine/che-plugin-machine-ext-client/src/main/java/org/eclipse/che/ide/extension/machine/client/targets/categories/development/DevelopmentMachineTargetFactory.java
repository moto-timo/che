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

import org.eclipse.che.ide.extension.machine.client.MachineLocalizationConstant;
import org.eclipse.che.ide.extension.machine.client.targets.Target;
import org.eclipse.che.ide.extension.machine.client.targets.TargetFactory;

import javax.validation.constraints.NotNull;

/**
 * Factory for {@link DevelopmentMachineTarget} instances.
 *
 * @author Oleksii Orel
 */
public class DevelopmentMachineTargetFactory extends TargetFactory {
    private final MachineLocalizationConstant    machineLocale;
    private final DevelopmentView                view;

    protected DevelopmentMachineTargetFactory(@NotNull MachineLocalizationConstant machineLocale,
                                              @NotNull DevelopmentView view) {
        this.machineLocale = machineLocale;
        this.view = view;
    }


    @NotNull
    @Override
    public DevelopmentMachineTarget createTarget(@NotNull String name) {
        final DevelopmentMachineTarget target = new DevelopmentMachineTarget();

        target.setName(name);
        target.setCategory(this.machineLocale.targetsViewCategoryDevelopment());
        target.setView(view);
        target.setDirty(false);

        return target;
    }

    @Override
    public DevelopmentMachineTarget createDefaultTarget() {
        //not implemented
        return null;
    }

    @Override
    public void deleteTarget(Target target) {
      //not implemented
    }

}
