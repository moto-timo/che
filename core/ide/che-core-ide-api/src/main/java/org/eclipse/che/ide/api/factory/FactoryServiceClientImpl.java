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

import com.google.common.base.Joiner;
import com.google.gwt.http.client.RequestBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.factory.shared.dto.Factory;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.MimeType;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.DtoUnmarshallerFactory;
import org.eclipse.che.ide.rest.HTTPHeader;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;
import org.eclipse.che.ide.util.Pair;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {@link FactoryServiceClient} service.
 *
 * @author Vladyslav Zhukovskii
 */
@Singleton
public class FactoryServiceClientImpl implements FactoryServiceClient {
    public static final String API_FACTORY_BASE_URL = "/api/factory/";

    private final AsyncRequestFactory    asyncRequestFactory;
    private final DtoUnmarshallerFactory unmarshallerFactory;
    private final LoaderFactory          loaderFactory;

    @Inject
    public FactoryServiceClientImpl(AsyncRequestFactory asyncRequestFactory,
                                    DtoUnmarshallerFactory unmarshallerFactory,
                                    LoaderFactory loaderFactory) {
        this.asyncRequestFactory = asyncRequestFactory;
        this.unmarshallerFactory = unmarshallerFactory;
        this.loaderFactory = loaderFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getFactory(@NotNull String factoryId, boolean validate, @NotNull AsyncRequestCallback<Factory> callback) {
        StringBuilder url = new StringBuilder(API_FACTORY_BASE_URL).append(factoryId);
        if (validate) {
            url.append("?").append("validate=true");
        }
        asyncRequestFactory.createGetRequest(url.toString()).header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON)
                           .send(callback);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void getFactorySnippet(String factoryId, String type, AsyncRequestCallback<String> callback) {
        final String requestUrl = API_FACTORY_BASE_URL + factoryId + "/snippet?type=" + type;
        asyncRequestFactory.createGetRequest(requestUrl).header(HTTPHeader.ACCEPT, MimeType.TEXT_PLAIN).send(callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getFactoryJson(String workspaceId, String path, AsyncRequestCallback<Factory> callback) {
        final StringBuilder url = new StringBuilder(API_FACTORY_BASE_URL + "workspace/").append(workspaceId);
        if (path != null) {
            url.append("?path=").append(path);
        }
        asyncRequestFactory.createGetRequest(url.toString())
                           .header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON)
                           .loader(loaderFactory.newLoader("Getting info about factory..."))
                           .send(callback);
    }

    @Override
    public Promise<Factory> getFactoryJson(String workspaceId, String path) {
        String url = API_FACTORY_BASE_URL + "workspace/" + workspaceId;
        if (path != null) {
            url += path;
        }

        return asyncRequestFactory.createGetRequest(url)
                                  .header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON)
                                  .loader(loaderFactory.newLoader("Getting info about factory..."))
                                  .send(unmarshallerFactory.newUnmarshaller(Factory.class));
    }

    @Override
    public Promise<Factory> saveFactory(@NotNull Factory factory) {
        return asyncRequestFactory.createPostRequest(API_FACTORY_BASE_URL, factory)
                                  .header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON)
                                  .header(HTTPHeader.CONTENT_TYPE, MimeType.APPLICATION_JSON)
                                  .loader(loaderFactory.newLoader("Creating factory..."))
                                  .send(unmarshallerFactory.newUnmarshaller(Factory.class));
    }

    @Override
    public Promise<List<Factory>> findFactory(@Nullable Integer skipCount,
                                              @Nullable Integer maxItems,
                                              @Nullable List<Pair<String, String>> params) {
        final List<Pair<String, String>> allParams = new LinkedList<>();
        if (params != null) {
            allParams.addAll(params);
        }
        if (maxItems != null) {
            allParams.add(Pair.of("maxItems", maxItems.toString()));
        }
        if (skipCount != null) {
            allParams.add(Pair.of("skipCount", skipCount.toString()));
        }
        return asyncRequestFactory.createGetRequest(API_FACTORY_BASE_URL + "find" + queryString(allParams))
                                  .header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON)
                                  .header(HTTPHeader.CONTENT_TYPE, MimeType.APPLICATION_JSON)
                                  .loader(loaderFactory.newLoader("Searching factory..."))
                                  .send(unmarshallerFactory.newListUnmarshaller(Factory.class));
    }

    @Override
    public Promise<Factory> updateFactory(String id, Factory factory) {
        return asyncRequestFactory.createRequest(RequestBuilder.PUT, API_FACTORY_BASE_URL + id, factory, false)
                                  .header(HTTPHeader.CONTENT_TYPE, MimeType.APPLICATION_JSON)
                                  .loader(loaderFactory.newLoader("Updating factory..."))
                                  .send(unmarshallerFactory.newUnmarshaller(Factory.class));
    }

    /**
     * Forms the query string from collection of query params
     *
     * @param pairs
     *         an iterable collection of query params
     * @return query string
     */
    private static String queryString(Collection<Pair<String, String>> pairs) {
        if (pairs.isEmpty()) {
            return "";
        }
        final Joiner joiner = Joiner.on("&");
        final List<String> params = new LinkedList<>();
        for (Pair<String, String> pair : pairs) {
            params.add(pair.first + '=' + pair.second);
        }
        return '?' + joiner.join(params);
    }
}
