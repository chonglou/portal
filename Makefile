
all:
	cd portal;mvn package
	cd server;mvn package
	-mkdir -p tmp/etc
	-mkdir -p tmp/var/apps
	#-cp portal/target/portal.war tmp/var/apps/ROOT.war
	-cp portal/target/portal.war tmp/ROOT.war
	-cp server/target/server-jar-with-dependencies.jar tmp/server.jar
	-cp tools/run.sh tmp/
	@echo JDK路径：${JAVA_HOME}
	


clean:
	cd portal;mvn clean
	cd server;mvn clean
	-rm -r tmp



