/*
 * Copyright (c) 2015-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 */
'use strict';

import {gitMixinId, subversionMixinId} from '../repository/project-repository-data';

export class ProjectRepositoryCtrl {

  /**
   * Controller for the project local repository and remote repositories details
   * @ngInject for Dependency injection
   * @author Oleksii Orel
   */
  constructor($route, cheAPI, lodash) {
    this.cheAPI = cheAPI;
    this.lodash = lodash;

    this.remoteGitRepositories = [];
    this.localGitRepository = null;
    this.remoteSvnRepository = null;
    this.isEmptyState = false;

    var workspaceId = $route.current.params.workspaceId;
    var projectPath = '/' + $route.current.params.projectName;

    this.wsagent = this.cheAPI.getWorkspace().getWorkspaceAgent(workspaceId);

    if (!this.wsagent.getProject().getProjectDetailsByKey(workspaceId, projectPath)) {
      let promise = this.wsagent.getProject().fetchProjectDetails(workspaceId, projectPath);

      promise.then(() => {
        var projectDetails = this.wsagent.getProject().getProjectDetailsByKey(workspaceId, projectPath);
        this.updateRepositories(projectDetails);
      });
    } else {
      var projectDetails = this.wsagent.getProject().getProjectDetailsByKey(workspaceId, projectPath);
      this.updateRepositories(projectDetails);
    }

  }

  updateRepositories(projectDetails) {
    if (!projectDetails.mixins || !projectDetails.mixins.length) {
      this.isEmptyState = true;
      return;
    }

    if (projectDetails.mixins.indexOf(subversionMixinId) !== -1) {
      //update remote svn url
      if (!this.cheAPI.getSvn().getRemoteUrlByKey(projectDetails.workspaceId, projectDetails.path)) {
        let promise = this.cheAPI.getSvn().fetchRemoteUrl(projectDetails.workspaceId, projectDetails.path);

        promise.then(() => {
          this.remoteSvnRepository = this.cheAPI.getSvn().getRemoteUrlByKey(projectDetails.workspaceId, projectDetails.path);
        });
      } else {
        this.remoteSvnRepository = this.cheAPI.getSvn().getRemoteUrlByKey(projectDetails.workspaceId, projectDetails.path);
      }
    }

    if (projectDetails.mixins.indexOf(gitMixinId) !== -1) {
      //update git local url
      if (!this.wsagent.getGit().getLocalUrlByKey(projectDetails.workspaceId, projectDetails.path)) {
        let promise = this.wsagent.getGit().fetchLocalUrl(projectDetails.workspaceId, projectDetails.path);

        promise.then(() => {
          this.localGitRepository = this.wsagent.getGit().getLocalUrlByKey(projectDetails.workspaceId, projectDetails.path);
        });
      } else {
        this.localGitRepository = this.wsagent.getGit().getLocalUrlByKey(projectDetails.workspaceId, projectDetails.path);
      }

      //update git remote urls
      if (!this.wsagent.getGit().getRemoteUrlArrayByKey(projectDetails.workspaceId, projectDetails.path)) {
        let promise = this.wsagent.getGit().fetchRemoteUrlArray(projectDetails.workspaceId, projectDetails.path);

        promise.then(() => {
          this.remoteGitRepositories = this.wsagent.getGit().getRemoteUrlArrayByKey(projectDetails.workspaceId, projectDetails.path);
        });
      } else {
        this.remoteGitRepositories = this.wsagent.getGit().getRemoteUrlArrayByKey(projectDetails.workspaceId, projectDetails.path);
      }
    }

  }

}
