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

import org.codehaus.groovy.runtime.NullObject
import groovy.transform.CompileStatic as CS


/**
 * A case/match extension for Groovy.
 * 
 * It allows the following syntaxes:
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
   * Matches an object against the options defined in the <code>matches</code> closure.
   * The first matching object is returned.
   * If no match is found and an <code>otherwise</code> value is provided,
   * it will be returned.
   * 
   * @param self: the object to match against.
   * @param matches: a closure with options to match.
   * @return the matching object. If it is a closure, it will be executed
     * curried with the <code>self</code>. If none provided, <code>null</code>.
   */
   static <T> Object "case"(T self, @DelegatesTo(value=Resolver, strategy=Closure.DELEGATE_FIRST) Closure matches) {
    def resolver = new SingleMatcher(self: self)
    matches.delegate = resolver
    matches( self )
    resolver.done()
    
    if (resolver.matched) {
      return resolver.result
    } else {
      def other = resolver.otherwiseValue
      return (other instanceof Closure) ? ((Closure) other)() : other
    }
  }
  
  
  /**
   * Matches an object against the options defined in the <code>matches</code> closure. 
   * The first matching object is returned.
   * If no match is found and an <code>otherwise</code> value is provided,
   * it will be returned.
   * If the returning value is a closure, it will be curried with the <code>self</code> 
   * object, but won't be executed.
   * 
   * @param self: the object to match against
   * @param matches: a closure with options to match
   * @return a matching object. If it is a closure, it will be curried with 
   * the <code>self</code> object. <code>null</code> otherwise.
   */
  static <T> Object caseLazy(T self, 
      @DelegatesTo(value=Resolver, strategy=Closure.DELEGATE_FIRST) Closure matches) {
    def resolver = new SingleLazyMatcher(self: self)
    matches.delegate = resolver
    matches( self )
    resolver.done()
    
    def clos = (resolver.matched) ? resolver.result : resolver.otherwiseValue
  }
  
  
  /**
   * Collects a list of the values matched against a <code>self</code> object.
   * If no value matches the <code>self</code> object, the <code>otherwise</code>
   * value will be used, if provided.
   * If the return is a Closure or a list of Closures, they will be curried
   * with the <code>self</code> object.
   *
   * @param self: the object to match against
   * @param matches: a closure with options to match
   * @return either a List of matching objects, a single value from <code>otherwise</code>
   * or <code>null</code>, otherwise.
   */
  static <T> Object caseCollect(T self, 
      @DelegatesTo(value=Resolver, strategy=Closure.DELEGATE_FIRST) Closure matches) {
    def resolver = new CollectMatcher(self: self)
    matches.delegate = resolver
    matches( self )
    resolver.done()
    
    if (resolver.matches) {
      return resolver.matches
    } else { 
      def other = resolver.otherwiseValue
      return (other instanceof Closure) ? ((Closure) other)() : other
    }
  }
  
  
  /**
   * Return a list of objects, from the options in the <code>matches</code> 
   * closure which matched the <code>self</code> object. If there are Closure
   * objects in the returning list, they won't be executed.
   * Each resulting closure will be curried with the <code>self</code> parameter.
   * 
   * @param self: the object to be tested against
   * @param matches: a closure with options to match
   * @return either a list of objects which matched the <code>self</code> object,
   * a single object from the <code>otherwise</code> value, or <code>null</code>.
   */
  static <T> Object caseCollectLazy(T self, 
      @DelegatesTo(value=Resolver, strategy=Closure.DELEGATE_FIRST) Closure matches) {
    def resolver = new LazyCollectMatcher(self: self)
    matches.delegate = resolver
    matches( self )
    resolver.done()
    
    if (resolver.matches.size() == 0 && resolver.otherwiseValue) {
      return resolver.otherwiseValue
    }
    else {
      return resolver.matches
    } 
  }
  
}



