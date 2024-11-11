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
package com.ibm.intoto.attestation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ibm.intoto.test.CommonTestUtils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ResourceDescriptorTest {

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_builderNull() {
        ResourceDescriptor descriptor = new ResourceDescriptor(null);

        assertNull(descriptor.getAnnotations(), "Annotations should have been null but were: " + descriptor.getAnnotations());
        assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
        assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
        assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
        assertNull(descriptor.getName(), "Name should have been null but was: " + descriptor.getName());
        assertNull(descriptor.getUri(), "URI should have been null but was: " + descriptor.getUri());

        DigestSet digest = descriptor.getDigest();
        assertTrue(digest.build().isEmpty(), "Digest set should have been empty but was: " + digest.build());

        JsonObject descriptorJson = descriptor.toJson();
        assertTrue(descriptorJson.isEmpty(), "Built ResourceDescriptor should have been empty but was: " + descriptorJson);
    }

    @Test
    public void test_builderEmpty() {
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertNull(descriptor.getAnnotations(), "Annotations should have been null but were: " + descriptor.getAnnotations());
        assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
        assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
        assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
        assertNull(descriptor.getName(), "Name should have been null but was: " + descriptor.getName());
        assertNull(descriptor.getUri(), "URI should have been null but was: " + descriptor.getUri());

        DigestSet digest = descriptor.getDigest();
        assertTrue(digest.build().isEmpty(), "Digest set should have been empty but was: " + digest.build());

        JsonObject descriptorJson = descriptor.toJson();
        assertTrue(descriptorJson.isEmpty(), "Built ResourceDescriptor should have been empty but was: " + descriptorJson);
    }

    @Test
    public void test_name() {
        final String expectedValue = "file.war";
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.name(expectedValue);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertDescriptorContainsOnlyExpectedStringEntry(descriptor, ResourceDescriptor.KEY_NAME, expectedValue);
    }

    @Test
    public void test_uri() {
        final String expectedValue = "https://localhost/path/to/file";
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.uri(expectedValue);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertDescriptorContainsOnlyExpectedStringEntry(descriptor, ResourceDescriptor.KEY_URI, expectedValue);
    }

    @Test
    public void test_digest() {
        final DigestSet digest = new DigestSet();
        digest.put(DigestSet.ALG_SHA256, "value1");
        digest.put(DigestSet.GITCOMMIT, "1234567890123456789012345678901234567890");
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.digest(digest);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertDescriptorIsMissingAllEntriesExcept(descriptor, ResourceDescriptor.KEY_DIGEST);
        assertEquals(digest, descriptor.getDigest());
        assertEquals(digest.build(), descriptor.toJson().getJsonObject(ResourceDescriptor.KEY_DIGEST));
    }

    @Test
    public void test_content() {
        final String expectedValue = "some stream of content";
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.content(expectedValue);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertDescriptorContainsOnlyExpectedStringEntry(descriptor, ResourceDescriptor.KEY_CONTENT, expectedValue);
    }

    @Test
    public void test_downloadLocation() {
        final String expectedValue = "https://localhost/some/place/to/download";
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.downloadLocation(expectedValue);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertDescriptorContainsOnlyExpectedStringEntry(descriptor, ResourceDescriptor.KEY_DOWNLOAD_LOCATION, expectedValue);
    }

    @Test
    public void test_mediaType() {
        final String expectedValue = "text/plain";
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.mediaType(expectedValue);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertDescriptorContainsOnlyExpectedStringEntry(descriptor, ResourceDescriptor.KEY_MEDIA_TYPE, expectedValue);
    }

    @Test
    public void test_annotations() {
        final JsonObject expectedValue = Json.createObjectBuilder().add("boolean", true).add("string", "value").build();
        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.annotations(expectedValue);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        assertDescriptorIsMissingAllEntriesExcept(descriptor, ResourceDescriptor.KEY_ANNOTATIONS);
        assertEquals(expectedValue, descriptor.getAnnotations());
        assertEquals(expectedValue, descriptor.toJson().getJsonObject(ResourceDescriptor.KEY_ANNOTATIONS));
    }

    @Test
    public void test_allEntries() {
        final String expectedName = "file.war";
        final String expectedUri = "https://localhost/path/to/file";
        final DigestSet expectedDigest = new DigestSet();
        expectedDigest.put(DigestSet.ALG_SHA256, "value1");
        expectedDigest.put(DigestSet.GITCOMMIT, "1234567890123456789012345678901234567890");
        final String expectedContent = "some stream of content";
        final String expectedDownloadLocation = "https://localhost/some/place/to/download";
        final String expectedMediaType = "text/plain";
        final JsonObject expectedAnnotations = Json.createObjectBuilder().add("boolean", true).add("string", "value").build();

        ResourceDescriptor.Builder builder = new ResourceDescriptor.Builder();
        builder.name(expectedName);
        builder.uri(expectedUri);
        builder.digest(expectedDigest);
        builder.content(expectedContent);
        builder.downloadLocation(expectedDownloadLocation);
        builder.mediaType(expectedMediaType);
        builder.annotations(expectedAnnotations);
        ResourceDescriptor descriptor = new ResourceDescriptor(builder);

        JsonObject descriptorJson = descriptor.toJson();
        testUtils.assertJsonStringEntryMatches("ResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_NAME, expectedName);
        testUtils.assertJsonStringEntryMatches("ResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_URI, expectedUri);
        testUtils.assertJsonStringEntryMatches("ResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_CONTENT, expectedContent);
        testUtils.assertJsonStringEntryMatches("ResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_DOWNLOAD_LOCATION, expectedDownloadLocation);
        testUtils.assertJsonStringEntryMatches("ResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_MEDIA_TYPE, expectedMediaType);
        testUtils.assertJsonEntryMatches("ResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_DIGEST, expectedDigest.build());
        testUtils.assertJsonEntryMatches("ResourceDescriptor", descriptorJson, ResourceDescriptor.KEY_ANNOTATIONS, expectedAnnotations);
    }

    private void assertDescriptorContainsOnlyExpectedStringEntry(ResourceDescriptor descriptor, String keyToExpect, String expectedValue) {
        assertDescriptorIsMissingAllEntriesExcept(descriptor, keyToExpect);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("ResourceDescriptor", descriptor.toJson(), keyToExpect, expectedValue);
    }

    private void assertDescriptorIsMissingAllEntriesExcept(ResourceDescriptor descriptor, String keyToExpect) {
        if (!ResourceDescriptor.KEY_NAME.equals(keyToExpect)) {
            assertNull(descriptor.getName(), "Name should have been null.");
        }
        if (!ResourceDescriptor.KEY_URI.equals(keyToExpect)) {
            assertNull(descriptor.getUri(), "URI should have been null.");
        }
        if (!ResourceDescriptor.KEY_DIGEST.equals(keyToExpect)) {
            DigestSet digest = descriptor.getDigest();
            assertTrue(digest.build().isEmpty(), "Digest set should have been empty but was: " + digest.build());
        }
        if (!ResourceDescriptor.KEY_CONTENT.equals(keyToExpect)) {
            assertNull(descriptor.getContent(), "Content should have been null.");
        }
        if (!ResourceDescriptor.KEY_DOWNLOAD_LOCATION.equals(keyToExpect)) {
            assertNull(descriptor.getDownloadLocation(), "Download location should have been null.");
        }
        if (!ResourceDescriptor.KEY_MEDIA_TYPE.equals(keyToExpect)) {
            assertNull(descriptor.getMediaType(), "Media type should have been null.");
        }
        if (!ResourceDescriptor.KEY_ANNOTATIONS.equals(keyToExpect)) {
            assertNull(descriptor.getAnnotations(), "Annotations should have been null.");
        }
    }

}
