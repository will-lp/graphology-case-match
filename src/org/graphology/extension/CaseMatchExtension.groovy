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
    def resolver = new SingleCaseMatchResolver(self: self)
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
 */
@CS abstract class CaseMatchResolver implements Resolver {

  /*
   * The object which a closure is being matched against, i.e:
   * object.match {}
   *   ^ this guy
   */
  def self
  
  /*
   * The "otherwise" value which will be used when no match was found
   */
  Object otherwiseValue
  
  /**
   * Express a "when" condition. It tries to matches a value against the 
   * condition using Groovy's switch statement
   */
  boolean when(Object condition, Closure result) {
    boolean matched = false
    
    switch(self) {
      case condition: matched = true
    }
    
    if (self instanceof NullObject && condition == null) { // God, why?
      matched = true 
    }
    
    //println "[log] matching $self against $condition = $matched"
    return matched
  }
  
  
  /*
   * Sets the 'otherwise' condition
   */  
  void otherwise(Object c) {
    this.otherwiseValue = c
  }
  
}


/**
 * Matches a single condition but doesn't execute the result closure
 */
@CS class SingleLazyCaseMatchResolver extends CaseMatchResolver {
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
 * Matches a single condition and execute the result closure
 */
@CS class SingleCaseMatchResolver extends SingleLazyCaseMatchResolver {
  boolean when(Object condition, Closure result) {
    if (super.when(condition, result)) {
      this.result = result self
    }
  }
  
}


/**
 * Collect every matching result to an object
 * #TBD
 */
@CS class CollectCaseMatchResolver extends CaseMatchResolver {
}


/**
 * Collect every matching result to an object as a list of closures
 * to be executed later
 * #TBD
 */
@CS class LazyCollectCaseMatchResolver extends CaseMatchResolver {
}




/*def shoot = { Closure c -> if (!matched) matched = c() }
    def log = { println it }
    
    if (condition instanceof Closure)
    { 
      matched = condition(self)
    }
    else if (condition instanceof Collection) 
    { 
      shoot { condition.contains(self) }
    }
    else if (condition.getClass() in [Object[], int[], long[], double[], char[], byte[], boolean[], short[]]) 
    {
      shoot { self in condition }
    }
    else 
    {
      shoot { self == condition }
      shoot { self.isCase(condition) }
      
      Class clazz = self.getClass()
      shoot { if (condition.getClass() == Class) (condition as Class).isInstance(self) }
      shoot { clazz.isAssignableFrom(condition.getClass()) }
      shoot { clazz.isCase(condition.getClass()) }
      shoot { clazz == condition.getClass() } 
    }
    
    if (condition instanceof Closure) {
      println "$self matched condition = $matched"
    } else {
      println "$self matched $condition = $matched"
    }*/
