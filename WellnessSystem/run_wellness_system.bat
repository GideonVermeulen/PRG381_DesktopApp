@echo off
echo ========================================
echo    WELLNESS SYSTEM LAUNCHER
echo ========================================
echo.

REM Set the project directory
set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src
set LIB_DIR=%PROJECT_DIR%src\lib

echo Project Directory: %PROJECT_DIR%
echo Source Directory: %SRC_DIR%
echo Library Directory: %LIB_DIR%
echo.

REM Check if JAR files exist
if not exist "%LIB_DIR%\derby.jar" (
    echo ERROR: derby.jar not found in %LIB_DIR%
    echo Please ensure all Derby JAR files are in the src/lib directory.
    pause
    exit /b 1
)

if not exist "%LIB_DIR%\derbyshared.jar" (
    echo ERROR: derbyshared.jar not found in %LIB_DIR%
    pause
    exit /b 1
)

if not exist "%LIB_DIR%\derbytools.jar" (
    echo ERROR: derbytools.jar not found in %LIB_DIR%
    pause
    exit /b 1
)

echo ✅ All required JAR files found
echo.

REM Build the classpath
set CLASSPATH=%LIB_DIR%\derby.jar;%LIB_DIR%\derbyshared.jar;%LIB_DIR%\derbytools.jar;%SRC_DIR%

echo Classpath: %CLASSPATH%
echo.

echo 🔄 Compiling application...
javac -cp "%CLASSPATH%" %SRC_DIR%\wellnesssystem\WellnessSystem.java %SRC_DIR%\wellnesssystem\view\AppointmentsInterface.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check for compilation errors above.
    pause
    exit /b 1
)

echo ✅ Compilation successful
echo.

echo 🚀 Starting Wellness System...
echo.
java -cp "%CLASSPATH%" wellnesssystem.WellnessSystem

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Application failed to start!
    echo Check the error messages above.
    pause
    exit /b 1
)

echo.
echo ✅ Application completed successfully
pause 