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
package org.eclipse.che.plugin.jdb.server.utils;

import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.debug.shared.model.impl.LocationImpl;
import org.eclipse.che.api.debugger.server.exceptions.DebuggerException;
import org.eclipse.che.api.debugger.shared.dto.Location;
import org.eclipse.che.dto.server.DtoFactory;
import org.eclipse.che.ide.ext.java.shared.OpenDeclarationDescriptor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaModel;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.che.api.debug.shared.model.Location;

/**
 * Java DebuggerUtil
 *
 * @author Alexander Andrienko
 */
public class JavaDebuggerUtils {
    private static final JavaModel JAVA_MODEL = JavaModelManager.getJavaModelManager().getJavaModel();

    public static Location getLocation(String projectPath, com.sun.jdi.Location location) throws DebuggerException {
        String fqn = location.declaringType().name();
        IJavaProject project = JAVA_MODEL.getJavaProject(projectPath);

        if (project == null) {
            throw new DebuggerException("Project by path: " + projectPath + " was not found");
        }

        try {
            IType type = project.findType(fqn);
        } catch (JavaModelException e) {
            throw  new DebuggerException(""); //todo need message
        }

        if (type == null) {//create todo util method follow
            throw new DebuggerException("Type with fully qualified name: " + fqn + " was not found");//todo need think about it
        }

        if (type.isBinary()) {
            IClassFile classFile = type.getClassFile();
            int libId = classFile.getAncestor(IPackageFragmentRoot.PACKAGE_FRAGMENT_ROOT).hashCode();
            return new LocationImpl(classFile.getType().getFullyQualifiedName(), location.lineNumber(), true, libId, projectPath);//todo we have a problem with projectPath!!!!!
        } else {
            ICompilationUnit compilationUnit = type.getCompilationUnit();
            return new LocationImpl(compilationUnit.getPath().toOSString(), location.lineNumber(), false, projectPath);
        }
    }
}
