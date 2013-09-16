

all:
	cd portal; mvn package
	cd httpd; mvn package
	-mkdir -p tmp/etc
	-cp httpd/src/main/resources/{config.properties,logback.xml} tmp/etc
	-mkdir -p tmp/var/{log,apps}
	-cp portal/target/portal.war tmp/var/apps/ROOT.war
	-cp httpd/target/httpd-jar-with-dependencies.jar tmp/httpd.jar
	-cp tools/run.sh tmp/
	@echo JDK路径：${JAVA_HOME}
	


clean:
	cd portal; mvn clean
	cd httpd; mvn clean
	-rm -r tmp






