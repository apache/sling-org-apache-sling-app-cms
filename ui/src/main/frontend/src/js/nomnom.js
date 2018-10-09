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

    // public
    nomnom.decorate = function(selector, config) {
        if (debug) {
            console.log("storing selector" + selector);
        }
        tagSelectors[selector] = config;
        document.querySelectorAll(selector).forEach(function(node){
            wrap(node, config);
        });
    };

    var tagSelectors = {};
    var debug = false;
    var elementMap = new WeakMap();

    new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            mutation.addedNodes.forEach(function(node) {
                check(node);
            });
        });
    }).observe(document.body, {
        attributes : false,
        childList : true,
        subtree : true,
        characterData : false
    });

    var wrap = function(node, config) {
        if (debug) {
            console.log("decorating element " + node + node.name);
        }
        var configSet;
        if (elementMap.has(node)) {
            configSet = elementMap.get(node);
        } else {
            configSet = new Set();
            elementMap.set(node, configSet);
        }
        if (configSet.has(config)) {
            return;
        }
        configSet.add(config);
        var configInstance = new config();
        var names = Object.getOwnPropertyNames(config.prototype);
        var keys = Object.keys(configInstance);
        names.forEach(function(name) {
            if (name === "constructor") {
                return;
            }
            if (debug) {
                console.log("   decorating " + name);
            }
            if (name.indexOf("::") !== -1) {
                registerEventHandler(node, name, configInstance[name]);
            } else {
                node[name] = configInstance[name];
            }
        });
        keys.forEach(function(key) {
            if (debug) {
                console.log("   decorating " + key);
            }
            node[key] = configInstance[key];
        });
        if (node['initCallback']) {
            node['initCallback'].call(node);
        }
    };

    // generic function to wrap the event handler in the case that
    // we only want it to fire for a specific child event
    var targetedEventHandler = function(fn, correctTarget) {
        return function(event) {
            if (!event.target.matches(correctTarget)) {
                return;
            }
            fn.apply(this, arguments);
        };
    };

    // generic function to disable default event bubbling
    var disableEventDefault = function(fn) {
        return function(event) {
            event.preventDefault();
            fn.apply(this, arguments);
        };
    };

    var registerEventHandler = function(node, propertyName, func) {
        var elementIndex = propertyName.indexOf("::");
        var eventName = propertyName.substring(0, elementIndex);
        var selector = propertyName.substring(elementIndex + 2,
                propertyName.length);
        var childSelector = eventName.split(':');
        if (childSelector[1]) {
            console.log("capture bubbling events for " + childSelector[1]);
            eventName = childSelector[0];
            func = targetedEventHandler(func, childSelector[1]);
        }
        if (selector === "handle") {
            func = disableEventDefault(func);
        }
        if (selector === "listen" || selector === "handle") {
            node.addEventListener(eventName, function(event) {
                func.call(node, event);
            });
            return;
        }
        if (selector === "document") {
            document.addEventListener(eventName, function(event) {
                func.call(node, event);
            });
            return;
        }
    }

    var check = function(node) {
        if (!node.querySelectorAll) {
            return;
        }
        for ( var selector in tagSelectors) {
            if (debug) {
                console.log("checking new nodes for " + selector);
            }
            if (node.matches(selector)) {
                wrap(node, tagSelectors[selector]);
                return;
            }
            var found = node.querySelectorAll(":scope " + selector);
            if (!found) {
                return;
            }
            found.forEach(function(item) {
                if (debug) {
                    console.log("node found for " + selector);
                }
                wrap(item, tagSelectors[selector])
            });
        }
    };

    return nomnom;

})(window.nomnom = window.nomnom || {});