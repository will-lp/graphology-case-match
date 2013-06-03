#!/bin/sh

#########
# Pretty analog build script, jest because, 
# it's a simple extension :-)
#########

file="graphology-case-match-extension.jar"

echo " * Cleaning older version..."
rm "dist/$file"
rm "classes/ -R"

echo " * Compiling..."
groovyc src/org/graphology/extension/*.groovy -d "./classes/"

cp META-INF -R ./classes

echo " * Jar-ing..."
cd classes
jar c . > ../dist/$file
cd ..

#echo " * Copying to '.groovy/lib'"
#cp dist/$file > ~/.groovy/lib/

echo " * Launching tests"
groovy -cp dist/$file test/TestCase.groovy
