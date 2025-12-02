# Bài 5: Hệ thống Đấu giá Trực tuyến (Online Auction System)

**Mức độ**: Khó (Hard)
**Giao thức**: TCP
**Kiến thức**: Socket, Multithreading, Synchronization, Shared State Management.

## Mô tả bài toán
Xây dựng một hệ thống đấu giá trực tuyến gồm Server và nhiều Client. Server quản lý các sản phẩm đang được đấu giá. Các Client kết nối tới Server để xem sản phẩm và đặt giá (Bid).

## Yêu cầu chức năng

### 1. Server
- **Quản lý sản phẩm**: Server lưu trữ danh sách các sản phẩm (ID, Tên, Giá khởi điểm, Giá hiện tại, Người đang giữ giá cao nhất).
- **Đa luồng (Multithreading)**: Chấp nhận nhiều Client kết nối cùng lúc.
- **Đồng bộ hóa (Synchronization)**: Đảm bảo khi nhiều Client cùng Bid một sản phẩm, dữ liệu giá không bị sai lệch (Race Condition).
- **Broadcast**: Khi có một Client đặt giá thành công, Server phải thông báo giá mới cho tất cả các Client khác đang kết nối để họ biết.

### 2. Client
- **Đăng nhập**: Nhập tên người dùng khi bắt đầu.
- **Menu chức năng**:
    1. **VIEW**: Xem danh sách sản phẩm, giá hiện tại và người đang giữ giá cao nhất.
    2. **BID <ID> <Price>**: Đặt giá cho sản phẩm.
        - Giá đặt phải lớn hơn giá hiện tại.
        - Nhận thông báo thành công hoặc thất bại từ Server.
    3. **EXIT**: Ngắt kết nối.
- **Nhận thông báo thời gian thực**: Client phải có một luồng riêng (Thread) để liên tục lắng nghe thông báo từ Server (ví dụ: "Sản phẩm X vừa có giá mới: 1000 bởi User A") ngay cả khi đang đợi nhập lệnh.

## Kịch bản ví dụ
1. **User A** kết nối, xem sản phẩm 1 (Laptop, giá 10tr).
2. **User B** kết nối, xem sản phẩm 1.
3. **User A** BID sản phẩm 1 giá 11tr -> Server cập nhật giá, thông báo cho A "Thành công".
4. Server gửi thông báo cho **User B** (và cả A): "Sản phẩm Laptop đã có giá mới: 11tr bởi User A".
5. **User B** thấy thông báo, quyết định BID 12tr...

## Gợi ý kỹ thuật
- **Server**: Dùng `ServerSocket`, `ExecutorService`. Dùng `List<ClientHandler>` để quản lý danh sách các kết nối nhằm thực hiện Broadcast.
- **Client**: Cần 2 luồng:
    - Luồng chính: Đọc input từ bàn phím và gửi lên Server.
    - Luồng phụ (Listener): Liên tục đọc dữ liệu từ `InputStream` của Socket để hiển thị thông báo từ Server.
- **Dữ liệu**: Có thể dùng `ConcurrentHashMap` hoặc `synchronized` block để bảo vệ dữ liệu sản phẩm.
