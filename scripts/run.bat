@echo off
SET DEVELOPMENT_HOME=../
echo Starting Application - xl-spaceship
cd %DEVELOPMENT_HOME%\
call mvn spring-boot:run
pause;