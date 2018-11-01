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

    if (!Element.prototype.matches) {
        Element.prototype.matches = Element.prototype.msMatchesSelector;
    }
    
    if (typeof NodeList.prototype.forEach !== "function" && typeof Array.prototype.forEach === "function")  {
        NodeList.prototype.forEach = Array.prototype.forEach;
    }

    var tagSelectors = {};
    var debug = false;
    var elementMap = new WeakMap();
    
  // public
  nomnom.decorate = function(selector, config) {
    if (debug) {
          console.log("storing selector" + selector);
      }
      tagSelectors[selector] = config;
      var foundItems = document.querySelectorAll(selector);
      if (foundItems) {
          foundItems.forEach(function(node) {
              wrap(node, config);
          });
      }
  };

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
      var configSet = elementMap.get(node);
      if (!configSet) {
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
          }
          if (name === "methods") {
              handleMethods(node, config[name]);
          }
      });
      keys.forEach(function(key) {
          if (debug) {
              console.log("   decorating " + key);
          }
          node[key] = config[key];
      });
      if (config.callbacks) {
          if (config.callbacks.created){
              config.callbacks.created.call(node);
          }
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
  
  var handleMethods = function(node, funcs) {
      for ( var funcName in funcs) {
          node[funcName] = funcs[funcName];
      }
  };

  var registerEventHandlers = function(node, data, events) {
      for ( var eventName in events) {
          var possibleFunc = events[eventName];
          var targetNode = node;
          if (typeof possibleFunc !== "object") {
              targetNode.addEventListener(eventName, function(event) {
                  possibleFunc.call(node, event, data);
              });
          } else {
              var selector = eventName;
              if (selector === "document") {
                  targetNode = document;
              }
              for ( var childEventName in possibleFunc) {
                  var func = possibleFunc[childEventName];
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
      var checkSet = new Set();
      checkSet.add(node);
      checkSet.forEach(function(foundElement){
          if (foundElement.querySelectorAll) {
              check(foundElement);
          }
          var foundElements = foundElement.children;
          if (foundElements){
              for (var i = 0; i < foundElements.length; i++) {
                  checkSet.add(foundElements[i]);
              }
          }
          checkSet["delete"](foundElement);
      });
  }

  var check = function(node) {
      for ( var selector in tagSelectors) {
          var found = false;
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