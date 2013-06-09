package org.graphology.extension

import groovy.transform.CompileStatic as CS

/**
 * Matches a single condition but doesn't execute the result closure
 */
@CS class SingleLazyMatcher extends Resolver implements Matcher {
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
  
}

