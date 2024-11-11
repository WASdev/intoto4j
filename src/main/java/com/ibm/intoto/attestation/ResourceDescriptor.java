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

import com.ibm.intoto.attestation.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * A size-efficient description of any software artifact or resource (mutable or immutable).
 * <p>
 * See https://github.com/in-toto/attestation/blob/main/spec/v1/resource_descriptor.md.
 */
public class ResourceDescriptor {

    public static final String KEY_NAME = "name";
    public static final String KEY_URI = "uri";
    public static final String KEY_DIGEST = "digest";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_DOWNLOAD_LOCATION = "downloadLocation";
    public static final String KEY_MEDIA_TYPE = "mediaType";
    public static final String KEY_ANNOTATIONS = "annotations";

    /**
     * Machine-readable identifier for distinguishing between descriptors.
     * <p>
     * The semantics are up to the producer and consumer. The name SHOULD be stable, such as a filename, to allow consumers to
     * reliably use the name as part of their policy.
     */
    protected String name;

    /**
     * A URI used to identify the resource or artifact globally. This field is REQUIRED unless either digest or content is set.
     */
    protected String uri;

    /**
     * A set of cryptographic digests of the contents of the resource or artifact. This field is REQUIRED unless either uri or
     * content is set.
     * <p>
     * When known, the producer SHOULD set this field to denote an immutable artifact or resource. The producer and consumer
     * SHOULD agree on acceptable algorithms.
     */
    protected DigestSet digest = new DigestSet();

    /**
     * The contents of the resource or artifact. This field is REQUIRED unless either {@code uri} or {@code digest} is set.
     * <p>
     * The producer MAY use this field in scenarios where including the contents of the resource/artifact directly in the
     * attestation is deemed more efficient for consumers than providing a pointer to another location. To maintain size
     * efficiency, the size of content SHOULD be less than 1KB.
     * <p>
     * The semantics are up to the producer and consumer. The {@code uri} or {@code mediaType} MAY be used by the producer as
     * hints for how consumers should parse content.
     */
    protected String content;

    /**
     * The location of the described resource or artifact, if different from the {@code uri}.
     * <p>
     * To enable automated downloads by consumers, the specified location SHOULD be resolvable.
     */
    protected String downloadLocation;

    /**
     * The MIME Type (i.e., media type) of the described resource or artifact.
     * <p>
     * For resources or artifacts that do not have a standardized MIME type, producers SHOULD follow RFC 6838 (Sections 3.2-3.4)
     * conventions of prefixing types with x., prs., or vnd. to avoid collisions with other producers.
     */
    protected String mediaType;

    /**
     * This field MAY be used to provide additional information or metadata about the resource or artifact that may be useful to
     * the consumer when evaluating the attestation against a policy.
     * <p>
     * For maximum flexibility annotations may be any mapping from a field name to any JSON value (string, number, object, array,
     * boolean or null).
     * <p>
     * The producer and consumer SHOULD agree on the semantics, and acceptable fields and values in the annotations map. Producers
     * SHOULD follow the same naming conventions for annotation fields as for extension fields.
     */
    protected JsonObject annotations;

    protected ResourceDescriptor() {
    }

    protected ResourceDescriptor(Builder builder) {
        if (builder == null) {
            return;
        }
        this.name = builder.name;
        this.uri = builder.uri;
        this.digest = builder.digest;
        this.content = builder.content;
        this.downloadLocation = builder.downloadLocation;
        this.mediaType = builder.mediaType;
        this.annotations = builder.annotations;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public DigestSet getDigest() {
        return digest;
    }

    public String getContent() {
        return content;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public String getMediaType() {
        return mediaType;
    }

    public JsonObject getAnnotations() {
        return annotations;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Utils.addIfNonNullAndNotEmpty(name, KEY_NAME, builder);
        Utils.addIfNonNullAndNotEmpty(uri, KEY_URI, builder);
        Utils.addIfNonNullAndNotEmpty(digest.build(), KEY_DIGEST, builder);
        Utils.addIfNonNullAndNotEmpty(content, KEY_CONTENT, builder);
        Utils.addIfNonNullAndNotEmpty(downloadLocation, KEY_DOWNLOAD_LOCATION, builder);
        Utils.addIfNonNullAndNotEmpty(mediaType, KEY_MEDIA_TYPE, builder);
        Utils.addIfNonNullAndNotEmpty(annotations, KEY_ANNOTATIONS, builder);
        return builder.build();
    }

    public static class Builder {

        protected String name;
        protected String uri;
        protected DigestSet digest = new DigestSet();
        protected String content;
        protected String downloadLocation;
        protected String mediaType;
        protected JsonObject annotations;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder digest(DigestSet digest) {
            this.digest = digest;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder downloadLocation(String downloadLocation) {
            this.downloadLocation = downloadLocation;
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder annotations(JsonObject annotations) {
            this.annotations = annotations;
            return this;
        }

        public ResourceDescriptor build() throws Exception {
            return new ResourceDescriptor(this);
        }
    }

}
