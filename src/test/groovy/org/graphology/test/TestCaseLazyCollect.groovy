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

package org.graphology.test

import groovy.transform.CompileStatic as CS


class TestCaseLazyCollect extends GroovyTestCase {

  void testLazyCollect() {
    def matchedChi = false
    def k = (List) "chicken".caseCollectLazy {
      when { it ==~ /^chi.*/ } then { matchedChi = true }
      when(BigDecimal) { 90.0 }
      when(String, "matched string")
      otherwise "no matches"
    }
    assert !matchedChi
    assert k[0] instanceof Closure
    assert k[1] == "matched string"
    k[0]()
    assert matchedChi
  }
  
  
  @CS void testOtherwise() {
    def k = (Closure) "a".caseCollectLazy { 
      when Collection then { Collection }
      otherwise { LinkedList }
      when List then { List }
    }
    assert k instanceof Closure
    assert k() == LinkedList
  }
  
  
  @CS void testLazyClosureEvaluation() {
    def a = (List) 50.0.caseCollectLazy { 
      when BigDecimal then { "big decimal" }
      when String then { "string" }
      when Number then { "number" }
      when Object then "object"
      otherwise "none"
    }
    assert a[0] instanceof Closure
    assert a[1] instanceof Closure
    assert a[2] == "object"
  }
  
  
  void testZeroMatchesList() {
    def s = new State()
    List<Closure> procedures = s.caseCollectLazy {
      when { it.intState > 10 } then { assert false }
      when { it.decimalState > 11.0 } then { assert false }
    }
    assert procedures == []
  }
  
  
  void testDelayedStateChange() {
    def s = new State()
    List<Closure> procedures = s.caseCollectLazy {
      when { it.intState < 10 } then { 
        s.intState = 45
      }
      when s.intState == 10 then { assert false, "boom" }
      
      // this guy doesn't match, switch/case doesn't pick it up
      when s.decimalState < 50 then { State state ->  
        state.decimalState = 90 
      }
      
      when { s.decimalState < 50 } then { State state ->
        state.decimalState = 7000
      }
    }
    assert s.intState == 0
    assert s.decimalState == 10
    procedures.each { it() }
    assert s.intState == 45
    assert s.decimalState == 7000
  }
  
  
  @CS void testLongCode() {
    def a = 90
    def list = (List<Closure>) a.caseCollectLazy { 
      when Integer then { Integer x -> 
        x * 2
      }
      when Number then { Integer y ->
        y * 10
      }
      otherwise { Integer z ->
        z * 5
      }
    }
    
    assert list[0]() == 180
    assert list[1]() == 900
  }
  
  
  void testNoWhenProvided() {
    shouldFail {
      def a = [].caseCollectLazy {
        when String then
      }
    }
  }
  
  
  void testWrongThenSyntax() {
    shouldFail {
      def a = 9.caseCollectLazy {
        when List 
        then Collection
      }
    }
  }
  
  
  void testLineBreakThenSyntax() {
    def a = [].caseCollectLazy {
      when List \
      then Collection
    }
    assert a == [Collection]
  }
  
  
}



class State {
  Integer intState = 0
  BigDecimal decimalState = 10.0
  
}
