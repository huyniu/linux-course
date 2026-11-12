# Bài 1: Khởi tạo môi trường phát triển cơ bản với Docker Compose

## Nguyên nhân lỗi `Connection refused`

Mỗi container có network namespace riêng. Bên trong container `backend`, `localhost` (127.0.0.1)
trỏ về **chính container backend**, không phải máy host và cũng không phải container `postgres`.
Trong container backend không có tiến trình nào lắng nghe cổng 5432, nên kết nối bị từ chối.

Docker Compose tự tạo một network chung cho các service trong file và cung cấp DNS nội bộ:
mỗi service được truy cập bằng **tên service**. Vì vậy backend phải kết nối tới `postgres:5432`.

## Cách sửa

```diff
- SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/storex
+ SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/storex
```

Thêm vào service `backend`:

- `depends_on: [postgres]`: khởi động container postgres trước backend.
- `restart: on-failure`: Postgres cần vài giây để sẵn sàng; nếu backend kết nối quá sớm và lỗi thì tự khởi động lại.

Đã bỏ dòng `version: '3'` vì Docker Compose v2 coi trường này là lỗi thời và in cảnh báo.

## Chạy và kiểm tra

```bash
docker compose up -d
docker compose ps
docker compose logs backend | grep -E "HikariPool|Started"
```

Ảnh log kết nối thành công: `compose_logs.png`.
