@echo off
rem ===========================================================================
rem  AsyncJLink - build a release.
rem
rem  Compiles both jars and assembles everything an end user needs into
rem  .\release\ : the jars, a launcher, paths.yaml, mcp.json and SETUP.md.
rem
rem  Usage:
rem      build-release.bat [path\to\pfcasync.jar]
rem
rem  pfcasync.jar is located from, in order:
rem      1. the first argument
rem      2. the PFCASYNC_JAR environment variable
rem      3. .\libs\pfcasync.jar
rem  It is needed to compile against, but is never copied into the release:
rem  it belongs to the Creo installation and is version-matched to it.
rem ===========================================================================

setlocal enabledelayedexpansion

set "ROOT=%~dp0"
set "RELEASE=%ROOT%release"
set "FAILED="

echo.
echo ===========================================================================
echo  AsyncJLink release build
echo ===========================================================================
echo.

rem --- 1. locate pfcasync.jar --------------------------------------------

set "PFCASYNC="
if not "%~1"=="" (
    set "PFCASYNC=%~1"
) else if not "%PFCASYNC_JAR%"=="" (
    set "PFCASYNC=%PFCASYNC_JAR%"
) else if exist "%ROOT%libs\pfcasync.jar" (
    set "PFCASYNC=%ROOT%libs\pfcasync.jar"
)

if "!PFCASYNC!"=="" (
    echo [ERROR] pfcasync.jar not found.
    echo.
    echo Supply it in one of these ways:
    echo     build-release.bat "D:\appli\Creo 3.0\M120\Common Files\text\java\pfcasync.jar"
    echo     set PFCASYNC_JAR=^<path^>
    echo     copy the jar to "%ROOT%libs\pfcasync.jar"
    echo.
    goto :fail
)

if not exist "!PFCASYNC!" (
    echo [ERROR] pfcasync.jar does not exist at:
    echo         !PFCASYNC!
    echo.
    goto :fail
)

echo [1/5] pfcasync.jar
echo       !PFCASYNC!
echo.

rem --- 2. checks that need no JDK ----------------------------------------

echo [2/5] Validating the generated command layer
where python >nul 2>&1
if errorlevel 1 (
    echo       Python not found - skipping ^(optional^).
) else (
    python "%ROOT%tools\validate_generated.py"
    if errorlevel 1 (
        echo [ERROR] The generated sources failed validation.
        goto :fail
    )
    python "%ROOT%tools\test_generator.py"
    if errorlevel 1 (
        echo [ERROR] The generator test suite failed.
        goto :fail
    )
)
echo.

rem --- 3. compile ---------------------------------------------------------

set "GRADLE=gradle"
if exist "%ROOT%gradlew.bat" set "GRADLE=%ROOT%gradlew.bat"

where %GRADLE% >nul 2>&1
if errorlevel 1 (
    if not exist "%ROOT%gradlew.bat" (
        echo [ERROR] Gradle not found on PATH and no gradlew.bat in this folder.
        echo         Install Gradle, or run "gradle wrapper" once to create gradlew.bat.
        echo.
        goto :fail
    )
)

echo [3/5] Compiling
call "%GRADLE%" -p "%ROOT%." clean build -PpfcasyncJar="!PFCASYNC!" --console=plain
if errorlevel 1 (
    echo.
    echo [ERROR] The Gradle build failed. Nothing was copied to release\.
    goto :fail
)
echo.

set "CLI_JAR=%ROOT%Cli\build\libs\creoctl.jar"
set "MCP_JAR=%ROOT%Mcp\build\libs\mcp.jar"

if not exist "!CLI_JAR!" (
    echo [ERROR] Expected jar not produced: !CLI_JAR!
    goto :fail
)
if not exist "!MCP_JAR!" (
    echo [ERROR] Expected jar not produced: !MCP_JAR!
    goto :fail
)

rem --- 4. assemble release\ ----------------------------------------------

echo [4/5] Assembling release\

rem A paths.yaml that the user has already filled in must survive a rebuild,
rem so it is stashed before the folder is cleared and put back afterwards.
set "KEEP_PATHS="
if exist "%RELEASE%\paths.yaml" (
    copy /y "%RELEASE%\paths.yaml" "%TEMP%\asyncjlink-paths.yaml.keep" >nul
    set "KEEP_PATHS=1"
)

if exist "%RELEASE%" rmdir /s /q "%RELEASE%"
mkdir "%RELEASE%" 2>nul

copy /y "!CLI_JAR!"                        "%RELEASE%\creoctl.jar"  >nul || goto :copyfail
copy /y "!MCP_JAR!"                        "%RELEASE%\mcp.jar"      >nul || goto :copyfail
copy /y "%ROOT%Cli\creoctl.cmd"            "%RELEASE%\creoctl.cmd"  >nul || goto :copyfail
copy /y "%ROOT%packaging\SETUP.md"         "%RELEASE%\SETUP.md"     >nul || goto :copyfail
copy /y "%ROOT%Config\paths.example.yaml"  "%RELEASE%\paths.example.yaml" >nul || goto :copyfail

if defined KEEP_PATHS (
    copy /y "%TEMP%\asyncjlink-paths.yaml.keep" "%RELEASE%\paths.yaml" >nul
    del /q "%TEMP%\asyncjlink-paths.yaml.keep" >nul 2>&1
    echo       Kept your existing paths.yaml.
) else (
    copy /y "%ROOT%Config\paths.example.yaml" "%RELEASE%\paths.yaml" >nul || goto :copyfail
    echo       Created paths.yaml from the template - edit it before first use.
)

rem --- 5. generate mcp.json with real absolute paths ----------------------

echo [5/5] Writing mcp.json

rem JSON needs every backslash doubled.
set "MCPJAR=%RELEASE%\mcp.jar"
set "MCPJAR_JSON=!MCPJAR:\=\\!"
set "CFG=%RELEASE%\paths.yaml"
set "CFG_JSON=!CFG:\=\\!"

set "OUT=%RELEASE%\mcp.json"
> "!OUT!" echo {
>>"!OUT!" echo   "mcpServers": {
>>"!OUT!" echo     "creo-jlink": {
>>"!OUT!" echo       "command": "java",
>>"!OUT!" echo       "args": [
>>"!OUT!" echo         "-Dasyncjlink.config=!CFG_JSON!",
>>"!OUT!" echo         "-jar",
>>"!OUT!" echo         "!MCPJAR_JSON!"
>>"!OUT!" echo       ]
>>"!OUT!" echo     }
>>"!OUT!" echo   }
>>"!OUT!" echo }

echo.
echo ===========================================================================
echo  Release ready:  %RELEASE%
echo ===========================================================================
dir /b "%RELEASE%"
echo.
echo Next steps:
echo   1. Edit "%RELEASE%\paths.yaml" - set pro_comm_msg_exe and nms_port.
echo   2. Verify:  java -jar "%RELEASE%\creoctl.jar" --selfcheck
echo   3. For AI clients, merge "%RELEASE%\mcp.json" into your client's config.
echo.
echo See "%RELEASE%\SETUP.md" for the full walkthrough.
echo.
endlocal
exit /b 0

:copyfail
echo.
echo [ERROR] Failed to copy a release file. Is release\ open in another program?

:fail
echo.
echo Release build FAILED.
echo.
endlocal
exit /b 1
