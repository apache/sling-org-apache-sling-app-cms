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
package org.apache.sling.cms.core.internal.rewriter;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.resource.ConfigurationResourceResolver;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A Sling Rewriter Transformer which maps references to images, links, etc that
 * are contained in the Sling repository.
 */
public class ReferenceMappingTransformer implements Transformer {

    private static final Logger log = LoggerFactory.getLogger(ReferenceMappingTransformer.class);

    private ContentHandler contentHandler;

    private SlingHttpServletRequest slingRequest;

    private ReferenceMappingTransformerConfig config;

    private boolean enabled = false;

    private ConfigurationResourceResolver resolver;

    private String[] attributes;

    public ReferenceMappingTransformer(ReferenceMappingTransformerConfig config,
            ConfigurationResourceResolver resolver) {
        this.config = config;
        this.resolver = resolver;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        contentHandler.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        contentHandler.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        contentHandler.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        contentHandler.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        contentHandler.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        atts = mapReferences(atts);
        contentHandler.startElement(uri, localName, qName, atts);
    }

    private Attributes mapReferences(Attributes atts) {
        if (enabled) {
            AttributesImpl newAtts = new AttributesImpl();
            for (int i = 0; i < atts.getLength(); i++) {
                String value = null;
                if (ArrayUtils.contains(attributes, atts.getLocalName(i).toLowerCase())
                        && atts.getValue(i).startsWith("/")) {
                    log.trace("Updating attribute {}", atts.getLocalName(i));
                    value = slingRequest.getResourceResolver().map(slingRequest, atts.getValue(i));
                    log.trace("Mapped value {}", value);
                } else {
                    log.trace("Skipping attribute {}", atts.getLocalName(i));
                    value = atts.getValue(i);
                }
                newAtts.addAttribute(atts.getURI(i), atts.getLocalName(i), atts.getQName(i), atts.getType(i), value);
            }
            return newAtts;
        } else {
            return atts;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        contentHandler.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        contentHandler.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        contentHandler.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        contentHandler.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        contentHandler.skippedEntity(name);
    }

    @Override
    public void dispose() {
        // Nothing required
    }

    @Override
    public void init(ProcessingContext context, ProcessingComponentConfiguration cfg) throws IOException {
        log.trace("init");
        slingRequest = context.getRequest();

        // make sure that the configuration is specified and that we're not currently in
        // edit mode
        if (config != null && config.enabledPaths() != null
                && !"true".equals(slingRequest.getAttribute(CMSConstants.ATTR_EDIT_ENABLED))) {
            for (String enabledPath : config.enabledPaths()) {
                if (slingRequest.getResource().getPath().startsWith(enabledPath)) {
                    enabled = true;
                    break;
                }
            }
            Resource configResource = resolver.getResource(slingRequest.getResource(), "site", "rewrite");
            if (configResource != null) {
                attributes = configResource.getValueMap().get("attributes", String[].class);
            } else {
                log.warn("Unable to find configuration for {}", slingRequest.getResource());
            }
        }
    }

    @Override
    public void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }

}
