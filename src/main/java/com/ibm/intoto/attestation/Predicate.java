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

import jakarta.json.JsonObject;

/**
 * The Predicate is the innermost layer of the attestation, containing arbitrary metadata about the Statement's subject.
 */
public abstract class Predicate {

    /**
     * TypeURI that identifies what the predicate mans.
     */
    public abstract String getPredicateType();

    /**
     * Additional, type-dependent parameters.
     */
    public abstract JsonObject getPredicateParameters();

}
