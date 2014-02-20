graphology-case-match
=====================

A Groovy extension module providing case/match for objects. Just download it from [build/libs/graphology-case-match.jar](https://github.com/will-lp/graphology-case-match/raw/master/build/libs/graphology-case-match-extension.jar), drop it in the classpath of your project (or in the directory .groovy/lib of your home folder) and you are good to go. 

It relies on Groovy's `switch(obj)` to define the matching, so using [Groovy's documentation](http://groovy.codehaus.org/Logical%2BBranching) provides a good overview of this extension behavior.

It can use `@CompileStatic`, `@TypeChecked` and also provides a DSL-like construct in the form of `when condition then result`.

Graphology adds the following four methods to every object:



## `case`

Matches an object against the options defined in a closure.
The first matching object is returned. If no match is found and an otherwise value is provided, it will be returned.
If the returning object is a closure, it will be executed curried with the caller object.

    Object case(Object self, Closure matches)

A small example:

    def a = 90
    def b = a.case { 
      when( "doe" )  { "matched 'doe' string" }
      when( Number ) { "matched number" }
      when( Date )   { "matched date" }
    }
    assert b == "matched number"
    
An example using the DSL syntax:

    def a = ["a", "b", "c"]
    def b = a.case {
      when Collection then { value -> value[1] * 5 } // 'a' can be accessed as a parameter
      otherwise { assert false }
    }
    assert b == "bbbbb"


## `caseLazy`
    
Matches an object against the options defined in a closure. 
The first matching object is returned. If no match is found and an <code>otherwise</code> value is provided,
it will be returned. If the returning value is a closure, it will be curried with the <code>self</code> 
object, but won't be executed.
 
    Object caseLazy(Object self, Closure matches)

A small example with static compilation:

    @CompileStatic void testStaticLazy() {
      def j = "j"
      def closureWasExecuted = false
      def k = j.caseLazy { 
        when Calendar then { false }
        otherwise { String s -> closureWasExecuted = true; s.toUpperCase() }
      }
      assert k instanceof Closure
      assert !closureWasExecuted
      assert k() == "J"
      assert closureWasExecuted
    }


## `caseCollect`

Collects a list of the values matched against an object.
If no value matches the object, the otherwise value will be used, if provided.
If the return is a Closure or a list of Closures, they will be executed with the caller object.

    Object caseCollect(Object self, Closure matches)
    
A small example collecting closures and values. Every closure gets executed:

    def a = 90
    def b = a.caseCollect {
      when Integer then 1
      when Number then { 2 }
      when 90 then 3
      when { it > 90 } then { 4 }
      when { it < 100 } then 5
      otherwise 6
    }
    assert b instanceof List
    assert b == [1, 2, 3, 5]


## `caseCollectLazy`

Return a list of objects, from the matches in a closure.
If there are Closure objects in the returning list, they won't be executed.
Each resulting closure will be curried with the caller object.

    Object caseCollectLazy(Object self, Closure matches)
    
A small example, also showing delayed execution:

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

More scenarios can be found in the scripts at the [test folder](https://github.com/will-lp/graphology-case-match/tree/master/test).


