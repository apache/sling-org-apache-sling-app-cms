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
/* eslint-env es6, browser */
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
        var data = config.data;
        names.forEach(function(name) {
            if (name === "constructor") {
                return;
            }
            if (debug) {
                console.log("   decorating " + name);
            }
            if (name === "events") {
                registerEventHandlers(node, data, config[name]);
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
    var targetedEventHandler = function(fn, correctTarget, data) {
        return function(event) {
            if (!event.target.matches(correctTarget)) {
                return;
            }
            fn.call(this, event, data);
        };
    };

    var registerEventHandlers = function(node, data, events) {
        for ( var eventName in events) {
            let possibleFunc = events[eventName];
            let targetNode = node;
            if (typeof possibleFunc !== "object") {
                targetNode.addEventListener(eventName, function(event) {
                    possibleFunc.call(node, event, data);
                });
            } else {
                let selector = eventName;
                if (selector === "document") {
                    targetNode = document;
                }
                for ( var childEventName in possibleFunc) {
                    let func = possibleFunc[childEventName];
                    if (selector !== "document"){
                        func = targetedEventHandler(func, selector, data);
                    }
                    targetNode.addEventListener(childEventName,
                            function(event) {
                                func.call(node, event, data);
                            });
                }
            }
        }
    };

    var checkAll = function(node) {
        var checkSet = new Set([node]);
        checkSet.forEach(function(element){
            if (element.querySelectorAll) {
                check(element);
            }
            let elements = element.children;
            if (elements){
                for (let i = 0; i < elements.length; i++) {
                    checkSet.add(elements[i]);
                }
            }
            checkSet.delete(element);
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