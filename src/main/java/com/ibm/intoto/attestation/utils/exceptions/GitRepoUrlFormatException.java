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
package com.ibm.intoto.attestation.utils.exceptions;

import com.ibm.intoto.attestation.utils.GitUtils;

public class GitRepoUrlFormatException extends GitRepoUrlException {

    private static final String ERROR_MSG = "The owner and repository name cannot be extracted from the [%s] Git repo URL because the URL does not match an expected pattern. The expected patterns are: %s.";

    public GitRepoUrlFormatException(String url) {
        super(String.format(ERROR_MSG, url, GitUtils.getExpectedUrlPatterns()));
    }

}
