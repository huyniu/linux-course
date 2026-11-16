# Banking Project

Hệ thống ngân hàng mẫu chạy bằng Docker Compose, gồm 3 service trong mạng nội bộ `banking-network`.

| Service | Mô tả | Cổng ra host |
|---|---|---|
| `core-db` | PostgreSQL 15, database `banking_db`, dữ liệu lưu trên volume `pg-data` | không mở |
| `account-service` | Spring Boot: tạo và truy vấn tài khoản | 8080 |
| `transaction-service` | Spring Boot: chuyển tiền, gọi nội bộ sang `account-service` | 8081 |

## Cách chạy

```bash
cp .env.example .env        # rồi sửa DB_PASSWORD trong .env
docker compose up -d --build
docker compose ps
```

`.env` nằm trong `.gitignore`, chỉ `.env.example` được đưa lên Git.

## API

### account-service (`http://localhost:8080`)

| Method | Đường dẫn | Mô tả |
|---|---|---|
| POST | `/api/accounts` | Tạo tài khoản: `{"accountNumber","ownerName","initialBalance"}`. Trả 201, hoặc 409 nếu trùng số tài khoản |
| GET | `/api/accounts` | Danh sách tài khoản |
| GET | `/api/accounts/{accountNumber}` | Thông tin một tài khoản, 404 nếu không có |
| POST | `/api/accounts/{accountNumber}/debit` | Trừ tiền (nội bộ, `transaction-service` gọi) |
| POST | `/api/accounts/{accountNumber}/credit` | Cộng tiền (nội bộ, `transaction-service` gọi) |

### transaction-service (`http://localhost:8081`)

| Method | Đường dẫn | Mô tả |
|---|---|---|
| POST | `/api/transactions/transfer` | Chuyển tiền: `{"fromAccount","toAccount","amount"}` |
| GET | `/api/transactions` | Lịch sử giao dịch |

Luồng chuyển tiền:

1. `transaction-service` gọi `GET http://account-service:8080/api/accounts/{fromAccount}` bằng `RestTemplate` để kiểm tra tài khoản nguồn. Không tồn tại thì trả 404.
2. Kiểm tra tài khoản đích theo cách tương tự.
3. Số dư không đủ thì trả 422.
4. Gọi `debit` tài khoản nguồn rồi `credit` tài khoản đích. Nếu `credit` lỗi thì hoàn tiền về tài khoản nguồn.
5. Lưu giao dịch vào bảng `transactions`.

Địa chỉ `account-service` lấy từ biến `ACCOUNT_SERVICE_URL=http://account-service:8080`, tức là tên service trong Docker network, không dùng `localhost` hay IP tĩnh.

Hai bước trừ và cộng tiền là hai request riêng, chưa phải một transaction phân tán. Nếu `transaction-service` dừng đột ngột giữa hai bước thì cần đối soát thủ công.

## Từng bài tập

| Bài | Nội dung | Commit | Ảnh trong `evidence/` |
|---|---|---|---|
| 1 | `banking-app` + `core-db`, HikariCP kết nối `banking_db` | `Banking - Bai 1` | `bai1_banking_app_log.png` |
| 2 | Đổi thành `account-service`, thêm REST API tài khoản | `Banking - Bai 2` | `bai2_create_account_and_db.png` |
| 3 | Mạng `banking-network`, volume `pg-data`, bỏ cổng 5432 | `Banking - Bai 3` | `bai3_down_up_data_persist.png` |
| 4 | `transaction-service` gọi `account-service` qua DNS nội bộ | `Banking - Bai 4` | `bai4_transfer_api.png`, `bai4_code_and_logs.png` |

## Postman

Import file `banking.postman_collection.json` vào Postman, hoặc chạy bằng Postman CLI:

```bash
postman collection run banking.postman_collection.json
```

Request "Chuyen tien" trong collection chuyển tới tài khoản `1000000001`, nên cần tạo tài khoản này trước.
