# Bài 2: Giải thích cơ chế healthcheck và volume

## Vì sao backend bị crash?

`depends_on` dạng danh sách (`- postgres`) chỉ đảm bảo **container** postgres/redis được **start** trước.
Container start xong nhưng tiến trình bên trong chưa chắc đã sẵn sàng: Postgres còn mất vài giây
khởi tạo dữ liệu trước khi nhận kết nối. Backend kết nối ngay lúc đó sẽ bị từ chối và crash.

## Healthcheck đã dùng

| Service | Lệnh kiểm tra | Ý nghĩa |
|---|---|---|
| postgres | `pg_isready -U admin -d storex_db` | Công cụ có sẵn của PostgreSQL, trả về 0 khi server đã nhận kết nối |
| redis | `redis-cli ping` | Redis trả `PONG` khi sẵn sàng phục vụ |

Các tham số:

- `interval: 5s`: chạy lệnh kiểm tra mỗi 5 giây.
- `timeout: 3s`: lệnh chạy quá 3 giây thì tính là thất bại.
- `retries: 10`: thất bại liên tiếp 10 lần thì container bị đánh dấu `unhealthy`.
- `start_period: 10s` (postgres): thời gian khởi động ban đầu, các lần thất bại trong khoảng này không bị tính.

Trạng thái container đi từ `starting` sang `healthy` khi lệnh kiểm tra thành công.

## depends_on với `condition: service_healthy`

```yaml
depends_on:
  postgres:
    condition: service_healthy
  redis:
    condition: service_healthy
```

Docker Compose chỉ khởi động `backend` khi **cả postgres và redis đều ở trạng thái `healthy`**,
nên backend luôn kết nối được ngay từ lần đầu.

## Volume giữ dữ liệu

```yaml
volumes:
  - pgdata:/var/lib/postgresql/data
```

`/var/lib/postgresql/data` là nơi Postgres lưu dữ liệu. Thư mục này được gắn vào named volume `pgdata`,
do Docker quản lý và nằm ngoài vòng đời container. `docker compose down` xóa container nhưng **không xóa volume**,
nên khi `up` lại dữ liệu vẫn còn. Chỉ `docker compose down -v` mới xóa volume.

## Kiểm chứng

Ảnh `healthcheck_volume.png`:

1. `docker compose up -d`: backend chờ postgres và redis `Healthy` rồi mới `Started`.
2. Thêm một sản phẩm qua API.
3. `docker compose down` rồi `docker compose up -d`.
4. Gọi API lại, sản phẩm vẫn còn.
