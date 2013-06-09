#!/bin/sh

#########
# Pretty analog build script, just because, 
# it's a simple extension :-)
#########

file="graphology-case-match-extension.jar"

echo " * Cleaning older version..."
rm "dist/$file"
rm "classes/ -R"
rm "~/.groovy/lib/$file"

echo " * Compiling..."
groovyc src/org/graphology/extension/*.groovy -d "./classes/"

echo " * Jar-ing..."
cd classes
jar c . > ../dist/$file
cd ..

#echo " * Copying to '.groovy/lib'"
#cp dist/$file > ~/.groovy/lib/

echo " * Launching tests"
groovy -cp dist/$file test/TestCase.groovy
groovy -cp dist/$file test/TestCaseLazy.groovy
groovy -cp dist/$file test/TestCaseCollect.groovy
groovy -cp dist/$file test/TestCaseLazyCollect.groovy

