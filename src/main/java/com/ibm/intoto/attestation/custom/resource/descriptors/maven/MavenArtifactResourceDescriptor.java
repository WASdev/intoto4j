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
package com.ibm.intoto.attestation.custom.resource.descriptors.maven;

import org.apache.maven.model.Dependency;

import com.ibm.intoto.attestation.ResourceDescriptor;
import com.ibm.intoto.attestation.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

/**
 * A ResourceDescriptor type to encapsulate a Maven artifact.
 */
public class MavenArtifactResourceDescriptor extends ResourceDescriptor {

    public static final String KEY_ANNOTATION_TYPE = "type";
    public static final String KEY_ANNOTATION_SCOPE = "scope";

    // TODO
    public static final String URI_FORMAT = "https://central.sonatype.com/artifact/%s/%s/%s";

    private String groupId = null;
    private String artifactId = null;
    private String version = null;
    private String type = null;
    private String scope = null;

    public MavenArtifactResourceDescriptor(Dependency artifact) {
        this.groupId = artifact.getGroupId();
        this.artifactId = artifact.getArtifactId();
        this.version = artifact.getVersion();
        this.type = artifact.getType();
        this.scope = artifact.getScope();
        this.name = groupId + ":" + artifactId + ":" + version;
        this.uri = String.format(URI_FORMAT, groupId, artifactId, version);
        setAnnotations();
    }

    private void setAnnotations() {
        JsonObjectBuilder annotationsBuilder = Json.createObjectBuilder();
        Utils.addIfNonNullAndNotEmpty(type, "type", annotationsBuilder);
        Utils.addIfNonNullAndNotEmpty(scope, "scope", annotationsBuilder);
        annotations = annotationsBuilder.build();
    }

}
