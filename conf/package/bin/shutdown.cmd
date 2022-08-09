@echo off
rem
rem ${project.build.finalName} shutdown
rem
if not exist "%JAVA_HOME%\bin\jps.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1

setlocal

set "PATH=%JAVA_HOME%\bin;%PATH%"

echo killing ${project.build.finalName} server

for /f "tokens=1" %%i in ('jps -m ^| find "${project.build.finalName}-${server.port}"') do ( taskkill /F /PID %%i )

echo Done!
