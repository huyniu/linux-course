# Bài 4: Xây dựng hệ thống Reverse Proxy với Nginx

## Vì sao Nginx trả về `502 Bad Gateway`?

`nginx_proxy`, `user_service`, `product_service` là ba container riêng biệt. Bên trong container Nginx,
`localhost` là **chính container Nginx**. Ở đó không có tiến trình nào lắng nghe cổng 8081/8082,
nên Nginx không kết nối được tới upstream và trả về `502 Bad Gateway`.

Docker Compose đặt các service vào cùng một network và cung cấp DNS nội bộ theo **tên service**,
vì vậy Nginx phải gọi `user_service:8081` và `product_service:8082`.

## Cách sửa `nginx.conf`

```diff
  location /users/ {
-     proxy_pass http://localhost:8081/;
+     proxy_pass http://user_service:8081/;
  }
  location /products/ {
-     proxy_pass http://localhost:8082/;
+     proxy_pass http://product_service:8082/;
  }
```

Bổ sung các header `Host`, `X-Real-IP`, `X-Forwarded-For`, `X-Forwarded-Proto` để backend biết thông tin request gốc.

## Rewrite path

`proxy_pass` có dấu `/` ở cuối nên Nginx **thay tiền tố** `location` bằng `/`:

| Request tới proxy | Chuyển tiếp tới |
|---|---|
| `http://localhost/users/` | `http://user_service:8081/` |
| `http://localhost/users/42` | `http://user_service:8081/42` |
| `http://localhost/products/abc?x=1` | `http://product_service:8082/abc?x=1` |

Nếu backend lại mong nhận đủ đường dẫn `/users/42`, bỏ phần path ở `proxy_pass` (`proxy_pass http://user_service:8081;`).

## Service giả lập

Image `storex-user` và `storex-product` không có sẵn, nên thư mục `user-service/` và `product-service/` build hai service giả lập
(Nginx nhỏ) lắng nghe cổng 8081/8082 và trả về JSON chứa tên service cùng path nhận được. Nhờ vậy có thể thấy rõ request được định tuyến và rewrite thế nào.

## Chạy và kiểm tra

```bash
docker compose up -d --build
curl http://localhost/users/
curl http://localhost/users/42
curl http://localhost/products/
curl http://localhost/products/abc?color=red
```

Ảnh kết quả: `reverse_proxy.png`.
