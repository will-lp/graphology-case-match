/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// package org.graphology.ast
// 
// import org.codehaus.groovy.ast.expr.MethodCallExpression as MCE
// 
// class MethodMatcher {
  // 
  // def log = Log.instance
  // 
  // private methods
  // 
  // MethodMatcher() {
    // def stdMap = [matched: false, methodExpression: null]
    // 
    // methods = [:]
    // 
    // GraphologyVisitor.GRAPHOLOGY_METHODS.each {
      // methods[it] = stdMap.clone()
    // }
    // 
    // methods.whenThen = stdMap.clone() 
  // }
  // 
  // 
  // def match(MCE method) {
    // def name = method.text
    // 
    // boolean whenThen = ( name ==~ /this.when\(.*\).then\(.*\)/ )
    // 
    // log.debug "method '${name}' matched 'when/then' pattern: = $whenThen"
    // 
    // if (whenThen) {
      // methods.whenThen.matched = true
      // methods.whenThen.methodExpression = method
      // true
    // } else if (method.methodAsString in GraphologyVisitor.GRAPHOLOGY_METHODS) {
      // methods[ method.methodAsString ].matched = true
      // methods[ method.methodAsString ].methodExpression = method
      // true
    // } else {
      // false
    // }
    // 
  // }
  // 
  // def getWhenThenExpression() {
    // methods.whenThen.methodExpression
  // }
  // 
  // def matchedAll() { 
    // methods.whenThen.matched && (methods.any {
      // it.key in GraphologyVisitor.GRAPHOLOGY_METHODS && it.value.matched 
    // })
  // }
  // 
// }

