# Bài 6: Chat Nhóm Đơn Giản (Simple Group Chat)

**Mức độ**: Dễ - Trung bình
**Giao thức**: TCP
**Kiến thức**: Socket, Multithreading, IO Streams, Collections.

## Mô tả bài toán
Xây dựng một chương trình Chat Room cơ bản sử dụng Java Socket (TCP). Chương trình cho phép nhiều người dùng kết nối vào Server và gửi tin nhắn. Khi một người gửi tin, tất cả những người khác đang online đều nhận được (Broadcast). Server đóng vai trò trung gian và giám sát nội dung chat.

## Yêu cầu chức năng

### 1. Server
- **Quản lý kết nối**:
    - Chấp nhận nhiều Client kết nối đồng thời (Multithreading).
    - Lưu trữ danh sách các Client đang online.
- **Xử lý tin nhắn (Broadcast)**:
    - Khi nhận tin nhắn từ Client A, Server gửi lại tin nhắn đó cho tất cả Client khác (B, C, D...).
    - **Giám sát**: Hiển thị nội dung tin nhắn và người gửi lên màn hình Server để quản trị viên theo dõi.
- **Quản lý trạng thái**:
    - Xử lý khi Client ngắt kết nối: Xóa khỏi danh sách và thông báo cho các Client còn lại.

### 2. Client
- **Kết nối**: Nhập IP và Port Server để tham gia.
- **Định danh**: Nhập tên (Nickname) khi vừa kết nối.
- **Giao diện**:
    - Nhập tin nhắn từ bàn phím để gửi.
    - Hiển thị tin nhắn nhận được từ người khác ngay lập tức.
- **Luồng xử lý**:
    - Cần xử lý song song việc **Gửi tin** (Write) và **Nhận tin** (Read) để không bị chặn (Blocking).

## Kịch bản ví dụ
1. **Server** Start: Listening on port 12345...
2. **User A (Tuan)** Connect: Nhập tên "Tuan".
3. **Server**: Log "Client Tuan connected".
4. **User B (Lan)** Connect: Nhập tên "Lan".
5. **Tuan**: Gõ "Hello moi nguoi".
6. **Server**: Broadcast "[Tuan]: Hello moi nguoi".
7. **Lan**: Nhận được "[Tuan]: Hello moi nguoi".
8. **Lan**: Gõ "Hi Tuan".
9. **Server**: Broadcast "[Lan]: Hi Tuan".
10. **Tuan**: Nhận được "[Lan]: Hi Tuan".
11. **Tuan**: Exit.
12. **Server**: Broadcast "Tuan has left the chat".
