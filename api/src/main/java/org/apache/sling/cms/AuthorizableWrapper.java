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

import java.util.Iterator;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for working with JackRabbit Authorizables in JSPs and Sling Models
 * from a Resource
 */
public interface AuthorizableWrapper {

    /**
     * Gets the JackRabbit Authorizable
     * 
     * @return a JackRabbit Authorizable
     */
    @NotNull
    Authorizable getAuthorizable();

    /**
     * Gets the declared members of this authorizable. For Users this will return an
     * empty iterator.
     * 
     * @return the declared members of this authorizable
     */
    @NotNull
    Iterator<Authorizable> getDeclaredMembers();

    /**
     * Get the groups this authorizable is a member of
     * 
     * @return the direct membership
     */
    @NotNull
    Iterator<Group> getDeclaredMembership();

    /**
     * Gets a collection of all of the groups this user belongs to including
     * containing groups.
     * 
     * @return the groups the user belongs to
     */
    @NotNull
    Iterator<String> getGroupNames();

    /**
     * Get the id of the current user.
     * 
     * @return the current user's ID
     */
    @NotNull
    public String getId();

    /**
     * Gets the transitive members of this authorizable. For Users this will return
     * an empty iterator.
     * 
     * @return the transitive members of this authorizable
     */
    @NotNull
    Iterator<Authorizable> getMembers();

    /**
     * Gets the transitive membership of this authorizable
     * 
     * @return the transitive membership
     */
    @NotNull
    Iterator<Group> getMembership();

    /**
     * Returns true if the authorizable is a user and is the admin user or is a
     * member of the administrators group.
     * 
     * @return true if the user is a super user
     */
    public boolean isAdministrator();

    /**
     * Returns true if the authorizable is a member of the group
     * 
     * @param groupName the name of the group to check
     * @return true if the authorizable is a member of the group
     */
    public boolean isMember(String groupName);

}
