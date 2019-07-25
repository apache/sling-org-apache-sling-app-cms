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
package org.apache.sling.cms.reference.form.impl.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.form.impl.SlingContextHelper;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.reference.forms.impl.FormRequestImpl;
import org.apache.sling.cms.reference.forms.impl.actions.SendEmailAction;
import org.apache.sling.cms.reference.forms.impl.actions.SendEmailActonConfig;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

public class SendEmailActionTest {

    @Rule
    public final SlingContext context = new SlingContext();
    private SendEmailAction action;
    private ResourceResolver resolver;

    @Before
    public void init() throws NoSuchFieldException, SecurityException {
        SlingContextHelper.initContext(context);
        context.request().setResource(context.resourceResolver().getResource("/form/jcr:content/container/form"));

        resolver = context.resourceResolver();
        action = new SendEmailAction();

        FieldSetter.setField(action, action.getClass().getDeclaredField("jobManager"), Mockito.mock(JobManager.class));

        action.activate(new SendEmailActonConfig() {

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
    }

    @Test
    public void testHandles() {

        assertTrue(action.handles(resolver.getResource("/form/jcr:content/container/form/actions/sendemail")));

        assertFalse(action.handles(resolver.getResource("/form/jcr:content/container/form/actions")));

        assertFalse(action.handles(resolver.getResource("/form/jcr:content/container/form/actions/updateprofile")));

    }

    @Test
    public void testHandleForm() throws FormException {

        FormRequest formRequest = new FormRequestImpl(context.request());
        FormActionResult result = action
                .handleForm(resolver.getResource("/form/jcr:content/container/form/actions/sendemail"), formRequest);

        assertTrue(result.isSucceeded());
    }

    @Test
    public void testProcess() throws FormException {

        Job job = Mockito.mock(Job.class);
        Mockito.when(job.getProperty(SendEmailAction.TO, String.class)).thenReturn("test@user.com");
        Mockito.when(job.getProperty(SendEmailAction.FROM, String.class)).thenReturn("another@user.com");
        Mockito.when(job.getProperty(SendEmailAction.SUBJECT, String.class)).thenReturn("SLING CMS");
        Mockito.when(job.getProperty(SendEmailAction.MESSAGE, String.class)).thenReturn("WOW!");

        JobResult result = action.process(job);
        assertTrue(result == JobResult.OK);
    }

}
