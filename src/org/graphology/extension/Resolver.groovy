package org.graphology.extension

import org.codehaus.groovy.runtime.NullObject
import groovy.transform.CompileStatic as CS
import groovy.transform.PackageScope as PS


/**
 * Express the basic condition to match an object and store the 
 * state of the process. 
 * Provides the common behavior for the case/match pattern
 *
 * @author will_lp
 */
@CS abstract class Resolver {

  /*
   * The object which a closure is being matched against, i.e:
   * object.case {}
   *   ^ this guy
   */
  def self
  
  /*
   * The "otherwise" value which will be used when no match was found
   */
  Object otherwiseValue
  
  /*
   * The result of the process
   */
  def result
  
  
  /**
   * Express a condition. It tries to matches a value against the 
   * condition using Groovy's switch statement
   * @param condition an object to be tested against 'self'
   * @param result a closure to be executed
   */
  protected boolean matches(Object condition) {
    boolean matched = false
    
    switch(self) { case condition: matched = true }
    
    if (self instanceof NullObject && condition == null) { // Weird. Bug?
      matched = true 
    }
    
    matched
  }
  
  
  /*
   * Sets the 'otherwise' condition
   */  
  void otherwise(Object c) {
    this.otherwiseValue = c
  }
  
  
  /*
   * Called by the <code>Then</code> object when the user
   */
  abstract void when(Object condition, Object result)
  
  
  /*
   * Checks whether we are waiting for a <code>then</code> input
   * from the user
   */
  boolean waitingForThen = false
  
  /*
   * Will be called by the <code>Then</code> object when the
   * user passes a result
   */
  def doneWithThen() { waitingForThen = false }
  
  
  /*
   * For a DSL-ish case/when. Returns a <code>Then</code> object
   * where the user can call a <code>then(result)</code> method.
   * Provides the following syntax:
   * <code>when String then new Date()</code>
   * <code>when {it < 90} then { throw new RuntimeException() }</code>
   */
  Then when(Object condition) {
    waitingForThen = true
    new ThenImpl(this, condition)
  }
  
  
  /**
   * Needs to be manually called when this object finishes matching
   * the user input.
   * If any state is invalid, this method will throw an exception
   */
  void done() {
    if (waitingForThen) {
      throw new IllegalStateException(
          "You didn't provided a then() result for the when() method")
    }
  }
  
  
  /**
   * Returns the <code>otherwiseValue</code>. If it is a closure, it
   * will be curried with the <code>self</code> object
   */
  Object getOtherwiseValue() {
    if (otherwiseValue instanceof Closure) {
      return ((Closure)otherwiseValue).curry(self)
    } else {
      return otherwiseValue
    }
  }
  
}


