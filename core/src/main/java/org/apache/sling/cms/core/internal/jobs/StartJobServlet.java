
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
package org.apache.sling.cms.core.internal.jobs;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.cms.CMSJobManager;
import org.apache.sling.cms.i18n.I18NDictionary;
import org.apache.sling.cms.i18n.I18NProvider;
import org.apache.sling.event.jobs.Job;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/cms/startjob",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST })
public class StartJobServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -8860513787587588985L;

    @Reference
    private transient I18NProvider provider;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        I18NDictionary dictionary = provider.getDictionary(request);
        String message = null;
        CMSJobManager jobMgr = request.adaptTo(CMSJobManager.class);
        if (jobMgr != null) {
            Job job = jobMgr.startJob();
            if(job != null) {
                message = dictionary.get("slingcms.jobs.jobstarted");
            }else {
                message = dictionary.get("slingcms.jobs.badrequest");
                response.sendError(500, message);
            }
        } else {
            message = dictionary.get("slingcms.jobs.badrequest");
            response.sendError(400, message);
        }
        response.setContentType("application/json");
        response.getWriter().write("{\"title\":\"" + message + "\"}");
    }
}
