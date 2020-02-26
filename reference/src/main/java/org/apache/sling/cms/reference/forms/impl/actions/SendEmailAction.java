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
package org.apache.sling.cms.reference.forms.impl.actions;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.text.StringSubstitutor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.commons.messaging.mail.MailService;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { FormAction.class })
public class SendEmailAction implements FormAction {

    public static final String FROM = "from";
    private static final Logger log = LoggerFactory.getLogger(SendEmailAction.class);

    public static final String MESSAGE = "message";
    public static final String SUBJECT = "subject";
    public static final String TO = "to";

    @Reference
    private MailService mailService;

    @Override
    public FormActionResult handleForm(final Resource actionResource, final FormRequest request) throws FormException {
        final StringSubstitutor sub = new StringSubstitutor(request.getFormData());

        final ValueMap properties = actionResource.getValueMap();
        final String to = sub.replace(properties.get(TO, String.class));
        log.debug("Sending message to {}", to);

        try {
            @NotNull
            final MimeMessage message = mailService.getMessageBuilder()
                    .from(sub.replace(properties.get(FROM, String.class)))
                    .to(to)
                    .subject(sub.replace(properties.get(SUBJECT, String.class)))
                    .text(sub.replace(properties.get(MESSAGE, String.class))).build();

            log.debug("Sending message...");
            mailService.sendMessage(message);
            log.debug("Message sent successfully!");
            return FormActionResult.success("Message sent successfully!");
        } catch (final MessagingException e) {
            log.error("Failed to send message", e);
            return FormActionResult.failure("Failed to send message: " + e.toString());
        }
    }

    @Override
    public boolean handles(final Resource actionResource) {
        return "reference/components/forms/actions/sendemail".equals(actionResource.getResourceType());
    }

}
