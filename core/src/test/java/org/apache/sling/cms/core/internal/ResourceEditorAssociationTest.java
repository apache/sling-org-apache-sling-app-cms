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
package org.apache.sling.cms.core.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ResourceEditorAssociationTest {

    private ResourceEditorAssociation rea;

    @Before
    public void init() {
        rea = new ResourceEditorAssociation();
        rea.activate(new ResourceEditorAssociationConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String pathPattern() {
                return "/content.*";
            }

            @Override
            public String editor() {
                return "/cms/site/sites.html";
            }

            @Override
            public String resourceType() {
                return CMSConstants.NT_SITE;
            }

            @Override
            public String parentType() {
                return null;
            }
        });
    }

    @Test
    public void testMatching() {
        Resource matching = Mockito.mock(Resource.class);
        Mockito.when(matching.getPath()).thenReturn("/content/sling-adobe-org");
        Mockito.when(matching.getResourceType()).thenReturn(CMSConstants.NT_SITE);

        assertTrue(rea.matches(matching));

        assertEquals("/cms/site/sites.html", rea.getEditor());
    }

    @Test
    public void testNonMatchingType() {
        Resource matching = Mockito.mock(Resource.class);
        Mockito.when(matching.getPath()).thenReturn("/content/sling-adobe-org");
        Mockito.when(matching.getResourceType()).thenReturn(CMSConstants.NT_PAGE);

        assertFalse(rea.matches(matching));
    }

    @Test
    public void testNonMatchingPath() {
        Resource matching = Mockito.mock(Resource.class);
        Mockito.when(matching.getPath()).thenReturn("/conf/sling");
        Mockito.when(matching.getResourceType()).thenReturn(CMSConstants.NT_SITE);

        assertFalse(rea.matches(matching));
    }
}
