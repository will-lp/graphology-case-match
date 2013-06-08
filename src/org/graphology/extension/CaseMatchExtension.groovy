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
    
    if (resolver.matched) {
      return resolver.result
    } else {
      def otherwiseValue = resolver.otherwiseValue
      if (otherwiseValue) {
        if (otherwiseValue instanceof Closure) {
          return otherwiseValue(self)
        } else {
          return otherwiseValue
        }
      } else {
        return null
      }
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
    
    def clos = (resolver.matched) ? resolver.result : resolver.otherwiseValue
    if (clos instanceof Closure) {
      return clos.curry(self)
    } else {
      return clos
    }
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
    
    if (resolver.matches) {
      return resolver.matches
    } else { 
      def other = resolver.otherwiseValue
      if (other instanceof Closure) {
        return ((Closure)other)(self)
      } else {
        return other
      }
    }
  }
  
  
  /**
   * Return a list of object, specified in the <code>matches</code> closure,
   * which matches the <code>self</code> object. If there are Closure
   * objects in the returning list, they won't be executed.
   * If the self object doesn't match any condition, it will return a single
   * list containing the result from the 'otherwise' method. If no 'otherwise'
   * method was provided, an empty list is returned
   * 
   * @param self the object to be testes against
   * @param matches a collection
   * @return List list of objects which matched the <code>self</code> object
   */
  static List caseLazyCollect(Object self, Closure matches) {
  
  }
  
}



