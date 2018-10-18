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
package org.apache.sling.cms.core.i18n.impl;

import java.util.ResourceBundle;

import org.apache.sling.cms.i18n.I18NDictionary;

/**
 * Implementation of the I18NDictionary interface
 */
public class I18NDictionaryImpl implements I18NDictionary {

    private ResourceBundle bundle;

    protected I18NDictionaryImpl(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public String get(String key) {
        return bundle.getString(key);
    }

    @Override
    public String get(String key, Object[] args) {
        String localized = get(key);

        if (localized == null) {
            return key;
        }
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object o = args[i];
                if (o != null) {
                    localized = localized.replace("{" + i + "}", o.toString());
                }
            }
        }
        return localized;
    }

}
