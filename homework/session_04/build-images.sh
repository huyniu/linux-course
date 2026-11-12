#!/usr/bin/env bash
# build-images.sh - Build image storex-backend dung chung cho cac bai Session 04
# Cach chay: ./build-images.sh  (trong thu muc homework/session_04)

set -euo pipefail
cd "$(dirname "$0")"

docker build -t storex-backend:latest ./storex-backend
docker tag storex-backend:latest storex-backend:v2
docker tag storex-backend:latest storex-backend:v3
