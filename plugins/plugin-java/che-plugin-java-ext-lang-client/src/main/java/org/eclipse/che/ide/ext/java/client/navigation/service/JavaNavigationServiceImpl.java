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
package org.eclipse.che.ide.ext.java.client.navigation.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.callback.AsyncPromiseHelper;
import org.eclipse.che.ide.MimeType;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.ext.java.shared.Jar;
import org.eclipse.che.ide.ext.java.shared.JarEntry;
import org.eclipse.che.ide.ext.java.shared.OpenDeclarationDescriptor;
import org.eclipse.che.ide.ext.java.shared.dto.ClassContent;
import org.eclipse.che.ide.ext.java.shared.dto.ImplementationsDescriptorDTO;
import org.eclipse.che.ide.ext.java.shared.dto.model.CompilationUnit;
import org.eclipse.che.ide.ext.java.shared.dto.model.JavaProject;
import org.eclipse.che.ide.ext.java.shared.dto.model.MethodParameters;
import org.eclipse.che.ide.resource.Path;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.DtoUnmarshallerFactory;
import org.eclipse.che.ide.rest.StringUnmarshaller;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;

import java.util.List;

import static org.eclipse.che.api.promises.client.callback.PromiseHelper.newCallback;
import static org.eclipse.che.api.promises.client.callback.PromiseHelper.newPromise;
import static org.eclipse.che.ide.MimeType.APPLICATION_JSON;
import static org.eclipse.che.ide.rest.HTTPHeader.ACCEPT;

/**
 * @author Evgen Vidolob
 */
@Singleton
public class JavaNavigationServiceImpl implements JavaNavigationService {

    private final AppContext             appContext;
    private final LoaderFactory          loaderFactory;
    private final AsyncRequestFactory    requestFactory;
    private final String                 workspaceId;
    private final DtoUnmarshallerFactory unmarshallerFactory;

    @Inject
    public JavaNavigationServiceImpl(AppContext appContext,
                                     LoaderFactory loaderFactory,
                                     DtoUnmarshallerFactory unmarshallerFactory,
                                     AsyncRequestFactory asyncRequestFactory) {
        this.appContext = appContext;
        this.loaderFactory = loaderFactory;
        this.requestFactory = asyncRequestFactory;
        this.workspaceId = appContext.getDevMachine().getId();
        this.unmarshallerFactory = unmarshallerFactory;
    }

