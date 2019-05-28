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
package org.apache.sling.cms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Shared utility functions
 */
public class CMSUtils {

    private CMSUtils() {
    }

    /**
     * Adapts the collection of resources ensuring that if any cannot be adapted it
     * is excluded from the returned list.
     * 
     * @param resources the collection of resources to adapt
     * @param type      the type to which to adapt the resources
     * @param           <T> the type to which the resources are adapted
     * @return the list of adapted classes
     */
    @NotNull
    public static final <T> List<T> adaptResources(Collection<Resource> resources, Class<T> type) {
        List<T> values = new ArrayList<>();
        if (resources != null) {
            for (Resource resource : resources) {
                T val = resource.adaptTo(type);
                if (val != null) {
                    values.add(val);
                }
            }
        }
        return values;
    }

    /**
     * Adapts the array of resources ensuring that if any cannot be adapted it is
     * excluded from the returned list.
     * 
     * @param resources the array of resources to adapt
     * @param type      the type to which to adapt the resources
     * @param           <T> the type to which the resources are adapted
     * @return the list of adapted classes
     */
    @NotNull
    public static final <T> List<T> adaptResources(Resource[] resources, Class<T> type) {
        return adaptResources(Arrays.asList(resources), type);
    }

    /**
     * Looks up the resource tree to find the parent resource with the specified
     * jcr:primaryType.
     * 
     * @param resource the resource to search from
     * @param type     the primary type to find
     * @return the parent of the type or null
     */
    @Nullable
    public static final Resource findParentResourceofType(Resource resource, String type) {
        if (resource != null) {
            if (type.equals(resource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class))) {
                return resource;
            } else {
                return findParentResourceofType(resource.getParent(), type);
            }
        }
        return null;
    }

    /**
     * Look up the resource tree to find a parent of a publishable type.
     * 
     * @param resource the resource to search from
     * @return the parent publishable type
     */
    @Nullable
    public static final Resource findPublishableParent(Resource resource) {
        if (resource != null) {
            String type = resource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class);
            if (ArrayUtils.contains(CMSConstants.PUBLISHABLE_TYPES, type)) {
                return resource;
            } else if (resource.getParent() != null) {
                return findPublishableParent(resource.getParent());
            }
        }
        return null;
    }

    /**
     * Return true of the resource (or it's publishable parent) is published or
     * false otherwise. If the resource is not contained within a publishable parent
     * it is considered published.
     * 
     * @param resource the resource to check
     * @return whether or not the resource is published
     */
    public static final boolean isPublished(Resource resource) {
        boolean published = true;
        Resource publishable = findPublishableParent(resource);
        if (publishable != null) {
            Resource content = publishable.getChild(JcrConstants.JCR_CONTENT);
            if (content != null && !(content.getValueMap().get(CMSConstants.PN_PUBLISHED, false))) {
                published = false;
            }
        }
        return published;
    }

}
