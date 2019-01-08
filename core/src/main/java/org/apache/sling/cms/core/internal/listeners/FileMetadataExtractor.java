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
package org.apache.sling.cms.core.internal.listeners;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ExternalResourceChangeListener;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.cms.File;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Resource Change Listener which extracts the metadata from sling:Files when
 * they are uploaded.
 */
@Component(service = { FileMetadataExtractor.class, ResourceChangeListener.class,
        ExternalResourceChangeListener.class }, property = { ResourceChangeListener.CHANGES + "=ADDED",
                ResourceChangeListener.PATHS + "=/content",
                ResourceChangeListener.PATHS + "=/static" }, immediate = true)
public class FileMetadataExtractor implements ResourceChangeListener, ExternalResourceChangeListener {

    public static final String NN_METADATA = "metadata";
    public static final String PN_X_PARSED_BY = "X-Parsed-By";

    @Reference
    private ResourceResolverFactory factory;

    private static final Logger log = LoggerFactory.getLogger(FileMetadataExtractor.class);

    public void extractMetadata(File file) {
        extractMetadata(file.getResource());
    }

    public void extractMetadata(Resource resource) {
        try {
            log.info("Extracting metadata from {}", resource.getPath());
            ResourceResolver resolver = resource.getResourceResolver();
            InputStream is = resource.adaptTo(InputStream.class);
            Resource content = resource.getChild(JcrConstants.JCR_CONTENT);
            if (content == null) {
                log.warn("Content resource is null");
                return;
            }
            Map<String, Object> properties = new HashMap<>();
            Resource metadata = content.getChild(NN_METADATA);
            if (metadata != null) {
                properties = metadata.adaptTo(ModifiableValueMap.class);
            } else {
                properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
            }
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata md = new Metadata();
            ParseContext context = new ParseContext();
            parser.parse(is, handler, md, context);
            for (String name : md.names()) {
                updateProperty(properties, name, md);
            }
            if (metadata == null) {
                resolver.create(content, NN_METADATA, properties);
            }
            resolver.commit();
            log.info("Metadata extracted from {}", resource.getPath());
        } catch (Exception e) {
            log.warn("Exception extracting metadata from: " + resource.getPath(), e);
        }
    }

    private void updateProperty(Map<String, Object> properties, String name, Metadata metadata) {
        log.trace("Updating property: {}", name);
        String filtered = Text.escapeIllegalJcrChars(name);
        Property property = Property.get(name);
        if (property != null) {
            if (metadata.isMultiValued(property)) {
                properties.put(filtered, metadata.getValues(property));
            } else if (metadata.getDate(property) != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(metadata.getDate(property));
                properties.put(filtered, cal);
            } else if (metadata.getInt(property) != null) {
                properties.put(filtered, metadata.getInt(property));
            } else {
                properties.put(filtered, metadata.get(property));
            }
        } else {
            properties.put(filtered, metadata.get(name));
        }
    }

    @Override
    public void onChange(List<ResourceChange> changes) {
        Map<String, Object> serviceParams = new HashMap<>();
        serviceParams.put(ResourceResolverFactory.SUBSERVICE, "sling-cms-metadata");
        ResourceResolver serviceResolver = null;
        try {
            serviceResolver = factory.getServiceResourceResolver(serviceParams);
            for (ResourceChange rc : changes) {
                Resource changed = serviceResolver.getResource(rc.getPath());
                extractMetadata(changed);
            }
        } catch (LoginException e) {
            log.error("Exception getting service user", e);
        } finally {
            if (serviceResolver != null) {
                serviceResolver.close();
            }
        }

    }

}
