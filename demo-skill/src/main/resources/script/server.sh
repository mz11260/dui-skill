#!/bin/bash
# 当前脚本路径
current_file_path=$(cd `dirname $0`; pwd)
cd "$current_file_path"
cd ../

PROJECT_HOME=`pwd`
JAR_NAME=''

for loop in `ls $PROJECT_HOME/*.jar`;do
   JAR_NAME=${loop}
   break
done

SERVER_NAME=${JAR_NAME##*/} #  ## 号截取，删除左边字符，保留右边字符
SERVER_NAME=${SERVER_NAME%.*} # %号截取，删除右边字符，保留左边字符,从右边开始，删除第一个出现的.及右边的字符
PID=$(ps aux | grep ${JAR_NAME} | grep -v grep | awk '{print $2}' )
SUF=".pid"

#JVM参数
#export JAVA_OPTS="-Xms768m -Xmx2048m -XX:PermSize=64m -XX:MaxPermSize=1024m -XX:+UseG1GC"

start(){
  if [ ! -n "$PID" ]; then
    echo "Usage JAVA_HOME: ${JAVA_HOME}"
    echo "正在启动 $SERVER_NAME"
    nohup ${JAVA_HOME}/bin/java -jar ${JAR_NAME} > /dev/null 2>&1 &
    echo $! > ${SERVER_NAME}${SUF}
    echo "启动完成 $SERVER_NAME"
#    if [ test -z "$2" ]
#    then
#      echo "脚本执行完毕"
#    else
#      echo "正在打印启动日志..."
#      sleep 5s
#      tail -fn 0 --pid=`ps uxh|grep 'sed[[:space:]]\/Started'|awk '{print $2}'|sort -nr|head -1` ${PROJECT_HOME}/logs/info.log|sed /Started[[:space:]].*[[:space:]].*[[:space:]].*[[:space:]]seconds/Q
#    fi
  else
      echo "$SERVER_NAME (pid  $PID) 正在运行..."
  fi
}


status(){
   if [ ! -n "$PID" ]; then
     echo "$SERVER_NAME 已停止"
   else
     echo "$SERVER_NAME (pid  $PID) 正在运行..."
   fi
}


stop(){
    if [ ! -n "$PID" ]; then
     echo "$SERVER_NAME 已停止"
    else
      echo "$SERVER_NAME 停止中.."
      kill -9 $PID
      rm ${PROJECT_HOME}/${SERVER_NAME}${SUF}
      echo "$SERVER_NAME 完毕"
      PID=$(ps aux | grep ${JAR_NAME} | grep -v grep | awk '{print $2}' )
    fi
}

restart(){
    stop
    sleep 3s
    start
}

case $1 in
  start)
    start
    ;;
  stop)
    stop
    ;;
  status)
    status
    ;;
  restart)
    restart
    ;;
  *)
    echo $"Usage: {start|stop|status|restart}"
esac
exit 0;