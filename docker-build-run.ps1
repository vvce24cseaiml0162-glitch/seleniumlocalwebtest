# Docker Build & Run Automation Script
param(
    [string]$ImageName = "selenium-testng-automation:local"
)

Write-Host "=== Building Docker Image ($ImageName) ===" -ForegroundColor Cyan
docker build -t $ImageName .

if ($LASTEXITCODE -eq 0) {
    Write-Host "=== Running Selenium TestNG Tests inside Container ===" -ForegroundColor Green
    docker run --rm --shm-size=2g $ImageName
} else {
    Write-Host "Docker build failed! Please check Docker daemon is running." -ForegroundColor Red
}
