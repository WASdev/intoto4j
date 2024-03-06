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
package io.intoto.attestation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;

import io.intoto.attestation.utils.exceptions.GitRepoUrlException;
import io.intoto.attestation.utils.exceptions.GitRepoUrlFormatException;
import io.intoto.attestation.utils.exceptions.GitRepoUrlNullOrEmptyException;

public class GitUtilsTest {

    private final String user = "brutus";
    private final String repoName = "hello-world";

    @Test
    void test_buildGitHubRepoUrl_null() {
        final String input = null;
        try {
            String result = GitUtils.buildGitHubRepoUrl(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_buildGitHubRepoUrl_empty() {
        final String input = "";
        try {
            String result = GitUtils.buildGitHubRepoUrl(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_buildGitHubRepoUrl_notUrl() {
        final String input = "The quick brown fox jumps over the lazy dog.";
        try {
            String result = GitUtils.buildGitHubRepoUrl(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlFormatException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_buildGitHubRepoUrl_validGitRepo() {
        final String input = "git@github.com:" + user + "/" + repoName + ".git";
        try {
            String result = GitUtils.buildGitHubRepoUrl(input);
            assertEquals("https://github.com/" + user + "/" + repoName, result);
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_buildGitHubRepoUrl_validHttpsGitRepo() {
        final String input = "https://github.com/" + user + "/" + repoName + ".git";
        try {
            String result = GitUtils.buildGitHubRepoUrl(input);
            assertEquals("https://github.com/" + user + "/" + repoName, result);
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoOwner_null() {
        final String input = null;
        try {
            String result = GitUtils.getRepoOwner(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwner_empty() {
        final String input = "";
        try {
            String result = GitUtils.getRepoOwner(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwner_notUrl() {
        final String input = "The quick brown fox jumps over the lazy dog.";
        try {
            String result = GitUtils.getRepoOwner(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlFormatException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwner_validGitRepo() {
        final String input = "git@github.com:" + user + "/" + repoName + ".git";
        try {
            String result = GitUtils.getRepoOwner(input);
            assertEquals(user, result);
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoOwner_validHttpsRepo() {
        final String input = "https://github.com/" + user + "/" + repoName + ".git";
        try {
            String result = GitUtils.getRepoOwner(input);
            assertEquals(user, result);
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoName_null() {
        final String input = null;
        try {
            String result = GitUtils.getRepoName(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoName_empty() {
        final String input = "";
        try {
            String result = GitUtils.getRepoName(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoName_notUrl() {
        final String input = "The quick brown fox jumps over the lazy dog.";
        try {
            String result = GitUtils.getRepoName(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlFormatException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoName_validGitRepo() {
        final String input = "git@github.com:" + user + "/" + repoName + ".git";
        try {
            String result = GitUtils.getRepoName(input);
            assertEquals(repoName, result);
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoName_validHttpsRepo() {
        final String input = "https://github.com/" + user + "/" + repoName + ".git";
        try {
            String result = GitUtils.getRepoName(input);
            assertEquals(repoName, result);
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_null() {
        final String input = null;
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_empty() {
        final String input = "";
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_notUrl() {
        final String input = "The quick brown fox jumps over the lazy dog.";
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlFormatException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_gitRepoSubstring() {
        // Missing the ".git" suffix
        final String input = "git@github.com:" + user + "/" + repoName;
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlFormatException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_validGitRepo() {
        final String input = "git@github.com:" + user + "/" + repoName + ".git";
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            assertEquals(user, result.group(GitUtils.REGEX_GROUP_NAME_OWNER), "Did not successfully extract the repo owner.");
            assertEquals(repoName, result.group(GitUtils.REGEX_GROUP_NAME_REPO_NAME), "Did not successfully extract the repo name.");
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_httpsRepoSubstring() {
        // Missing the ".git" suffix
        final String input = "https://github.com/" + user + "/" + repoName;
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            fail("Should have thrown an exception, but got [" + result + "].");
        } catch (GitRepoUrlFormatException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_validHttpRepo() {
        final String input = "http://github.com/" + user + "/" + repoName + ".git";
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            assertEquals(user, result.group(GitUtils.REGEX_GROUP_NAME_OWNER), "Did not successfully extract the repo owner.");
            assertEquals(repoName, result.group(GitUtils.REGEX_GROUP_NAME_REPO_NAME), "Did not successfully extract the repo name.");
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_validHttpWWWRepo() {
        final String input = "http://www.github.com/" + user + "/" + repoName + ".git";
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            assertEquals(user, result.group(GitUtils.REGEX_GROUP_NAME_OWNER), "Did not successfully extract the repo owner.");
            assertEquals(repoName, result.group(GitUtils.REGEX_GROUP_NAME_REPO_NAME), "Did not successfully extract the repo name.");
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

    @Test
    void test_getRepoOwnerAndNameMatcher_validHttpsRepo() {
        final String input = "https://github.com/" + user + "/" + repoName + ".git";
        try {
            Matcher result = GitUtils.getRepoOwnerAndNameMatcher(input);
            assertEquals(user, result.group(GitUtils.REGEX_GROUP_NAME_OWNER), "Did not successfully extract the repo owner.");
            assertEquals(repoName, result.group(GitUtils.REGEX_GROUP_NAME_REPO_NAME), "Did not successfully extract the repo name.");
        } catch (GitRepoUrlException e) {
            fail("Should not have thrown an exception, but got: " + e);
        }
    }

}
