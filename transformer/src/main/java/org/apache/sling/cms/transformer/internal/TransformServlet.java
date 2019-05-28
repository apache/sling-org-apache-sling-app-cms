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
package org.apache.sling.cms.transformer.internal;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.cms.transformer.FileThumbnailTransformer;
import org.apache.sling.cms.transformer.OutputFileFormat;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A servlet to transform images using the FileThumbnailTransformer API. Can be
 * invoked using the syntax:
 * 
 * /content/file/path.jpg.transform/command-param1-param2/command2-param1-param2.png
 */
@Component(service = { Servlet.class }, property = { "sling.servlet.extensions=transform",
        "sling.servlet.resourceTypes=sling:File", "sling.servlet.resourceTypes=nt:file" })
public class TransformServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(TransformServlet.class);

    private static final long serialVersionUID = -1513067546618762171L;

    @Reference
    private transient FileThumbnailTransformer transformer;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String original = response.getContentType();
        try {
            response.setContentType(OutputFileFormat.forRequest(request).getMimeType());
            transformer.transformFile(request, response.getOutputStream());
        } catch (Exception e) {
            log.error("Exception transforming image", e);
            response.setContentType(original);
            response.sendError(400, "Could not transform image with provided commands");
        }
    }

}
