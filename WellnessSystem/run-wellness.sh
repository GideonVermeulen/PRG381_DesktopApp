#!/bin/bash
echo "Starting Wellness System..."
echo ""
CLASSPATH="src:src/lib/derby.jar:src/lib/derbyshared.jar:src/lib/derbytools.jar"
java -cp "$CLASSPATH" wellnesssystem.WellnessSystem 