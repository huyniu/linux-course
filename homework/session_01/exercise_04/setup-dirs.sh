#!/usr/bin/env bash
# setup-dirs.sh - Tao thu muc ung dung user-service va phan quyen toi gian
# Yeu cau: da chay initial-script.sh (exercise_03) de co user/group quickbite.
# Cach chay:
#   chmod +x setup-dirs.sh
#   sudo ./setup-dirs.sh
#
# Y nghia ma quyen 750 (rwxr-x---):
#   7 = rwx (4+2+1) cho CHU SO HUU (user quickbite): doc, ghi, vao thu muc.
#   5 = r-x (4+0+1) cho NHOM (group quickbite): doc va vao thu muc, khong duoc ghi/xoa.
#   0 = --- (0)     cho NGUOI KHAC: khong duoc lam gi, ke ca liet ke hay doc file
#                   cau hinh nhay cam (mat khau DB, secret key...) ben trong.
# => Nguyen tac Least Privilege: chi user chay dich vu moi thao tac duoc,
#    nhom chi duoc doc, nguoi ngoai bi chan hoan toan.

set -euo pipefail

APP_USER="quickbite"
APP_GROUP="quickbite"
BASE_DIR="/opt/quickbite"
APP_DIR="$BASE_DIR/user-service"

if [[ "$EUID" -ne 0 ]]; then
    echo "[ERROR] Vui long chay script voi sudo: sudo ./setup-dirs.sh" >&2
    exit 1
fi

if ! id "$APP_USER" > /dev/null 2>&1; then
    echo "[ERROR] Chua co user '$APP_USER'. Hay chay initial-script.sh truoc." >&2
    exit 1
fi

echo "[1/3] Tao thu muc $APP_DIR..."
mkdir -p "$APP_DIR"

echo "[2/3] Doi chu so huu $BASE_DIR cho $APP_USER:$APP_GROUP..."
chown -R "$APP_USER:$APP_GROUP" "$BASE_DIR"

echo "[3/3] Dat quyen 750 cho $BASE_DIR..."
chmod 750 "$BASE_DIR"

echo
echo "Hoan tat! Kiem tra:"
ls -la /opt