    @Override
    public void findDeclaration(String projectPath, String fqn, int offset, AsyncRequestCallback<OpenDeclarationDescriptor> callback) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/find-declaration" +
                     "?projectpath=" + projectPath + "&fqn=" + fqn + "&offset=" + offset;
        requestFactory.createGetRequest(url).send(callback);
    }

    @Override
    public Promise<OpenDeclarationDescriptor> findDeclaration(Path project, String fqn, int offset) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/find-declaration?projectpath=" + project + "&fqn=" + fqn +
                     "&offset=" + offset;
        return requestFactory.createGetRequest(url).send(unmarshallerFactory.newUnmarshaller(OpenDeclarationDescriptor.class));
    }

    public void getExternalLibraries(String projectPath, AsyncRequestCallback<List<Jar>> callback) {
        String url =
                appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/libraries?projectpath=" + projectPath;
        requestFactory.createGetRequest(url).send(callback);
    }

    @Override
    public Promise<List<Jar>> getExternalLibraries(Path project) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/libraries?projectpath=" + project.toString();

        return requestFactory.createGetRequest(url).send(unmarshallerFactory.newListUnmarshaller(Jar.class));
    }

    @Override
    public void getLibraryChildren(String projectPath, int libId, AsyncRequestCallback<List<JarEntry>> callback) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/lib/children" +
                     "?projectpath=" + projectPath + "&root=" + libId;
        requestFactory.createGetRequest(url).send(callback);
    }

    @Override
    public Promise<List<JarEntry>> getLibraryChildren(Path project, int libId) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/lib/children?projectpath=" + project.toString() + "&root=" + libId;

        return requestFactory.createGetRequest(url).send(unmarshallerFactory.newListUnmarshaller(JarEntry.class));
    }

    @Override
    public void getChildren(String projectPath, int libId, String path, AsyncRequestCallback<List<JarEntry>> callback) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/children" +
                     "?projectpath=" + projectPath + "&root=" + libId + "&path=" + path;
        requestFactory.createGetRequest(url).send(callback);
    }

    @Override
    public Promise<List<JarEntry>> getChildren(Path project, int libId, Path path) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/children?projectpath=" + project + "&root=" + libId +
                     "&path=" + path;
        return requestFactory.createGetRequest(url).send(unmarshallerFactory.newListUnmarshaller(JarEntry.class));
    }

    @Override
    public void getEntry(String projectPath, int libId, String path, AsyncRequestCallback<JarEntry> callback) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/entry" +
                     "?projectpath=" + projectPath + "&root=" + libId + "&path=" + path;
        requestFactory.createGetRequest(url).send(callback);
    }

    @Override
    public Promise<JarEntry> getEntry(Path project, int libId, String path) {
        String url =
                appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/entry?projectpath=" + project + "&root=" + libId + "&path=" + path;
        return requestFactory.createGetRequest(url).send(unmarshallerFactory.newUnmarshaller(JarEntry.class));
    }

    @Override
    public void getContent(String projectPath, int libId, String path, AsyncRequestCallback<ClassContent> callback) {
        String url = getContentUrl(projectPath, libId, path);

        requestFactory.createGetRequest(url).send(callback);
    }

    @Override
    public Promise<String> getContent(Path project, int libId, Path path) {
        String url = getContentUrl(project.toString(), libId, path.toString());

        return requestFactory.createGetRequest(url).send(new StringUnmarshaller());
    }

    @Override
    public void getContent(String projectPath, String fqn, AsyncRequestCallback<ClassContent> callback) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/contentbyfqn?projectpath=" + projectPath + "&fqn=" + fqn;
        requestFactory.createGetRequest(url).send(callback);
    }

    @Override
    public Promise<String> getContent(Path project, String fqn) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/contentbyfqn?projectpath=" + project.toString() + "&fqn=" + fqn;

        return requestFactory.createGetRequest(url).send(new StringUnmarshaller());
    }

    /** {@inheritDoc} */
    @Override
    public Promise<CompilationUnit> getCompilationUnit(Path project, String fqn, boolean showInherited) {
        final String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/compilation-unit?projectpath=" + project + "&fqn=" + fqn +
                           "&showinherited=" + showInherited;

        return requestFactory.createGetRequest(url)
                             .header(ACCEPT, APPLICATION_JSON)
                             .send(unmarshallerFactory.newUnmarshaller(CompilationUnit.class));
    }

    @Override
    public Promise<ImplementationsDescriptorDTO> getImplementations(Path project, String fqn, int offset) {
        final String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/implementations?projectpath=" + project + "&fqn=" + fqn +
                           "&offset=" + offset;

        return requestFactory.createGetRequest(url)
                             .header(ACCEPT, APPLICATION_JSON)
                             .loader(loaderFactory.newLoader())
                             .send(unmarshallerFactory.newUnmarshaller(ImplementationsDescriptorDTO.class));
    }

    @Override
    public Promise<List<JavaProject>> getProjectsAndPackages(boolean includePackage) {
        final String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId +
                           "/navigation/get/projects/and/packages"
                           + "?includepackages=" + includePackage;

        return newPromise(new AsyncPromiseHelper.RequestCall<List<JavaProject>>() {
            @Override
            public void makeCall(AsyncCallback<List<JavaProject>> callback) {

                requestFactory.createGetRequest(url)
                              .header(ACCEPT, APPLICATION_JSON)
                              .loader(loaderFactory.newLoader())
                              .send(newCallback(callback, unmarshallerFactory.newListUnmarshaller(JavaProject.class)));
            }
        });
    }

    @Override
    public String getContentUrl(String projectPath, int libId, String path) {
        return appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/content" +
               "?projectpath=" + projectPath + "&root=" + libId + "&path=" + path;
    }

    @Override
    public Promise<List<MethodParameters>> getMethodParametersHints(String projectPath, String fqn, int offset, int lineStartOffset) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/parameters" +
                     "?projectpath=" + projectPath + "&fqn=" + fqn + "&offset=" + offset + "&lineStart=" + lineStartOffset;

        return requestFactory.createGetRequest(url)
                             .header(ACCEPT, MimeType.APPLICATION_JSON)
                             .loader(loaderFactory.newLoader("Getting parameters..."))
                             .send(unmarshallerFactory.newListUnmarshaller(MethodParameters.class));
    }

    @Override
    public Promise<List<MethodParameters>> getMethodParametersHints(Path project, String fqn, int offset, int lineStartOffset) {
        String url = appContext.getDevMachine().getWsAgentBaseUrl() + "/jdt/" + workspaceId + "/navigation/parameters" +
                     "?projectpath=" + project + "&fqn=" + fqn + "&offset=" + offset + "&lineStart=" + lineStartOffset;

        return requestFactory.createGetRequest(url)
                             .header(ACCEPT, MimeType.APPLICATION_JSON)
                             .loader(loaderFactory.newLoader("Getting parameters..."))
                             .send(unmarshallerFactory.newListUnmarshaller(MethodParameters.class));
    }
}
