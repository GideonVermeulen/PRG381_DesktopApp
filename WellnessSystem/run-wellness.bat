@echo off
echo Starting Wellness System...
echo.
set CLASSPATH=src;src\lib\derby.jar;src\lib\derbyshared.jar;src\lib\derbytools.jar
java -cp "%CLASSPATH%" wellnesssystem.WellnessSystem
pause 