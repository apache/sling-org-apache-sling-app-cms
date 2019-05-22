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
package org.apache.sling.cms.core.internal.transformation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.sling.cms.File;
import org.apache.sling.cms.transformation.OutputFileFormat;
import org.apache.sling.cms.transformation.ThumbnailProvider;
import org.osgi.service.component.annotations.Component;

import com.google.common.net.MediaType;

/**
 * Provides Thumbnails for Microsoft PPT and PPTX files.
 */
@Component(service = ThumbnailProvider.class)
public class SlideShowThumbnailProvider implements ThumbnailProvider {

    @Override
    public boolean applies(File file) {
        MediaType mt = MediaType.parse(file.getContentType());
        return mt.is(MediaType.MICROSOFT_POWERPOINT) || mt.is(MediaType.OOXML_PRESENTATION);
    }

    @Override
    public InputStream getThumbnail(File file) throws IOException {
        SlideShow<?, ?> ppt = null;
        MediaType mt = MediaType.parse(file.getContentType());
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = file.getResource().adaptTo(InputStream.class)) {
            if (mt.is(MediaType.MICROSOFT_POWERPOINT)) {
                ppt = new HSLFSlideShow(is);
            } else {
                ppt = new XMLSlideShow(is);
            }
            Dimension dim = ppt.getPageSize();
            List<? extends Slide<?, ?>> slides = ppt.getSlides();

            BufferedImage img = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, dim.width, dim.height));

            if (slides != null && !slides.isEmpty()) {
                slides.get(0).draw(graphics);
            }

            ImageIO.write(img, OutputFileFormat.PNG.toString(), baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } finally {
            if (ppt != null) {
                ppt.close();
            }
        }
    }

}
