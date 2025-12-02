# Bài 5: Đấu giá Đơn giản (Simple Auction)

**Mức độ**: Trung bình - Khó
**Giao thức**: TCP (Khuyến nghị) hoặc UDP
**Kiến thức**: Socket, Multithreading, Timer, Synchronization.

## Mô tả bài toán
Xây dựng hệ thống đấu giá đơn giản cho **một phiên đấu giá** tại một thời điểm. Server sẽ mở phiên đấu giá, đếm ngược thời gian, và xác định người thắng cuộc. Sau khi phiên kết thúc, Server tự động reset để bắt đầu phiên mới.

## Yêu cầu chức năng

### 1. Server
- **Quản lý phiên đấu giá**:
    - Chỉ có **1 sản phẩm** duy nhất trong một phiên.
    - Thiết lập **Giá sàn** (Starting Price) và **Thời gian đếm ngược** (ví dụ: 30 giây).
- **Xử lý đấu giá (Real-time)**:
    - Nhận mức giá đặt (Bid) từ các Client.
    - Kiểm tra tính hợp lệ: Giá đặt > Giá hiện tại.
    - **Broadcast**: Khi có giá mới, lập tức thông báo cho tất cả Client đang kết nối (VD: "User A vừa đặt 500").
- **Quản lý thời gian (Timer)**:
    - Đếm ngược thời gian thực.
    - Có thể thông báo thời gian còn lại định kỳ (VD: mỗi 5s hoặc 10s).
    - **Kết thúc phiên**: Khi hết giờ, Server chốt người thắng cuộc, thông báo cho tất cả Client, và bắt đầu phiên mới.

### 2. Client
- **Kết nối**: Nhập tên (Username) để tham gia.
- **Giao diện**:
    - Hiển thị thông tin phiên đấu giá hiện tại (Giá cao nhất, Người giữ giá, Thời gian còn lại).
    - Nhập lệnh để đặt giá (VD: nhập số tiền `500` để bid).
- **Nhận thông báo (Listener)**:
    - Luôn lắng nghe thông báo từ Server để cập nhật giao diện ngay lập tức khi:
        - Có người khác đặt giá cao hơn.
        - Thời gian sắp hết.
        - Thông báo người thắng cuộc khi hết giờ.

## Kịch bản ví dụ
1. **Server** Start: Giá sàn 100, Time 60s.
2. **User A** vào: Thấy giá 100. Bid 120.
3. **Server**: Broadcast "Giá mới 120 bởi User A".
4. **User B** vào: Thấy giá 120. Bid 150.
5. **Server**: Broadcast "Giá mới 150 bởi User B".
6. ... (Hết giờ) ...
7. **Server**: Broadcast "HẾT GIỜ! User B thắng với giá 150".
8. **Server**: "Bắt đầu phiên mới...", Reset giá về 100, Time 60s.
