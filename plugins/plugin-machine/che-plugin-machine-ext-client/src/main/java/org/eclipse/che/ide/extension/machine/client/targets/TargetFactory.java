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
package org.eclipse.che.ide.extension.machine.client.targets;

import org.eclipse.che.api.machine.shared.dto.CommandDto;

import javax.validation.constraints.NotNull;

/**
 * Factory for {@link Target} instances.
 *
 * @author Oleksii Orel
 */
public abstract class TargetFactory {

    /**
     * Creates a new command configuration based on the given {@link CommandDto}.
     *
     * @param name
     */
    public abstract Target createTarget(@NotNull String name);

    public abstract Target createDefaultTarget();

    public abstract void deleteTarget(Target target);

}
