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

package org.graphology.extension

import groovy.transform.CompileStatic as CS

/**
 * Matches a single condition but doesn't execute the result closure
 * 
 * @author will_lp
 */
@CS class SingleLazyMatcher extends AbstractMatcher {
  def matched = false
  
  void when(Object condition, Object result) {
    if (!matched) {
      matched = matches(condition)
      if (matched) {
        if (result instanceof Closure) {
          this.result = ((Closure)result).curry(self)
        } else {
          this.result = result
        }
      }
    }
  }
  
  
  Object getMatchedResult() {
    matched ? result : otherwiseValue
  }
  
}


