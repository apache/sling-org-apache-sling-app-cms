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
package org.apache.sling.cms.reference.form.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.reference.forms.impl.FormHandler;
import org.apache.sling.cms.reference.forms.impl.FormRequestImpl;
import org.apache.sling.cms.reference.forms.impl.actions.SendEmailAction;
import org.apache.sling.cms.reference.forms.impl.actions.SendEmailActonConfig;
import org.apache.sling.cms.reference.forms.impl.fields.SelectionHandler;
import org.apache.sling.cms.reference.forms.impl.fields.TextareaHandler;
import org.apache.sling.cms.reference.forms.impl.fields.TextfieldHandler;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import com.google.common.collect.ImmutableMap;

public class FormHandlerTest {
    @Rule
    public final SlingContext context = new SlingContext();
    private FormHandler formHandler;

    @Before
    public void init() throws NoSuchFieldException, SecurityException, FormException {
        SlingContextHelper.initContext(context);
        context.request().setMethod("POST");
        context.request().setResource(context.resourceResolver().getResource("/form/jcr:content/container/form"));

        context.request()
                .setParameterMap(ImmutableMap.<String, Object>builder().put("requiredtextarea", "Hello World!")
                        .put("singleselect", "Hello World!").put("anotherkey", "Hello World!").put("money", "123")
                        .put("patternfield", "123").put("double", "2.7").put("integer", "2")
                        .put("datefield", "2019-02-02").build());

        FormRequestImpl formRequest = new FormRequestImpl(context.request());

        FieldSetter.setField(formRequest, formRequest.getClass().getDeclaredField("fieldHandlers"),
                Arrays.asList(new SelectionHandler(), new TextareaHandler(), new TextfieldHandler()));

        formRequest.init();

        formHandler = new FormHandler() {
            private static final long serialVersionUID = 1L;

            protected FormRequest getFormRequest(SlingHttpServletRequest request) {
                return formRequest;
            }
        };

        SendEmailAction sendEmailAction = new SendEmailAction();
        sendEmailAction.activate(new SendEmailActonConfig() {

            @Override
            public String hostName() {
                return "smtp.mailtrap.io";
            }

            @Override
            public int smtpPort() {
                return 587;
            }

            @Override
            public boolean tlsEnabled() {
                return true;
            }

            @Override
            public String username() {
                return "e7cfc0e9bb9b80";
            }

            @Override
            public String password() {
                return "b9902898ce236a";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

        });
        FieldSetter.setField(sendEmailAction, SendEmailAction.class.getDeclaredField("jobManager"),
                Mockito.mock(JobManager.class));

        FieldSetter.setField(formHandler, FormHandler.class.getDeclaredField("formActions"),
                Arrays.asList(sendEmailAction));
        context.request().setMethod("POST");
    }

    @Test
    public void testPost() throws ServletException, IOException {
        formHandler.service(context.request(), context.response());
    }

}
