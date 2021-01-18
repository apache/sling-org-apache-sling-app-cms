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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableMap;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.reference.forms.impl.actions.SendEmailAction;
import org.apache.sling.cms.reference.forms.impl.fields.SelectionHandler;
import org.apache.sling.cms.reference.forms.impl.fields.TextareaHandler;
import org.apache.sling.cms.reference.forms.impl.fields.TextfieldHandler;
import org.apache.sling.commons.messaging.mail.MailService;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class FormHandlerTest {
    @Rule
    public final SlingContext context = new SlingContext();
    private FormHandler formHandler;
    private MailService mailService;
    private FormRequestImpl formRequest;

    @Before
    public void init() throws NoSuchFieldException, SecurityException, FormException {
        SlingContextHelper.initContext(context);
        context.request().setMethod("POST");
        context.request()
                .setParameterMap(ImmutableMap.<String, Object>builder().put("requiredtextarea", "Hello World!")
                        .put("singleselect", "Hello World!").put("anotherkey", "Hello World!").put("money", "123")
                        .put("patternfield", "123").put("double", "2.7").put("integer", "2")
                        .put("datefield", "2019-02-02").build());
        context.currentResource(Mockito.mock(Resource.class));

        formRequest = new FormRequestImpl(context.request(), null,
                Arrays.asList(new SelectionHandler(new SelectionHandler.Config() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return null;
                    }

                    @Override
                    public String[] supportedTypes() {
                        return new String[] { SelectionHandler.DEFAULT_RESOURCE_TYPE };
                    }

                }), new TextareaHandler(new TextareaHandler.Config() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return null;
                    }

                    @Override
                    public String[] supportedTypes() {
                        return new String[] { TextareaHandler.DEFAULT_RESOURCE_TYPE };
                    }

                }), new TextfieldHandler(new TextfieldHandler.Config() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return null;
                    }

                    @Override
                    public String[] supportedTypes() {
                        return new String[] { TextfieldHandler.DEFAULT_RESOURCE_TYPE };
                    }

                })));

        mailService = Mockito.mock(MailService.class);
        Mockito.when(mailService.getMessageBuilder()).thenReturn(new MockMessageBuilder());
        final SendEmailAction sendEmailAction = new SendEmailAction(mailService);
        formHandler = new FormHandler(Arrays.asList(sendEmailAction)) {
            private static final long serialVersionUID = 1L;

            protected FormRequest getFormRequest(final SlingHttpServletRequest request) {
                return formRequest;
            }
        };
    }

    @Test
    public void testPost() throws ServletException, IOException, FormException {

        context.request().setResource(context.resourceResolver().getResource("/form/jcr:content/container/form"));

        formHandler.service(context.request(), context.response());
        Mockito.verify(mailService).sendMessage(Mockito.any());
    }

    @Test
    public void testNoActions() throws ServletException, IOException, FormException {

        context.request()
                .setResource(context.resourceResolver().getResource("/form-no-actions/jcr:content/container/form"));

        formHandler.service(context.request(), context.response());

        assertTrue(HttpServletResponse.SC_MOVED_TEMPORARILY == context.response().getStatus());
        assertEquals("/form-no-actions.html?error=actions", context.response().getHeader("Location"));
        Mockito.verify(mailService, never()).sendMessage(Mockito.any());
    }

}
