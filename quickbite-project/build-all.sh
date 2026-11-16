#!/usr/bin/env bash
# build-all.sh - Build file JAR va dong goi Docker image cho ca 4 dich vu
# Cach chay: ./build-all.sh  (trong thu muc quickbite-project)

set -euo pipefail
cd "$(dirname "$0")"

for s in user restaurant order notification; do
    echo "==> $s-service"
    (
        cd "$s-service"
        chmod +x gradlew
        ./gradlew bootJar -q
        docker build -q -t "quickbite-$s-service:latest" .
    )
done

docker images | grep quickbite
