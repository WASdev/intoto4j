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

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Map;

import io.intoto.attestation.exceptions.DigestCalculationException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Utils {

    public static void addIfNonNullAndNotEmpty(String entry, String key, JsonObjectBuilder builder) {
        if (entry != null && !entry.isEmpty()) {
            builder.add(key, entry);
        }
    }

    public static void addIfNonNullAndNotEmpty(JsonObject entry, String key, JsonObjectBuilder builder) {
        if (entry != null && !entry.isEmpty()) {
            builder.add(key, entry);
        }
    }

    public static void addIfNonNullAndNotEmpty(JsonArray entry, String key, JsonObjectBuilder builder) {
        if (entry != null && !entry.isEmpty()) {
            builder.add(key, entry);
        }
    }

    public static void addIfNonNullAndNotEmpty(Map entry, String key, JsonObjectBuilder builder) {
        if (entry != null && !entry.isEmpty()) {
            JsonObjectBuilder entryBuilder = Json.createObjectBuilder(entry);
            builder.add(key, entryBuilder.build());
        }
    }

    public static String calculateSha256ForFile(File file) throws DigestCalculationException {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            byte[] fileHashBytes = MessageDigest.getInstance("SHA-256").digest(fileBytes);
            return new BigInteger(1, fileHashBytes).toString(16);
        } catch (Exception e) {
            throw new DigestCalculationException(file, e);
        }
    }

}
