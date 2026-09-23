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

if not exist bin mkdir bin

echo Compiling...
javac -d bin src\main\Main.java src\models\*.java
if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

echo Starting KrazyKitchen...
echo.
java -cp bin main.Main

echo.
pause
endlocal
