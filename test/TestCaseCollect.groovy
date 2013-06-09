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

import groovy.transform.CompileStatic as CS


class TestCaseCollect extends GroovyTestCase {
  
  void testCollect() {
    def a = 90
    def b = a.caseCollect {
      when Integer then 1
      when Number then 2
      when 90 then 3
      when { it > 90 } then 4
      when { it < 100 } then 5
      
      otherwise 6
    }
    assert b instanceof List
    assert b == [1, 2, 3, 5]
  }
  
  
  void testOtherwise() {
    def a = "b".caseCollect {
      when Number then "number"
      when Date then "date"
      otherwise { "unknown" }
    }
    assert a == "unknown"
  }
  
  
  @CS void testEagerClosureEvaluation() {
    def a = 50.0.caseCollect { 
      when BigDecimal then { "big decimal" }
      when String then { "string" }
      when Number then { "number" }
      when Object then "object"
      otherwise "none"
    }
    assert a == ["big decimal", "number", "object"]
  }
  
  
  void testMalformedThen() {
    shouldFail { 
      def p = new Date().caseCollect { // shit.
        when Date
        otherwise "fail syntax"
      }
    }
  }
  
  
  void testList() {
    def b = [].caseCollect {
      otherwise "not a collection"
      when Collection, "collection"
      when List, { "list" }
      when ArrayList then "arraylist"
    }
    assert b == ["collection", "list", "arraylist"]
  }
  
  
  @CS void testClosureCollection() {
    def list = [
      { Integer i -> i * 90 },
      { Integer i -> i * 10 },
      100,
      { Integer i -> "a" * i }
    ].collect { 
      it.case { 
        when Closure then { Closure c -> c(2) }
        when Integer then it
      }
    }
    assert list == [180, 20, 100, "aa"]
  }
  
  
  void testCollectMixedWithClosures() {
    def a = 90
    def b = a.caseCollect {
      when Integer then 1
      when Number then { 2 }
      when 90 then 3
      otherwise 6
      when { it > 90 } then { 4 }
      when { it < 100 } then 5
    }
    assert b instanceof List
    assert b == [1, 2, 3, 5]
  }
  
  
}
