# Selenium Java Project with TestNG, GitHub Actions & Docker CI/CD

Automated functional test suite built with **Selenium WebDriver (Java)** and **TestNG**, integrated with a complete CI/CD pipeline using **GitHub Actions** and containerized deployment to **Docker Hub**.

---

## 📁 Project Structure

```text
SeleniumLocalWebTest/
├── .github/
│   └── workflows/
│       └── selenium-tests.yml        # Multi-stage GitHub Actions CI/CD pipeline
├── src/
│   ├── main/
│   │   └── resources/
│   │       └── webpage/
│   │           └── login.html        # Local HTML application under test (resources)
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── LocalLoginTest.java  # TestNG Selenium test class
├── webpage/
│   └── login.html                    # Root copy of HTML application
├── Dockerfile                        # Multi-stage Docker build for headless Chrome & TestNG
├── pom.xml                           # Maven project configuration (Selenium 4.25.0 + TestNG 7.10.2)
├── testng.xml                        # TestNG suite execution descriptor
├── .gitignore                        # Standard Java/Maven/IDE ignore rules
├── run-tests.bat                     # Quick local test runner script
├── push-to-github.ps1                # Git repository initialization and push helper
└── docker-build-run.ps1              # Local Docker build and run script
```

---

## 🛠️ Features & Highlights

1. **Selenium 4.25.0 + TestNG 7.10.2**:
   - Automated browser interaction with Selenium WebDriver.
   - Comprehensive assertions and reporting via TestNG.
   - Cross-environment file locator for `login.html` (supports classpath and filesystem paths).
2. **Headless Chrome Optimization**:
   - Uses `--headless=new`, `--no-sandbox`, `--disable-dev-shm-usage`, and standard resolution `1920x1080` for smooth cloud execution.
   - Dynamic detection for local GUI testing (`-Dheadless=false`) vs CI headless execution.
3. **GitHub Actions CI/CD (`selenium-tests.yml`)**:
   - **Job 1 (`selenium-test`)**: Sets up JDK 17, installs Google Chrome, executes `mvn clean test`, and uploads the HTML test reports as artifacts.
   - **Job 2 (`build-and-publish-docker`)**: Triggers only on merges/pushes to `main` upon test success. Logs into Docker Hub, builds multi-stage container image, and pushes tags (`latest` and `${{ github.sha }}`).
4. **Resilient Dockerfile**:
   - Multi-stage build with `maven:3.9.6-eclipse-temurin-17`.
   - Installs official `google-chrome-stable` with proper APT repository keys.
   - Includes full Maven CLI and pre-cached `.m2` dependencies so `mvn test` executes inside container without extra network overhead.

---

## 🚀 Quick Start Guide

### 1. Run Tests Locally

**Via Maven CLI:**
```bash
# Run headless (recommended for CI/terminals)
mvn clean test

# Run with visible Chrome window
mvn clean test -Dheadless=false
```

**Via Windows Script:**
Double-click `run-tests.bat` or run:
```powershell
.\run-tests.bat
```

**In Eclipse / IntelliJ:**
1. Open Eclipse -> **File** -> **Import** -> **Existing Maven Projects** -> Select `SeleniumLocalWebTest`.
2. Right-click `testng.xml` (or `LocalLoginTest.java`).
3. Select **Run As** -> **TestNG Suite** (or **TestNG Test**).
4. View results in the TestNG Results tab and in `target/surefire-reports/index.html`.

---

## 🧪 Test Cases Implemented

| # | Test Name | Description | Expected Outcome |
|---|-----------|-------------|------------------|
| 1 | `validLoginTest` | Enters `student` / `12345` and clicks login | `#message` displays `"Login Successful"` |
| 2 | `invalidLoginTest` | Enters `wrong` / `wrong` and clicks login | `#message` displays `"Invalid Username or Password"` |
| 3 | `checkBackgroundColor` | Inspects CSS `background-color` of `#loginBox` | Matches `"rgba(173, 216, 230, 1)"` (lightblue) |

---

## 🚢 GitHub Actions & Docker Hub Setup

### 1. Configure GitHub Secrets
In your GitHub repository:
- Go to **Settings** -> **Secrets and variables** -> **Actions** -> **New repository secret**.
- Add the following two secrets:
  - `DOCKERHUB_USERNAME`: Your Docker Hub username (e.g. `dradityacr`)
  - `DOCKERHUB_TOKEN`: Your Docker Hub Personal Access Token (Read & Write permissions)

### 2. Push Code to GitHub
Using the provided PowerShell helper:
```powershell
.\push-to-github.ps1 -RepoUrl "https://github.com/<YOUR-USERNAME>/SeleniumLocalWebTest.git"
```

Or manually:
```bash
git init
git add .
git commit -m "Add Selenium Java TestNG project with GitHub Actions and Docker CI/CD"
git branch -M main
git remote add origin https://github.com/<YOUR-USERNAME>/SeleniumLocalWebTest.git
git push -u origin main
```

---

## 🐳 Running in Docker Locally

Build and run the container locally:
```powershell
.\docker-build-run.ps1
```
Or with raw Docker commands:
```bash
docker build -t selenium-testng-automation .
docker run --rm --shm-size=2g selenium-testng-automation
```
