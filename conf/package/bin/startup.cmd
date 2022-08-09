@echo off
rem
rem ${project.build.finalName} startup
rem
if not exist "%JAVA_HOME%\bin\java.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1
set "JAVA=%JAVA_HOME%\bin\java.exe"

setlocal enabledelayedexpansion

rem get current drive letter and path. baseDir\bin
set CURRENT_DIR=%~dp0
rem get the upper level directory. baseDir
set BASE_DIR="%CURRENT_DIR:~0,-5%"

rem jvm opts
set "JVM_OPTS=-Xms512m -Xmx512m -Xmn256m"

rem application opts
set "APPLICATION_OPTS=-Dfile.encoding=UTF-8"
set "APPLICATION_OPTS=%APPLICATION_OPTS% -jar %BASE_DIR%\target\${project.build.finalName}.jar"

rem command
set COMMAND="%JAVA%" %JVM_OPTS% %APPLICATION_OPTS% ${project.build.finalName}-${server.port} %*
echo %COMMAND%

rem cd base dir
cd %BASE_DIR%

rem start ${project.build.finalName} command
%COMMAND%