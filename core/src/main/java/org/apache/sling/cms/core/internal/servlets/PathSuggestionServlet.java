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
package org.apache.sling.cms.core.internal.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet which includes the content of the page when the page is accessed.
 */
@Component(service = { Servlet.class }, property = { "sling.servlet.paths=/bin/cms/paths",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET })
@Designate(ocd = PathSuggestionServletConfig.class)
public class PathSuggestionServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = -410942682163323725L;
    private static final Logger log = LoggerFactory.getLogger(PathSuggestionServlet.class);

    private static final Map<String, String[]> typeFilters = new HashMap<>();

    @Activate
    public void activate(PathSuggestionServletConfig config) {
        typeFilters.clear();
        for (String filter : config.typeFilters()) {
            String[] parts = filter.split("\\=");
            String key = parts[0];
            String[] types = parts[1].split("\\,");
            typeFilters.put(key, types);
        }
        log.info("Loaded type filters {}", typeFilters);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getParameter("path");

        if (StringUtils.isEmpty(path)) {
            path = "/";
        }
        log.debug("Finding valid paths under {}", path);

        String type = request.getParameter("type");
        if (!typeFilters.containsKey(type)) {
            type = "all";
        }
        log.debug("Filtering by type: {}", type);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        Resource parent = request.getResourceResolver().getResource(path);

        if (parent == null) {
            path = StringUtils.left(path, path.lastIndexOf('/'));
            if (StringUtils.isEmpty(path)) {
                path = "/";
            }

            log.debug("Using stemmed path {}", path);
            parent = request.getResourceResolver().getResource(path);
        }
        if (parent != null) {
            for (Resource child : parent.getChildren()) {
                if (isIncluded(child, type)) {
                    arrBuilder.add(child.getPath());
                }
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(arrBuilder.build().toString());
    }

    private boolean isIncluded(Resource child, String type) {
        try {
            Node node = child.adaptTo(Node.class);
            if (node != null) {
                for (String t : typeFilters.get(type)) {
                    if (node.isNodeType(t)) {
                        return true;
                    }
                }
            } else {
                log.debug("Unable to adapt child resource {} to node", child.getPath());
            }
        } catch (RepositoryException e) {
            log.warn("Unexpected exception accessing JCR Node", e);
        }
        return false;
    }
}
