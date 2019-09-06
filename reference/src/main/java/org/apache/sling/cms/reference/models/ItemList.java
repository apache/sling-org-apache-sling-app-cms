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

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for retrieving a list of items from the Sling repository using a JCR
 * SQL2 query.
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class ItemList {

    private static final Logger log = LoggerFactory.getLogger(ItemList.class);

    private int count;

    private int end;

    @RequestAttribute
    private String limit;

    private int page;

    private Integer[] pages;

    @RequestAttribute
    private String query;

    private SlingHttpServletRequest request;

    private List<Resource> items = new ArrayList<>();

    private int start;

    public ItemList(SlingHttpServletRequest request) {
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

    public String getQuery() {
        return query;
    }

    public List<Resource> getItems() {
        return items;
    }

    public int getStart() {
        return start;
    }

    @PostConstruct
    public void init() {

        log.trace("init");

        Set<String> distinct = new HashSet<>();

        if (request.getRequestPathInfo().getSuffix() != null) {
            query = query.replace("{SUFFIX}", request.getRequestPathInfo().getSuffix());
        }
        log.debug("Listing results of: {}", query);

        Iterator<Resource> res = request.getResourceResolver().findResources(query, Query.JCR_SQL2);
        while (res.hasNext()) {
            Resource result = res.next();
            if (!distinct.contains(result.getPath())) {
                items.add(result);
                distinct.add(result.getPath());
            }
        }
        count = items.size();
        log.debug("Found {} results", count);

        if (StringUtils.isNotBlank(request.getParameter("page")) && request.getParameter("page").matches("\\d+")) {
            page = Integer.parseInt(request.getParameter("page"), 10) - 1;
            log.debug("Using page {}", page);
        } else {
            page = 0;
            log.debug("Page {} not specified or not valid", request.getParameter("page"));
        }

        int l = Integer.parseInt(this.limit, 10);
        if (page * l >= count) {
            start = count;
        } else {
            start = page * l;
        }
        log.debug("Using start {}", start);

        if ((page * l) + l >= count) {
            end = count;
        } else {
            end = (page * l) + l;
        }
        log.debug("Using end {}", end);
        items = items.subList(start, end);

        List<Integer> pgs = new ArrayList<>();
        int max = ((int) Math.ceil((double) count / l)) + 1;
        for (int i = 1; i < max; i++) {
            pgs.add(i);
        }
        pages = pgs.toArray(new Integer[pgs.size()]);
        if (log.isDebugEnabled()) {
            log.debug("Loaded pages {}", Arrays.toString(pages));
        }
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
