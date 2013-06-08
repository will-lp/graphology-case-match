package org.graphology.extension

import groovy.transform.CompileStatic as CS

/**
 * Matches a single condition and execute the result closure
 */
@CS class SingleMatcher extends Resolver implements Matcher {
  def matched = false
  
  void checkMatch(Object condition, Object result) {
    if (!matched) {
      matched = matches(condition)
      if (matched) {
        if (result instanceof Closure) {
          this.result = ((Closure)result)(self)
        } else {
          this.result = result
        }
      }
    }
  }
  
  
}
