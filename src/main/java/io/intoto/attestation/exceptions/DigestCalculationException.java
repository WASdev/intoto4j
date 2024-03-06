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
package io.intoto.attestation.exceptions;

import java.io.File;

public class DigestCalculationException extends Exception {

    private static final String ERROR_MSG = "An error occurred while calculating the digest for the %s file: %s";

    private File file;

    public DigestCalculationException(File file, Throwable t) {
        super(t);
        this.file = file;
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_MSG, file.getAbsolutePath(), this.getCause());
    }

}
