echo off
cd C:\ADO_FCC_FireAutomation\Automation\
C:
mvn clean test -Dxml=custom -Denv=custom-smoke
