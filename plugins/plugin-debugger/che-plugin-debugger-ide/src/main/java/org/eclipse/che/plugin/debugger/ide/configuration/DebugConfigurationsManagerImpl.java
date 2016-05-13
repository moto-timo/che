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
package org.eclipse.che.plugin.debugger.ide.configuration;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.debug.DebugConfiguration;
import org.eclipse.che.ide.api.debug.DebugConfigurationType;
import org.eclipse.che.ide.api.debug.DebugConfigurationsManager;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.plugin.debugger.ide.configuration.dto.DebugConfigurationDto;
import org.eclipse.che.ide.util.loging.Log;
import org.eclipse.che.ide.util.storage.LocalStorage;
import org.eclipse.che.ide.util.storage.LocalStorageProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;

/**
 * Implementation of {@link DebugConfigurationsManager}.
 *
 * @author Artem Zatsarynnyi
 */
public class DebugConfigurationsManagerImpl implements DebugConfigurationsManager {

    private static final String LOCAL_STORAGE_DEBUG_CONF_KEY = "che-debug-configurations";

    private final DtoFactory                        dtoFactory;
    private final DebugConfigurationTypeRegistry    configurationTypeRegistry;
    private final Optional<LocalStorage>            localStorageOptional;
    private final Set<ConfigurationChangedListener> configurationChangedListeners;
    private final List<DebugConfiguration>          configurations;

    private DebugConfiguration currentDebugConfiguration;

    @Inject
    public DebugConfigurationsManagerImpl(LocalStorageProvider localStorageProvider,
                                          DtoFactory dtoFactory,
                                          DebugConfigurationTypeRegistry debugConfigurationTypeRegistry) {
        this.dtoFactory = dtoFactory;
        configurationTypeRegistry = debugConfigurationTypeRegistry;
        localStorageOptional = Optional.fromNullable(localStorageProvider.get());
        configurationChangedListeners = new HashSet<>();
        configurations = new ArrayList<>();

        loadConfigurations();
    }

    private void loadConfigurations() {
        for (DebugConfigurationDto descriptor : retrieveConfigurations()) {
            final DebugConfigurationType type = configurationTypeRegistry.getConfigurationTypeById(descriptor.getType());
            // skip configuration if it's type isn't registered
            if (type != null) {
                try {
                    configurations.add(new DebugConfiguration(type,
                                                              descriptor.getName(),
                                                              descriptor.getHost(),
                                                              descriptor.getPort(),
                                                              descriptor.getConnectionProperties()));
                } catch (IllegalArgumentException e) {
                    Log.warn(EditDebugConfigurationsPresenter.class, e.getMessage());
                }
            }
        }
    }

    private List<DebugConfigurationDto> retrieveConfigurations() {
        List<DebugConfigurationDto> configurationsList;

        if (localStorageOptional.isPresent()) {
            final LocalStorage localStorage = localStorageOptional.get();
            final Optional<String> data = Optional.fromNullable(localStorage.getItem(LOCAL_STORAGE_DEBUG_CONF_KEY));
            if (data.isPresent() && !data.get().isEmpty()) {
                configurationsList = dtoFactory.createListDtoFromJson(data.get(), DebugConfigurationDto.class);
            } else {
                configurationsList = emptyList();
            }
        } else {
            configurationsList = emptyList();
        }

        return configurationsList;
    }

    @Override
    public Optional<DebugConfiguration> getCurrentDebugConfiguration() {
        return Optional.fromNullable(currentDebugConfiguration);
    }

    @Override
    public void setCurrentDebugConfiguration(@Nullable DebugConfiguration debugConfiguration) {
        currentDebugConfiguration = debugConfiguration;
    }

    @Override
    public List<DebugConfiguration> getConfigurations() {
        return new ArrayList<>(configurations);
    }

    @Override
    public DebugConfiguration createConfiguration(String typeId,
                                                  String name,
                                                  String host,
                                                  int port,
                                                  Map<String, String> connectionProperties) {
        final DebugConfigurationType configurationType = configurationTypeRegistry.getConfigurationTypeById(typeId);

        final DebugConfiguration configuration = new DebugConfiguration(configurationType,
                                                                        generateUniqueConfigurationName(configurationType, name),
                                                                        host,
                                                                        port,
                                                                        connectionProperties);
        configurations.add(configuration);
        saveConfigurations();
        fireConfigurationAdded(configuration);

        return configuration;
    }

    private String generateUniqueConfigurationName(DebugConfigurationType configurationType, String customName) {
        Set<String> configurationNames = new HashSet<>(configurations.size());
        for (DebugConfiguration configuration : configurations) {
            configurationNames.add(configuration.getName());
        }

        final String configurationName;

        if (customName == null || customName.isEmpty()) {
            configurationName = "Remote " + configurationType.getDisplayName();
        } else {
            if (!configurationNames.contains(customName)) {
                return customName;
            }
            configurationName = customName + " copy";
        }

        if (!configurationNames.contains(configurationName)) {
            return configurationName;
        }

        for (int count = 1; count < 1000; count++) {
            if (!configurationNames.contains(configurationName + "-" + count)) {
                return configurationName + "-" + count;
            }
        }

        return configurationName;
    }

    @Override
    public void removeConfiguration(DebugConfiguration configuration) {
        if (getCurrentDebugConfiguration().isPresent() && getCurrentDebugConfiguration().get().equals(configuration)) {
            setCurrentDebugConfiguration(null);
        }

        configurations.remove(configuration);
        saveConfigurations();
        fireConfigurationRemoved(configuration);
    }

    private void saveConfigurations() {
        if (localStorageOptional.isPresent()) {
            List<DebugConfigurationDto> configurationDtos = new ArrayList<>();

            for (DebugConfiguration configuration : configurations) {
                configurationDtos.add(dtoFactory.createDto(DebugConfigurationDto.class)
                                                .withType(configuration.getType().getId())
                                                .withName(configuration.getName())
                                                .withHost(configuration.getHost())
                                                .withPort(configuration.getPort())
                                                .withConnectionProperties(configuration.getConnectionProperties()));
            }

            localStorageOptional.get().setItem(LOCAL_STORAGE_DEBUG_CONF_KEY, dtoFactory.toJson(configurationDtos));
        }
    }

    @Override
    public void addConfigurationsChangedListener(ConfigurationChangedListener listener) {
        configurationChangedListeners.add(listener);
    }

    @Override
    public void removeConfigurationsChangedListener(ConfigurationChangedListener listener) {
        configurationChangedListeners.remove(listener);
    }

    private void fireConfigurationAdded(DebugConfiguration configuration) {
        for (ConfigurationChangedListener listener : configurationChangedListeners) {
            listener.onConfigurationAdded(configuration);
        }
    }

    private void fireConfigurationRemoved(DebugConfiguration configuration) {
        for (ConfigurationChangedListener listener : configurationChangedListeners) {
            listener.onConfigurationRemoved(configuration);
        }
    }
}
