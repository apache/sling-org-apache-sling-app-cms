/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.cms.core.internal.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;

import org.apache.sling.cms.core.internal.operations.PropertyHintNodeNameGenerator.Config;
import org.junit.Test;

public class PropertyHintNodeNameGeneratorTest {

    private PropertyHintNodeNameGenerator generator = new PropertyHintNodeNameGenerator();

    private Config DEFAULT_CONFIG = new Config() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public String allowed_chars() {
            return "abcdefghijklmnopqrstuvwxyz_-";
        }

        @Override
        public String replacement_char() {
            return "-";
        }

    };

    @Test
    public void testFilter() {
        generator.activate(DEFAULT_CONFIG);

        String filtered = generator.filter("A*dsf--dsfas__sdf_");
        assertNotNull(filtered);
        assertEquals("a-dsf--dsfas__sdf_", filtered);

        filtered = generator.filter("A*dsf--dsfas&&sdf*");
        assertNotNull(filtered);
        assertEquals("a-dsf--dsfas-sdf-", filtered);

        filtered = generator.filter("*A*dsf--dsfa  s&&sdf*");
        assertNotNull(filtered);
        assertEquals("-a-dsf--dsfa-s-sdf-", filtered);

        filtered = generator.filter("");
        assertNotNull(filtered);
        assertEquals("-", filtered);

        try {
            filtered = generator.filter(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

}
