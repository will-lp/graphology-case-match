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
  static <R extends Resolver> R create(Class<R> clazz, self, Closure matcher) {
    def resolver = (Resolver) clazz.newInstance()
    resolver.self = self
    
    matcher.resolveStrategy = Closure.DELEGATE_FIRST
    matcher.delegate = resolver
    matcher( self )
    resolver.done()
    resolver
  }
  
}
