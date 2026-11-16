#!/usr/bin/env bash
# setup-db.sh - Chuan bi mang ngoai quickbite-net va cac database cho 4 dich vu
# Yeu cau: container quickbite-db (postgres:15-alpine) da duoc tao o Session 02.
# Cach chay: ./setup-db.sh

set -euo pipefail

NETWORK="quickbite-net"
DB_CONTAINER="quickbite-db"

# Tao mang ngoai neu chua co
docker network inspect "$NETWORK" > /dev/null 2>&1 || docker network create "$NETWORK"

# Khoi dong database va gan vao mang quickbite-net
docker start "$DB_CONTAINER" > /dev/null
docker network connect "$NETWORK" "$DB_CONTAINER" 2> /dev/null || true

# Doi Postgres san sang
until docker exec "$DB_CONTAINER" pg_isready -U postgres > /dev/null 2>&1; do
    sleep 1
done

# Moi dich vu mot database rieng
for db in quickbite_user_db quickbite_restaurant_db quickbite_order_db quickbite_notification_db; do
    exists=$(docker exec "$DB_CONTAINER" psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname = '$db'")
    if [[ "$exists" != "1" ]]; then
        docker exec "$DB_CONTAINER" psql -U postgres -c "CREATE DATABASE $db"
    fi
done

docker exec "$DB_CONTAINER" psql -U postgres -c "\l quickbite*"
