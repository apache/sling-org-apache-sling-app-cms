/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.cms.transformer.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.webconsole.AbstractWebConsolePlugin;
import org.apache.felix.webconsole.WebConsoleConstants;
import org.apache.sling.cms.transformer.ThumbnailProvider;
import org.apache.sling.cms.transformer.TransformationHandler;
import org.apache.sling.cms.transformer.Transformer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Lists;

/**
 * Simple web console plugin for listing out the registered thumbnail providers
 * and transformation handler
 */
@Component(property = { Constants.SERVICE_DESCRIPTION + "=Web Console Plugin for Apache Sling CMS Transformer",
        Constants.SERVICE_VENDOR + "=The Apache Software Foundation",
        WebConsoleConstants.PLUGIN_LABEL + "=" + TransformerWebConsole.CONSOLE_LABEL,
        WebConsoleConstants.PLUGIN_TITLE + "=" + TransformerWebConsole.CONSOLE_TITLE,
        WebConsoleConstants.CONFIG_PRINTER_MODES + "=always",
        WebConsoleConstants.PLUGIN_CATEGORY + "=Status" }, service = { Servlet.class })
public class TransformerWebConsole extends AbstractWebConsolePlugin {

    private static final long serialVersionUID = 4819043498961127418L;
    public static final String CONSOLE_LABEL = "slingcms-transformer";
    public static final String CONSOLE_TITLE = "Sling CMS Transformer";

    @SuppressWarnings({ "squid:S2078", "squid:S2226" }) // ignore since this field is is injected by OSGi
    @Reference
    private transient Transformer transformer;

    @Override
    public String getTitle() {
        return CONSOLE_TITLE;
    }

    @Override
    public String getLabel() {
        return CONSOLE_LABEL;
    }

    @Override
    protected void renderContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        PrintWriter pw = httpServletResponse.getWriter();
        pw.println("<div id='content' class='ui-widget'><br>");
        pw.println("<pre>");
        pw.println("Registered Thumbnail Providers");
        pw.println("========================");
        List<ThumbnailProvider> providers = ((TransformerImpl) transformer).getThumbnailProviders();
        Lists.reverse(providers).forEach(p -> pw.println(p.getClass().getName()));
        pw.println("</pre><br/>");
        pw.println("<pre>");
        pw.println("Registered Transformation Handlers");
        pw.println("========================");

        List<TransformationHandler> handlers = ((TransformerImpl) transformer).getHandlers();
        handlers.forEach(h -> pw.println(h.getResourceType() + "=" + h.getClass().getCanonicalName()));
        pw.println("</pre>");
        pw.println("</div>");
    }

}
