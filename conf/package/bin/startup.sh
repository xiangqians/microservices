#!/bin/bash

# 判断$JAVA_HOME变量的值为空或者当前环境$JAVA_HOME/bin/java不存在时
if [ -z "$JAVA_HOME" ] || [ ! -e "${JAVA_HOME}/bin/java" ]; then
  # $HOME表示用户家目录
  JAVA_HOMES="$HOME/jdk:/usr/java"
  JAVA_HOMES=(${JAVA_HOMES//:/ })
  for VAR_JAVA_HOME in ${JAVA_HOMES[@]}
  do
    # 判断是否存在此路径
    if [ -e "$VAR_JAVA_HOME/bin/java" ]; then
      JAVA_HOME=$VAR_JAVA_HOME
    fi
  done
fi

#  判断$JAVA_HOME变量的值是否为空
if [ -z "$JAVA_HOME" ]; then
  # 使用which获取javac命令所在路径，并获取当前所在文件名的目录（dirname）
  JAVA_PATH=`dirname $(readlink -f $(which javac))`
  # 检测$JAVA_PATH是否为空
  if [ "x$JAVA_PATH" != "x" ]; then
    JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
  fi

  # 再次判断$JAVA_HOME变量的值是否为空
  if [ -z "$JAVA_HOME" ]; then
    error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
  fi
fi
#echo "JAVA_HOME $JAVA_HOME"

JAVA="${JAVA_HOME}/bin/java"
# 当前目录
#BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# 当前目录的上级目录
BASE_DIR="$(dirname $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"

# jvm opts
JVM_OPTS="-Xms512m -Xmx512m -Xmn256m"

# application opts
APPLICATION_OPTS="-Dfile.encoding=UTF-8"
APPLICATION_OPTS="$APPLICATION_OPTS -jar ${BASE_DIR}/target/${project.build.finalName}.jar"

# 校验logs目录是否存在，不存在则创建
if [ ! -d "${BASE_DIR}/logs" ]; then
  mkdir "${BASE_DIR}/logs"
fi
# check the start.out log output file
if [ ! -f "${BASE_DIR}/logs/start.out" ]; then
  touch "${BASE_DIR}/logs/start.out"
fi

# cd base dir
cd ${BASE_DIR}

# start ${project.build.finalName}
echo "${JAVA} ${JVM_OPTS} ${APPLICATION_OPTS} > ${BASE_DIR}/logs/start.out 2>&1 &"
nohup ${JAVA} ${JVM_OPTS} ${APPLICATION_OPTS} ${project.build.finalName}-${server.port} >> ${BASE_DIR}/logs/start.out 2>&1 &
echo "${project.build.finalName}(port=${server.port}) is starting, you can check the ${BASE_DIR}/logs/start.out"