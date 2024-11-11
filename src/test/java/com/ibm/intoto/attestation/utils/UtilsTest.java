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
package com.ibm.intoto.attestation.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.ibm.intoto.attestation.exceptions.DigestCalculationException;
import com.ibm.intoto.test.Constants;

public class UtilsTest {

    @Test
    void test_calculateSha256ForFile() {
        final String expected = Constants.SHA_FILE_SIMPLT_TXT;
        try {
            String result = Utils.calculateSha256ForFile(new File(Constants.FILE_PATH_SIMPLE_TXT));
            assertEquals(expected, result);
        } catch (DigestCalculationException e) {
            fail(e);
        }
    }

}
