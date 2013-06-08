package org.graphology.extension

import groovy.transform.CompileStatic as CS

/**
 * The implementation to provide a DSL-ish construct like
 * <code>when condition then result</code>
 */
@CS class ThenImpl implements Then {
  Resolver resolver
  Object condition
  
  ThenImpl(Resolver resolver, Object condition) { 
    this.resolver = resolver 
    this.condition = condition
  }
  
  void then(Object result) {
    assert resolver, "No Resolver was passed to this object"
    resolver.doneWithThen()
    resolver.when condition, result
  }
}
