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

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

import org.apache.sling.commons.messaging.mail.MessageBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mockito.Mockito;

public class MockMessageBuilder implements MessageBuilder {

    @Override
    public @NotNull MessageBuilder to(@NotNull final String address, @NotNull final String name)
            throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder to(@NotNull final String address) throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder to(@NotNull final InternetAddress to) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder text(@NotNull final String text) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder subject(@NotNull final String subject) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder replyTo(@NotNull final String address, final String name) throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder replyTo(@NotNull final String address) throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder replyTo(@NotNull final InternetAddress replyTo) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder inline(@NotNull final byte[] content, @NotNull final String type,
            @NotNull final String cid, @Nullable final Header[] headers) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder inline(@NotNull final byte[] content, @NotNull final String type,
            @NotNull final String cid) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder html(@NotNull final String html) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder headers(@NotNull final InternetHeaders headers) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder header(@NotNull final String name, @Nullable final String value) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder from(@NotNull final String address, @NotNull final String name)
            throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder from(@NotNull final String address) throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder from(@NotNull final InternetAddress from) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder cc(@NotNull final String address, @NotNull final String name)
            throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder cc(@NotNull final String address) throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder cc(@NotNull final InternetAddress cc) {
        return this;
    }

    @Override
    public @NotNull MimeMessage build() throws MessagingException {
        return Mockito.mock(MimeMessage.class);
    }

    @Override
    public @NotNull MessageBuilder bcc(@NotNull final String address, final String name) throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder bcc(@NotNull final String address) throws AddressException {
        return this;
    }

    @Override
    public @NotNull MessageBuilder bcc(@NotNull final InternetAddress bcc) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder attachment(@NotNull final byte[] content, @NotNull final String type,
            @NotNull final String filename, @Nullable final Header[] headers) {
        return this;
    }

    @Override
    public @NotNull MessageBuilder attachment(@NotNull final byte[] content, @NotNull final String type,
            @NotNull final String filename) {
        return this;
    }

}