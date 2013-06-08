package org.graphology.extension

/**
 * Express the basic structure of a resolver. It is implemented
 * by the four main strategies of the case/match
 */
interface Matcher {
  void when(Object condition, Object result)
  Then when(Object condition)
  void otherwise(Object o)
}
