#!/bin/bash
javac -d build/ -cp "packages/twitter4j/*:packages/jackson/*:packages/apache_http_client/*" -encoding UTF-8 src/*.java
java -cp "src/:packages/twitter4j/*:packages/jackson/*:packages/apache_http_client/*" build/HyanBot
