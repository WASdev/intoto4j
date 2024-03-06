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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jakarta.json.JsonArray;

public class SubjectTest {

    @Test
    public void test_noResourceDescriptors() {
        Subject.Builder subjectBuilder = new Subject.Builder();
        Subject subject = subjectBuilder.build();
        assertTrue(subject.toJson().isEmpty(), "Subject should have been empty but was: " + subject.toJson());
    }

    @Test
    public void test_oneResourceDescriptor_empty() throws Exception {
        Subject.Builder subjectBuilder = new Subject.Builder();
        subjectBuilder.resourceDescriptor(new ResourceDescriptor.Builder().build());
        Subject subject = subjectBuilder.build();
        assertTrue(subject.toJson().isEmpty(), "Subject should have been empty but was: " + subject.toJson());
    }

    @Test
    public void test_oneResourceDescriptor() throws Exception {
        Subject.Builder subjectBuilder = new Subject.Builder();
        ResourceDescriptor resourceDescriptor = new ResourceDescriptor.Builder().name("file.war").uri("http://localhost/somewhere/unique").build();
        subjectBuilder.resourceDescriptor(resourceDescriptor);
        Subject subject = subjectBuilder.build();
        JsonArray subjectJson = subject.toJson();
        assertFalse(subjectJson.isEmpty(), "Subject should not have been empty but was.");
        assertEquals(1, subjectJson.size(), "Subject size did not match expected value.");
        assertEquals(resourceDescriptor.toJson(), subjectJson.get(0), "Resource descriptor in subject did not match expected value.");
    }

    @Test
    public void test_multipleResourceDescriptors() throws Exception {
        Subject.Builder subjectBuilder = new Subject.Builder();
        ResourceDescriptor resourceDescriptor1 = new ResourceDescriptor.Builder().name("file.war").uri("http://localhost/somewhere/unique").build();
        ResourceDescriptor resourceDescriptor2 = new ResourceDescriptor.Builder().content("hello, world!").mediaType("text/plain").build();
        ResourceDescriptor resourceDescriptor3 = resourceDescriptor1;
        subjectBuilder.resourceDescriptor(resourceDescriptor1);
        subjectBuilder.resourceDescriptor(resourceDescriptor2);
        subjectBuilder.resourceDescriptor(resourceDescriptor3);
        Subject subject = subjectBuilder.build();
        JsonArray subjectJson = subject.toJson();
        assertFalse(subjectJson.isEmpty(), "Subject should not have been empty but was.");
        assertEquals(3, subjectJson.size(), "Subject size did not match expected value. Subject was: " + subjectJson);
        assertEquals(resourceDescriptor1.toJson(), subjectJson.get(0), "Resource descriptor in subject did not match expected value.");
        assertEquals(resourceDescriptor2.toJson(), subjectJson.get(1), "Resource descriptor in subject did not match expected value.");
        assertEquals(resourceDescriptor3.toJson(), subjectJson.get(2), "Resource descriptor in subject did not match expected value.");
    }

}
