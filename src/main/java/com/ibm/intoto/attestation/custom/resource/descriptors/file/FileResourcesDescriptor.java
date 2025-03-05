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

import java.io.File;

import com.ibm.intoto.attestation.DigestSet;
import com.ibm.intoto.attestation.ResourceDescriptor;
import com.ibm.intoto.attestation.custom.resource.descriptors.file.exceptions.WarFileException;
import com.ibm.intoto.attestation.exceptions.DigestCalculationException;
import com.ibm.intoto.attestation.exceptions.FileDoesNotExistException;
import com.ibm.intoto.attestation.exceptions.FileNullException;
import com.ibm.intoto.attestation.exceptions.NotAFileException;
import com.ibm.intoto.attestation.utils.Utils;

/**
 * A ResourceDescriptor type to encapsulate the WAR file created from a Maven project.
 */
public class FileResourcesDescriptor extends ResourceDescriptor {

    public FileResourcesDescriptor(File war) throws WarFileException {
        try {
            if (war == null) {
                throw new FileNullException();
            }
            if (!war.exists()) {
                throw new FileDoesNotExistException(war.getAbsolutePath());
            }
            if (!war.isFile()) {
                throw new NotAFileException(war.getAbsolutePath());
            }
            this.name = war.getName();
            calculateWarDigest(war);
        } catch (Exception e) {
            throw new WarFileException(e.getMessage());
        }
    }

    private void calculateWarDigest(File war) throws DigestCalculationException {
        String hash = Utils.calculateSha256ForFile(war);
        digest.put(DigestSet.ALG_SHA256, hash);
    }

}
