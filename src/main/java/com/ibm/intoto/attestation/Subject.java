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

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;

/**
 * Set of software artifacts that the attestation applies to. Each element represents a single software artifact. Each element
 * MUST have digest set.
 * <p>
 * The {@code name} field may be used as an identifier to distinguish this artifact from others within the subject. Similarly,
 * other {@code ResourceDescriptor} fields may be used as required by the context. The semantics are up to the producer and
 * consumer and they MAY use them when evaluating policy. If the name is not meaningful, leave the field unset or use {@code "_"}.
 * For example, a SLSA Provenance attestation might use the {@code name} to specify output filename, expecting the consumer to
 * only consider entries with a particular name. Alternatively, a vulnerability scan attestation might leave {@code name} unset
 * because the results apply regardless of what the artifact is named.
 * <p>
 * If set, {@code name} and {@code uri} SHOULD be unique within subject.
 * <p>
 * See https://github.com/in-toto/attestation/blob/main/spec/v1/statement.md.
 */
public class Subject {

    private JsonArray resourceDescriptors;

    private Subject(Builder builder) {
        this.resourceDescriptors = builder.resourceDescriptors.build();
    }

    public JsonArray toJson() {
        return resourceDescriptors;
    }

    public static class Builder {

        private JsonArrayBuilder resourceDescriptors = Json.createArrayBuilder();

        public Builder resourceDescriptor(ResourceDescriptor resourceDescriptor) {
            if (!resourceDescriptor.toJson().isEmpty()) {
                resourceDescriptors.add(resourceDescriptor.toJson());
            }
            return this;
        }

        public Subject build() {
            return new Subject(this);
        }
    }

}
