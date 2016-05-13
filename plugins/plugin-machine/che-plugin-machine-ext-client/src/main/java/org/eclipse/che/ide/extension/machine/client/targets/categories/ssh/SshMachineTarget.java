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

import org.eclipse.che.api.machine.shared.dto.recipe.RecipeDescriptor;
import org.eclipse.che.ide.extension.machine.client.targets.Target;

import java.util.Objects;

/**
 * The implementation of {@link Target}.
 *
 * @author Vitaliy Guliy
 * @author Oleksii Orel
 */
public class SshMachineTarget implements Target {

    private String name;

    private String category;

    private String host;

    private String port;

    private String userName;

    private String password;

    private RecipeDescriptor recipe;

    /**
     * Indicate if target has unsaved changes.
     */
    private boolean dirty;

    private boolean connected;

    private SshView view;

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

    @Override
    public void onSelect() {
        view.updateTargetView(this);
    }

    /** Returns ssh host. */
    public String getHost() {
        return host;
    }


    /**
     * Sets SSH host.
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /** Returns ssh port. */
    public String getPort() {
        return port;
    }

    /**
     * Sets SSH port.
     *
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /** Returns user name. */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets SSH userName.
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Returns user password. */
    public String getPassword() {
        return password;
    }

    /**
     * Sets SSH password.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets SSH view.
     *
     * @param view
     */
    public void setView(SshView view) {
        this.view = view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SshMachineTarget)) {
            return false;
        }

        SshMachineTarget other = (SshMachineTarget)o;

        return Objects.equals(getName(), other.getName())
               && Objects.equals(getCategory(), other.getCategory())
               && Objects.equals(getRecipe(), other.getRecipe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCategory(), getRecipe());
    }

}
