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
package org.apache.sling.cms.core.internal.models;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.sling.cms.Page;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PageImplTest {

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() {
        SlingCMSTestHelper.initContext(context);
    }

    @Test
    public void testValid() {
        context.currentResource("/content/apache/sling-apache-org/index");
        Page page = context.currentResource().adaptTo(Page.class);

        assertNotNull(page);

        assertArrayEquals(new String[] { "Community" }, page.getKeywords());
        assertEquals("Apache Sling - Bringing Back the Fun!", page.getTitle());
        assertEquals("/conf/global/site/templates/base-page", page.getTemplatePath());
        assertNotNull(page.getTemplatePath());
    }
}