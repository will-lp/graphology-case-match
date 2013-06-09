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
import org.graphology.extension.Matcher

class TestCase extends GroovyTestCase {

  void testBasic() {
    def a = 90
    def b = a.case { 
      when( "doe" )  { "matched 'doe' string" }
      when( Number ) { "matched number" }
      when( Date )   { "matched date" }
    }
    assert b == "matched number"
  }
  
  
  void testNulls() {
    def a = null
    def b = a.case {
      when( String ) { "matched string" }
      when( 80 )     { "matched integer '80'" }
      when( null )   { "matched null" }
    }
    assert b == "matched null"
  }
  
  
  void testNoOption() {
    def a = 1
    def b = a.case {
      when( String )   { "matched string" }
      when( Object[] ) { "matched object[]" }
    }
    assert b == null
  }
  
  
  void testOtherwise() {
    def a = new Date()
    def b = a.case {
      when(String) { "matched string" }
      otherwise { "no match" }
    }
    assert b == "no match"
  }
  
  
  void testDate() {
    def toDate = { Date.parse "yyyy-MM-dd", it }
    def a = toDate "2000-01-01"
    def b = a.case {
      when({ a > toDate("2000-12-01") }) { "greater than 2000-12-01" }
      when({ a < toDate("2001-01-01") }) { "less than 2001-01-01" }
    }
    assert b == "less than 2001-01-01"
  }
  
  
  @CS void testStatic() {
    def a = 100
    def b = a.case { 
      when({ a > 300 }) { "matched more than 300" }
      when({ a < 200 }) { "matched less than 200" }
    }
    assert b == "matched less than 200"
  }
  
  
  @CS void testStaticParameter() {
    def a = 100
    def b = a.case {
      when({ Number n -> n > 300 }) { "matched more than 300" }
      when({ Number n -> n < 200 }) { "matched less than 200" }
    }
    assert b == "matched less than 200"
  }
  
  
  @CS void testStaticOtherwise() {
    def a = 100
    def b = a.case { Matcher m ->
      m.when({ Number n -> n > 100 }) { "matched more than 300" }
      m.when({ Number n -> n < 100 }) { "matched less than 100" }
      m.otherwise { "no match" }
    }
    assert b == "no match"
  }
  
  
  void testDirect() {
    def a = 100
    def b = a.case {
      when({ false }) { "nope" }
      when({ false }) { "nope" }
      when( 100 ) { "matched 100" }
      when( { throw new RuntimeException("Shouldn't have been executed") } ) {}
    }
    assert b == "matched 100"
  }
  
  
  void testDelegatedValue() {
    def a = [1, 2, 3, 4, 5]
    def b = a.case {
      when({it.contains(1)}) { "contains one" }
      when({it.isEmpty()}) { "empty" }
      otherwise { "no matches" }
    }
    assert b == "contains one"
  }
  
  
  void testEmptyList() {
    def a = []
    def b = a.case {
      when({it.isEmpty()}) { "empty" }
      otherwise { "non empty" }
    }
    assert b == "empty"
  }
  
  
  void testNonEmptyList() {
    def a = [1]
    def b = a.case {
      when({it.isEmpty()}) { "empty" }
      otherwise "non empty"
    }
    assert b == "non empty"
  }
  
  
  void testList() {
    def a = [] as LinkedList
    def b = a.case {
      when(ArrayList) { "arraylist" }
      when(LinkedList) { "linkedlist" }
      otherwise "list"
    }
    assert b == "linkedlist"
  }
  
  
  void testCollection() {
    def a = []
    def b = a.case {
      when(Collection) { "collection" }
      when(ArrayList) { "arraylist" }
      otherwise "unknown"
    }
    assert b == "collection"
  }
  
  
  void testNumberInList() {
    def a = 2
    def b = a.case {
      when([1, 2, 3, 4, 5]) { "in list" }
      otherwise "not in list"
    }
    assert b == "in list"
  }
  
  
  void testListInRange() {
    def a = [1, 2, 3]
    def b = a.case {
      when([2, 3, 4, 5, 6]) { "in list" }
      when([1..4]) { "in range" }
      when([[1, 2], [1, 2, 3]]) { "in nested list" } // just to remember this is how java werkz
      otherwise "not in list"
    }
    assert b == "in nested list"
  }
  
  
  void testListInList() {
    def a = [1, 2, 3]
    def b = a.case {
      when([2, 3, 4, 5, 6]) { "in wrong list" }
      when([1, 2, 3, 4]) { "in list" }
      when([[1, 2, 3]]) { "in nested list" } // see testListInRange
      otherwise "not in list"
    }
    assert b == "in nested list"
  }
  
  
  void testItemInArray() {
    def a = 1
    def b = a.case {
      when([1, 2] as int[]) { "in array" }
      otherwise "not in array"
    }
    assert b == "in array"
  }
  
  
  void testThenClosure() {
    def a = ["a", "b", "c"]
    def b = a.case {
      when Collection then { value -> value[1] * 5 }
      otherwise { assert false }
    }
    assert b == "bbbbb"
  }
  
  
  void testThenException() {
    def a = (char) 'a'
    try {
      def b = a.case {
        when Character
        when(String) { "string" }
      }
      assert false, "Should've thrown an IllegalStateException"
    } catch (IllegalStateException ise) {
      assert true
    } 
  }
  
  
  void testEarlySuccess() {
    def b = 90.case {
      when Integer then "integer"
      when {throw new RuntimeException("Shouldn't get here")} then new Date()
    }
    assert b == "integer"
  }
  
  
  void testEarlyFailure() {
    def a = "test"
    try {
      def b = a.case {
        when { throw new RuntimeException() } then true
        when true then true
      }
    } catch (RuntimeException re) {
      assert true
    }
  }
  
  
  @CS void testStaticWhenDsl() {
    def b = ((char)'g').case { 
      when Integer then "integer"
      when Character then { true }
      otherwise "no match"
    }
    assert b == true
  }
  
  
  @CS void testWhenClosure() {
    def userInputWithLongVariableName = 110
    def b = userInputWithLongVariableName.case { 
      when { Integer a -> a < 110 } then { throw new RuntimeException() }
      when { userInputWithLongVariableName >= 110 } then { new Date() }
      otherwise "no match"
    }
    assert b instanceof Date
  }
  
  
  void testClass() {
    def b = File.case {
      when File then { "file" }
      when InputStream then { "input stream" }
      otherwise "none"
    }
    assert b == "file"
  }
  
}
