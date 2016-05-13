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

import org.eclipse.che.api.machine.shared.dto.recipe.RecipeDescriptor;

/**
 * Default wrapper for the machine.
 *
 * @author Oleksii Orel
 */
public class TargetDefaultImpl implements Target {

    private String name;
    private String category;
    private boolean connected;

    @Override
    public RecipeDescriptor getRecipe() {
        return null;
    }

    @Override
    public void setRecipe(RecipeDescriptor recipe) {
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    @Override
    public void setDirty(boolean dirty) {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void onSelect() {
    }

    @Override
    public boolean onRestore() {
        return true;
    }
}
