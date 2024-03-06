/*
 * Copyright 2023 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package io.intoto.attestation.custom.resource.descriptors.git;

import io.intoto.attestation.ResourceDescriptor;
import io.intoto.attestation.utils.GitUtils;
import io.intoto.attestation.utils.exceptions.GitRepoUrlException;

/**
 * A ResourceDescriptor type to encapsulate the Git repository that stores a Maven project.
 */
public class GitRepositoryResourceDescriptor extends ResourceDescriptor {

    private String gitRepoUrl = null;
    private String ref = null;

    private GitRepositoryResourceDescriptor(Builder builder) {
        super(builder);
        this.gitRepoUrl = builder.gitRepoUrl;
        this.ref = builder.ref;
    }

    public String getGitRepoUrl() {
        return gitRepoUrl;
    }

    public String getRef() {
        return ref;
    }

    public static class Builder extends ResourceDescriptor.Builder {

        private String gitRepoUrl = null;
        private String ref = null;

        public Builder(String gitRepoUrl) throws GitRepoUrlException {
            this.gitRepoUrl = gitRepoUrl;
            this.uri = GitUtils.buildGitHubRepoUrl(gitRepoUrl);
        }

        public Builder ref(String ref) {
            this.ref = ref;
            return this;
        }

        public GitRepositoryResourceDescriptor build() {
            return new GitRepositoryResourceDescriptor(this);
        }
    }

}
