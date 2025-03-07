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
import com.ibm.intoto.attestation.custom.resource.descriptors.file.exceptions.ResourceFileException;
import com.ibm.intoto.attestation.exceptions.DigestCalculationException;
import com.ibm.intoto.attestation.exceptions.FileDoesNotExistException;
import com.ibm.intoto.attestation.exceptions.FileNullException;
import com.ibm.intoto.attestation.exceptions.NotAFileException;
import com.ibm.intoto.attestation.utils.Utils;

/**
 * A ResourceDescriptor type to encapsulate the WAR file created from a Maven project.
 */
public class FileResourceDescriptor extends ResourceDescriptor {

    public FileResourceDescriptor(File packageName) throws ResourceFileException {
        try {
            if (packageName == null) {
                throw new FileNullException();
            }
            if (!packageName.exists()) {
                throw new FileDoesNotExistException(packageName.getAbsolutePath());
            }
            if (!packageName.isFile()) {
                throw new NotAFileException(packageName.getAbsolutePath());
            }
            this.name = packageName.getName();
            calculateDigest(packageName);
        } catch (Exception e) {
            throw new ResourceFileException(e.getMessage());
        }
    }

    private void calculateDigest(File packageName) throws DigestCalculationException {
        String hash = Utils.calculateSha256ForFile(packageName);
        digest.put(DigestSet.ALG_SHA256, hash);
    }

}
