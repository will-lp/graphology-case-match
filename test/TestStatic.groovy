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
import groovy.transform.TypeChecked as TC

class TestStatic extends GroovyTestCase {
  
  @TC void testStaticCase() {
    def b = 5.case {
      when( Integer ) { Integer i -> "a" * i }
      otherwise { Integer i -> "b" * i }
    }
    assert b == "aaaaa"
  }
  
  @CS void testCaseWithDelegatesTo() {
    def list = (List<Object>) [1, 2.0, "string", 4, (byte)0x4f, new Date()]
    
    def k = list.collect {
      it.case { 
        when String then { String s -> s * 2 }
        when Integer then { Integer i -> i * 3 }
        when Date then "date"
        otherwise it
      }
    }
    
    assert k == [3, 2.0, "stringstring", 12, (byte)0x4f, "date"]
  }
  
  
  @CS void testNested() {
    def a = [value: 10.00]
    def result = a.case {
      when Map then { Map m ->
        m['value'].case {
          when BigDecimal then {
            "this is a bigdecimal"
          }
          otherwise "value is not a bigdecimal"
        }
      }
      otherwise "not a expando"
    }
    assert result == "this is a bigdecimal"
  }
  
}
