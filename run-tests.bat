@echo off
echo ========================================================
echo   Running Selenium Java TestNG Tests Locally
echo ========================================================
mvn clean test -Dheadless=true
pause
