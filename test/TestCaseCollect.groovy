import groovy.transform.CompileStatic as CS
import org.graphology.extension.Matcher


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
    def a = 50.0.caseCollect { Matcher m ->
      m.when BigDecimal then { "big decimal" }
      m.when String then { "string" }
      m.when Number then { "number" }
      m.when Object then "object"
      m.otherwise "none"
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
  
}
