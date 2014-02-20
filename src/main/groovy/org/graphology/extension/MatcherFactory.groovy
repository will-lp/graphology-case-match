package org.graphology.extension


/**
 * Ease the creation of Resolvers and closure setup.
 * 
 * @author will_lp
 * 
 */
@groovy.transform.CompileStatic
class MatcherFactory {
  
  /**
   * Creates a Resolver, accordingly to the passed class, sets up the 
   * passed closure and executes it.
   * 
   * @param clazz: the class, which extends Resolver, to create a new object
   * @param self: the object to test against
   * @param matcher: the closure containing methods to test against 'self'
   * @return the resolver created
   */
  static <M extends Matcher> M create(Class<M> clazz, self, Closure matcherClosure) {
    def matcher = (Matcher) clazz.newInstance()
    matcher.self = self
    
    matcherClosure.resolveStrategy = Closure.DELEGATE_FIRST
    matcherClosure.delegate = matcher
    matcherClosure( self )
    matcher.done()
    matcher
  }
  
  
  /**
   * Creates a resolver and returns its result
   *
   * @param clazz: the class, which extends Resolver, to create a new object
   * @param self: the object to test against
   * @param matcher: the closure containing methods to test against 'self'
   * @return the result from the resolver
   */
  static <M extends Matcher> Object getResult(Class<M> clazz, self, Closure matcherClosure) {
    create( clazz, self, matcherClosure ).matchedResult
  }
  
}
