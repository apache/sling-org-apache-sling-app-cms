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
package org.apache.sling.cms.core.internal.filters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Mockito;

public class CMSSecurityConfigInstanceTest {

    @Test
    public void testNullParams() {
        CMSSecurityConfigInstance securityConfig = new CMSSecurityConfigInstance();
        securityConfig.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return null;
            }

            @Override
            public String[] allowedPatterns() {
                return null;
            }

            @Override
            public String group() {
                return null;
            }

        });
        assertTrue(securityConfig.applies(Mockito.mock(HttpServletRequest.class)));
        assertFalse(securityConfig.isUriAllowed("/"));
        assertNull(securityConfig.getGroupName());

    }

    @Test
    public void testPatterns() {
        CMSSecurityConfigInstance securityConfig = new CMSSecurityConfigInstance();
        securityConfig.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return null;
            }

            @Override
            public String[] allowedPatterns() {
                return new String[] { "\\/", "\\/[a-z]+" };
            }

            @Override
            public String group() {
                return null;
            }

        });

        assertTrue(securityConfig.isUriAllowed("/"));
        assertTrue(securityConfig.isUriAllowed("/abc"));
        assertFalse(securityConfig.isUriAllowed("/1"));

    }

    @Test
    public void testDomains() {
        CMSSecurityConfigInstance securityConfig = new CMSSecurityConfigInstance();
        securityConfig.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return new String[] { "sling.apache.org", "adapt.to" };
            }

            @Override
            public String[] allowedPatterns() {
                return null;
            }

            @Override
            public String group() {
                return null;
            }

        });

        HttpServletRequest validRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(validRequest.getServerName()).thenReturn("sling.apache.org");
        assertTrue(securityConfig.applies(validRequest));

        HttpServletRequest inValidRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(inValidRequest.getServerName()).thenReturn("www.onion.com");
        assertFalse(securityConfig.applies(inValidRequest));

    }

}
