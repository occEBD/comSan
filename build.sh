#!/bin/sh
PAPER_API="paper-api.jar"
SRC_DIR=src
OUT_DIR=out
JARNAME=CommandSanitizer.jar
FILE_COUNT=$(find "$SRC_DIR" -name "*.java" | wc -l | tr -d ' ')
BUILD_TIMESTAMP=$(date +%Y%m%d%H%M%S)
BUILD_ID="${FILE_COUNT}-${BUILD_TIMESTAMP}"
JARNAME="CommandSanitizer-${BUILD_ID}.jar"


set -e
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"


find "$SRC_DIR" -name "*.java" > sources.txt
javac -cp "$PAPER_API" -d "$OUT_DIR" @sources.txt


# copy resources
cp -r src/resources/* "$OUT_DIR/"
cp plugin.yml "$OUT_DIR"


cd "$OUT_DIR"
jar cf ../"$JARNAME" .
cd -


echo "Built $JARNAME. Copy it to your server's plugins/ folder."