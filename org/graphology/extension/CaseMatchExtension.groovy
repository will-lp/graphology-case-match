package org.graphology.extension

import org.codehaus.groovy.runtime.NullObject
import groovy.transform.CompileStatic as CS

/**
 * A case/match extension for Groovy.
 * 
 * It uses the following syntax:
 * 
 * <pre>
 * object.matches {
 *   when(condition) { result }
 *   when(condition2) { result2 }
 *   when({ closureCondition }) { valueResult }
 *   when { closureCondition2 } { result }
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
  static def match(Object self, Closure matches) {
    def resolver = new DirectCaseMatchResolver(self: self)
    matches.delegate = resolver
    matches( resolver )
    if (resolver.matched) {
      return resolver.result
    } else {
      def otherwiseValue = resolver.otherwiseValue
      if (otherwiseValue) {
        if (otherwiseValue instanceof Closure) {
          return otherwiseValue.call(self)
        } else {
          return otherwiseValue
        }
      } else {
        return null
      }
    }
  }
  
}


/**
 * Express the basic structure of a resolver
 *
 */
@CS interface Resolver {
  boolean when(Object condition, Closure result)
  void otherwise(Object o)
}


/**
 * Express the basic condition to match an object and store the 
 * state of the process
 *
 */
@CS abstract class CaseMatchResolver implements Resolver {
  def self
  
  Object otherwiseValue
  
  boolean when(Object condition, Closure result) {
    boolean matched = false
    if (!matched) { matched = self == condition }
    if (!matched) { matched = self.isCase(condition) }
    
    Class clazz = self.getClass()
    if (!matched && condition.getClass() == Class) { matched = (condition as Class).isInstance(self) }
    if (!matched) { matched = clazz.isAssignableFrom(condition.getClass()) }
    if (!matched) { matched = clazz.isCase(condition.getClass()) }
    if (!matched) { matched = clazz == condition.getClass() } 
    
    println "${self} matched condition = ${matched}"
    return matched
  }
  
  void otherwise(Object c) {
    this.otherwiseValue = c
  }
  
}


/**
 * Matches a single condition but doesn't execute the result closure
 *
 */
@CS class DirectLazyCaseMatchResolver extends CaseMatchResolver {
  def result
  def matched
  
  boolean when(Object condition, Closure result) {
    if (!matched) {
      matched = super.when condition, result
      if (matched == true) {
        this.result = result
        return true
      }
    }
    return false
  }
  
}


/**
 * Matches a condition and execute the result closure
 *
 */
@CS class DirectCaseMatchResolver extends DirectLazyCaseMatchResolver {
  boolean when(Object condition, Closure result) {
    if (super.when(condition, result))
      this.result = result self
  }
  
}


class CollectCaseMatchResolver extends CaseMatchResolver {
}


class LazyCollectCaseMatchResolver extends CaseMatchResolver {
}


