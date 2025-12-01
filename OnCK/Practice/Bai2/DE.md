# BÀI TẬP THỰC HÀNH - BÀI 2 (Nâng cao)

## Đề bài: Hệ thống xử lý điểm sinh viên

### Mô tả

Viết chương trình Client-Server (cả TCP và UDP) để xử lý và tính toán thống kê điểm sinh viên.

### Yêu cầu

**Client:**
1. Gửi danh sách điểm của sinh viên theo định dạng JSON
   ```json
   {
     "studentId": "SV001",
     "name": "Nguyen Van A",
     "scores": [8.5, 9.0, 7.5, 8.0, 9.5]
   }
   ```
2. Nhận kết quả thống kê từ Server (format JSON)
3. Hiển thị kết quả ra màn hình

**Server:**
1. Nhận dữ liệu JSON từ Client
2. Kiểm tra tính hợp lệ:
   - JSON format đúng
   - `studentId` không rỗng và có độ dài từ 4-10 ký tự
   - `name` không rỗng
   - Mảng `scores` có từ 1-10 phần tử
   - Mỗi điểm trong khoảng [0, 10]
3. Tính toán thống kê:
   - Điểm trung bình (average)
   - Điểm cao nhất (max)
   - Điểm thấp nhất (min)
   - Xếp loại (grade):
     - "Xuất sắc" nếu average >= 9.0
     - "Giỏi" nếu 8.0 <= average < 9.0
     - "Khá" nếu 7.0 <= average < 8.0
     - "Trung bình" nếu 5.0 <= average < 7.0
     - "Yếu" nếu average < 5.0
4. Gửi kết quả về Client dưới dạng JSON:
   ```json
   {
     "studentId": "SV001",
     "name": "Nguyen Van A",
     "average": 8.5,
     "max": 9.5,
     "min": 7.5,
     "grade": "Giỏi",
     "totalScores": 5
   }
   ```
5. Nếu dữ liệu không hợp lệ, gửi về:
   ```json
   {
     "error": "Mô tả lỗi cụ thể"
   }
   ```

**Server phải xử lý đa luồng để phục vụ nhiều Client cùng lúc.**

### Ví dụ:

**Input từ Client:**
```json
{
  "studentId": "SV001",
  "name": "Nguyen Van A",
  "scores": [8.5, 9.0, 7.5, 8.0, 9.5]
}
```

**Output từ Server:**
```json
{
  "studentId": "SV001",
  "name": "Nguyen Van A",
  "average": 8.5,
  "max": 9.5,
  "min": 7.5,
  "grade": "Giỏi",
  "totalScores": 5
}
```

**Input không hợp lệ:**
```json
{
  "studentId": "SV",
  "name": "",
  "scores": [11.0, -1.0]
}
```

**Output lỗi:**
```json
{
  "error": "Invalid studentId length, name is empty, scores contain invalid values"
}
```

### Gợi ý:

1. **Xử lý JSON:**
   - Có thể sử dụng thư viện `org.json` (cần import)
   - Hoặc tự parse/build JSON string thủ công (khuyến khích)
   
2. **Tính toán:**
   - Sử dụng vòng lặp để tính average, max, min
   - Dùng if-else để xác định grade
   
3. **Validation:**
   - Kiểm tra từng trường một
   - Thu thập tất cả lỗi và gửi về một lần

4. **Multi-threading:**
   - UDP: Sử dụng `ExecutorService`
   - TCP: Sử dụng `ExecutorService` cho mỗi connection

### Cấu trúc thư mục:

```
OnCK/Practice/Bai2/
├── DE.md
├── TCP/
│   ├── client.java
│   └── server.java
└── UDP/
    ├── client.java
    └── server.java
```

### Yêu cầu kỹ thuật:

- **UDP:** Port 6000, Package: `OnCK.Practice.Bai2.UDP`
- **TCP:** Port 6001, Package: `OnCK.Practice.Bai2.TCP`
- Server phải in ra console mỗi khi nhận và gửi dữ liệu
- Client phải in ra console mỗi khi gửi và nhận dữ liệu
- Xử lý JSON **không bắt buộc** dùng thư viện bên ngoài

### Hướng dẫn kiểm tra:

1. **Test case 1:** Dữ liệu hợp lệ
   ```
   studentId: "SV001"
   name: "Nguyen Van A"
   scores: [8.5, 9.0, 7.5, 8.0, 9.5]
   Expected: average=8.5, grade="Giỏi"
   ```

2. **Test case 2:** Điểm xuất sắc
   ```
   studentId: "SV002"
   name: "Tran Thi B"
   scores: [9.0, 9.5, 9.2, 9.8, 9.0]
   Expected: average=9.3, grade="Xuất sắc"
   ```

3. **Test case 3:** Dữ liệu không hợp lệ
   ```
   studentId: "SV"
   name: ""
   scores: [11.0, -5.0]
   Expected: error message
   ```

4. **Test case 4:** Điểm yếu
   ```
   studentId: "SV003"
   name: "Le Van C"
   scores: [3.5, 4.0, 4.5]
   Expected: average=4.0, grade="Yếu"
   ```

### Thử thách thêm (Nâng cao):

- Xử lý nhiều sinh viên trong một request (mảng sinh viên)
- Thêm chức năng lưu kết quả vào file
- Thêm timestamp cho mỗi request
- Cache kết quả để tránh tính toán lại cho cùng một sinh viên

**Chúc bạn làm bài tốt!** 🚀💪
