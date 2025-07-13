@echo off
echo ========================================
echo    WELLNESS SYSTEM DATABASE FIX
echo ========================================
echo.
echo This utility will fix Derby database restart issues.
echo.
echo Press any key to continue...
pause >nul

echo.
echo Step 1: Compiling database utilities...
javac -cp "src/lib/*;src" src/db/DBConnection.java src/db/DatabaseManager.java src/db/DatabaseCleanup.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to compile database utilities!
    echo Make sure you have Java installed and are in the correct directory.
    pause
    exit /b 1
)

echo.
echo Step 2: Running database cleanup...
java -cp "src/lib/*;src" db.DatabaseCleanup

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Database cleanup failed!
    echo Try manually deleting the wellnessDB folder and running DatabaseSetup.
    pause
    exit /b 1
)

echo.
echo Step 3: Testing database connection...
java -cp "src/lib/*;src" db.DatabaseSetup

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Database setup failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo    DATABASE FIX COMPLETED!
echo ========================================
echo.
echo You can now run the Wellness System application.
echo.
pause 