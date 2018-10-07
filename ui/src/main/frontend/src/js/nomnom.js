/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
(function(nomnom) {

    var debug = false;
    
    // public
    nomnom.decorate = function(matchType, config) {
        if (debug){
            console.log("storing selector"+ matchType);
        }
        tagSelectors[matchType] = config;
        var nodes = document.querySelectorAll(matchType);
        for (var index = 0; index < nodes.length; ++index) {
            wrap(nodes[index], config);
        }
    };

    // private
    var tagSelectors = {};

    new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            mutation.addedNodes.forEach(function(node) {
                check(node);
            });
        });
    }).observe(document.body, {
        attributes : false,
        childList : true,
        subtree:true,
        characterData : false
    });

    var wrap = function(node, config) {
        if (debug){
            console.log("decorating element "+ node + node.name);
        }
        var names = Object.getOwnPropertyNames(config.prototype);
        names.forEach(function(name) {
            if (debug){
                console.log("   decorating "+ name);
            }
            if (name.indexOf("::") !== -1) {
                registerEventHandler(node, name, config.prototype[name]);
            } else {
                node[name] = config.prototype[name];
            }
        });
        if (node['init']) {
            node['init'].call(node);
        }
    };

    var registerEventHandler = function(node, propertyName, func) {
        var elementIndex = propertyName.indexOf("::");
        var eventName = propertyName.substring(0, elementIndex);
        var selector = propertyName.substring(elementIndex + 2,
                propertyName.length);
        var childSelector = eventName.split(':');
        if (childSelector[1]) {
            console.log("capture bubbling events for "
                    + childSelector[1]);
            eventName = childSelector[0];
        }
        if (selector === "this") {
            node.addEventListener(eventName, function(event) {
                if (childSelector[1]) {
                    console.log(" looking for "
                            + childSelector[1]);
                    console.log(" target is "
                            + event.target);
                    if (!event.target.matches(childSelector[1])) {
                        return;
                    }
                }
                func.call(node, event);
            });
            return;
        }
        if (selector === "handler") {
            node.addEventListener(eventName, function(event) {
                event.preventDefault();
                if (childSelector[1]) {
                    if (!event.target.matches(childSelector[1])) {
                        return;
                    }
                }
                func.call(node, event);
            });
            return;
        }
        if (selector === "document") {
            document.addEventListener(eventName, function(event) {
                if (childSelector[1]) {
                    if (!event.target.matches(childSelector[1])) {
                        return;
                    }
                }
                func.call(node, event);
            });
            return;
        }
    }

    var check = function(node) {
        if (node.querySelectorAll) {
            for ( var selector in tagSelectors) {
                if (debug) {
                    console.log("checking new nodes for "+selector);
                }
                var found = node.querySelectorAll(selector);
                if (found) {
                    found.forEach(function(item) {
                        if (debug) {
                            console.log("html node found for "+selector);
                        }
                        wrap(item, tagSelectors[selector])
                    });
                }
            }
        }
    };

    return nomnom;

})(window.nomnom = window.nomnom || {});