@echo off
SET DEVELOPMENT_HOME= ../
echo Building - workspace Please wait...
cd %DEVELOPMENT_HOME%\
call mvn clean install  > consoleOutput.txt
echo Build completed...
pause;