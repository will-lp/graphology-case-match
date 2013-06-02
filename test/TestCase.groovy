import groovy.transform.CompileStatic as CS
import org.graphology.extension.Resolver

class TestCase extends GroovyTestCase {

  static main(args) {
    new TestCase().with { 
      testStaticParameter()
    }
  }
  

  void testBasic() {
    println "test basic"
    def a = 90
    def b = a.match { m ->
      when( "doe" )  { "matched 'doe' string" }
      when( Number ) { "matched number" }
      when( Date )   { "matched date" }
    }
    println b
    assert b == "matched number"
  }
  
  
  void testNulls() {
    println "test against null"
    def a = null
    def b = a.match {
      when( String ) { "matched string" }
      when( null )   { "matched null" }
      when( 80 )     { "matched integer '80'" }
    }
    assert b == "matched null"
  }
  
  
  void testLists() {
    def a = [1, 2, 3, 4, 5]
    def b = a.match {
      when(a.isEmpty()) { }
    }
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
  }
  

}
