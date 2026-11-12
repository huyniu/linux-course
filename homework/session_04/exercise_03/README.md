# Bài 3: Tối ưu hóa biến môi trường và Custom Network

## Vấn đề của cấu hình cũ

1. Mật khẩu `supersecret_dont_share` được hardcode trong `docker-compose.yml`. Khi public mã nguồn thì ai cũng đọc được.
2. Cổng `5432` của Postgres được mở ra máy host. Bất kỳ ai truy cập được host đều có thể kết nối thẳng vào database.
3. Mọi container dùng chung default bridge network, không có sự cô lập giữa Backend/Database và các container khác.

## Cách sửa

### Tách cấu hình nhạy cảm ra file `.env`

- `.env.example`: file mẫu, **được commit**, chỉ chứa giá trị giả.
- `.env`: file thật chứa mật khẩu, **bị bỏ qua** bởi `.gitignore`, không đưa lên Git.

Docker Compose tự đọc file `.env` cùng thư mục và thay vào các biến `${DB_HOST}`, `${DB_USER}`, `${DB_PASS}`, `${DB_NAME}`.

Chạy lần đầu:

```bash
cp .env.example .env
# sửa DB_PASS trong .env thành mật khẩu thật
docker compose up -d
```

### Bỏ port mapping 5432

Service `postgres` không còn mục `ports`. Backend vẫn kết nối được qua network nội bộ, còn máy host thì không truy cập trực tiếp được nữa.

### Custom network `storex-net`

Cả `backend` và `postgres` chỉ tham gia `storex-net`. Container nào nằm ngoài network này (ví dụ container chạy trên default bridge) sẽ không phân giải được tên `postgres` và không ping được DB.

## Kiểm chứng

Ảnh `env_network.png`:

- `.env` nằm trong `.gitignore`, compose dùng biến `${...}`.
- `docker compose ps`: postgres không còn cổng mở ra host.
- `docker network inspect storex-net`: chỉ có backend và postgres.
- Backend vẫn gọi API và đọc/ghi DB bình thường.
- Container trên default bridge: `ping postgres` thất bại (`bad address`).
- Từ host, kết nối `localhost:5432` bị từ chối.
