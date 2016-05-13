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
package org.eclipse.che.api.debug.shared.model.impl;

import org.eclipse.che.api.debug.shared.model.Location;

/**
 * @author Anatoliy Bazko
 */
public class LocationImpl implements Location {
    private final String target;
    private final int    lineNumber;
    private final boolean binary;
    private final int libId;
    private final String projectPath;

    public LocationImpl(String target, int lineNumber, boolean binary, int libId, String projectPath) {
        this.target = target;
        this.lineNumber = lineNumber;
        this.binary = binary;
        this.libId = libId;
        this.projectPath = projectPath;
    }

    public LocationImpl(String target) {
        this(target, 0, false, -1, null);
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public boolean isBinary() {
        return binary;
    }

    @Override
    public int getLibId() {
        return 0;
    }

    @Override
    public String getProjectPath() {
        return projectPath;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof LocationImpl)) return false;
//
//        LocationImpl location = (LocationImpl)o;
//
//        if (lineNumber != location.lineNumber) return false;
//        return !(target != null ? !target.equals(location.target) : location.target != null);
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = target != null ? target.hashCode() : 0;
//        result = 31 * result + lineNumber;
//        return result;
//    }

//todo rework equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationImpl location = (LocationImpl)o;

        if (lineNumber != location.lineNumber) return false;
        if (binary != location.binary) return false;
        if (libId != location.libId) return false;
        if (target != null ? !target.equals(location.target) : location.target != null) return false;
        return projectPath != null ? projectPath.equals(location.projectPath) : location.projectPath == null;

    }

    @Override
    public int hashCode() {
        int result = target != null ? target.hashCode() : 0;
        result = 31 * result + lineNumber;
        result = 31 * result + (binary ? 1 : 0);
        result = 31 * result + libId;
        result = 31 * result + (projectPath != null ? projectPath.hashCode() : 0);
        return result;
    }
}
