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


class TestCaseLazy extends GroovyTestCase {
  
  void testLaziness() {
    def a = 5
    def b = a.caseLazy {
      when ArrayList then 5
      when Integer, { times -> "a" * times }
      otherwise "no match"
    }
    assert b instanceof Closure
    assert b() == "aaaaa"
  }
  
  
  void testCodeIsNotRun() {
    def a = 900.caseLazy {
      when Integer then { throw new RuntimeException("if you run me, i'll explode :-)") }
      otherwise "no match"
    }
    assert a instanceof Closure
    try { 
      a()
      assert false
    } catch (RuntimeException e) {
      assert true
    }
  }
  
  
  void testCurriedLazy() {
    def a = 3
    def b = a.caseLazy {
      when ArrayList then 900
      when Integer, { times, text -> text * times }
      otherwise "no match"
    }
    assert b instanceof Closure
    assert b("jj") == "jjjjjj"
  }
  
  
  @CS void testStaticLazy() {
    def j = "j"
    def closureWasExecuted = false
    def k = (Closure) j.caseLazy { 
      when Calendar then { false }
      otherwise { String s -> closureWasExecuted = true; s.toUpperCase() }
    }
    assert k instanceof Closure
    assert !closureWasExecuted
    assert k() == "J"
    assert closureWasExecuted
  }
  
  
  @CS void testNonLazyFirst() {
    def q = "p".case { 
      when String then "matched string p"
      when "p" then { "matched p" }
    }
    assert q == "matched string p"
  }
  
  
  void testScrewThenSyntax() {
    def a = 90.caseLazy {
      def then = when Integer
      when Collection, "collection"
      then.then "integer" // weird, but reasonable.
    }
    assert a == "integer"
  }
  
}
