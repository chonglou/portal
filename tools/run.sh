#!/bin/sh
JAVA_HOME=$HOME/local/jdk1.8.0
CLASSPATH=.:$JAVA_HOME/lib
PATH=$JAVA_HOME/bin:$PATH
export JAVA_HOME CLASSPATH PATH

java -jar httpd.jar
