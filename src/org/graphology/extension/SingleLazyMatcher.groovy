package org.graphology.extension

import groovy.transform.CompileStatic as CS

/**
 * Matches a single condition but doesn't execute the result closure
 */
@CS class SingleLazyMatcher extends Resolver implements Matcher {
  def matched = false
  
  void checkMatch(Object condition, Object result) {
    if (!matched) {
      matched = matches(condition)
      if (matched) {
        this.result = result
      }
    }
  }
  
}


