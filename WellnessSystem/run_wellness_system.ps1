# Wellness System Launcher (PowerShell)
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "    WELLNESS SYSTEM LAUNCHER" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set the project directory
$PROJECT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path
$SRC_DIR = Join-Path $PROJECT_DIR "src"
$LIB_DIR = Join-Path $PROJECT_DIR "src\lib"

Write-Host "Project Directory: $PROJECT_DIR" -ForegroundColor Yellow
Write-Host "Source Directory: $SRC_DIR" -ForegroundColor Yellow
Write-Host "Library Directory: $LIB_DIR" -ForegroundColor Yellow
Write-Host ""

# Check if JAR files exist
$requiredJars = @("derby.jar", "derbyshared.jar", "derbytools.jar")
$missingJars = @()

foreach ($jar in $requiredJars) {
    $jarPath = Join-Path $LIB_DIR $jar
    if (-not (Test-Path $jarPath)) {
        $missingJars += $jar
    }
}

if ($missingJars.Count -gt 0) {
    Write-Host "ERROR: Missing JAR files:" -ForegroundColor Red
    foreach ($jar in $missingJars) {
        Write-Host "  - $jar" -ForegroundColor Red
    }
    Write-Host "Please ensure all Derby JAR files are in the src/lib directory." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✅ All required JAR files found" -ForegroundColor Green
Write-Host ""

# Build the classpath
$classpath = "$LIB_DIR\derby.jar;$LIB_DIR\derbyshared.jar;$LIB_DIR\derbytools.jar;$SRC_DIR"

Write-Host "Classpath: $classpath" -ForegroundColor Yellow
Write-Host ""

Write-Host "🔄 Compiling application..." -ForegroundColor Yellow
$compileResult = javac -cp "`"$classpath`"" "$SRC_DIR\wellnesssystem\WellnessSystem.java" 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Compilation failed!" -ForegroundColor Red
    Write-Host "Please check for compilation errors above." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✅ Compilation successful" -ForegroundColor Green
Write-Host ""

Write-Host "🚀 Starting Wellness System..." -ForegroundColor Yellow
Write-Host ""

java -cp "`"$classpath`"" wellnesssystem.WellnessSystem

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Application failed to start!" -ForegroundColor Red
    Write-Host "Check the error messages above." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "✅ Application completed successfully" -ForegroundColor Green
Read-Host "Press Enter to exit" 