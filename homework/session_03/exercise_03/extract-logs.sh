#!/usr/bin/env bash
# extract-logs.sh - Trich xuat 15 dong log cuoi kem moc thoi gian cua rikkei-course-service
# Cach chay: sudo ./extract-logs.sh

set -euo pipefail

CONTAINER_NAME="rikkei-course-service"

# --tail 15: chi lay 15 dong cuoi
# -t (--timestamps): gan moc thoi gian vao dau moi dong
docker logs --tail 15 -t "$CONTAINER_NAME"
