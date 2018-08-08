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
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A TransformerFactory service instance for creating a Transformer to rewrite
 * all of the links, images, etc which reference other content in the Sling
 * repository.
 */
@Component(property = { "pipeline.type=referencemapping" }, service = {
		TransformerFactory.class }, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ReferenceMappingTransformerConfig.class)
public class ReferenceMappingTransformerFactory implements TransformerFactory {

	private static final Logger log = LoggerFactory.getLogger(ReferenceMappingTransformerFactory.class);
	private ReferenceMappingTransformerConfig config;

	@Reference
	private ConfigurationResourceResolver resolver;

	
	@Activate
    public void activate(ReferenceMappingTransformerConfig config) {
        this.config = config;
    }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.rewriter.TransformerFactory#createTransformer()
	 */
	@Override
	public Transformer createTransformer() {
		log.trace("createTransformer");
		return new ReferenceMappingTransformer(config, resolver);
	}

}
