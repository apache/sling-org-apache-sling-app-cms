/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.sling.cms.core.operations;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.core.models.ReferenceOperation;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.SlingPostConstants;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>UpdateReferencesPostOperation</code> class will update all of the
 * references from a resource which is being moved or deleted to another
 * resource.
 */
@Component(immediate = true)
@Service
@Property(name = Constants.SERVICE_RANKING, intValue = -1)
public class UpdateReferencesPostOperation implements SlingPostProcessor {

	public static final String RP_UPDATE_REFERENCES = SlingPostConstants.RP_PREFIX + "updateReferences";

	private static final Logger log = LoggerFactory.getLogger(UpdateReferencesPostOperation.class);

	@Override
	public void process(SlingHttpServletRequest request, List<Modification> changes) throws Exception {
		if ((SlingPostConstants.OPERATION_DELETE.equals(request.getParameter(SlingPostConstants.RP_OPERATION))
				|| SlingPostConstants.OPERATION_MOVE.equals(request.getParameter(SlingPostConstants.RP_OPERATION)))
				&& "true".equalsIgnoreCase(request.getParameter(RP_UPDATE_REFERENCES))) {

			final String find = request.getResource().getPath();
			final String destination = request.getParameter(SlingPostConstants.RP_DEST);
			log.debug("Using destination: {}", destination);
			ReferenceOperation ro = new ReferenceOperation(request.getResource()) {
				@Override
				public void doProcess(Resource resource, String matchingKey) {
					ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
					log.trace("Updating references in property {}@{}", resource.getPath(), matchingKey);
					if (properties.get(matchingKey) instanceof String) {
						String value = properties.get(matchingKey, String.class).replace(find, destination);
						properties.put(matchingKey, value);
						log.trace("Updated value {}", value);
					} else if (properties.get(matchingKey) instanceof String[]) {
						String[] values = properties.get(matchingKey, String[].class);
						for (int i = 0; i < values.length; i++) {
							values[i] = values[i].replace(find, destination);
						}
						properties.put(matchingKey, values);
						log.trace("Updated values {}", Arrays.toString(values));
					}
					changes.add(Modification.onModified(resource.getPath()));
				}
			};
			ro.init();
		}
	}
}
