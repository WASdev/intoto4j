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
package com.ibm.intoto.attestation.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.intoto.attestation.utils.exceptions.GitRepoUrlException;
import com.ibm.intoto.attestation.utils.exceptions.GitRepoUrlFormatException;
import com.ibm.intoto.attestation.utils.exceptions.GitRepoUrlNullOrEmptyException;

public class GitUtils {

    public static final String REGEX_GROUP_NAME_OWNER = "owner";
    public static final String REGEX_GROUP_NAME_REPO_NAME = "repo";

    /**
     * Regex Pattern for extracting the owner and repositiory name from a Git remote URL in the format git@github.com:<owner>/<repo-name>.git
     */
    public static final Pattern GIT_REPO_OWNER_AND_NAME_PATTERN = Pattern.compile("^git@github.(ibm.)?com:(?<" + REGEX_GROUP_NAME_OWNER + ">[^\\/]+)\\/(?<" + REGEX_GROUP_NAME_REPO_NAME + ">.+).git$", Pattern.CASE_INSENSITIVE);

    /**
     * Regex Pattern for extracting the owner and repositiory name from a Git remote URL in the format https://github.com/<owner>/<repo-name>.git
     */
    public static final Pattern GIT_REPO_OWNER_AND_NAME_PATTERN_HTTP = Pattern.compile("^https?://(?:[^@]+@)?(www.)?github.(ibm.)?com/(?<" + REGEX_GROUP_NAME_OWNER + ">[^\\/]+)\\/(?<" + REGEX_GROUP_NAME_REPO_NAME + ">.+).git$", Pattern.CASE_INSENSITIVE);

    public static final String GITHUB_REPO_URL_FORMAT = "https://github.com/%s/%s";

    public static String buildGitHubRepoUrl(String gitRemoteOriginUrl) throws GitRepoUrlException {
        String owner = getRepoOwner(gitRemoteOriginUrl);
        String repoName = getRepoName(gitRemoteOriginUrl);
        return String.format(GITHUB_REPO_URL_FORMAT, owner, repoName);
    }

    public static String getRepoOwner(String gitRemoteOriginUrl) throws GitRepoUrlException {
        if (gitRemoteOriginUrl == null || gitRemoteOriginUrl.isEmpty()) {
            throw new GitRepoUrlNullOrEmptyException();
        }
        Matcher matcher = getRepoOwnerAndNameMatcher(gitRemoteOriginUrl);
        return matcher.group(REGEX_GROUP_NAME_OWNER);
    }

    public static String getRepoName(String gitRemoteOriginUrl) throws GitRepoUrlException {
        if (gitRemoteOriginUrl == null || gitRemoteOriginUrl.isEmpty()) {
            throw new GitRepoUrlNullOrEmptyException();
        }
        Matcher matcher = getRepoOwnerAndNameMatcher(gitRemoteOriginUrl);
        return matcher.group(REGEX_GROUP_NAME_REPO_NAME);
    }

    public static Matcher getRepoOwnerAndNameMatcher(String gitRemoteOriginUrl) throws GitRepoUrlException {
        if (gitRemoteOriginUrl == null || gitRemoteOriginUrl.isEmpty()) {
            throw new GitRepoUrlNullOrEmptyException();
        }
        Matcher repoNameMatcher = GIT_REPO_OWNER_AND_NAME_PATTERN.matcher(gitRemoteOriginUrl);
        if (!repoNameMatcher.matches()) {
            repoNameMatcher = GIT_REPO_OWNER_AND_NAME_PATTERN_HTTP.matcher(gitRemoteOriginUrl);
            if (!repoNameMatcher.matches()) {
                throw new GitRepoUrlFormatException(gitRemoteOriginUrl);
            }
        }
        return repoNameMatcher;
    }

    public static List<String> getExpectedUrlPatterns() {
        return List.of(GIT_REPO_OWNER_AND_NAME_PATTERN.toString(), GIT_REPO_OWNER_AND_NAME_PATTERN_HTTP.toString());
    }

}
