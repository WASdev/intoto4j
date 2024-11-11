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

import org.junit.jupiter.api.Test;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class PredicateTest {

    @Test
    public void test_nullParameters() throws Exception {
        TestPredicate predicate = new TestPredicate(null);
        assertEquals(TestPredicate.PREDICATE_TYPE, predicate.getPredicateType(), "Predicate type did not match.");
        assertNull(predicate.getPredicateParameters(), "Predicate parameters were expected to be null but weren't.");
    }

    @Test
    public void test_emptyParameters() throws Exception {
        TestPredicate predicate = new TestPredicate(JsonObject.EMPTY_JSON_OBJECT);
        assertEquals(TestPredicate.PREDICATE_TYPE, predicate.getPredicateType(), "Predicate type did not match.");
        assertEquals(JsonObject.EMPTY_JSON_OBJECT, predicate.getPredicateParameters(), "Predicate parameters did not match.");
    }

    @Test
    public void test_nonEmptyParameters() throws Exception {
        JsonObject predicateParameters = Json.createObjectBuilder().add("special-key", "special value").build();
        TestPredicate predicate = new TestPredicate(predicateParameters);
        assertEquals(TestPredicate.PREDICATE_TYPE, predicate.getPredicateType(), "Predicate type did not match.");
        assertEquals(predicateParameters, predicate.getPredicateParameters(), "Predicate parameters did not match.");
    }

}
