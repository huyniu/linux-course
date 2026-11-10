#!/usr/bin/env bash
# setup-workspace.sh - Thiet lap user va khong gian lam viec cho Rikkei LMS (course-service)
# Cach chay:
#   chmod +x setup-workspace.sh
#   sudo ./setup-workspace.sh
#
# Quyen 755 (drwxr-xr-x):
#   7 = rwx cho chu so huu rikkeilms: duy nhat user nay duoc ghi (tao/sua/xoa file).
#   5 = r-x cho nhom rikkeilms va 5 = r-x cho nguoi khac: chi doc va vao thu muc, khong duoc ghi.

set -euo pipefail

APP_USER="rikkeilms"
APP_DIR="/opt/rikkei/course-service"

if [[ "$EUID" -ne 0 ]]; then
    echo "[ERROR] Vui long chay script voi sudo: sudo ./setup-workspace.sh" >&2
    exit 1
fi

# In lenh ra man hinh truoc khi chay de de theo doi
run() {
    echo "+ $*"
    "$@"
}

# 1. Tao user rikkeilms (kem group cung ten), service khong chay bang root
if id "$APP_USER" > /dev/null 2>&1; then
    echo "User '$APP_USER' da ton tai, bo qua."
else
    # -r: system user, -U: tao group cung ten, -s nologin: khong cho dang nhap
    run useradd -r -U -s /usr/sbin/nologin "$APP_USER"
fi

# 2. Tao thu muc chua ma nguon va cau hinh
run mkdir -p "$APP_DIR"

# 3. Chuyen quyen so huu cho rikkeilms
run chown -R "$APP_USER:$APP_USER" "$APP_DIR"

# 4. Chi chu so huu duoc ghi
run chmod 755 "$APP_DIR"

# 5. Kiem tra
run ls -ld "$APP_DIR"
