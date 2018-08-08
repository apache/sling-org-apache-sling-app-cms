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
package org.apache.sling.cms.core.internal.rewriter;

import org.apache.sling.caconfig.resource.ConfigurationResourceResolver;
import org.apache.sling.rewriter.Serializer;
import org.apache.sling.rewriter.SerializerFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * This sax serializer serializes HTML5 Compliant Markup
 */
@Component(service = SerializerFactory.class, property = { Constants.SERVICE_VENDOR + "=The Apache Software Foundation",
		"pipeline.type=html5-serializer" })
public class Html5SerializerFactory implements SerializerFactory {

	@Reference
	private ConfigurationResourceResolver resolver;

	/**
	 * @see org.apache.sling.rewriter.SerializerFactory#createSerializer()
	 */
	@Override
	public Serializer createSerializer() {
		return new HTML5Serializer(resolver);
	}

}
