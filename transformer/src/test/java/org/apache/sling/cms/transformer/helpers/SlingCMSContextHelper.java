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
package org.apache.sling.cms.transformer.helpers;

import java.io.InputStream;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;

import com.google.common.base.Function;

public class SlingCMSContextHelper {

    public static final void initContext(SlingContext context) {
        context.addModelsForPackage("org.apache.sling.cms.core.internal.models");
        context.addModelsForPackage("org.apache.sling.cms.core.models");

        context.load().json("/content.json", "/content");
        context.load().binaryResource("/apache.png", "/content/apache/sling-apache-org/index/apache.png/jcr:content");
        context.load().binaryResource("/sling.pdf", "/content/apache/sling-apache-org/index/sling.pdf/jcr:content");
        context.load().binaryResource("/Sling.docx", "/content/apache/sling-apache-org/index/Sling.docx/jcr:content");
        context.load().binaryResource("/Sling.pptx", "/content/apache/sling-apache-org/index/Sling.pptx/jcr:content");
        context.load().binaryResource("/Sling.ppt", "/content/apache/sling-apache-org/index/Sling.ppt/jcr:content");
        context.load().binaryResource("/editor.min.css", "/content/apache/sling-apache-org/index/editor.min.css/jcr:content");

        context.registerAdapter(Resource.class, InputStream.class, new Function<Resource, InputStream>() {
            public InputStream apply(Resource input) {
                return input.getValueMap().get("jcr:content/jcr:data", InputStream.class);
            }
        });

    }
}
