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
package org.apache.sling.cms.reference.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.jcr.query.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for retrieving search results and pagination based on a search of the
 * Sling CMS repository.
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class Search {

	private static final Logger log = LoggerFactory.getLogger(Search.class);

	public static final String TERM_PARAMETER = "q";

	@ValueMapValue
	private String basePath;

	private int count;

	private int end;

	@ValueMapValue
	private int limit;

	private int page;

	private Integer[] pages;

	private SlingHttpServletRequest request;

	private List<Resource> results = new ArrayList<Resource>();

	private int start;

	public Search(SlingHttpServletRequest request) {
		this.request = request;
	}

	public int getCount() {
		return count;
	}

	public int getCurrentPage() {
		return page + 1;
	}

	public int getEnd() {
		return end;
	}

	public Integer[] getPages() {
		return pages;
	}

	public List<Resource> getResults() {
		return results;
	}

	public int getStart() {
		return start;
	}

	public String getTerm() {
		return request.getParameter(TERM_PARAMETER);
	}

	@PostConstruct
	public void init() {

		Set<String> distinct = new HashSet<String>();

		String term = Text.escapeIllegalXpathSearchChars(request.getParameter(TERM_PARAMETER)).replaceAll("'", "''");

		String query = "SELECT parent.* FROM [sling:Page] AS parent INNER JOIN [nt:base] AS child ON ISDESCENDANTNODE(child,parent) WHERE ISDESCENDANTNODE(parent, '"
				+ basePath + "') AND CONTAINS(child.*, '" + term + "')";
		log.debug("Searching for pages with {} under {} with query: {}", term, basePath, query);
		Iterator<Resource> res = request.getResourceResolver().findResources(query, Query.JCR_SQL2);
		while (res.hasNext()) {
			Resource result = res.next();
			if (!distinct.contains(result.getPath())) {
				results.add(result);
				distinct.add(result.getPath());
			}
		}
		count = results.size();
		log.debug("Found {} results", count);

		if (StringUtils.isNotBlank(request.getParameter("page")) && request.getParameter("page").matches("\\d+")) {
			page = Integer.parseInt(request.getParameter("page"), 10) - 1;
			log.debug("Using page {}", page);
		} else {
			page = 0;
			log.debug("Page {} not specified or not valid", request.getParameter("page"));
		}

		if (page * limit >= count) {
			start = count;
		} else {
			start = page * limit;
		}
		log.debug("Using start {}", start);

		if ((page * limit) + limit >= count) {
			end = count;
		} else {
			end = (page * limit) + limit;
		}
		log.debug("Using end {}", end);
		results = results.subList(start, end);

		List<Integer> pgs = new ArrayList<Integer>();
		int max = ((int) Math.ceil((double) count / limit)) + 1;
		for (int i = 1; i < max; i++) {
			pgs.add(i);
		}
		pages = pgs.toArray(new Integer[pgs.size()]);
		log.debug("Loaded pages {}", Arrays.toString(pages));
	}

	public boolean isFirst() {
		return page == 0;
	}

	public boolean isLast() {
		if (pages.length > 0) {
			return page + 1 == pages[pages.length - 1];
		}
		return true;
	}
}
