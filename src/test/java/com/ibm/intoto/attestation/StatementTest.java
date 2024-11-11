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

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.ibm.intoto.attestation.exceptions.StatementValueNullException;
import com.ibm.intoto.test.CommonTestUtils;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class StatementTest {

    private String type = Statement.TYPE_IN_TOTO_STATEMENT;
    private String predicateType = "https://localhost/statement/v1";

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_nullType() throws Exception {
        Subject subject = getBasicSubject();
        try {
            new Statement.Builder(null, subject, predicateType);
            fail("Should have thrown an exception, but didn't.");
        } catch (StatementValueNullException e) {
            testUtils.assertExceptionMatchesPattern(e, Statement.KEY_TYPE);
        }
    }

    @Test
    public void test_nullSubject() throws Exception {
        try {
            new Statement.Builder(type, null, predicateType);
            fail("Should have thrown an exception, but didn't.");
        } catch (StatementValueNullException e) {
            testUtils.assertExceptionMatchesPattern(e, Statement.KEY_SUBJECT);
        }
    }

    @Test
    public void test_nullPredicateType() throws Exception {
        Subject subject = getBasicSubject();
        try {
            new Statement.Builder(type, subject, null);
            fail("Should have thrown an exception, but didn't.");
        } catch (StatementValueNullException e) {
            testUtils.assertExceptionMatchesPattern(e, Statement.KEY_PREDICATE_TYPE);
        }
    }

    @Test
    public void test_minimalStatement() throws Exception {
        Subject subject = getBasicSubject();
        try {
            Statement statement = new Statement.Builder(type, subject, predicateType).build();

            JsonObject statementJson = statement.toJson();
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_TYPE, type);
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_PREDICATE_TYPE, predicateType);
            testUtils.assertJsonEntryMatches("Statement", statementJson, Statement.KEY_SUBJECT, subject.toJson());
            testUtils.assertJsonDoesNotContainKey("Statement", statementJson, Statement.KEY_PREDICATE);
        } catch (Exception e) {
            fail("Should not have thrown an exception but did: " + e);
        }
    }

    @Test
    public void test_predicateNullParameters() throws Exception {
        Subject subject = getBasicSubject();
        TestPredicate predicate = new TestPredicate(null);
        try {
            Statement.Builder statementBuilder = new Statement.Builder(type, subject, predicateType);
            statementBuilder.predicate(predicate);

            JsonObject statementJson = statementBuilder.build().toJson();
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_TYPE, type);
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_PREDICATE_TYPE, predicateType);
            testUtils.assertJsonEntryMatches("Statement", statementJson, Statement.KEY_SUBJECT, subject.toJson());
            testUtils.assertJsonDoesNotContainKey("Statement", statementJson, Statement.KEY_PREDICATE);
        } catch (Exception e) {
            fail("Should not have thrown an exception but did: " + e);
        }
    }

    @Test
    public void test_predicateEmptyParameters() throws Exception {
        Subject subject = getBasicSubject();
        TestPredicate predicate = new TestPredicate(JsonObject.EMPTY_JSON_OBJECT);
        try {
            Statement.Builder statementBuilder = new Statement.Builder(type, subject, predicateType);
            statementBuilder.predicate(predicate);

            JsonObject statementJson = statementBuilder.build().toJson();
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_TYPE, type);
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_PREDICATE_TYPE, predicateType);
            testUtils.assertJsonEntryMatches("Statement", statementJson, Statement.KEY_SUBJECT, subject.toJson());
            testUtils.assertJsonDoesNotContainKey("Statement", statementJson, Statement.KEY_PREDICATE);
        } catch (Exception e) {
            fail("Should not have thrown an exception but did: " + e);
        }
    }

    @Test
    public void test_predicateWithParameters() throws Exception {
        Subject subject = getBasicSubject();
        JsonObject predicateParameters = Json.createObjectBuilder().add("special-key", "special value").build();
        TestPredicate predicate = new TestPredicate(predicateParameters);
        try {
            Statement.Builder statementBuilder = new Statement.Builder(type, subject, TestPredicate.PREDICATE_TYPE);
            statementBuilder.predicate(predicate);

            JsonObject statementJson = statementBuilder.build().toJson();
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_TYPE, type);
            testUtils.assertJsonStringEntryMatches("Statement", statementJson, Statement.KEY_PREDICATE_TYPE, TestPredicate.PREDICATE_TYPE);
            testUtils.assertJsonEntryMatches("Statement", statementJson, Statement.KEY_SUBJECT, subject.toJson());
            testUtils.assertJsonEntryMatches("Statement", statementJson, Statement.KEY_PREDICATE, predicateParameters);
        } catch (Exception e) {
            fail("Should not have thrown an exception but did: " + e);
        }
    }

    private Subject getBasicSubject() throws Exception {
        Subject.Builder subjectBuilder = new Subject.Builder();
        ResourceDescriptor resourceDescriptor = new ResourceDescriptor.Builder().name("file.war").build();
        subjectBuilder.resourceDescriptor(resourceDescriptor);
        return subjectBuilder.build();
    }

}
