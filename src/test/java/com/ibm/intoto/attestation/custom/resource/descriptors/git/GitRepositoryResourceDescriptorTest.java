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
package com.ibm.intoto.attestation.custom.resource.descriptors.git;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.ibm.intoto.attestation.DigestSet;
import com.ibm.intoto.attestation.ResourceDescriptor;
import com.ibm.intoto.attestation.utils.exceptions.GitRepoUrlException;
import com.ibm.intoto.attestation.utils.exceptions.GitRepoUrlFormatException;
import com.ibm.intoto.attestation.utils.exceptions.GitRepoUrlNullOrEmptyException;
import com.ibm.intoto.test.CommonTestUtils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class GitRepositoryResourceDescriptorTest {

    private final String user = "brutus";
    private final String repoName = "hello-world";

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_repoUrlNull() {
        final String repoUrl = null;
        try {
            new GitRepositoryResourceDescriptor.Builder(repoUrl);
            fail("Should have thrown an exception but didn't.");
        } catch (GitRepoUrlNullOrEmptyException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_repoUrlNotValid() {
        final String repoUrl = "not a url";
        try {
            new GitRepositoryResourceDescriptor.Builder(repoUrl);
            fail("Should have thrown an exception but didn't.");
        } catch (GitRepoUrlFormatException e) {
            // Expected
        } catch (GitRepoUrlException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_repoUrlValid() {
        final String repoUrl = "git@github.com:" + user + "/" + repoName + ".git";
        final String expectedGitRepoUri = "https://github.com/" + user + "/" + repoName;
        try {
            GitRepositoryResourceDescriptor.Builder builder = new GitRepositoryResourceDescriptor.Builder(repoUrl);
            GitRepositoryResourceDescriptor descriptor = builder.build();

            assertEquals(repoUrl, descriptor.getGitRepoUrl(), "Git repo URL did not match expected value.");
            assertNull(descriptor.getRef(), "Ref should have been null but was: " + descriptor.getRef());
            assertEquals(expectedGitRepoUri, descriptor.getUri(), "URI did not match the expected value.");

            DigestSet digest = descriptor.getDigest();
            assertNotNull(digest, "Digest set should not have been null but was.");
            JsonObject digestJson = digest.build();
            assertTrue(digestJson.isEmpty(), "Digest set should have been empty but was: " + digestJson);

            assertNull(descriptor.getName(), "Name should have been null but was: " + descriptor.getName());
            assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
            assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
            assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
            assertNull(descriptor.getAnnotations(), "Annotations should have been null but were: " + descriptor.getAnnotations());

            JsonObject descriptorJson = descriptor.toJson();
            testUtils.assertJsonContainsOnlyExpectedStringEntry("GitRepositoryResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_URI, expectedGitRepoUri);
        } catch (Exception e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_ref() {
        final String repoUrl = "git@github.com:" + user + "/" + repoName + ".git";
        final String expectedGitRepoUri = "https://github.com/" + user + "/" + repoName;
        final String ref = "refs/heads/main";
        try {
            GitRepositoryResourceDescriptor.Builder builder = new GitRepositoryResourceDescriptor.Builder(repoUrl);
            builder.ref(ref);
            GitRepositoryResourceDescriptor descriptor = builder.build();

            assertEquals(repoUrl, descriptor.getGitRepoUrl(), "Git repo URL did not match expected value.");
            assertEquals(ref, descriptor.getRef(), "Ref did not match the expected value.");
            assertEquals(expectedGitRepoUri, descriptor.getUri(), "URI did not match the expected value.");

            DigestSet digest = descriptor.getDigest();
            assertNotNull(digest, "Digest set should not have been null but was.");
            JsonObject digestJson = digest.build();
            assertTrue(digestJson.isEmpty(), "Digest set should have been empty but was: " + digestJson);

            assertNull(descriptor.getName(), "Name should have been null but was: " + descriptor.getName());
            assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
            assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
            assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
            assertNull(descriptor.getAnnotations(), "Annotations should have been null but were: " + descriptor.getAnnotations());

            JsonObject descriptorJson = descriptor.toJson();
            testUtils.assertJsonContainsOnlyExpectedStringEntry("GitRepositoryResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_URI, expectedGitRepoUri);
        } catch (Exception e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_allEntries() {
        final String repoUrl = "git@github.com:" + user + "/" + repoName + ".git";
        final String ref = "refs/heads/main";

        final String expectedName = "file.war";
        final String expectedUri = "https://localhost/path/to/file";
        final DigestSet expectedDigest = new DigestSet();
        expectedDigest.put(DigestSet.GITCOMMIT, "1234567890123456789012345678901234567890");
        final String expectedContent = "some stream of content";
        final String expectedDownloadLocation = "https://localhost/some/place/to/download";
        final String expectedMediaType = "text/plain";
        final JsonObject expectedAnnotations = Json.createObjectBuilder().add("boolean", true).add("string", "value").build();

        try {
            GitRepositoryResourceDescriptor.Builder builder = new GitRepositoryResourceDescriptor.Builder(repoUrl);
            builder.ref(ref);
            builder.name(expectedName);
            builder.uri(expectedUri);
            builder.digest(expectedDigest);
            builder.content(expectedContent);
            builder.downloadLocation(expectedDownloadLocation);
            builder.mediaType(expectedMediaType);
            builder.annotations(expectedAnnotations);
            GitRepositoryResourceDescriptor descriptor = builder.build();

            // ref and gitRepoUrl are not actually added to the built JSON object
            JsonObject descriptorJson = descriptor.toJson();
            assertEquals(repoUrl, descriptor.getGitRepoUrl(), "Git repo URL did not match expected value.");
            assertFalse(descriptorJson.toString().contains(repoUrl), "JSON should not have included \"" + repoUrl + "\" but did. JSON data was: " + descriptorJson);
            assertEquals(ref, descriptor.getRef(), "Ref did not match the expected value.");
            assertFalse(descriptorJson.toString().contains(ref), "JSON should not have included \"" + ref + "\" but did. JSON data was: " + descriptorJson);

            testUtils.assertJsonStringEntryMatches("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_NAME, expectedName);
            testUtils.assertJsonStringEntryMatches("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_URI, expectedUri);
            testUtils.assertJsonStringEntryMatches("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_CONTENT, expectedContent);
            testUtils.assertJsonStringEntryMatches("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_DOWNLOAD_LOCATION, expectedDownloadLocation);
            testUtils.assertJsonStringEntryMatches("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_MEDIA_TYPE, expectedMediaType);
            testUtils.assertJsonEntryMatches("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_DIGEST, expectedDigest.build());
            testUtils.assertJsonEntryMatches("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_ANNOTATIONS, expectedAnnotations);
        } catch (Exception e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_refWithHttpGitRepoUrlUsingUsernameAndToken() {
        final String repoUrl = "https://gitclientuser:ghd_AZTjAqHeRgitclienttokenS86VDZCA2ptGcA@github.com/" + user + "/" + repoName +".git";
        final String expectedGitRepoUri = "https://github.com/" + user + "/" + repoName;
        final String ref = "refs/heads/main";
        try {
            GitRepositoryResourceDescriptor.Builder builder = new GitRepositoryResourceDescriptor.Builder(repoUrl);
            builder.ref(ref);
            GitRepositoryResourceDescriptor descriptor = builder.build();

            assertEquals(repoUrl, descriptor.getGitRepoUrl(), "Git repo URL did not match expected value.");
            assertEquals(ref, descriptor.getRef(), "Ref did not match the expected value.");
            assertEquals(expectedGitRepoUri, descriptor.getUri(), "URI did not match the expected value.");

            DigestSet digest = descriptor.getDigest();
            assertNotNull(digest, "Digest set should not have been null but was.");
            JsonObject digestJson = digest.build();
            assertTrue(digestJson.isEmpty(), "Digest set should have been empty but was: " + digestJson);

            assertNull(descriptor.getName(), "Name should have been null but was: " + descriptor.getName());
            assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
            assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
            assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
            assertNull(descriptor.getAnnotations(), "Annotations should have been null but were: " + descriptor.getAnnotations());

            JsonObject descriptorJson = descriptor.toJson();
            testUtils.assertJsonContainsOnlyExpectedStringEntry("GitRepositoryResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_URI, expectedGitRepoUri);
        } catch (Exception e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_refWithHttpsGitRepoUrl() {
        final String repoUrl = "https://github.com/" + user + "/" + repoName +".git";
        final String expectedGitRepoUri = "https://github.com/" + user + "/" + repoName;
        final String ref = "refs/heads/main";
        try {
            GitRepositoryResourceDescriptor.Builder builder = new GitRepositoryResourceDescriptor.Builder(repoUrl);
            builder.ref(ref);
            GitRepositoryResourceDescriptor descriptor = builder.build();

            assertEquals(repoUrl, descriptor.getGitRepoUrl(), "Git repo URL did not match expected value.");
            assertEquals(ref, descriptor.getRef(), "Ref did not match the expected value.");
            assertEquals(expectedGitRepoUri, descriptor.getUri(), "URI did not match the expected value.");
        } catch (Exception e) {
            fail("Encountered unexpected exception: " + e);
        }
    }
    @Test
    public void test_refWithHttpIbmGitRepoUrl() {
        final String repoUrl = "http://github.ibm.com/" + user + "/" + repoName +".git";
        final String expectedGitRepoUri = "https://github.com/" + user + "/" + repoName;
        final String ref = "refs/heads/main";
        try {
            GitRepositoryResourceDescriptor.Builder builder = new GitRepositoryResourceDescriptor.Builder(repoUrl);
            builder.ref(ref);
            GitRepositoryResourceDescriptor descriptor = builder.build();

            assertEquals(repoUrl, descriptor.getGitRepoUrl(), "Git repo URL did not match expected value.");
            assertEquals(ref, descriptor.getRef(), "Ref did not match the expected value.");
            assertEquals(expectedGitRepoUri, descriptor.getUri(), "URI did not match the expected value.");
        } catch (Exception e) {
            fail("Encountered unexpected exception: " + e);
        }
    }
    

}
