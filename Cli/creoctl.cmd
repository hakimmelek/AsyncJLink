@echo off
rem ---------------------------------------------------------------------------
rem creoctl - run one Creo J-Link operation and print the result as JSON.
rem
rem This wrapper is optional. `java -jar creoctl.jar ...` works on its own, because the process
rem bootstraps itself: it reads paths.yaml and re-executes once with the right environment and with
rem pfcasync.jar on the classpath.
rem
rem Using this script instead skips that extra process, which is worth it when creoctl is called in a
rem loop from a script.
rem ---------------------------------------------------------------------------

setlocal

if "%ASYNCJLINK_HOME%"=="" set "ASYNCJLINK_HOME=%~dp0"

rem Match these to paths.yaml. When they are already correct the jar does not re-launch itself.
if "%PTCNMSPORT%"==""       set "PTCNMSPORT=1239"
if "%PRO_COMM_MSG_EXE%"=="" set "PRO_COMM_MSG_EXE=D:\appli\Creo 3.0\M120\Common Files\x86e_win64\obj\pro_comm_msg.exe"

set "PFCASYNC=%PFCASYNC_JAR%"
if "%PFCASYNC%"=="" set "PFCASYNC=D:\appli\Creo 3.0\M120\Common Files\text\java\pfcasync.jar"

java -cp "%ASYNCJLINK_HOME%creoctl.jar;%PFCASYNC%" com.asyncjlink.cli.CreoCtl %*

endlocal
exit /b %ERRORLEVEL%
