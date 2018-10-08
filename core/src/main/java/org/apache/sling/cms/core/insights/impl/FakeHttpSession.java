/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.cms.core.insights.impl;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * A fake session to use for making server-side requests.
 */
@SuppressWarnings("deprecation")
public class FakeHttpSession implements HttpSession {

    public long getCreationTime() {
        throw new UnsupportedOperationException();
    }

    public String getId() {
        throw new UnsupportedOperationException();
    }

    public long getLastAccessedTime() {
        throw new UnsupportedOperationException();
    }

    public ServletContext getServletContext() {
        throw new UnsupportedOperationException();
    }

    public void setMaxInactiveInterval(int i) {
        throw new UnsupportedOperationException();
    }

    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException();
    }

    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException();
    }

    public Object getAttribute(String s) {
        throw new UnsupportedOperationException();
    }

    public Object getValue(String s) {
        throw new UnsupportedOperationException();
    }

    public Enumeration<?> getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    public String[] getValueNames() {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void putValue(String s, Object o) {
        throw new UnsupportedOperationException();
    }

    public void removeAttribute(String s) {
        throw new UnsupportedOperationException();
    }

    public void removeValue(String s) {
        throw new UnsupportedOperationException();
    }

    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public boolean isNew() {
        throw new UnsupportedOperationException();
    }

}