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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Set of one or more cryptographic digests for a single software artifact or metadata object.
 * <p>
 * See https://github.com/in-toto/attestation/blob/main/spec/v1/digest_set.md.
 */
public class DigestSet {

    public static final String ALG_SHA256 = "sha256";
    public static final String GITCOMMIT = "gitCommit";

    private Map<String, String> sets = new HashMap<>();

    public void put(String algorithm, String digestValue) {
        if (algorithm == null || digestValue == null) {
            return;
        }
        sets.put(algorithm, digestValue);
    }

    public JsonObject build() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Entry<String, String> entry : sets.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sets == null) ? 0 : sets.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DigestSet other = (DigestSet) obj;
        if (sets == null) {
            if (other.sets != null) {
                return false;
            }
        } else if (!sets.equals(other.sets)) {
            return false;
        }
        return true;
    }

}
