package org.graphology.extension

import org.codehaus.groovy.runtime.NullObject
import groovy.transform.CompileStatic as CS


/**
 * A case/match extension for Groovy.
 * 
 * It uses the following syntax:
 * 
 * <pre>
 * object.case {
 *   when(condition) { result }
 *   when(condition2) { result2 }
 *   when({ closureCondition }) { valueResult }
 *   when { closureCondition2 } { result }
 *   when condition3 then result3
 *   when condition4 then { result4 }
 *   otherwise { defaultResult }
 * }
 * </pre>
 * 
 * @author will_lp
 * @date 06/02/2013
 */
@CS class CaseMatchExtension {
  
  
  /**
   * Matches an object against the options defined in the "matches" closure
   * 
   * @param object the object to match against.
   * @param matches a closure with options to match.
   */
  static def "case"(Object self, Closure matches) {
    def resolver = new SingleMatcher(self: self)
    matches.delegate = resolver
    matches( resolver )
    resolver.done()
    
    
    if (resolver.matched) {
      return resolver.result
    } else {
      def other = resolver.otherwiseValue
      return (other instanceof Closure) ? other() : other
    }
  }
  
  
  /**
   * Matches an object in a lazy way, i.e., if the object matches
   * a condition returning a closure, the closure will be returned
   * without being executed.
   * The returning closure will also be curried with the <code>self</code> object
   * as the first parameter
   * 
   * @param self
   * @param matches
   * @return a curried closure with the `self` object, if any
   */
  static def caseLazy(Object self, Closure matches) {
    def resolver = new SingleLazyMatcher(self: self)
    matches.delegate = resolver
    matches( resolver )
    resolver.done()
    
    def clos = (resolver.matched) ? resolver.result : resolver.otherwiseValue
  }
  
  
  /**
   * Collects a list of the values matched against a <code>self</code> object.
   * 
   * @param self
   * @param matches
   * @return List
   */
  static Object caseCollect(Object self, Closure matches) {
    def resolver = new CollectMatcher(self: self)
    matches.delegate = resolver
    matches( resolver )
    resolver.done()
    
    if (resolver.matches) {
      return resolver.matches
    } else { 
      def other = resolver.otherwiseValue
      return (other instanceof Closure) ? other() : other
    }
  }
  
  
  /**
   * Return a list of object, specified in the <code>matches</code> closure,
   * which matches the <code>self</code> object. If there are Closure
   * objects in the returning list, they won't be executed.
   * Each resulting closure will be curried with the <code>self</code> parameter
   * 
   * @param self the object to be testes against
   * @param matches a collection
   * @return List list of objects which matched the <code>self</code> object
   */
  static Object caseCollectLazy(Object self, Closure matches) {
    def resolver = new LazyCollectMatcher(self: self)
    matches.delegate = resolver
    matches( resolver )
    resolver.done()
    
    if (resolver.matches.size() == 0 && resolver.otherwiseValue) {
      return resolver.otherwiseValue
    }
    else {
      return resolver.matches
    } 
  }
  
}



