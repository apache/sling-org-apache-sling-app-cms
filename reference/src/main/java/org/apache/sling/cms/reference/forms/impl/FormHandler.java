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
import org.apache.commons.text.StringSubstitutor;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.PageManager;
import org.apache.sling.cms.ResourceTree;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.resourceTypes=reference/components/forms/form",
        "sling.servlet.methods=POST", "sling.servlet.extensions=html", "sling.servlet.selectors=allowpost" })
public class FormHandler extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(FormHandler.class);

    private transient List<FormAction> formActions;

    private static final long serialVersionUID = -8149443208959899098L;

    @Activate
    public FormHandler(@Reference(policyOption = ReferencePolicyOption.GREEDY) List<FormAction> formActions) {
        this.formActions = formActions;
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        ValueMap properties = request.getResource().getValueMap();

        String pagePath = Optional.ofNullable(request.getResource().adaptTo(PageManager.class))
                .map(PageManager::getPage).map(Page::getPath)
                .orElse(StringUtils.substringBefore(request.getResource().getPath(), "/" + JcrConstants.JCR_CONTENT));
        String successPage = null;
        String errorPage = pagePath;

        StringSubstitutor sub = null;
        try {
            log.debug("Extracting form request...");
            FormRequest formRequest = request.adaptTo(FormRequest.class);
            if (formRequest == null) {
                log.warn("Unable to create form request");
                response.sendRedirect(this.resolveUrl(request, errorPage, "error=init"));
                return;
            }

            log.debug("Loading fields...");
            boolean fieldsLoadSucceeded = ((FormRequestImpl) formRequest).initFields();
            sub = new StringSubstitutor(formRequest.getFormData());
            errorPage = sub.replace(properties.get("errorPage", pagePath));
            if (!fieldsLoadSucceeded) {
                log.warn("Field initialization failed, check logs");
                response.sendRedirect(this.resolveUrl(request, errorPage, "error=fields"));
                return;
            }
            request.getSession().setAttribute(formRequest.getSessionId(), formRequest.getFormData());

            log.debug("Calling actions...");
            callActions(request, formRequest);
            successPage = sub.replace(properties.get("successPage", pagePath));
            request.getSession().removeAttribute(formRequest.getSessionId());
        } catch (FormException e) {
            log.warn("Exception executing actions", e);
            response.sendRedirect(request.getResourceResolver().map(request, pagePath) + ".html?error=actions");
            return;
        }

        if (StringUtils.isNotBlank(successPage)) {
            if ("forward".equals(properties.get("successAction", String.class))) {
                SlingHttpServletRequestWrapper requestWrapper = new SlingHttpServletRequestWrapper(request) {
                    @Override
                    public String getMethod() {
                        return "GET";
                    }
                };
                request.getRequestDispatcher(successPage).forward(requestWrapper, response);
            } else {
                response.sendRedirect(resolveUrl(request, successPage, "message=success"));
            }
        } else {
            response.sendRedirect(resolveUrl(request, pagePath, "message=success"));
        }
    }

    private void callActions(SlingHttpServletRequest request, FormRequest formRequest) throws FormException {
        Resource actions = request.getResource().getChild("actions");
        if (actions == null) {
            throw new FormException("No actions provided to handle this form submission");
        }
        List<Resource> actionResources = ResourceTree.stream(actions).map(ResourceTree::getResource)
                .collect(Collectors.toList());

        for (Resource actionResource : actionResources) {
            log.debug("Finding action handler for: {}", actionResource);
            FormAction action = formActions.stream().filter(fa -> fa.handles(actionResource)).findFirst().orElse(null);
            if (action != null) {
                FormActionResult result = action.handleForm(actionResource, formRequest);
                if (!result.isSucceeded()) {
                    throw new FormException(
                            "Failed to invoke action: " + action + " with message: " + result.getMessage());
                } else {
                    log.debug("Successfully invoked action: {}", result.getMessage());
                }
            }
        }
    }

    private String resolveUrl(SlingHttpServletRequest request, String url, String qs) {
        if (url.contains("?")) {
            qs = "&" + qs;
        } else {
            qs = "?" + qs;
        }
        if (url.startsWith("/")) {
            if (!url.contains(".html")) {
                url += ".html";
            }
            url += qs;
            return request.getResourceResolver().map(request, url);
        } else {
            return url + qs;
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
