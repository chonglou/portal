#!/bin/sh
JAVA_HOME=$HOME/local/jdk1.8.0
CLASSPATH=.:$JAVA_HOME/lib
PATH=$JAVA_HOME/bin:$PATH
export JAVA_HOME CLASSPATH PATH

java -server -cp etc:httpd.jar com.odong.App
