@echo off
SET DEVELOPMENT_HOME=../
echo Starting Test Case Run...
cd %DEVELOPMENT_HOME%\
call mvn test > testRunConsoleOutput.txt
echo Test Case Run Completed...
pause;