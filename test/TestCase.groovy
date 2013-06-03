import groovy.transform.CompileStatic as CS
import org.graphology.extension.Resolver

class TestCase extends GroovyTestCase {

  /*static main(args) {
    new TestCase().with { 
      testNulls()
    }
  }*/
  
  
  void testBasic() {
    def a = 90
    def b = a.match { m ->
      when( "doe" )  { "matched 'doe' string" }
      when( Number ) { "matched number" }
      when( Date )   { "matched date" }
    }
    assert b == "matched number"
  }
  
  
  void testNulls() {
    def a = null
    def b = a.match {
      when( String ) { "matched string" }
      when( 80 )     { "matched integer '80'" }
      when( null )   { "matched null" }
    }
    assert b == "matched null"
  }
  
  
  void testNoOption() {
    def a = 1
    def b = a.match {
      when( String )   { "matched string" }
      when( Object[] ) { "matched object[]" }
    }
    assert b == null
  }
  
  
  void testOtherwise() {
    def a = new Date()
    def b = a.match {
      when(String) { "matched string" }
      otherwise { "no match" }
    }
    assert b == "no match"
  }
  
  
  void testDate() {
    def toDate = { Date.parse "yyyy-MM-dd", it }
    def a = toDate "2000-01-01"
    def b = a.match {
      when({ a > toDate("2000-12-01") }) { "greater than 2000-12-01" }
      when({ a < toDate("2001-01-01") }) { "less than 2001-01-01" }
    }
    assert b == "less than 2001-01-01"
  }
  
  
  @CS void testStatic() {
    def a = 100
    def b = a.match { org.graphology.extension.Resolver r ->
      r.when({ a > 300 }) { "matched more than 300" }
      r.when({ a < 200 }) { "matched less than 200" }
    }
    assert b == "matched less than 200"
  }
  
  
  @CS void testStaticParameter() {
    def a = 100
    def b = a.match { Resolver r ->
      r.when({ Number n -> n > 300 }) { "matched more than 300" }
      r.when({ Number n -> n < 200 }) { "matched less than 200" }
    }
    assert b == "matched less than 200"
  }
  
  
  @CS void testStaticOtherwise() {
    def a = 100
    def b = a.match { Resolver r ->
      r.when({ Number n -> n > 100 }) { "matched more than 300" }
      r.when({ Number n -> n < 100 }) { "matched less than 100" }
      r.otherwise { "no match" }
    }
    assert b == "no match"
  }
  
  
  void testDirect() {
    def a = 100
    def b = a.match {
      when({ false }) { "nope" }
      when({ false }) { "nope" }
      when( 100 ) { "matched 100" }
      when( { throw new RuntimeException("Shouldn't have been executed") } ) {}
    }
    assert b == "matched 100"
  }
  
  
  void testDelegatedValue() {
    def a = [1, 2, 3, 4, 5]
    def b = a.match {
      when({it.contains(1)}) { "contains one" }
      when({it.isEmpty()}) { "empty" }
      otherwise { "no matches" }
    }
    assert b == "contains one"
  }
  
  
  void testEmptyList() {
    def a = []
    def b = a.match {
      when({it.isEmpty()}) { "empty" }
      otherwise { "non empty" }
    }
    assert b == "empty"
  }
  
  
  void testNonEmptyList() {
    def a = [1]
    def b = a.match {
      when({it.isEmpty()}) { "empty" }
      otherwise "non empty"
    }
    assert b == "non empty"
  }
  
  
  void testList() {
    def a = [] as LinkedList
    def b = a.match {
      when(ArrayList) { "arraylist" }
      when(LinkedList) { "linkedlist" }
      otherwise "list"
    }
    assert b == "linkedlist"
  }
  
  
  void testCollection() {
    def a = []
    def b = a.match {
      when(Collection) { "collection" }
      when(ArrayList) { "arraylist" }
      otherwise "unknown"
    }
    assert b == "collection"
  }
  
  
  void testNumberInList() {
    def a = 2
    def b = a.match {
      when([1, 2, 3, 4, 5]) { "in list" }
      otherwise "not in list"
    }
    assert b == "in list"
  }
  
  
  void testListInRange() {
    def a = [1, 2, 3]
    def b = a.match {
      when([2, 3, 4, 5, 6]) { "in list" }
      when([1..4]) { "in range" }
      when([[1, 2], [1, 2, 3]]) { "in nested list" } // just to remember this is how java werkz
      otherwise "not in list"
    }
    assert b == "in nested list"
  }
  
  
  void testListInList() {
    def a = [1, 2, 3]
    def b = a.match {
      when([2, 3, 4, 5, 6]) { "in wrong list" }
      when([1, 2, 3, 4]) { "in list" }
      when([[1, 2, 3]]) { "in nested list" } // see testListInRange
      otherwise "not in list"
    }
    assert b == "in nested list"
  }
  
  
  void testItemInArray() {
    def a = 1
    def b = a.match {
      when([1, 2] as int[]) { "in array" }
      otherwise "not in array"
    }
    assert b == "in array"
  }

}
