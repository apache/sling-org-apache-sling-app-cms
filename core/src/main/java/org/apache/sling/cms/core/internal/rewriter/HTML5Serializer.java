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
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.resource.ConfigurationResourceResolver;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Serializer;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Serializer for writing HTML5 compliant markup
 */
public class HTML5Serializer implements Serializer {

    private static final int CHAR_EQ = 61;

    private static final int CHAR_GT = 62;

    private static final int CHAR_SP = 32;

    private static final int CHAR_LT = 60;

    private static final Set<String> emptyTags = new HashSet<>();
    static {
        emptyTags.addAll(Arrays.asList("br", "area", "link", "img", "param", "hr", "input", "col", "base", "meta"));
    }
    private PrintWriter writer;

    private ConfigurationResourceResolver resolver;

    private Resource rewriteConfig;

    public HTML5Serializer(ConfigurationResourceResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void characters(char[] buffer, int offset, int length) throws SAXException {
        if (length == 0) {
            writer.flush();
        } else {
            writer.write(buffer, offset, length);
        }
    }

    @Override
    public void dispose() {
        // Nothing required
    }

    @Override
    public void endDocument() throws SAXException {
        writer.flush();
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (!emptyTags.contains(localName)) {
            writer.write("</");
            writer.write(localName);
            writer.write(CHAR_GT);
        }
    }

    @Override
    public void endPrefixMapping(String s) throws SAXException {
        // Nothing required
    }

    @Override
    public void ignorableWhitespace(char[] ac, int i, int j) throws SAXException {
        // Nothing required
    }

    @Override
    public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
        if (context.getWriter() == null) {
            throw new IllegalArgumentException("Failed to initialize HTML5Serializer, null writer specified!");
        } else {
            writer = context.getWriter();
            rewriteConfig = resolver.getResource(context.getRequest().getResource(), "site", "rewrite");
        }
    }

    @Override
    public void processingInstruction(String s, String s1) throws SAXException {
        // Nothing required
    }

    @Override
    public void setDocumentLocator(Locator locator1) {
        // Nothing required
    }

    @Override
    public void skippedEntity(String s) throws SAXException {
        // Nothing required
    }

    @Override
    public void startDocument() throws SAXException {
        writer.println(rewriteConfig.getValueMap().get("doctype", String.class));
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        boolean endSlash = false;
        writer.write(CHAR_LT);
        writer.write(localName);

        for (int i = 0; i < atts.getLength(); i++) {
            if ("endSlash".equals(atts.getQName(i))) {
                endSlash = true;
            }
            String value = atts.getValue(i);
            if (shouldContinue(localName, atts, i)) {
                continue;
            }
            writer.write(CHAR_SP);
            writer.write(atts.getLocalName(i));

            writer.write(CHAR_EQ);
            writer.write('"');
            writer.write(value);
            writer.write('"');
        }

        if (endSlash) {
            writer.write("/");
        }
        writer.write(CHAR_GT);
    }

    private boolean shouldContinue(String localName, Attributes atts, int i) {
        if ("endSlash".equals(atts.getQName(i))) {
            return true;
        }
        if ("a".equals(localName) && "shape".equals(atts.getLocalName(i))) {
            return true;
        }
        if ("iframe".equals(localName)
                && ("frameborder".equals(atts.getLocalName(i)) || "scrolling".equals(atts.getLocalName(i)))) {
            return true;
        }
        if ("br".equals(localName) && ("clear".equals(atts.getLocalName(i)))) {
            return true;
        }
        if (atts.getValue(i) == null) {
            return true;
        }
        return false;
    }

    @Override
    public void startPrefixMapping(String s, String s1) throws SAXException {
        // Nothing required
    }
}
