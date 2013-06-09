graphology-case-match
=====================

A Groovy extension module providing case/match for objects. Just download it (from [dist/graphology-case-match.jar](https://github.com/will-lp/graphology-case-match/blob/master/dist/graphology-case-match-extension.jar), drop it in the classpath of your project (or in the directory .groovy/lib of your home folder) and you are good to go. 

It relies on Groovy's `switch(obj)` to define the matching, so using Groovy's documentation provides a good overview of this extension behavior.

It is statically compilable, requiring using an explicit object from the API to do so. It also provides a DSL-like construct in the form of `when condition then result`.

Graphology adds the following four methods to every object:

## `case`

    /**
     * Matches an object against the options defined in the <code>matches</code> closure.
     * The first matching object is returned.
     * If no match is found and an <code>otherwise</code> value is provided,
     * it will be returned.
     * 
     * @param self: the object to match against.
     * @param matches: a closure with options to match.
     * @return the matching object. If it is a closure, it will be executed
     * curried with the <code>self</code> object. If none provided, <code>null</code>.
     */
    def "case"(Object self, Closure matches)

A small example:

    def a = 90
    def b = a.case { m ->
      when( "doe" )  { "matched 'doe' string" }
      when( Number ) { "matched number" }
      when( Date )   { "matched date" }
    }
    assert b == "matched number"
    
An example using the DSL syntax:

    def a = ["a", "b", "c"]
    def b = a.case {
      when Collection then { value -> value[1] * 5 }
      otherwise { assert false }
    }
    assert b == "bbbbb"


## `caseLazy`
    
    /**
     * Matches an object against the options defined in the <code>matches</code> closure. 
     * The first matching object is returned
     * If no match is found and an <code>otherwise</code> value is provided,
     * it will be returned.
     * If the returning value is a closure, it will be curried with the <code>self</code> 
     * object, but won't be executed.
     * 
     * @param self: the object to match against
     * @param matches: a closure with options to match
     * @return a matching object. If it is a closure, it will be curried with 
     * the <code>self</code> object. <code>null</code> otherwise.
     */
    def caseLazy(Object self, Closure matches)

A small example showing the static compilation:

    @CompileStatic void testStaticLazy() {
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


## `caseCollect`

    /**
     * Collects a list of the values matched against a <code>self</code> object.
     * If no value matches the <code>self</code> object, the <code>otherwise</code>
     * value will be used, if provided.
     * If the return is a Closure or a list of Closures, they will be curried
     * with the <code>self</code> object.
     *
     * @param self: the object to match against
     * @param matches: a closure with options to match
     * @return either a List of matching objects, a single value from <code>otherwise</code>
     * or <code>null</code>, otherwise.
     */
    Object caseCollect(Object self, Closure matches)
    
A small example collecting closures and not closures values. Every values gets executed:

    def a = 90
    def b = a.caseCollect {
      when Integer then 1
      when Number then { 2 }
      when 90 then 3
      otherwise 6
      when { it > 90 } then { 4 }
      when { it < 100 } then 5
    }
    assert b instanceof List
    assert b == [1, 2, 3, 5]


## `caseCollectLazy`

    /**
     * Return a list of objects, from the options in the <code>matches</code> 
     * closure which matched the <code>self</code> object. If there are Closure
     * objects in the returning list, they won't be executed.
     * Each resulting closure will be curried with the <code>self</code> parameter.
     * 
     * @param self: the object to be tested against
     * @param matches: a closure with options to match
     * @return either a list of objects which matched the <code>self</code> object,
     * a single object from the <code>otherwise</code> value, or <code>null</code>.
     */
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


