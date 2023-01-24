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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.NamespaceRegistry;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@Component(service = FileMetadataExtractor.class)
public class FileMetadataExtractorImpl implements FileMetadataExtractor {

    private static final Logger log = LoggerFactory.getLogger(FileMetadataExtractorImpl.class);

    private ResourceResolverFactory resolverFactory;

    @Activate
    public FileMetadataExtractorImpl(@Reference ResourceResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
    }

    @Override
    public Map<String, Object> extractMetadata(File file) throws IOException {
        try {
            return extractMetadata(file.getResource());
        } catch (SAXException | TikaException | RepositoryException | LoginException e) {
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
                properties.put("SHA1", generateSha1(resource));
                resource.getResourceResolver().refresh();
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
        } catch (SAXException | TikaException | RepositoryException | LoginException e) {
            throw new IOException("Failed to parse metadata", e);
        }
    }

    public String generateSha1(Resource resource) throws IOException {
        try (InputStream is = resource.adaptTo(InputStream.class)) {
            String sha1 = DigestUtils.sha1Hex(is);
            log.info("Generated SHA {} for {}", sha1, resource.getPath());
            return sha1;
        }
    }

    public Map<String, Object> extractMetadata(Resource resource)
            throws IOException, SAXException, TikaException, RepositoryException, LoginException {
        log.info("Extracting metadata from {}", resource.getPath());
        Map<String, Object> properties = new HashMap<>();
        try (InputStream is = resource.adaptTo(InputStream.class)) {
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata md = new Metadata();
            ParseContext context = new ParseContext();
            try {
                parser.parse(is, handler, md, context);
            } catch (SAXException se) {
                if ("WriteLimitReachedException".equals(se.getClass().getSimpleName())) {
                    log.info("Write limit reached for {}", resource.getPath());
                } else {
                    throw se;
                }
            }

            try (ResourceResolver adminResolver = resolverFactory.getAdministrativeResourceResolver(null)) {
                NamespaceRegistry registry = adminResolver.adaptTo(Session.class).getWorkspace().getNamespaceRegistry();
                for (String name : md.names()) {
                    putMetadata(properties, name, md, registry);
                }
            }
        }
        return properties;
    }

    protected String formatKey(String initialKey, NamespaceRegistry registry)
            throws RepositoryException {
        String namespace = null;
        String key = null;
        if (initialKey.contains(":")) {
            namespace = StringUtils.substringBefore(initialKey, ":");
            key = StringUtils.substringAfter(initialKey, ":");
        } else {
            key = initialKey;
        }
        key = key.replace(" ", "").replace("/", "-");
        if (namespace != null) {
            namespace = namespace.replace(" ", "").replace("/", "-");
            if (!ArrayUtils.contains(registry.getPrefixes(), namespace)) {
                registry.registerNamespace(namespace, "http://sling.apache.org/cms/ns/" + namespace);
            }
            return namespace + ":" + key;
        } else {
            return key;
        }
    }

    private void putMetadata(Map<String, Object> properties, String name, Metadata metadata, NamespaceRegistry registry)
            throws RepositoryException {
        log.trace("Updating property: {}", name);
        String filtered = formatKey(name, registry);
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
