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
package org.apache.sling.cms.core.publication;

import static org.junit.Assert.assertEquals;

import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.publication.INSTANCE_TYPE;
import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class PublicationPropertyProviderTest {

    @Rule
    public final SlingContext context = new SlingContext();

    private @NotNull PublicationPropertyProvider provider;

    @Before
    public void init() {
        SlingCMSTestHelper.initContext(context);

        PublicationManagerFactory factory = Mockito.mock(PublicationManagerFactory.class);

        Mockito.when(factory.getInstanceType()).thenReturn(INSTANCE_TYPE.RENDERER);

        context.registerService(PublicationManagerFactory.class, factory);

        provider = context.registerInjectActivateService(new PublicationPropertyProvider());
    }

    @Test
    public void testGetEntryPoint() {
        String expected = "/sling";
        PublicationPropertyProviderConfig config = Mockito.mock(PublicationPropertyProviderConfig.class);
        Mockito.when(config.endpointPath()).thenReturn(expected);
        provider.activate(config);
        assertEquals(expected, provider.getProperty(PublicationPropertyProvider.ENDPOINT_PATHS));
    }

    @Test
    public void testGetInstanceType() {
        assertEquals(INSTANCE_TYPE.RENDERER.toString(),
                provider.getProperty(PublicationPropertyProvider.INSTANCE_TYPE));
    }

}
