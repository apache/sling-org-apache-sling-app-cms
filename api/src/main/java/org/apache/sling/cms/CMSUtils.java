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
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;

/**
 * Shared utility functions
 */
public class CMSUtils {

	private CMSUtils() {
	}

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

	public static final <T> List<T> adaptResources(Resource[] resources, Class<T> type) {
		List<T> values = new ArrayList<>();
		if (resources != null) {
			for (Resource resource : resources) {
				values.add(resource.adaptTo(type));
			}
		}
		return values;
	}

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

	public static final Resource findPublishableParent(Resource resource) {
		String type = resource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class);
		if (ArrayUtils.contains(CMSConstants.PUBLISHABLE_TYPES, type)) {
			return resource;
		} else if (resource.getParent() != null) {
			return findPublishableParent(resource.getParent());
		}
		return null;
	}

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
