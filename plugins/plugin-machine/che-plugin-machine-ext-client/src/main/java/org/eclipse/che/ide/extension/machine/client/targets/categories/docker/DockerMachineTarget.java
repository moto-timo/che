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

import org.eclipse.che.api.machine.shared.dto.recipe.RecipeDescriptor;
import org.eclipse.che.ide.extension.machine.client.targets.Target;

import java.util.Objects;

/**
 * The implementation of {@link Target}.
 *
 * @author Oleksii Orel
 */
public class DockerMachineTarget implements Target {

    private String name;

    private String category;

    private RecipeDescriptor recipe;

    private String type;

    private String owner;

    private String sourceType;

    private String sourceUrl;


    /**
     * Indicate if target has unsaved changes.
     */
    private boolean dirty;

    private boolean connected;

    private DockerView view;

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
    public RecipeDescriptor getRecipe() {
        return recipe;
    }

    @Override
    public void setRecipe(RecipeDescriptor recipe) {
        this.recipe = recipe;
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
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean onRestore() {
        return view.restoreTargetFields(this);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    @Override
    public void onSelect() {
        view.updateTargetView(this);
    }

    /**
     * Sets Docker view.
     *
     * @param view
     */
    public void setView(DockerView view) {
        this.view = view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DockerMachineTarget)) {
            return false;
        }

        DockerMachineTarget other = (DockerMachineTarget)o;

        return Objects.equals(getName(), other.getName())
               && Objects.equals(getCategory(), other.getCategory())
               && Objects.equals(getRecipe(), other.getRecipe())
               && Objects.equals(getType(), other.getType())
               && Objects.equals(getOwner(), other.getOwner())
               && Objects.equals(getSourceType(), other.getSourceType())
               && Objects.equals(getSourceUrl(), other.getSourceUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCategory(), getRecipe(), getType(), getOwner(), getSourceType(), getSourceUrl());
    }
}
