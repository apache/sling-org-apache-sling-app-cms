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
package org.apache.sling.cms.publication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.ResourceTree;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.junit.Test;
import org.mockito.Mockito;

public class IsPublishableResourceTypeTest {

    @Test
    public void testTypes(){
        IsPublishableResourceType predicate = new IsPublishableResourceType();
        ResourceTree folder = mockResource(JcrConstants.NT_FOLDER);
        ResourceTree slingFolder = mockResource(JcrResourceConstants.NT_SLING_FOLDER);
        ResourceTree orderedFolder = mockResource(JcrResourceConstants.NT_SLING_ORDERED_FOLDER);
        ResourceTree slingFile = mockResource(CMSConstants.NT_FILE);
        ResourceTree site = mockResource(CMSConstants.NT_SITE);
        ResourceTree page = mockResource(CMSConstants.NT_PAGE);
        ResourceTree config = mockResource(CMSConstants.NT_CONFIG);
        ResourceTree component = mockResource("a/resource/type");


        assertFalse(predicate.test(null));
        assertFalse(predicate.test(folder));
        assertFalse(predicate.test(slingFolder));
        assertFalse(predicate.test(orderedFolder));
        assertTrue(predicate.test(slingFile));
        assertFalse(predicate.test(site));
        assertTrue(predicate.test(page));
        assertFalse(predicate.test(config));
        assertFalse(predicate.test(component));
    }

    public ResourceTree mockResource(String type){
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resource.getResourceType()).thenReturn(type);

        ResourceTree tree = Mockito.mock(ResourceTree.class);
        Mockito.when(tree.getResource()).thenReturn(resource);
        return tree;
    }
    
}
