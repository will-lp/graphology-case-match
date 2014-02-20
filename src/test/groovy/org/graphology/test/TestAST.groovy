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

// package org.graphology.test
 // 
// import groovy.transform.CompileStatic as CS
// import groovy.transform.TypeChecked as TC
// 
// class TestAST extends GroovyTestCase {
  // 
  // void testSimple() {
    // 
    // def clazz = Number
    // 
    // def result = "a".case {
      // when Integer then it.intValue()
      // when String then it.toUpperCase()
      // when clazz then { println "number clazz" }
      // when null then { println "shit" }
      // otherwise { throw new IllegalArgumentException("Should've matched") }
    // }
    // 
    // assert result == "A"
  // }
  // 
  // { println "does the AST visitor handles an init block?".case { } }
  // 
  // static { println "does the AST visitor handles a static init block".case { } }
  // 
  // void testWithMoreSimpleExpressions() {
  // 
    // println "dumb method call to make sure it doesn't mess with the ASTT"
    // 
    // "noVar".case {}
    // 
    // def result = "a".case {
      // when Integer then it.intValue()
      // when String then it.toUpperCase()
      // when Date then foo.bar
      // otherwise { throw new IllegalArgumentException("Should've matched") }
    // }
    // 
    // assert result == "A"  
  // }
  // 
  // 
  // void testNestedInOtherExpressions() {
    // if (true != false) {
      // def list = [1, 2, 3]
      // def error = { throw new RuntimeException("wrong, there are no number greater than 3 in $list") }
      // for (i in list) {
        // i.case {
          // when { it % 2 == 0 } then 'ok'
          // when { it > 3 } then error()
        // }
      // }
    // }
  // }
  // 
  // void testUnhandledSituation() {
    // try {
      // def result2 = "b".case actuallyAClosure()
      // assert false, "The ASTT doesn't handle closures declared in different places"
    // } catch (e) {
      // assert true
    // }
  // }
  // 
  // def actuallyAClosure() {
    // def error = { throw new RuntimeException("This situation is not handled by the ASTT. The result as a closure won't be known until runtime.") }
    // return { 
      // when String then error()
    // } 
  // }
  // 
  // 
  // void testNested() {
    // def map = [foo : { throw new RuntimeException("Oops, a wrong case evaluated whereas it should be lazy and shouldn't have matched") }]
    // 
    // def result = "a".case {
      // when String then 90.case {
        // when Number then( [].case {
          // when Map then { map.foo() }
          // when List then "matched string, number and list"
          // otherwise "wrong match on list"
        // })
        // otherwise "wrong match on number"
      // }
      // otherwise "wrong match on string"
    // }
    // 
    // assert result == "matched string, number and list"
    // 
  // }
  // 
  // 
  // void testLazyWhen() {
      // assert 90.case {
          // when (it in Integer) then 9000
          // when error() then "oops"
          // when "no such method".noSuchMethod() then "oops2"
          // otherwise "none"
      // } == 9000
      // 
  // }
  // 
  // 
  // def error() { throw new RuntimeException("Shouldn't have evaluated") }
  // 
  // @CS
  // void testArgumentsBecameTyped() {
      // def list = ["a", 90, 800.59, [a:'map value']].collect {
          // it.case {
              // when String then it.toUpperCase()
              // when Integer then { it.intValue() * 4 }
              // when BigDecimal then { a -> a.setScale(java.math.RoundingMode.UP, 1) }
              // when Map then { Map map -> map.a }
              // when null then error()
              // otherwise ["wow", "such no match"]
          // }
      // }
      // assert list == ["A", 360, 800.6, 'map value']
  // }
    // 
  // // TODO: test caseCollect
  // 
// }