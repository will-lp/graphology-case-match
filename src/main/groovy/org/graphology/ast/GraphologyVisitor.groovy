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

// package org.graphology.ast
// 
// import org.codehaus.groovy.control.SourceUnit
// import org.codehaus.groovy.control.CompilePhase
// import org.codehaus.groovy.syntax.SyntaxException
// import org.codehaus.groovy.transform.*
// import org.codehaus.groovy.ast.*
// 
// import org.codehaus.groovy.ast.stmt.Statement
// import org.codehaus.groovy.ast.stmt.ExpressionStatement
// import org.codehaus.groovy.ast.stmt.BlockStatement
// 
// import org.codehaus.groovy.ast.expr.ClosureExpression
// import org.codehaus.groovy.ast.expr.DeclarationExpression
// import org.codehaus.groovy.ast.expr.MethodCallExpression as MCE
// 
// 
// class GraphologyVisitor implements GroovyClassVisitor {
  // 
  // static final GRAPHOLOGY_METHODS = ['case', 'caseCollect', 'caseLazy', 'caseCollectLazy']
  // 
  // def log = Log.instance
  // 
  // SourceUnit unit
  // 
  // 
  // def error( message, line, column ) {
    // unit.addError new SyntaxException(message, line, column)
  // }
  // 
  // 
  // void searchCalls( MCE method, MethodMatcher matcher ) {
    // 
    // def name = method.methodAsString
    // 
    // log.debug "searchCalls method name=${name}"
    // 
    // matcher.match(method)
    // 
    // def args = method.arguments.expressions
    // 
    // if (args.size() > 0) {
      // def arg = args[0]
      // log.debug "arg class = ${arg.class}"
      // if (arg in ClosureExpression) {
        // searchStatements arg.code, matcher
      // }
    // }
    // 
    // if (matcher.matchedAll()) {
      // log.debug " !! ALL matched. time to work on turning 'then' a lazy man"
      // 
      // def closExpr = createClosureExpression matcher
      // //matcher.whenThenExpression.arguments = new ArgumentListExpression() // WTH was i thinking?
      // 
    // }
  // }
  // 
  // 
  // def createClosureExpression(MethodMatcher matcher) {
    // def whenMethod = matcher.whenThenExpression
    // 
    // def thenArg = whenMethod.arguments.expressions[0]
    // def whenArg = whenMethod.objectExpression.argumentList.expressions[0]
    // 
    // if (!(thenArg in ClosureExpression)) {
      // 
      // def getArgument = {
        // if (method in MCE) {
          // 
        // } else {
          // Object
        // }
      // }
      // 
      // def argClassNode = new ClassNode(getArgument()) 
      // 
      // def arguments = [new Argument(argClassNode, "it")]
      // 
      // def closure = new ClosureExpression(arguments, )
      // def block = new BlockExpression()
      // 
      // closure
    // }
    // else {
      // expr
    // }
    // 
  // }
  // 
  // 
  // void searchStatements(Statement statement, MethodMatcher matcher = new MethodMatcher()) {
    // 
    // log.levelInc()
    // log.debug "searchStatements with ${statement.class}"
    // 
    // statement.case {
      // when( BlockStatement ) { block ->
        // for (childStmt in block.statements) {
          // searchStatements childStmt, matcher 
        // }
      // }
      // 
      // when( ExpressionStatement ) { expr ->
        // 
        // expr.expression.case {
          // 
          // /*
           // * a method called 'case', etc. might be the start of a new case-match 
           // * expression and needs a new MethodMatcher to restart the matching.
           // */
          // when( MCE ) {
            // if (it.methodAsString in GRAPHOLOGY_METHODS) {
              // searchCalls it, new MethodMatcher() 
            // } else {
              // searchCalls it, matcher
            // }
          // }
          // 
          // /*
           // * A Declaration means the 'case', etc. is being assigned to a variable
           // */
          // when( DeclarationExpression ) {
            // if (it.rightExpression in MCE) {
              // searchCalls it.rightExpression, matcher
            // }
          // }
        // }
      // }
      // 
      // otherwise {
        // log.debug "skipping " + statement.getClass().simpleName
      // }
    // }
    // 
    // log.levelDec()
    // 
  // }
  // 
  // 
  // /* 
   // * Starting point.
   // */
  // void visitMethod(MethodNode node) {
    // def line = "   " + ("#" * 40)
    // log.debug line
    // log.debug "   # entering method ${node.name} #"
    // log.debug line
    // searchStatements node.code
  // }
  // 
  // void visitClass(ClassNode node) {}
  // void visitConstructor(ConstructorNode node) {}
  // void visitField(FieldNode node) {}
  // void visitProperty(PropertyNode node) {}
// }
