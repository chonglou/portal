
all:
	#cd portal;mvn package
	#cd server;mvn package
	-mkdir -p tmp/var/apps
	-cp portal/target/portal.war tmp/var/apps/ROOT.war
	-cp server/target/server-jar-with-dependencies.jar tmp/server.jar
	


clean:
	cd portal;mvn clean
	cd server;mvn clean
	-rm -r tmp



