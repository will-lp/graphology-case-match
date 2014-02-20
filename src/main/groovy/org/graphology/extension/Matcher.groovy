/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.graphology.extension

/**
 * Express the basic structure of a resolver. It is implemented
 * by the four main strategies of the case/match
 * 
 * @author will_lp
 */
interface Matcher {

  /**
   * Provides a matching of a condition against an object and a 
   * result, if the object matches.
   * 
   * @param condition: the condition to which <code>self</code> object
   * will be matched against
   * @param result: the result to be executed if the condition is matched
   */
  void when(Object condition, Object result)
  
  
  /**
   * Provides a statically compilable DSL-like way to match an object in 
   * the form:
   * <code>when condition then result</code>
   * @param condition: the condition to which <code>self</code> object
   * will be matched against
   * @return a Then object, which has a <code>then</code> method
   */
  Then when(Object condition)
  
  
  /**
   * When no option matches the caller object, this result
   * will be used
   * 
   * @param 
   */
  void otherwise(Object o)
  
  
  /*
   * Will be called by the <code>Then</code> object when the
   * user passes a result. Enables a statically compilable DSL
   * for case/matching.
   */
  def doneWithThen()
  
  
  /**
   * Needs to be manually called when this object finishes matching
   * the user input.
   * If any state is invalid, this method will throw an exception
   */
  void done()
  
  
  /**
   * Returns the matching result.
   */
  Object getMatchedResult()
  
  
  /**
   * Setter for the object which is being matched against
   */
  void setSelf(self)
  
  
  /**
   * Getter for the object which is being matched against
   */
  Object getSelf()
  
}
