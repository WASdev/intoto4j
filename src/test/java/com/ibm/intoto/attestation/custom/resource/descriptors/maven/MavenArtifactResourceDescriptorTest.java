/*
 * Copyright 2023, 2025 International Business Machines Corp.
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
package com.ibm.intoto.attestation.custom.resource.descriptors.maven;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.maven.model.Dependency;
import org.junit.jupiter.api.Test;

import com.ibm.intoto.attestation.DigestSet;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class MavenArtifactResourceDescriptorTest {

    // TODO

    @Test
    public void test_scopeTest() {
        final String groupId = "com.example";
        final String artifactId = "code-api";
        final String version = "1.0.0";
        final String scope = "test";
        final String type = "jar";
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependency.setScope(scope);
        dependency.setType(type);

        MavenArtifactResourceDescriptor descriptor = new MavenArtifactResourceDescriptor(dependency);
        assertDescriptorMatchesDependency(descriptor, groupId, artifactId, version, scope, type);
    }

    @Test
    public void test_scopeProvided() {
        final String groupId = "com.example";
        final String artifactId = "code-api";
        final String version = "1.0.0";
        final String scope = "provided";
        final String type = "jar";
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependency.setScope(scope);
        dependency.setType(type);

        MavenArtifactResourceDescriptor descriptor = new MavenArtifactResourceDescriptor(dependency);
        assertDescriptorMatchesDependency(descriptor, groupId, artifactId, version, scope, type);
    }

    private void assertDescriptorMatchesDependency(MavenArtifactResourceDescriptor descriptor, String groupId, String artifactId, String version, String scope, String type) {
        String expectedName = groupId + ":" + artifactId + ":" + version;
        assertEquals(expectedName, descriptor.getName(), "Name did not match the expected value.");
        String expectedUri = String.format(MavenArtifactResourceDescriptor.URI_FORMAT, groupId.replace(".","/"), artifactId, version);
        assertEquals(expectedUri, descriptor.getUri(), "URI did not match the expected value.");

        DigestSet digest = descriptor.getDigest();
        assertNotNull(digest, "Digest set should not have been null but was.");
        JsonObject digestJson = digest.build();
        assertTrue(digestJson.isEmpty(), "Digest set should have been empty but was: " + digestJson);

        JsonObjectBuilder annotationsBuilder = Json.createObjectBuilder();
        annotationsBuilder.add(MavenArtifactResourceDescriptor.KEY_ANNOTATION_SCOPE, scope);
        annotationsBuilder.add(MavenArtifactResourceDescriptor.KEY_ANNOTATION_TYPE, type);
        JsonObject expectedAnnotations = annotationsBuilder.build();
        assertEquals(expectedAnnotations, descriptor.getAnnotations(), "Annotations did not match the expected value.");

        assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
        assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
        assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
    }
    
    // Testing that the MavenArtifactResourceDescriptor() object initialization is not causing an exception
    // even if the Dependency object or its parameters are null. This is an edge case which is not expected 
    // in practice. Test is added to ensure bad paths do not cause unexpected exceptions.
    @Test
    public void test_emptyDependency() {
        final Dependency dependency = new Dependency();
        MavenArtifactResourceDescriptor descriptor = new MavenArtifactResourceDescriptor(dependency);
        assertNotNull(descriptor, "Object should not have been null but was.");
        
        DigestSet digest = descriptor.getDigest();
        assertNotNull(digest, "Digest set should not have been null but was.");
        JsonObject digestJson = digest.build();
        assertTrue(digestJson.isEmpty(), "Digest set should have been empty but was: " + digestJson);
      
        assertNull(descriptor.getUri(), "URI should have been null but was: " + descriptor.getUri());
        assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
        assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
        assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
    }

}
