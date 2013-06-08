package org.graphology.extension

import groovy.transform.CompileStatic as CS

/**
 * Collect every matching result to an object
 * #TBD
 */
@CS class CollectMatcher extends Resolver implements Matcher {
  List<?> matches = []
  
  void checkMatch(Object condition, Object result) {
    if (matches(condition)) {
      if (result instanceof Closure) {
        matches << ((Closure)result)(self)
      } else {
        matches << result
      }
    }
  }
}
