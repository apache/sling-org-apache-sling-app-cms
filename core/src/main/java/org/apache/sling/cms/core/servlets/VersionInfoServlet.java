/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sling.cms.core.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>VersionInfoServlet</code> renders list of versions available for
 * the current resource.
 *
 * At the moment only JCR nodes are supported.
 */
@Component(service = Servlet.class, property = { "service.description=Sling CMS version info servlet",
		"service.vendor=The Apache Software Foundation",
		"sling.servlet.resourceTypes=sling-cms/components/cms/versionmanager", "sling.servlet.methods=GET",
		"sling.servlet.selectors=VI", "sling.servlet.extensions=json" }, immediate = true)
public class VersionInfoServlet extends SlingSafeMethodsServlet {

	private static final Logger log = LoggerFactory.getLogger(VersionInfoServlet.class);

	private static final long serialVersionUID = 2980892473913646093L;

	@Override
	public void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
		log.trace("doGet");
		resp.setContentType(req.getResponseContentType());
		resp.setCharacterEncoding("UTF-8");

		try {
			resp.getWriter().write(getJsonObject(req.getRequestPathInfo().getSuffixResource()).toString());
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private JsonObject getJsonObject(Resource resource) throws RepositoryException {
		log.debug("Loading version history from {}", resource);
		final JsonObjectBuilder result = Json.createObjectBuilder();
		final Node node = resource.adaptTo(Node.class);
		if (node == null || !node.isNodeType(JcrConstants.MIX_VERSIONABLE)) {
			return result.build();
		}

		final VersionHistory history = node.getVersionHistory();
		final Version baseVersion = node.getBaseVersion();
		for (final VersionIterator it = history.getAllVersions(); it.hasNext();) {
			final Version v = it.nextVersion();
			final JsonObjectBuilder obj = Json.createObjectBuilder();
			obj.add("created", createdDate(v));
			obj.add("successors", getArrayBuilder(getNames(v.getSuccessors())));
			obj.add("predecessors", getArrayBuilder(getNames(v.getPredecessors())));

			obj.add("labels", getArrayBuilder(history.getVersionLabels(v)));
			obj.add("baseVersion", baseVersion.isSame(v));
			result.add(v.getName(), obj);
		}

		return Json.createObjectBuilder().add("versions", result).build();
	}

	private JsonArrayBuilder getArrayBuilder(String[] values) {
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for (String value : values) {
			builder.add(value);
		}

		return builder;
	}

	private JsonArrayBuilder getArrayBuilder(Collection<String> values) {
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for (String value : values) {
			builder.add(value);
		}

		return builder;
	}

	private static Collection<String> getNames(Version[] versions) throws RepositoryException {
		final List<String> result = new ArrayList<>();
		for (Version s : versions) {
			result.add(s.getName());
		}
		return result;
	}

	private static String createdDate(Node node) throws RepositoryException {
		Property prop = node.getProperty(JcrConstants.JCR_CREATED);
		if (prop != null) {
			return new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z").format(prop.getDate().getTime());
		} else {
			return "";
		}
	}

}
