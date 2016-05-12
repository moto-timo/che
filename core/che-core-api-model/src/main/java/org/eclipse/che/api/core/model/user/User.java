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
package org.eclipse.che.api.core.model.user;

import org.eclipse.che.commons.annotation.Nullable;

import java.util.List;

/**
 * Defines the user model.
 *
 * @author Yevhenii Voevodin
 */
public interface User {

    /**
     * Returns the identifier of the user (e.g. "user0x124567890").
     * The identifier value is unique and mandatory.
     */
    String getId();

    /**
     * Returns the user's email (e.g. user@codenvy.com).
     * The email is unique, mandatory and updatable.
     */
    String getEmail();

    /**
     * Returns the user's name (e.g. name_example).
     * The name is unique, mandatory and updatable.
     */
    String getName();

    /**
     * Returns the list of the user's aliases(including email and name).
     * That list may contain any values related to the user's identity (e.g. github profile id).
     */
    List<String> getAliases();

    /**
     * Returns the user's password.
     * The returned value may be the password placeholder such as 'none' or
     * even null, depends on the context.
     */
    @Nullable
    String getPassword();
}
