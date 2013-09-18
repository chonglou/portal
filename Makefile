
TOMCAT_HOME=${HOME}/local/tomcat
bak=${TOMCAT_HOME}/`date +%F`

all:
	cd portal; mvn package
	cd httpd; mvn package
	-mkdir -p tmp/etc
	-cp httpd/src/main/resources/config.properties tmp/etc
	-cp httpd/src/main/resources/logback.xml tmp/etc
	-mkdir -p tmp/var/log
	-mkdir -p tmp/var/apps
	-cp portal/target/portal.war tmp/var/apps/ROOT.war
	-cp httpd/target/httpd-jar-with-dependencies.jar tmp/httpd.jar
	-cp tools/run.sh tmp/
	@echo JDK路径：${JAVA_HOME}


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
	cd httpd; mvn clean
	-rm -r tmp






