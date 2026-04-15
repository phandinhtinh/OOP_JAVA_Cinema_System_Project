@echo off
title He Thong Quan Ly Rap Phim - Cinema Management System
echo =======================================================
echo     DANG FIX LOI JAVA_HOME VA KHOI DONG UNG DUNG
echo =======================================================

cd /d %~dp0

REM --- 1. THIET LAP JAVA_HOME CHINH XAC ---
REM Uu tien duong dan thuc te tren may ban truoc
if exist "C:\Program Files\Java\jdk-21.0.10" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
) else (
    REM Neu khong phai may ban, tu dong tim kiem java
    for /f "delims=" %%i in ('where java') do set "JAVA_BIN_PATH=%%i"
    if defined JAVA_BIN_PATH (
        set "JAVA_HOME=%JAVA_BIN_PATH:\bin\java.exe=%"
        set "JAVA_HOME=%JAVA_HOME:\bin\java=%"
    )
)

REM --- 2. EP MAVEN NHAN JAVA_HOME ---
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo [KIEM TRA] Java hien tai dang dung: %JAVA_HOME%
echo -------------------------------------------------------

REM --- 3. TIM VA CHAY MAVEN ---
set "MAVEN_CMD=C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd"

if exist "%MAVEN_CMD%" (
    echo [OK] Dang chay bang Maven cua NetBeans...
    REM Dung lenh SET de dam bao Maven nhan dung JAVA_HOME truoc khi call
    set JAVA_HOME=%JAVA_HOME%
    call "%MAVEN_CMD%" javafx:run
) else (
    echo [THONG BAO] Dang thu chay bang Maven he thong...
    call mvn javafx:run
)

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [LOI] Van bi loi JAVA_HOME. 
    echo Giai phap cuoi cung: Mo NetBeans -> Right Click Project -> Run.
)

pause