#!/usr/bin/env bash
# initial-script.sh - Thiet lap moi truong ban dau cho QuickBite
# Cach chay:
#   chmod +x initial-script.sh
#   sudo ./initial-script.sh

set -euo pipefail

APP_USER="quickbite"
APP_GROUP="quickbite"
PACKAGES=(openjdk-17-jdk git curl)

# Script can quyen root de cai goi va tao user
if [[ "$EUID" -ne 0 ]]; then
    echo "[ERROR] Vui long chay script voi sudo: sudo ./initial-script.sh" >&2
    exit 1
fi

# Tranh apt hien hop thoai hoi dap trong qua trinh cai dat
export DEBIAN_FRONTEND=noninteractive

echo "[1/4] Cap nhat he thong..."
sudo apt-get update && sudo apt-get upgrade -y

echo "[2/4] Cai dat cac goi: ${PACKAGES[*]}..."
apt-get install -y "${PACKAGES[@]}"

echo "[3/4] Kiem tra nhom '$APP_GROUP'..."
if getent group "$APP_GROUP" > /dev/null; then
    echo "      Nhom '$APP_GROUP' da ton tai, bo qua."
else
    groupadd --system "$APP_GROUP"
    echo "      Da tao nhom '$APP_GROUP'."
fi

echo "[4/4] Kiem tra user '$APP_USER'..."
if id "$APP_USER" > /dev/null 2>&1; then
    echo "      User '$APP_USER' da ton tai, bo qua."
else
    # -r: system user, khong tu tao thu muc home
    # -g: nhom chinh la quickbite
    # -s /bin/false: khoa dang nhap truc tiep
    useradd -r -g "$APP_GROUP" -s /bin/false "$APP_USER"
    echo "      Da tao user '$APP_USER'."
fi

echo
echo "Hoan tat! Kiem tra:"
id "$APP_USER"
getent passwd "$APP_USER"
java -version 2>&1 | head -n 1
