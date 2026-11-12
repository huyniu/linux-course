# Session 04: Docker Compose cơ bản (dự án StoreX)

| Bài | Thư mục | Nội dung |
|---|---|---|
| [Khá] 1 | `exercise_01/` | Sửa lỗi `Connection refused`: backend dùng tên service `postgres` thay vì `localhost` |
| [Khá] 2 | `exercise_02/` | Healthcheck cho postgres/redis, `depends_on: service_healthy`, volume giữ dữ liệu |
| [Giỏi] 1 | `exercise_03/` | Tách thông tin nhạy cảm ra `.env`, bỏ port 5432, custom network `storex-net` |
| [Giỏi] 2 | `exercise_04/` | Nginx reverse proxy dùng DNS nội bộ `user_service`, `product_service` |

Mỗi thư mục có `docker-compose.yml`, file giải thích (`README.md` hoặc `HEALTHCHECK.md`) và ảnh chụp kết quả chạy thật.

## Chuẩn bị image backend

Image `storex-backend` (bài 1–3) được build từ project Spring Boot trong `storex-backend/`
(REST API `/api/products`, kết nối PostgreSQL qua biến môi trường `SPRING_DATASOURCE_*`):

```bash
./build-images.sh
```

Script tạo các tag `storex-backend:latest`, `:v2`, `:v3`.
