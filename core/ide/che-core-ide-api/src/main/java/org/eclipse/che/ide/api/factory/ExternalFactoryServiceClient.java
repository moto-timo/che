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
package org.eclipse.che.ide.api.factory;

import org.eclipse.che.api.factory.shared.dto.Factory;
import org.eclipse.che.ide.rest.AsyncRequestCallback;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Client that need to plug external Factory resolver service.
 *
 * @author Florent Benoit
 */
public interface ExternalFactoryServiceClient {

    /**
     * Check if the external client will accept or not the parameters.
     * @param factoryParameters map of URL parameters dedicated to factories
     * @return true if it will be accepted else false
     */
    boolean accept(Map<String, String> factoryParameters);

    /**
     * Get factory object based on user parameters
     *
     * @param factoryParameters
     *         map containing factory data parameters provided through URL
     * @param validate
     *         indicates whether or not factory should be validated by accept validator
     * @param callback
     *         callback which return valid JSON object of factory or exception if occurred
     */
    void getFactory(@NotNull Map<String, String> factoryParameters, boolean validate, @NotNull AsyncRequestCallback<Factory> callback);

}
