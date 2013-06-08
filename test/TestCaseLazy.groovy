import groovy.transform.CompileStatic as CS
import org.graphology.extension.Matcher


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
    def k = j.caseLazy { Matcher m ->
      m.when Calendar then { false }
      m.otherwise { String s -> closureWasExecuted = true; s.toUpperCase() }
    }
    assert k instanceof Closure
    assert !closureWasExecuted
    assert k() == "J"
    assert closureWasExecuted
  }
  
  
  @CS void testNonLazyFirst() {
    def q = "p".case { Matcher m ->
      m.when String then "matched string p"
      m.when "p" then { "matched p" }
    }
    assert q == "matched string p"
  }
  
  
  void testScrewThenSyntax() {
    def a = 90.caseLazy {
      def then = when Integer
      when Collection, "collection"
      then.then "integer" // weird.
    }
    assert a == "integer"
  }
  
}
