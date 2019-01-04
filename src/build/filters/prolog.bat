@echo off

@REM The script path to reference the included JRE java file
SET SCRIPT_PATH=%~dp0
 
:APPEND_CLASSPATH
SET CLASSPATH="%SCRIPT_PATH%\lib\*"
 
%SCRIPT_PATH%\jre\bin\java -classpath %CLASSPATH% ${Main.Class} %*