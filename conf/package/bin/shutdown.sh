#!/bin/bash

BASE_DIR="$(dirname $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"
pid=`ps ax | grep -i '${project.build.finalName}-${server.port}' | grep ${BASE_DIR} | grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
        echo "No ${project.build.finalName}(port=${server.port}) running."
        exit -1;
fi

echo "The ${project.build.finalName}(port=${server.port}, pid=${pid}) is running..."

kill ${pid}

echo "Send shutdown request to ${project.build.finalName}(port=${server.port}, pid=${pid}) OK"
