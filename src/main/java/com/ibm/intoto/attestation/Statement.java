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

import com.ibm.intoto.attestation.exceptions.StatementValueNullException;
import com.ibm.intoto.attestation.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * The Statement is the middle layer of the attestation, binding it to a particular subject and unambiguously identifying the
 * types of the Predicate.
 * <p>
 * See https://github.com/in-toto/attestation/blob/main/spec/v1/statement.md.
 */
public class Statement {

    public static final String KEY_TYPE = "_type";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_PREDICATE_TYPE = "predicateType";
    public static final String KEY_PREDICATE = "predicate";

    public static final String TYPE_IN_TOTO_STATEMENT = "https://in-toto.io/Statement/v1";

    /**
     * Identifier for the schema of the Statement. Always https://in-toto.io/Statement/v1 for this version of the spec.
     */
    private String type;

    /**
     * Set of software artifacts that the attestation applies to. Each element represents a single software artifact. Each element
     * MUST have digest set.
     */
    private Subject subject;

    /**
     * URI identifying the type of the Predicate.
     */
    private String predicateType;

    /**
     * Additional parameters of the Predicate. Unset is treated the same as set-but-empty. MAY be omitted if predicateType fully
     * describes the predicate.
     */
    private Predicate predicate;

    private Statement(Builder builder) {
        this.type = builder.type;
        this.subject = builder.subject;
        this.predicateType = builder.predicateType;
        this.predicate = builder.predicate;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(KEY_TYPE, type);
        builder.add(KEY_SUBJECT, subject.toJson());
        builder.add(KEY_PREDICATE_TYPE, predicateType);
        if (predicate != null) {
            JsonObject predicateParameters = predicate.getPredicateParameters();
            Utils.addIfNonNullAndNotEmpty(predicateParameters, KEY_PREDICATE, builder);
        }
        return builder.build();
    }

    public static class Builder {

        private String type;
        private Subject subject;
        private String predicateType;
        private Predicate predicate;

        public Builder(String type, Subject subject, String predicateType) throws StatementValueNullException {
            if (type == null) {
                throw new StatementValueNullException(Statement.KEY_TYPE);
            }
            if (subject == null) {
                throw new StatementValueNullException(Statement.KEY_SUBJECT);
            }
            if (predicateType == null) {
                throw new StatementValueNullException(Statement.KEY_PREDICATE_TYPE);
            }
            this.type = type;
            this.subject = subject;
            this.predicateType = predicateType;
        }

        public Builder predicate(Predicate predicate) {
            this.predicate = predicate;
            return this;
        }

        public Statement build() {
            return new Statement(this);
        }
    }

}
