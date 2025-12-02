# Bài 5: Đấu giá Đơn giản (Simple Auction)

**Mức độ**: Trung bình - Khó
**Giao thức**: TCP
**Kiến thức**: Socket, Multithreading, Timer, Synchronization.

## Mô tả bài toán
Xây dựng hệ thống đấu giá đơn giản cho **một phiên đấu giá** tại một thời điểm. Server sẽ mở phiên đấu giá, đếm ngược thời gian (60s), và xác định người thắng cuộc.

## Yêu cầu chức năng

### 1. Server
- **Cấu hình phiên đấu giá**:
    - **Giá khởi điểm**: 50.
    - **Thời gian**: 60 giây/phiên.
    - **Thông báo**: Cập nhật thời gian còn lại mỗi 5 giây.
- **Quản lý kết nối**:
    - Chấp nhận nhiều Client kết nối qua Port 5001.
    - **Định danh Client**: Sử dụng địa chỉ IP và Port (VD: `127.0.0.1:54321`) để phân biệt người chơi (Không cần đăng nhập tên).
- **Xử lý đấu giá**:
    - Nhận mức giá (số nguyên) từ Client.
    - Kiểm tra hợp lệ: Giá đặt > Giá hiện tại.
    - **Broadcast**: Thông báo cho tất cả Client khi có giá mới cao hơn (VD: "[DAU GIA MOI] 127.0.0.1:54321 da dau gia 100d!").
    - Lưu lịch sử đấu giá.
- **Kết thúc phiên**:
    - Khi hết 60s, Server thông báo người thắng/thua cho từng Client.
    - **Ngắt kết nối** tất cả Client để reset server cho vòng mới.

### 2. Client
- **Kết nối**: Tự động kết nối đến Server (localhost:5001) khi chạy.
- **Giao diện Console**:
    - Nhận thông báo chào mừng và giá hiện tại.
    - Nhập số nguyên từ bàn phím để đặt giá.
- **Luồng xử lý (Multithreading)**:
    - **Thread Gửi**: Đọc số từ bàn phím -> Gửi lên Server.
    - **Thread Nhận**: Luôn lắng nghe và in ra màn hình các thông báo từ Server (Thời gian, Giá mới, Kết quả).

## Kịch bản chạy (Console Flow)

**Server:**
```text
Server is running on port 5001...
[TIMER] Thoi gian con lai: 55s | Gia hien tai: 50d
Client connected: 127.0.0.1:54321
Server received: 60
[DAU GIA MOI] 127.0.0.1:54321 da dau gia 60d!
...
[TIMER] 60s cycle - End round
[SERVER] Round ended!
```

**Client:**
```text
Client connected: localhost/127.0.0.1:5001
[SERVER] Chao mung den phien dau gia! Gia hien tai: 50d
Nhap gia dau gia: 60
Dau gia thanh cong! Gia hien tai: 60d

[DAU GIA MOI] 127.0.0.1:67890 da dau gia 70d!
Nhap gia dau gia: ...

[KET THUC] Chuc mung! Ban da thang voi gia: 100d
(Chương trình kết thúc)
```
