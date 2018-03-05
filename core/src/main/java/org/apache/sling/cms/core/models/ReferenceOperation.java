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
package org.apache.sling.cms.core.models;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.CMSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract operation for Reference operations.
 */
public abstract class ReferenceOperation {

	private static final Logger log = LoggerFactory.getLogger(ReferenceOperation.class);

	private Pattern regex = null;

	private Resource resource = null;

	public ReferenceOperation(Resource resource) {
		String path = resource.getPath().replace("/", "\\/");
		if (CMSConstants.NT_PAGE.equals(resource.getResourceType())) {
			regex = Pattern.compile(
					"(^" + path + "($|\\/)|(\\'|\\\")" + path + "(\\.html|\\'|\\\"|\\/))");
		} else {
			regex = Pattern.compile("(^" + path + "($|\\/)|(\\'|\\\")" + path + "(\\'|\\\"|\\/))");
		}
		this.resource = resource;
	}

	public void init() {
		log.debug("Finding references to {}", resource.getPath());

		String query = "SELECT * FROM [nt:base] AS s WHERE CONTAINS(s.*, '" + resource.getPath() + "')";
		Iterator<Resource> resources = resource.getResourceResolver().findResources(query, Query.JCR_SQL2);
		log.debug("Checking for references with: {}", query);
		while (resources.hasNext()) {
			Resource r = resources.next();
			log.debug("Checking for references in resource {}", r);
			ValueMap properties = r.getValueMap();
			for (String k : properties.keySet()) {
				if (properties.get(k) instanceof String) {
					Matcher matcher = regex.matcher(properties.get(k, String.class));
					if (matcher.find()) {
						log.trace("Found reference in property {}@{}", r.getPath(), k);
						doProcess(r, k);
					}
				} else if (properties.get(k) instanceof String[]) {
					boolean matches = false;
					for (String v : properties.get(k, String[].class)) {
						Matcher matcher = regex.matcher(v);
						if (matcher.find()) {
							matches = true;
							break;
						}
					}
					if (matches) {
						log.trace("Found reference in property {}@{}", r.getPath(), k);
						doProcess(r, k);
					}
				}
			}
		}
	}

	public abstract void doProcess(Resource resource, String matchingKey);

	public Pattern getRegex() {
		return regex;
	}
}
