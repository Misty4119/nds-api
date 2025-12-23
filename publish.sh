#!/bin/bash
# NDS API 發布腳本 (Linux/Mac)
# 用於快速發布 NDS API 到 Maven 倉庫

set -e

echo "========================================"
echo "NDS API 發布腳本"
echo "========================================"
echo ""

# 檢查是否在正確的目錄
if [ ! -f "build.gradle" ]; then
    echo "錯誤：請在 nds-api 目錄下執行此腳本"
    exit 1
fi

echo "[1/3] 清理舊構建..."
../gradlew :nds-api:clean

echo ""
echo "[2/3] 構建項目..."
../gradlew :nds-api:build

echo ""
echo "[3/3] 發布到 Maven 倉庫..."
../gradlew :nds-api:publish

echo ""
echo "========================================"
echo "發布成功！"
echo "========================================"
echo ""
echo "請訪問以下地址驗證："
echo "https://repo.repsy.io/mvn/linmimeng/releases/noie/linmimeng/noiedigitalsystem-api/"
echo ""

