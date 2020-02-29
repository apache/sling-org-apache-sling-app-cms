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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.query.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.poi.util.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.transformer.OutputFileFormat;
import org.apache.sling.cms.transformer.Transformation;
import org.apache.sling.cms.transformer.Transformer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A servlet to transform images using the FileThumbnailTransformer API. Can be
 * invoked using the syntax:
 * 
 * /content/file/path.jpg.transform/transformation-name.png
 */
@Component(service = { Servlet.class }, property = { "sling.servlet.extensions=transform",
        "sling.servlet.resourceTypes=sling:File", "sling.servlet.resourceTypes=nt:file" })
@Designate(ocd = TransformServletConfig.class)
public class TransformServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(TransformServlet.class);

    private static final long serialVersionUID = -1513067546618762171L;

    public static final String SERVICE_USER = "sling-cms-transformer";

    private transient TransformationServiceUser transformationServiceUser;

    private transient Transformer transformer;
    
    private transient TransformServletConfig config;
    
    @Activate
    public void activate(TransformServletConfig config) {
        this.config = config;
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        log.trace("doGet");

        String name = StringUtils.substringBeforeLast(request.getRequestPathInfo().getSuffix(), ".");
        response.setHeader("Content-Disposition", "filename=" + request.getResource().getName());
        String format = StringUtils.substringAfterLast(request.getRequestPathInfo().getSuffix(), ".");
        log.debug("Transforming resource: {} with transformation: {} to {}", request.getResource(), name, format);
        String original = response.getContentType();
        try (ResourceResolver serviceResolver = transformationServiceUser.getTransformationServiceUser()) {
            Transformation transformation = findTransformation(serviceResolver, name);
            if (transformation != null) {
                response.setContentType(OutputFileFormat.forRequest(request).getMimeType());
                String expectedPath = "jcr:content/renditions/" + name + "." + format;
                Resource rendition = request.getResource().getChild(expectedPath);
                if (rendition != null) {
                    log.debug("Using existing rendition {}", name);
                    IOUtils.copy(rendition.adaptTo(InputStream.class), response.getOutputStream());
                } else {
                    log.debug("Creating new rendition {}", name);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    transformer.transform(request.getResource(), transformation,
                            OutputFileFormat.valueOf(format.toUpperCase()), baos);
                    IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
                    if (CMSConstants.NT_FILE.equals(request.getResource().getResourceType())) {
                        Resource file = ResourceUtil.getOrCreateResource(serviceResolver,
                                request.getResource().getPath() + "/" + expectedPath,
                                Collections.singletonMap(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_FILE),
                                JcrConstants.NT_UNSTRUCTURED, false);
                        Map<String, Object> properties = new HashMap<>();
                        properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
                        properties.put(JcrConstants.JCR_DATA, new ByteArrayInputStream(baos.toByteArray()));
                        ResourceUtil.getOrCreateResource(serviceResolver,
                                file.getPath() + "/" + JcrConstants.JCR_CONTENT, properties,
                                JcrConstants.NT_UNSTRUCTURED, true);
                    }

                }
            } else {
                log.error("Exception transforming image: {} with transformation: {}", request.getResource(), name);
                response.setContentType(original);
                response.sendError(400, "Could not transform image with transformation: " + name);
            }
        } catch (Exception e) {
            log.error("Exception rendering transformed resource", e);
            response.setStatus(500);
            RequestDispatcherOptions op = new RequestDispatcherOptions();
            op.setReplaceSuffix(config.errorSuffix());
            op.setReplaceSelectors("transform");
            RequestDispatcher disp = request.getRequestDispatcher(config.errorResourcePath(), op);
            disp.forward(request, response);

        }
    }

    protected Transformation findTransformation(ResourceResolver serviceResolver, String name) {
        name = name.substring(1).replace("'", "''");
        log.debug("Finding transformations with {}", name);

        Iterator<Resource> transformations = serviceResolver.findResources(
                "SELECT * FROM [nt:unstructured] WHERE ISDESCENDANTNODE([/conf]) AND [sling:resourceType]='sling-cms/components/caconfig/transformation' AND [name]='"
                        + name + "'",
                Query.JCR_SQL2);
        if (transformations.hasNext()) {
            Resource transformation = transformations.next();
            return transformation.adaptTo(Transformation.class);
        }

        return null;
    }

    @Reference
    public void setTransformationServiceUser(TransformationServiceUser transformationServiceUser) {
        this.transformationServiceUser = transformationServiceUser;
    }

    @Reference
    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

}
