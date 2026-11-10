#!/usr/bin/env bash
# exec-frontend.sh - Khoi chay container Frontend QA voi bien moi truong va vao shell kiem tra
# Cach chay: sudo ./exec-frontend.sh

set -euo pipefail

CONTAINER_NAME="rikkei-frontend-qa"
IMAGE="nginx"
API_ENDPOINT="https://qa-api.rikkei.edu.vn"

docker rm -f "$CONTAINER_NAME" > /dev/null 2>&1 || true

# -e: truyen bien moi truong vao container
docker run -d --name "$CONTAINER_NAME" -e API_ENDPOINT="$API_ENDPOINT" "$IMAGE"

# Mo bash shell tuong tac ben trong container.
# Tai shell cua container, chay:  echo $API_ENDPOINT
docker exec -it "$CONTAINER_NAME" bash
