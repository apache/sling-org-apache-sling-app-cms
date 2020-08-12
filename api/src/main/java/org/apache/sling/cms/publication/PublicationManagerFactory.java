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
package org.apache.sling.cms.publication;

/**
 * Factory for retrieving the Publication Manager for the instance
 * configuration.
 */
public interface PublicationManagerFactory {

    /**
     * Returns the publication mode for this instance.
     * 
     * @return the publication mode fro the instance
     */
    PUBLICATION_MODE getPublicationMode();

    /**
     * Retrieves the publication manager applicable for this instance based on the
     * current publication mode.
     * 
     * @return the publication manager
     */
    PublicationManager getPublicationManager();
}