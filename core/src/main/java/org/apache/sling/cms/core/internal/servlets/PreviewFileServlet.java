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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.resourceTypes=sling-cms/file/preview",
        "sling.servlet.methods=GET" })
public class PreviewFileServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 6234007100684499058L;

    private static final Logger log = LoggerFactory.getLogger(PreviewFileServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String suffix = request.getRequestPathInfo().getSuffix();

        log.debug("Getting thumbnail for {}", suffix);
        Pattern p = Pattern.compile("([a-z\\-\\/]+)\\.([a-z]+)(\\/.+)?");
        Matcher matcher = p.matcher(suffix);

        if (matcher.matches()) {
            String path = matcher.group(1);
            String extension = matcher.group(2);
            String newSuffix = "";
            if (matcher.groupCount() >= 4 && matcher.group(3) != null) {
                newSuffix = matcher.group(3);
            }
            log.debug("Loaded path={}, extension={}, suffix={}", path, extension, newSuffix);

            RequestDispatcherOptions options = new RequestDispatcherOptions();
            options.setReplaceSuffix(newSuffix);
            options.setReplaceSelectors("");

            Resource resource = request.getResourceResolver().getResource(path + "." + extension);
            log.debug("getResolutionPathInfo={}", resource.getResourceMetadata().getResolutionPathInfo());
            RequestDispatcher dispatcher = request.getRequestDispatcher(resource, options);

            dispatcher.forward(request, response);
        } else {
            throw new ServletException("Unexpected suffix pattern");
        }

    }

}
