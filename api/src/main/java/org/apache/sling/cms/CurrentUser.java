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
package org.apache.sling.cms;

import java.util.Collection;

/**
 * Represents the current user, adaptable from a ResourceResolver.
 */
public interface CurrentUser {

    /**
     * Gets a collection of all of the groups this user belongs to including
     * containing groups.
     * 
     * @return the groups the user belongs to
     */
    public Collection<String> getGroups();

    /**
     * Get the id of the current user.
     * 
     * @return the current user's ID
     */
    public String getId();

    /**
     * Returns true if the user is a member of the group, is the admin user or is a
     * member of the administrators group.
     * 
     * @param groupName the name of the group to check
     * @return true if the use is a member of the group or is a super user
     */
    public boolean isMember(String groupName);
}
