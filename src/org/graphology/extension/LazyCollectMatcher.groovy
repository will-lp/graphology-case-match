package org.graphology.extension

import groovy.transform.CompileStatic as CS

/**
 * Collect every matching result to an object as a list of closures
 * to be executed later
 * #TBD
 */
@CS class LazyCollectMatcher extends Resolver implements Matcher {

  List matches = []
  
  void when(Object condition, Object result) {
    if (matches(condition)) {
      if (result instanceof Closure) {
        matches << ((Closure)result).curry(self)
      } else {
        matches << result
      }
    }
  }
  
}

