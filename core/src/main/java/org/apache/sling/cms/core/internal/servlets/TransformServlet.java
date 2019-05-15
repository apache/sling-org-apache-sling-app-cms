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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.cms.File;
import org.osgi.service.component.annotations.Component;

import com.google.common.net.MediaType;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

@Component(service = { Servlet.class }, property = { "sling.servlet.extensions=transform",
        "sling.servlet.resourceTypes=sling:File", "sling.servlet.resourceTypes=nt:file" })
public class TransformServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = -1513067546618762171L;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        Builder<? extends InputStream> builder = Thumbnails.of(getInputStream(request.getResource()));
        for (String cmd : request.getRequestPathInfo().getSuffix().split("/")) {
            if (cmd.startsWith("resize-")) {
                builder.size(Integer.parseInt(cmd.split("\\-")[1], 10), Integer.parseInt(cmd.split("\\-")[2], 10));
            }
        }
        builder.crop(Positions.CENTER);
        response.setContentType("image/png");
        builder.toOutputStream(response.getOutputStream());
    }

    private InputStream getInputStream(Resource resource) throws IOException {
        String contentType = Optional.ofNullable(resource.adaptTo(File.class)).map(File::getContentType).orElse("");
        if (contentType.startsWith("image")) {
            return resource.adaptTo(InputStream.class);
        }
        if (MediaType.PDF.toString().equals(contentType)) {
            PDDocument document = PDDocument.load(resource.adaptTo(InputStream.class));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bim, "jpeg", os);
            document.close();
            return new ByteArrayInputStream(os.toByteArray());
        }
        return null;
    }
}
