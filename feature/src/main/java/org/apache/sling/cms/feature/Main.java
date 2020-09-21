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
package org.apache.sling.cms.feature;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Bootstraping Sling CMS Feature Model");
        URL propertiesUrl =  Main.class.getClassLoader().getResource("slingcms.properties");
        Properties properties = new Properties();
        properties.load(propertiesUrl.openStream());

        String version = properties.getProperty("version");
        System.out.println("Version "+version);

        URL farUrl = Main.class.getClassLoader().getResource("lib/slingcms.far");
        List<String> arguments = new ArrayList<>();
        arguments.addAll(Arrays.asList(args));
        if(!arguments.contains("-f")){
            arguments.add("-f");
            arguments.add(farUrl.toString());
        }

        org.apache.sling.feature.launcher.impl.Main.main(arguments.toArray(new String[arguments.size()]));
    }
}