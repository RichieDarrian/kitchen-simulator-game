@echo off
setlocal
title KrazyKitchen

rem Always run from the project root so inventory.txt, restaurant.txt
rem and score.txt are found relative to the working directory.
cd /d "%~dp0"

where javac >nul 2>nul
if errorlevel 1 (
    echo [ERROR] javac not found. Install JDK 8 or later and make sure it is on your PATH.
    pause
    exit /b 1
)

rem Use the java.exe that sits next to the javac.exe we compile with,
rem so the compiler and runtime versions always match.
set "JAVAC="
for /f "delims=" %%i in ('where javac') do (
    if not defined JAVAC set "JAVAC=%%i"
)
for %%i in ("%JAVAC%") do set "JDK_BIN=%%~dpi"
set "JAVA=%JDK_BIN%java.exe"
if not exist "%JAVA%" set "JAVA=java"

if not exist bin mkdir bin

echo Using JDK: %JDK_BIN%
echo Compiling...
"%JAVAC%" -d bin src\main\Main.java src\models\*.java
if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

echo Starting KrazyKitchen...
echo.
"%JAVA%" -cp bin main.Main

echo.
pause
endlocal
