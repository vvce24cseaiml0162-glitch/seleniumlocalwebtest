# Push-to-GitHub Automation Script
param(
    [Parameter(Mandatory=$false)]
    [string]$RepoUrl
)

Write-Host "=== Initializing and Pushing SeleniumLocalWebTest to GitHub ===" -ForegroundColor Cyan

# 1. Initialize git if not already initialized
if (-not (Test-Path ".git")) {
    Write-Host "[1/5] Initializing Git repository..." -ForegroundColor Yellow
    git init
} else {
    Write-Host "[1/5] Git repository already initialized." -ForegroundColor Green
}

# 2. Add all files
Write-Host "[2/5] Staging files..." -ForegroundColor Yellow
git add .

# 3. Commit
Write-Host "[3/5] Committing changes..." -ForegroundColor Yellow
git commit -m "Add Selenium Java TestNG project with GitHub Actions and Docker CI/CD" -q

# 4. Set branch to main
Write-Host "[4/5] Setting branch to main..." -ForegroundColor Yellow
git branch -M main

# 5. Remote and push
if ($RepoUrl) {
    Write-Host "[5/5] Setting remote origin to $RepoUrl..." -ForegroundColor Yellow
    git remote remove origin 2>$null
    git remote add origin $RepoUrl
    Write-Host "Pushing to $RepoUrl..." -ForegroundColor Green
    git push -u origin main
} else {
    Write-Host @"
[5/5] Git repository is committed locally!
To push to your remote repository, run:
    git remote add origin https://github.com/<YOUR-USERNAME>/SeleniumLocalWebTest.git
    git push -u origin main
"@ -ForegroundColor Magenta
}
