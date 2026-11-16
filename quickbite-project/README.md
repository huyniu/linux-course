# QuickBite Project (Session 05)

Bốn dịch vụ Spring Boot (Java 17, Gradle), mỗi dịch vụ có `Dockerfile` riêng và một database riêng trong container `quickbite-db`.

| Dịch vụ | Thư mục | Image | Cổng | Database | Bảng do Hibernate tạo |
|---|---|---|---|---|---|
| quickbite-user | `user-service/` | `quickbite-user-service:latest` | 8081 | `quickbite_user_db` | `users`, `user_addresses`, `user_wallets` |
| quickbite-restaurant | `restaurant-service/` | `quickbite-restaurant-service:latest` | 8082 | `quickbite_restaurant_db` | `restaurants`, `menu_items` |
| quickbite-order | `order-service/` | `quickbite-order-service:latest` | 8083 | `quickbite_order_db` | `orders`, `order_items` |
| quickbite-notification | `notification-service/` | `quickbite-notification-service:latest` | 8084 | `quickbite_notification_db` | `notifications` |

## Cách chạy

```bash
./setup-db.sh              # tạo mạng ngoài quickbite-net, gắn quickbite-db, tạo 4 database
./build-all.sh             # ./gradlew bootJar + docker build cho cả 4 dịch vụ
cp .env.example .env       # rồi sửa mật khẩu thật trong .env
docker compose up -d --build
docker compose ps
```

## Ghi chú

- `docker-compose.yml` dùng mạng ngoài `quickbite-net` (`external: true`) để kết nối tới `quickbite-db` đang chạy độc lập.
- Mọi giá trị cấu hình lấy từ `.env` qua cú pháp `${...}`, có tiền tố riêng cho từng dịch vụ (`USER_`, `RESTAURANT_`, `ORDER_`, `NOTIFICATION_`).
- `.env` nằm trong `.gitignore`; chỉ `.env.example` (đã ẩn mật khẩu) được đưa lên Git.
- Cả 4 ứng dụng đặt `spring.jpa.hibernate.ddl-auto=update`.

Ảnh kết quả từng bài nằm trong `homework/session_05/`.
