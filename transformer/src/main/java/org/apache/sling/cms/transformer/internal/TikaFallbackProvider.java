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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.transformer.OutputFileFormat;
import org.apache.sling.cms.transformer.ThumbnailProvider;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@Component(service = ThumbnailProvider.class, property = {
        Constants.SERVICE_RANKING + "=" + Integer.MIN_VALUE }, immediate = true)
public class TikaFallbackProvider implements ThumbnailProvider {

    private static final Logger log = LoggerFactory.getLogger(TikaFallbackProvider.class);

    @Override
    public boolean applies(Resource resource) {
        return (CMSConstants.NT_FILE.equals(resource.getResourceType())
                || JcrConstants.NT_FILE.equals(resource.getResourceType()));
    }

    @Override
    public InputStream getThumbnail(Resource resource) throws IOException {

        log.info("Extracting content thumbnail from {}", resource.getPath());
        try {

            log.debug("Extracting file contents");
            String contents = extractContents(resource);

            log.debug("Creating thumbnail of file contents");
            int width = 500;
            int height = 500;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = image.createGraphics();
            JEditorPane jep = new JEditorPane("text/html", contents);
            jep.setSize(width, height);
            jep.print(graphics);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, OutputFileFormat.PNG.toString(), baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (SAXException | TikaException e) {
            throw new IOException("Failed to generate thumbnail from " + resource.getPath(), e);
        }
    }

    private String extractContents(Resource resource) throws IOException, TikaException, SAXException {
        InputStream is = resource.adaptTo(InputStream.class);
        Parser parser = new AutoDetectParser();
        WriteOutContentHandler woHandler = new WriteOutContentHandler();
        BodyContentHandler bHandler = new BodyContentHandler(woHandler);
        
        Metadata md = new Metadata();
        ParseContext context = new ParseContext();
        try {
            parser.parse(is, bHandler, md, context);
        } catch (SAXException se) {
            if (woHandler.isWriteLimitReached(se)) {
                log.debug("Reached write limit for preview generation");
            } else {
                throw se;
            }
        }
        return bHandler.toString();
    }

}
