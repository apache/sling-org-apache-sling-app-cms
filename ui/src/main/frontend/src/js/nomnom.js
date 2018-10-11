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
        document.querySelectorAll(selector).forEach(function(node) {
            wrap(node, config);
        });
    };

    var tagSelectors = {};
    var debug = false;
    var elementMap = new WeakMap();

    new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            mutation.addedNodes.forEach(function(node) {
                checkAll(node);
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
        var names = Object.getOwnPropertyNames(config);
        var keys = Object.keys(config);
        names.forEach(function(name) {
            if (name === "constructor") {
                return;
            }
            if (debug) {
                console.log("   decorating " + name);
            }
            if (name === "events") {
                registerEventHandlers(node, name, config[name]);
            } else {
                node[name] = config[name];
            }
        });
        keys.forEach(function(key) {
            if (debug) {
                console.log("   decorating " + key);
            }
            node[key] = config[key];
        });
        if (node.initCallback) {
            node.initCallback.call(node);
        }
    };

    // generic function to wrap the event handler in the case that
    // we only want it to fire for a specific child event
    var targetedEventHandler = function(fn, correctTarget) {
        return function(event) {
            if (!event.target.matches(correctTarget)) {
                return;
            }
            fn.call(this, event);
        };
    };

    var registerEventHandlers = function(node, propertyName, events) {
        for ( var eventName in events) {
            let possibleFunc = events[eventName];
            if (typeof possibleFunc === "function") {
                node.addEventListener(eventName, function(event) {
                    possibleFunc.call(node, event);
                });
            } else {
                let selector = eventName;
                let targetNode = node;
                for ( var childEventName in possibleFunc) {
                    let func = targetedEventHandler(
                            possibleFunc[childEventName], selector);
                    if (selector == "document") {
                        targetNode = document;
                    }
                    targetNode.addEventListener(childEventName,
                            function(event) {
                                func.call(node, event);
                            });
                }
            }
        }
    };

    var checkAll = function(node) {
        if (!node.querySelectorAll) {
            return;
        }
        var checkSet = new Set([node]);
        checkSet.forEach(function(node){
            let elements = node.children;
            for (let i = 0; i < elements.length; i++) {
                let element = elements[i];
                if (element.querySelectorAll) {
                    check(element);
                    checkSet.add(element);
                }
            }
            checkSet.delete(node);
        });
    }
    
    var check = function(node) {
        for ( var selector in tagSelectors) {
            let found = false;
            if (debug) {
                console.log("checking nodes for " + selector);
            }
            if (node.matches(selector)) {
                found = true;
                wrap(node, tagSelectors[selector]);
            }
            if (found && debug) {
                console.log("node found for " + selector);
            }
        }
    };

    return nomnom;

})(window.nomnom = window.nomnom || {});