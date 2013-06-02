#!/bin/sh

file="case-match-extension.jar"

groovyc org/graphology/extension/*.groovy

rm "~/.groovy/lib/$file"

jar c . > "dist/$file"

cp "dist/$file" > ~/.groovy/lib/

groovy test/TestCase.groovy
