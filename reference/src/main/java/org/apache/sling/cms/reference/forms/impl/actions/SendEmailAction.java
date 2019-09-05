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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { FormAction.class, JobConsumer.class }, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + SendEmailAction.TOPIC }, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SendEmailActonConfig.class)
public class SendEmailAction implements JobConsumer, FormAction {

    public static final String FROM = "from";
    private static final Logger log = LoggerFactory.getLogger(SendEmailAction.class);

    public static final String MESSAGE = "message";
    public static final String SUBJECT = "subject";
    public static final String TO = "to";
    public static final String TOPIC = "reference/form/sendemail";

    private SendEmailActonConfig config;

    @Reference
    private JobManager jobManager;

    @Activate
    public void activate(SendEmailActonConfig config) {
        this.config = config;
    }

    @Override
    public FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException {
        StrSubstitutor sub = new StrSubstitutor(request.getFormData());

        ValueMap properties = actionResource.getValueMap();
        String to = sub.replace(properties.get(TO, String.class));
        log.debug("Queueing contact message to {}", to);

        Map<String, Object> data = new HashMap<>();
        data.put(SendEmailAction.SUBJECT, sub.replace(properties.get(SUBJECT, String.class)));
        data.put(SendEmailAction.MESSAGE, sub.replace(properties.get(MESSAGE, String.class)));
        data.put(SendEmailAction.TO, to);
        data.put(SendEmailAction.FROM, sub.replace(properties.get(FROM, String.class)));
        jobManager.addJob(TOPIC, data);
        log.debug("Job queued successfully!");
        return FormActionResult.success("Email queued successfully!");
    }

    @Override
    public boolean handles(Resource actionResource) {
        return "reference/components/forms/actions/sendemail".equals(actionResource.getResourceType());
    }

    public JobResult process(final Job job) {
        log.trace("process");

        try {

            log.debug("Configuring connection to {}:{} with username {}", config.hostName(), config.smtpPort(),
                    config.username());
            Email email = new SimpleEmail();
            email.setHostName(config.hostName());
            email.setSmtpPort(config.smtpPort());
            email.setAuthenticator(new DefaultAuthenticator(config.username(), config.password()));
            email.setStartTLSEnabled(config.tlsEnabled());

            String from = job.getProperty(FROM, String.class);
            String to = job.getProperty(TO, String.class);
            String subject = job.getProperty(SUBJECT, String.class);
            String message = job.getProperty(MESSAGE, String.class);
            log.debug("Sending email from {} to {} with subject {}", from, to, subject);

            email.setFrom(from);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(to);

            email.send();
        } catch (EmailException e) {
            log.warn("Exception sending email for job " + job.getId(), e);
            return JobResult.FAILED;
        }

        // process the job and return the result
        return JobResult.OK;
    }
}
