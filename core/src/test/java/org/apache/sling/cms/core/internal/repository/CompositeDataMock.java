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
package org.apache.sling.cms.core.internal.repository;

import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.CompositeData;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * A mock for creating a fluent API version of a CompositeData Object.
 */
public class CompositeDataMock {

    private Map<String, Object> data = new HashMap<>();

    public static CompositeDataMock init() {
        return new CompositeDataMock();
    }

    public CompositeDataMock put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public CompositeData build() {
        CompositeData dc = Mockito.mock(CompositeData.class);
        Mockito.when(dc.get(Mockito.anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return data.get(invocation.getArguments()[0]);
            }
        });
        return dc;
    }

}