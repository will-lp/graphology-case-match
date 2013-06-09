import groovy.transform.CompileStatic as CS
import org.graphology.extension.Matcher


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
    def k = "a".caseCollectLazy { Matcher m ->
      m.when Collection then { Collection }
      m.otherwise { LinkedList }
      m.when List then { List }
    }
    assert k instanceof Closure
    assert k() == LinkedList
  }
  
  
  @CS void testLazyClosureEvaluation() {
    def a = (List) 50.0.caseCollectLazy { Matcher m ->
      m.when BigDecimal then { "big decimal" }
      m.when String then { "string" }
      m.when Number then { "number" }
      m.when Object then "object"
      m.otherwise "none"
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
      
      // this guy doesn't work, switch/case doesn't pick it up
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
    def list = (List<Closure>) a.caseCollectLazy { Matcher m ->
      m.when Integer then { Integer x -> 
        x * 2
      }
      m.when Number then { Integer y ->
        y * 10
      }
      m.otherwise { Integer z ->
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