#!/bin/sh
JAVA_HOME=$HOME/local/jdk1.8.0
CLASSPATH=.:$JAVA_HOME/lib
PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
export JAVA_HOME CLASSPATH PATH

root_dir=$(cd "$(dirname "$0")"; pwd)
pid=${root_dir}/var/run/.httpd
args=" -server -cp ${root_dir}/etc:${root_dir}/httpd.jar -pidfile ${pid} -DXms512m -DXmx1024m -Duser.dir=${root_dir} com.odong.App "

start(){
	if [ -s "$pid" ]; then
		echo "已经启动，请先停止。"
	else
		echo "正在启动..."
		${root_dir}/bin/jsvc  -outfile /dev/null -errfile /dev/null ${args}
	fi
}
stop(){
	if [ -s "$pid" ]; then
		echo "正在停止..."
		${root_dir}/bin/jsvc root  -stop ${args}
	else
		echo "尚未启动，请先启动。"
	fi
}

case $1 in
	start)
		start
		;;
	stop)
		stop 
		;;
	debug)
		if [ -s "$pid" ]; then
			echo "已经启动"
		else
			echo "正在调试启动..."
			${root_dir}/bin/jsvc -outfile ${root_dir}/var/log/stdout -errfile ${root_dir}/var/log/stderr -debug ${args}
		fi
		;;
	kill)
		if [ -s "$pid" ]; then
			echo "正在 kill"
			${root_dir}/bin/jsvc -outfile ${root_dir}/var/log/stdout -errfile ${root_dir}/var/log/stderr -stop ${args}
		else
			echo "还未启动"
		fi
		;;
	restart)
		stop
		sleep 2
		start
		;;
	*)
		echo "Usage: $0 {start|stop|debug|kill|restart}" 
		;;
esac





