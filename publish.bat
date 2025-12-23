@echo off
REM NDS API 發布腳本 (Windows)
REM 用於快速發布 NDS API 到 Maven 倉庫

echo ========================================
echo NDS API 發布腳本
echo ========================================
echo.

REM 檢查是否在正確的目錄
if not exist "build.gradle" (
    echo 錯誤：請在 nds-api 目錄下執行此腳本
    pause
    exit /b 1
)

echo [1/3] 清理舊構建...
call ..\gradlew.bat :nds-api:clean
if errorlevel 1 (
    echo 清理失敗！
    pause
    exit /b 1
)

echo.
echo [2/3] 構建項目...
call ..\gradlew.bat :nds-api:build
if errorlevel 1 (
    echo 構建失敗！請檢查錯誤信息
    pause
    exit /b 1
)

echo.
echo [3/3] 發布到 Maven 倉庫...
call ..\gradlew.bat :nds-api:publish
if errorlevel 1 (
    echo 發布失敗！請檢查認證信息
    pause
    exit /b 1
)

echo.
echo ========================================
echo 發布成功！
echo ========================================
echo.
echo 請訪問以下地址驗證：
echo https://repo.repsy.io/mvn/linmimeng/releases/noie/linmimeng/noiedigitalsystem-api/
echo.
pause

