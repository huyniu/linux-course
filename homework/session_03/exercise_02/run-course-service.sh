#!/usr/bin/env bash
# run-course-service.sh - Khoi chay Course-Service phien ban Mock bang Docker
# Cach chay: sudo ./run-course-service.sh

set -euo pipefail

CONTAINER_NAME="rikkei-course-service"
IMAGE="nginxdemos/hello"
HOST_PORT=8081
CONTAINER_PORT=80

# Xoa container cu (neu co) de chay lai khong bi trung ten
docker rm -f "$CONTAINER_NAME" > /dev/null 2>&1 || true

# -d: chay ngam (detached), --name: dat ten, -p host:container: anh xa cong
# Neu may chua co image, Docker se tu dong pull tu Docker Hub
docker run -d --name "$CONTAINER_NAME" -p "$HOST_PORT:$CONTAINER_PORT" "$IMAGE"

# Kiem tra container dang chay
docker ps --filter "name=$CONTAINER_NAME"

# Doi Nginx khoi dong roi goi thu dich vu qua cong 8081 cua host
sleep 2
curl -s "http://localhost:$HOST_PORT"
