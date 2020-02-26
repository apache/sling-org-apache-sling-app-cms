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

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.form.impl.MockMessageBuilder;
import org.apache.sling.cms.reference.form.impl.SlingContextHelper;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.reference.forms.impl.FormRequestImpl;
import org.apache.sling.cms.reference.forms.impl.actions.SendEmailAction;
import org.apache.sling.commons.messaging.mail.MailService;
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
    private MailService mailService;

    @Before
    public void init() throws NoSuchFieldException, SecurityException {
        SlingContextHelper.initContext(context);
        context.request().setResource(context.resourceResolver().getResource("/form/jcr:content/container/form"));

        resolver = context.resourceResolver();
        action = new SendEmailAction();

        mailService = Mockito.mock(MailService.class);
        Mockito.when(mailService.getMessageBuilder()).thenReturn(new MockMessageBuilder());

        FieldSetter.setField(action, action.getClass().getDeclaredField("mailService"), mailService);
    }

    @Test
    public void testHandles() {

        assertTrue(action.handles(resolver.getResource("/form/jcr:content/container/form/actions/sendemail")));

        assertFalse(action.handles(resolver.getResource("/form/jcr:content/container/form/actions")));

        assertFalse(action.handles(resolver.getResource("/form/jcr:content/container/form/actions/updateprofile")));

    }

    @Test
    public void testHandleForm() throws FormException {

        final FormRequest formRequest = new FormRequestImpl(context.request());
        final FormActionResult result = action
                .handleForm(resolver.getResource("/form/jcr:content/container/form/actions/sendemail"), formRequest);

        assertTrue(result.isSucceeded());
        Mockito.verify(mailService).sendMessage(Mockito.any());
    }

}
