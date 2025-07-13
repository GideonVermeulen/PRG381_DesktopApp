@echo off
echo Starting Wellness System from IDE...
echo.
echo Setting up classpath with Derby JARs...
set CLASSPATH=src;src\lib\derby.jar;src\lib\derbyshared.jar;src\lib\derbytools.jar
echo Classpath: %CLASSPATH%
echo.
echo Launching application...
java -cp "%CLASSPATH%" wellnesssystem.WellnessSystem
echo.
echo Application closed.
pause 