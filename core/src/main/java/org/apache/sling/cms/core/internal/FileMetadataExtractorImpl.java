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
package org.apache.sling.cms.core.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.File;
import org.apache.sling.cms.FileMetadataExtractor;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@Component(service = FileMetadataExtractor.class)
public class FileMetadataExtractorImpl implements FileMetadataExtractor {

    private static final String METADATA_EXTRACTOR_BUNDLE_NAME = "org.apache.sling.cms.metadata-extractor";

    private static final Logger log = LoggerFactory.getLogger(FileMetadataExtractorImpl.class);

    private BundleContext bcx;

    @Activate
    public void activate(ComponentContext context) {
        bcx = context.getBundleContext();
    }

    @Override
    public Map<String, Object> extractMetadata(File file) throws IOException {
        try {
            return extractMetadata(file.getResource());
        } catch (NoClassDefFoundError ncdfe) {

            log.info("Caught exception: {}, Attempting to reload metadata extractor bundle", String.valueOf(ncdfe));
            Bundle metadataExtractorBundle = Arrays.stream(bcx.getBundles())
                    .filter(b -> METADATA_EXTRACTOR_BUNDLE_NAME.equals(b.getSymbolicName())).findFirst().orElse(null);

            if (metadataExtractorBundle != null) {
                try {
                    log.debug("Reloading metadata bundle: {}", metadataExtractorBundle);
                    Bundle systemBundle = bcx.getBundle(0);
                    metadataExtractorBundle.stop();
                    metadataExtractorBundle.start();

                    FrameworkWiring frameworkWiring = systemBundle.adapt(FrameworkWiring.class);
                    frameworkWiring.refreshBundles(Collections.singleton(metadataExtractorBundle));
                    log.debug("Bundle reloaded successfully!");
                } catch (Exception e) {
                    log.warn("Failed to refresh metadata exporter packages", e);
                }
            }
            try {
                return extractMetadata(file.getResource());
            } catch (SAXException | TikaException | NoClassDefFoundError e) {
                throw new IOException("Failed to parse metadata after reloading metadata extractor bundle", e);
            }
        } catch (SAXException | TikaException e) {
            throw new IOException("Failed to parse metadata", e);
        }
    }

    @Override
    public void updateMetadata(File file) throws IOException {
        updateMetadata(file, true);
    }

    @Override
    public void updateMetadata(File file, boolean persist) throws IOException {
        log.trace("Updating metadata for {}, persist {}", file, persist);
        try {
            Resource resource = file.getResource();
            Resource content = resource.getChild(JcrConstants.JCR_CONTENT);
            if (content == null) {
                log.warn("Content resource is null");
                return;
            }
            Map<String, Object> properties = null;
            Resource metadata = content.getChild(CMSConstants.NN_METADATA);
            if (metadata != null) {
                properties = metadata.adaptTo(ModifiableValueMap.class);
            } else {
                properties = new HashMap<>();
                properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
            }
            if (properties != null) {
                properties.putAll(extractMetadata(file.getResource()));
                if (metadata == null) {
                    resource.getResourceResolver().create(content, CMSConstants.NN_METADATA, properties);
                }
                if (persist) {
                    resource.getResourceResolver().commit();
                }
                log.info("Metadata extracted from {}", resource.getPath());
            } else {
                throw new IOException("Unable to update metadata for " + resource.getPath());
            }
        } catch (SAXException | TikaException e) {
            throw new IOException("Failed to parse metadata", e);
        }

    }

    public Map<String, Object> extractMetadata(Resource resource) throws IOException, SAXException, TikaException {
        log.info("Extracting metadata from {}", resource.getPath());
        InputStream is = resource.adaptTo(InputStream.class);
        Map<String, Object> properties = new HashMap<>();
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata md = new Metadata();
        ParseContext context = new ParseContext();
        parser.parse(is, handler, md, context);
        for (String name : md.names()) {
            putMetadata(properties, name, md);
        }
        return properties;
    }

    private void putMetadata(Map<String, Object> properties, String name, Metadata metadata) {
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

}
