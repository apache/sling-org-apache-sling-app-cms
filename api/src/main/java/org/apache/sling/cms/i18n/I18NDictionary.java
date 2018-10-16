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
package org.apache.sling.cms.i18n;

/**
 * Simplified interface for interacting with Sling's i18n dictionary.
 * 
 * @see org.apache.sling.cms.i18n.I18NProvider
 */
public interface I18NDictionary {

    /**
     * Gets the value for the specified key.
     * 
     * @param key the key to use
     * @return the value for the key
     */
    String get(String key);

    /**
     * Get the value for the specified key replacing the tokens in order. To provide
     * tokens, in your i18n value, provide them in the format:
     * 
     * <pre>
     * Here's my key, this {0} will be replaced and so will this {1}
     * </pre>
     * 
     * @param key  the key to use
     * @param args the arguments to replace
     * @return the value for the key
     */
    String get(String key, Object[] args);
}
