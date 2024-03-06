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
package io.intoto.attestation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.intoto.test.CommonTestUtils;
import jakarta.json.JsonObject;

public class DigestSetTest {

    private CommonTestUtils testUtils = new CommonTestUtils();

    DigestSet set = new DigestSet();

    @BeforeEach
    public void beforeEach() {
        set = new DigestSet();
    }

    @Test
    public void test_put_nullAlgorithm() {
        String algorithm = null;
        String digest = "1234567890123456789012345678901234567890";
        set.put(algorithm, digest);

        JsonObject result = set.build();
        assertTrue(result.isEmpty(), "Result should have been empty but wasn't. Result was: " + result);
    }

    @Test
    public void test_put_emptyAlgorithm() {
        String algorithm = "";
        String digest = "1234567890123456789012345678901234567890";
        set.put(algorithm, digest);

        JsonObject result = set.build();
        testUtils.assertJsonContainsOnlyExpectedStringEntry("DigestSet", result, algorithm, digest);
    }

    @Test
    public void test_put_nullDigest() {
        String algorithm = "RS256";
        String digest = null;
        set.put(algorithm, digest);

        JsonObject result = set.build();
        assertTrue(result.isEmpty(), "Result should have been empty but wasn't. Result was: " + result);
    }

    @Test
    public void test_put_emptyDigest() {
        String algorithm = "ES512";
        String digest = "";
        set.put(algorithm, digest);

        JsonObject result = set.build();
        testUtils.assertJsonContainsOnlyExpectedStringEntry("DigestSet", result, algorithm, digest);
    }

    @Test
    public void test_put_goldenPath() {
        String algorithm = "ES512";
        String digest = "1234567890123456789012345678901234567890";
        set.put(algorithm, digest);

        JsonObject result = set.build();
        testUtils.assertJsonContainsOnlyExpectedStringEntry("DigestSet", result, algorithm, digest);
    }

    @Test
    public void test_equals_bothEmpty() {
        DigestSet set1 = new DigestSet();
        DigestSet set2 = new DigestSet();
        assertEquals(set1, set2);
    }

    @Test
    public void test_equals_oneEmpty() {
        DigestSet set1 = new DigestSet();
        set1.put(DigestSet.ALG_SHA256, "sha");
        DigestSet set2 = new DigestSet();
        assertNotEquals(set1, set2);
    }

    @Test
    public void test_equals_nonEmptyNotEqual() {
        DigestSet set1 = new DigestSet();
        set1.put(DigestSet.ALG_SHA256, "sha");
        DigestSet set2 = new DigestSet();
        set2.put(DigestSet.ALG_SHA256, "othersha");
        assertNotEquals(set1, set2);
    }

    @Test
    public void test_equals_nonEmptyEqual() {
        DigestSet set1 = new DigestSet();
        set1.put(DigestSet.ALG_SHA256, "sha");
        set1.put(DigestSet.GITCOMMIT, "1234567890123456789012345678901234567890");
        DigestSet set2 = new DigestSet();
        set2.put(DigestSet.ALG_SHA256, "sha");
        set2.put(DigestSet.GITCOMMIT, "1234567890123456789012345678901234567890");
        assertEquals(set1, set2);
    }

}
