
TOMCAT_HOME=${HOME}/local/tomcat
bak=${TOMCAT_HOME}/`date +%F`

all:
	cd portal; mvn package
	#-mkdir -p tmp/var/log
	#-mkdir -p tmp/var/apps
	#-cp portal/target/portal.war tmp/var/apps/ROOT.war
	@echo JDK路径：${JAVA_HOME}


rpcd:
	cd daemon; mvn clean; mvn package
	-mkdir -p tmp/daemon/etc
	-cp daemon/src/main/resources/config.properties tmp/daemon/etc
	-cp daemon/src/main/resources/logback.xml tmp/daemon/etc
	-cp daemon/target/daemon-jar-with-dependencies.jar tmp/daemon/portal.jar
	-cp tools/run.sh tmp/daemon/



tomcat:
	cd portal; mvn package
	-mkdir -p ${bak}
	-cp portal/target/portal.war ${bak}/ROOT.war
	cd ${TOMCAT_HOME};mv 0dong xw ${bak}/;mkdir 0dong xw
	cd ${TOMCAT_HOME}/0dong;mkdir ROOT;cd ROOT;unzip ${bak}/ROOT.war
	cd ${TOMCAT_HOME}/xw;mkdir ROOT;cd ROOT;unzip ${bak}/ROOT.war
	-rm ${TOMCAT_HOME}/0dong/ROOT/WEB-INF/classes/config.properties
	-cp ${bak}/0dong/ROOT/WEB-INF/classes/config.properties ${TOMCAT_HOME}/0dong/ROOT/WEB-INF/classes/config.properties
	-rm ${TOMCAT_HOME}/xw/ROOT/WEB-INF/classes/config.properties
	-cp ${bak}/xw/ROOT/WEB-INF/classes/config.properties ${TOMCAT_HOME}/xw/ROOT/WEB-INF/classes/config.properties
	@echo "部署完毕 备份至${bak}目录"




clean:
	cd portal; mvn clean
	cd daemon; mvn clean
	cd server; mvn clean
	-rm -r tmp/daemon
	-rm -r tmp/server






auto:
	cd core;mvn clean; mvn install
	cd platform; mvn clean

httpd:
	cd server; mvn clean; mvn package
	-mkdir -p tmp/server/etc
	-cp server/src/main/resources/config.properties tmp/server/etc
	-cp server/src/main/resources/logback.xml tmp/server/etc
	-cp server/target/server-jar-with-dependencies.jar tmp/server/portal.jar
	-cp tools/run.sh tmp/server/





