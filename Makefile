

all:
	cd portal; mvn package
	cd httpd; mvn package
	cd tools/unix; ./configure; make
	-mkdir -p tmp/{etc,bin}
	-mkdir -p tmp/var/{log,apps,run}
	-cp portal/target/portal.war tmp/var/apps/ROOT.war
	-cp httpd/target/httpd-jar-with-dependencies.jar tmp/httpd.jar
	-cp tools/unix/jsvc tmp/bin
	-cp tools/daemon.sh tmp/
	@echo JDK路径：${JAVA_HOME}
	


clean:
	cd portal; mvn clean
	cd httpd; mvn clean
	cd tools/unix; make clean
	-rm -r tmp






