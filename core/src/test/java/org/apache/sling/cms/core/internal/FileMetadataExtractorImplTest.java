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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import javax.jcr.NamespaceRegistry;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.File;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMetadataExtractorImplTest {

    private static final Logger log = LoggerFactory.getLogger(FileMetadataExtractorImplTest.class);

    private File file;

    private NamespaceRegistry registry;

    private ResourceResolverFactory resolverFactory;

    private FileMetadataExtractorImpl extractor;

    @Before
    public void init() throws RepositoryException, LoginException {

        ResourceResolver resolver = mock(ResourceResolver.class);

        resolverFactory = mock(ResourceResolverFactory.class);
        when(resolverFactory.getAdministrativeResourceResolver(null)).thenReturn(resolver);
        registry = mock(NamespaceRegistry.class);

        Workspace workspace = mock(Workspace.class);
        when(workspace.getNamespaceRegistry()).thenReturn(registry);
        Session session = mock(Session.class);
        when(session.getWorkspace()).thenReturn(workspace);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        Resource resource = mock(Resource.class);
        Mockito.when(resource.adaptTo(InputStream.class))
                .thenReturn(FileMetadataExtractorImplTest.class.getClassLoader().getResourceAsStream("apache.png"));

        file = Mockito.mock(File.class);
        Mockito.when(file.getResource()).thenReturn(resource);
        extractor = new FileMetadataExtractorImpl(resolverFactory);
    }

    @Test
    public void testExtractMetadata() throws IOException {
        Map<String, Object> metadata = extractor.extractMetadata(file);

        assertNotNull(metadata);
        assertTrue(metadata.size() > 0);

        log.info("Extracted metadata: {}", metadata);
    }

    @Test
    public void testFormatKey() throws IOException, RepositoryException {

        when(registry.getPrefixes()).thenReturn(new String[] { "GPS", "ExifSubIFD", "ExifIFD0", "tiff", "dcterms" });

        String[] keys = new String[] {
                "GPS:GPS Img Direction", "Exif SubIFD:Subject Distance Range", "Compression Type",
                "Number of Components", "Component 2", "Component 1", "Exif IFD0:X Resolution", "tiff:ResolutionUnit",
                "Exif SubIFD:Scene Type", "Exif SubIFD:Exposure Mode", "tiff:Make", "Exif SubIFD:Sharpness",
                "Exif SubIFD:Custom Rendered", "Component 3", "Exif SubIFD:Components Configuration",
                "Exif SubIFD:Metering Mode", "Exif SubIFD:White Balance Mode", "tiff:BitsPerSample",
                "Exif SubIFD:Sub-Sec Time Original", "meta:creation-date", "Creation-Date", "tiff:Orientation",
                "tiff:Software", "geo:long", "Exif SubIFD:Digital Zoom Ratio", "tiff:YResolution", "Y Resolution",
                "Exif SubIFD:Flash", "Thumbnail Height Pixels", "Last-Modified", "Exif SubIFD:Sub-Sec Time",
                "exif:ExposureTime", "File Size", "Exif SubIFD:Exif Version", "GPS:GPS Img Direction Ref",
                "Exif SubIFD:Focal Length", "Exif IFD0:Resolution Unit", "Exif SubIFD:Lens Model",
                "Exif SubIFD:Date/Time Original", "Exif SubIFD:Sub-Sec Time Digitized", "Resolution Units",
                "File Modified Date", "Exif SubIFD:Sensing Method", "Image Height", "Thumbnail Width Pixels",
                "GPS:GPS Longitude", "Exif SubIFD:Time Zone Original", "GPS:GPS Longitude Ref", "tiff:Model",
                "Exif SubIFD:Brightness Value", "exif:IsoSpeedRatings", "Exif SubIFD:Exposure Program",
                "Exif IFD0:Make", "GPS:GPS Altitude Ref", "Exif SubIFD:Aperture Value",
                "Exif SubIFD:Date/Time Digitized", "tiff:ImageWidth", "GPS:GPS Altitude", "Exif IFD0:Y Resolution",
                "date", "Exif SubIFD:ISO Speed Ratings", "Number of Tables", "Exif SubIFD:Time Zone Digitized",
                "Exif SubIFD:Exif Image Width", "Exif SubIFD:Contrast", "X Resolution",
                "Exif SubIFD:Exposure Bias Value", "Exif SubIFD:Saturation", "modified", "exif:FNumber",
                "Exif SubIFD:Shutter Speed Value", "Exif IFD0:Orientation", "Exif SubIFD:F-Number", "exif:FocalLength",
                "Exif IFD0:Software", "Exif IFD0:Date/Time", "Exif SubIFD:Scene Capture Type", "Exif SubIFD:Time Zone",
                "geo:lat", "Data Precision", "tiff:ImageLength", "Exif IFD0:Model", "dcterms:created",
                "dcterms:modified", "exif:Flash", "Last-Save-Date", "Exif SubIFD:Color Space",
                "Exif SubIFD:Focal Length 35", "Exif SubIFD:Exposure Time", "meta:save-date", "File Name",
                "GPS:GPS Latitude Ref", "Content-Type", "X-Parsed-By", "Exif SubIFD:Max Aperture Value",
                "tiff:XResolution", "exif:DateTimeOriginal", "Exif SubIFD:Subject Distance",
                "Exif SubIFD:FlashPix Version", "Exif SubIFD:Exif Image Height", "Image Width", "GPS:GPS Latitude",
                "Exif SubIFD:Lens Make", "GPS:GPS Date Stamp" };

        String[] formatted = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            formatted[i] = extractor.formatKey(keys[i], registry);
        }

        String[] expected = new String[] {
                "GPS:GPSImgDirection", "ExifSubIFD:SubjectDistanceRange", "CompressionType",
                "NumberofComponents", "Component2", "Component1", "ExifIFD0:XResolution", "tiff:ResolutionUnit",
                "ExifSubIFD:SceneType", "ExifSubIFD:ExposureMode", "tiff:Make", "ExifSubIFD:Sharpness",
                "ExifSubIFD:CustomRendered", "Component3", "ExifSubIFD:ComponentsConfiguration",
                "ExifSubIFD:MeteringMode", "ExifSubIFD:WhiteBalanceMode", "tiff:BitsPerSample",
                "ExifSubIFD:Sub-SecTimeOriginal", "meta:creation-date", "Creation-Date", "tiff:Orientation",
                "tiff:Software", "geo:long", "ExifSubIFD:DigitalZoomRatio", "tiff:YResolution", "YResolution",
                "ExifSubIFD:Flash", "ThumbnailHeightPixels", "Last-Modified", "ExifSubIFD:Sub-SecTime",
                "exif:ExposureTime", "FileSize", "ExifSubIFD:ExifVersion", "GPS:GPSImgDirectionRef",
                "ExifSubIFD:FocalLength", "ExifIFD0:ResolutionUnit", "ExifSubIFD:LensModel",
                "ExifSubIFD:Date-TimeOriginal", "ExifSubIFD:Sub-SecTimeDigitized", "ResolutionUnits",
                "FileModifiedDate", "ExifSubIFD:SensingMethod", "ImageHeight", "ThumbnailWidthPixels",
                "GPS:GPSLongitude", "ExifSubIFD:TimeZoneOriginal", "GPS:GPSLongitudeRef", "tiff:Model",
                "ExifSubIFD:BrightnessValue", "exif:IsoSpeedRatings", "ExifSubIFD:ExposureProgram",
                "ExifIFD0:Make", "GPS:GPSAltitudeRef", "ExifSubIFD:ApertureValue",
                "ExifSubIFD:Date-TimeDigitized", "tiff:ImageWidth", "GPS:GPSAltitude", "ExifIFD0:YResolution",
                "date", "ExifSubIFD:ISOSpeedRatings", "NumberofTables", "ExifSubIFD:TimeZoneDigitized",
                "ExifSubIFD:ExifImageWidth", "ExifSubIFD:Contrast", "XResolution",
                "ExifSubIFD:ExposureBiasValue", "ExifSubIFD:Saturation", "modified", "exif:FNumber",
                "ExifSubIFD:ShutterSpeedValue", "ExifIFD0:Orientation", "ExifSubIFD:F-Number", "exif:FocalLength",
                "ExifIFD0:Software", "ExifIFD0:Date-Time", "ExifSubIFD:SceneCaptureType", "ExifSubIFD:TimeZone",
                "geo:lat", "DataPrecision", "tiff:ImageLength", "ExifIFD0:Model", "dcterms:created",
                "dcterms:modified", "exif:Flash", "Last-Save-Date", "ExifSubIFD:ColorSpace",
                "ExifSubIFD:FocalLength35", "ExifSubIFD:ExposureTime", "meta:save-date", "FileName",
                "GPS:GPSLatitudeRef", "Content-Type", "X-Parsed-By", "ExifSubIFD:MaxApertureValue",
                "tiff:XResolution", "exif:DateTimeOriginal", "ExifSubIFD:SubjectDistance",
                "ExifSubIFD:FlashPixVersion", "ExifSubIFD:ExifImageHeight", "ImageWidth", "GPS:GPSLatitude",
                "ExifSubIFD:LensMake", "GPS:GPSDateStamp" };
        assertArrayEquals(expected, formatted);
    }

    @Test
    public void willRegisterNamespace() throws RepositoryException {
        when(registry.getPrefixes()).thenReturn(new String[] { "GPS" });
        Iterator<String> keys = Stream
                .of("GPS:GPSLatitudeRef", "Content-Type", "X-Parsed-By", "ExifSubIFD:MaxApertureValue").iterator();
        while (keys.hasNext()) {
            extractor.formatKey(keys.next(), registry);
        }
        verify(registry).registerNamespace(eq("ExifSubIFD"), anyString());
    }
}
