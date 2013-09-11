#!/bin/sh
JAVA_HOME=$HOME/local/jdk1.8.0
PATH=$JAVA_HOME/bin:$PATH
CLASSPATH=.:$JAVA_HOME/lib
export JAVA_HOME PATH CLASSPATH

java -jar server.jar
