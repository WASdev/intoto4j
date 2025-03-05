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
package com.ibm.intoto.attestation.custom.resource.descriptors.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.ibm.intoto.attestation.DigestSet;
import com.ibm.intoto.attestation.custom.resource.descriptors.file.exceptions.WarFileException;
import com.ibm.intoto.test.CommonTestUtils;
import com.ibm.intoto.test.Constants;
import jakarta.json.JsonObject;

public class FileResourcesDescriptorTest {

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_constructor_fileNull() {
        final File file = null;
        try {
            new FileResourcesDescriptor(file);
            fail("Should have thrown an exception but didn't.");
        } catch (WarFileException e) {
            // Expected
            testUtils.assertExceptionMatchesPattern(e, "file object is null");
        }
    }

    @Test
    public void test_constructor_fileIsDir() {
        final File file = new File(Constants.RESOURCES_DIR);
        try {
            new FileResourcesDescriptor(file);
            fail("Should have thrown an exception but didn't.");
        } catch (WarFileException e) {
            // Expected
            testUtils.assertExceptionMatchesPattern(e, "artifact is not a file");
        }
    }

    @Test
    public void test_constructor_fileDoesNotExist() {
        final File file = new File(Constants.RESOURCES_DIR + File.separator + "does-not-exist");
        try {
            new FileResourcesDescriptor(file);
            fail("Should have thrown an exception but didn't.");
        } catch (WarFileException e) {
            // Expected
            testUtils.assertExceptionMatchesPattern(e, "file does not exist");
        }
    }

    /**
     * The WarResourceDescriptor class doesn't actually care about format of the file with which it is instantiated.
     */
    @Test
    public void test_constructor_fileNotWar() {
        final File file = new File(Constants.FILE_PATH_SIMPLE_TXT);
        try {
            FileResourcesDescriptor descriptor = new FileResourcesDescriptor(file);

            assertEquals(Constants.FILE_NAME_SIMPLE_TXT, descriptor.getName());

            DigestSet digest = descriptor.getDigest();
            assertNotNull(digest, "Digest set should not have been null but was.");
            JsonObject digestJson = digest.build();
            testUtils.assertJsonContainsOnlyExpectedStringEntry("DigestSet", digestJson, DigestSet.ALG_SHA256, Constants.SHA_FILE_SIMPLT_TXT);

            assertNull(descriptor.getAnnotations(), "Annotations should have been null but were: " + descriptor.getAnnotations());
            assertNull(descriptor.getContent(), "Content should have been null but was: " + descriptor.getContent());
            assertNull(descriptor.getDownloadLocation(), "Download location should have been null but was: " + descriptor.getDownloadLocation());
            assertNull(descriptor.getMediaType(), "Media type should have been null but was: " + descriptor.getMediaType());
            assertNull(descriptor.getUri(), "URI should have been null but was: " + descriptor.getUri());
        } catch (WarFileException e) {
            fail("Caught an unexpected exception: " + e);
        }
    }
}
