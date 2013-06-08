import groovy.transform.CompileStatic as CS
import org.graphology.extension.Matcher


class TestCaseLazyCollect extends GroovyTestCase {

  void testLazyCollect() {
    def matchedChi = false
    def k = "chicken".caseCollectLazy {
      when { it ==~ /^chi/ } then { matchedChi = true }
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
    def k = "a".caseLazyCollect { Matcher m ->
      m.when Collection then { Collection }
      m.otherwise { LinkedList }
      m.when List then { List }
    }
    assert k[0] instanceof Closure
    assert k[0]() == LinkedList
  }
  
  
  @CS void testLazyClosureEvaluation() {
    def a = 50.0.caseCollect { Matcher m ->
      m.when BigDecimal then { "big decimal" }
      m.when String then { "string" }
      m.when Number then { "number" }
      m.when Object then "object"
      m.otherwise "none"
    }
    assert a == [Closure, Closure, "object"]
  }
  
}
