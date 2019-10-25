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
package org.apache.sling.cms.reference.forms.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.PageManager;
import org.apache.sling.cms.ResourceTree;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.resourceTypes=reference/components/forms/form",
        "sling.servlet.methods=POST", "sling.servlet.extensions=html", "sling.servlet.selectors=allowpost" })
public class FormHandler extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(FormHandler.class);

    @Reference(policyOption = ReferencePolicyOption.GREEDY)
    private List<FormAction> formActions;

    private static final long serialVersionUID = -8149443208959899098L;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String pagePath = Optional.ofNullable(request.getResource().adaptTo(PageManager.class))
                .map(PageManager::getPage).map(Page::getPath)
                .orElse(StringUtils.substringBefore(request.getResource().getPath(), "/" + JcrConstants.JCR_CONTENT));

        List<Resource> actionResources = ResourceTree.stream(request.getResource().getChild("actions"))
                .map(ResourceTree::getResource).collect(Collectors.toList());

        try {
            FormRequest formRequest = getFormRequest(request);
            if (formRequest == null) {
                log.warn("Unable to create form request");
                response.sendRedirect(request.getResourceResolver().map(request, pagePath) + ".html?error=fields");
                return;
            }
            request.getSession().setAttribute(formRequest.getSessionId(), formRequest.getFormData());
            for (Resource actionResource : actionResources) {
                log.debug("Finding action handler for: {}", actionResource);
                for (FormAction action : formActions) {
                    if (action.handles(actionResource)) {
                        log.debug("Invoking handler: {}", action.getClass());
                        action.handleForm(actionResource, formRequest);
                        break;
                    }
                }
            }
            request.getSession().removeAttribute(formRequest.getSessionId());
        } catch (FormException e) {
            log.warn("Exception executing actions", e);
            response.sendRedirect(request.getResourceResolver().map(request, pagePath) + ".html?error=actions");
            return;
        }

        String thankYouPage = request.getResource().getValueMap().get("successPage", String.class);
        if (StringUtils.isNotBlank(thankYouPage)) {
            if ("forward".equals(request.getResource().getValueMap().get("successAction", String.class))) {
                SlingHttpServletRequestWrapper requestWrapper = new SlingHttpServletRequestWrapper(request) {
                    @Override
                    public String getMethod() {
                        return "GET";
                    }
                };

                request.getRequestDispatcher(thankYouPage).forward(requestWrapper, response);
            } else {
                response.sendRedirect(
                        request.getResourceResolver().map(request, thankYouPage) + ".html?message=success");
            }
        } else {
            response.sendRedirect(request.getResourceResolver().map(request, pagePath) + ".html?message=success");
        }
    }

    protected FormRequest getFormRequest(SlingHttpServletRequest request) throws FormException {
        FormRequest fr = request.adaptTo(FormRequest.class);
        if (fr != null) {
            ((FormRequestImpl) fr).initFields();
            return fr;
        } else {
            throw new FormException("Unable to adapt to a form request");
        }
    }
}
