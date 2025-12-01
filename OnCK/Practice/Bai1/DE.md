# BÀI TẬP THỰC HÀNH

## Đề bài: Tính số ngày giữa hai ngày

### Yêu cầu:

Viết chương trình UDP Client-Server với các chức năng sau:

**Client:**
1. Gửi hai ngày theo định dạng `dd/MM/yyyy` (ngày bắt đầu và ngày kết thúc) đến Server
2. Nhận kết quả từ Server là số ngày giữa hai ngày đó
3. Hiển thị kết quả ra màn hình

**Server:**
1. Nhận hai ngày từ Client
2. Kiểm tra tính hợp lệ của hai ngày:
   - Định dạng đúng `dd/MM/yyyy`
   - Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc
3. Tính số ngày giữa hai ngày (bao gồm cả ngày bắt đầu và ngày kết thúc)
4. Gửi kết quả về cho Client
5. Nếu dữ liệu không hợp lệ, gửi về `-1`

**Server phải xử lý đa luồng (multi-threading) để phục vụ nhiều Client cùng lúc.**

### Ví dụ:

**Input từ Client:**
```
Ngày bắt đầu: 01/01/2025
Ngày kết thúc: 10/01/2025
```

**Output từ Server:**
```
Số ngày: 10
```

**Input từ Client (không hợp lệ):**
```
Ngày bắt đầu: 10/01/2025
Ngày kết thúc: 05/01/2025
```

**Output từ Server:**
```
-1
```

### Gợi ý:

1. Sử dụng `DatagramSocket` và `DatagramPacket` cho UDP
2. Sử dụng `ExecutorService` để xử lý đa luồng
3. Sử dụng `LocalDate` và `DateTimeFormatter` để xử lý ngày tháng
4. Sử dụng `ChronoUnit.DAYS.between()` để tính số ngày giữa hai ngày
5. Format gửi từ Client: `"ngày1|ngày2"` (ví dụ: `"01/01/2025|10/01/2025"`)

### Cấu trúc thư mục:

```
OnCK/Practice/UDP/
├── client.java
└── server.java
```

### Yêu cầu kỹ thuật:

- Port: 5000
- Package: `OnCK.Practice.UDP`
- Server phải in ra console mỗi khi nhận và gửi dữ liệu
- Client phải in ra console mỗi khi gửi và nhận dữ liệu

---

## Hướng dẫn kiểm tra:

1. Chạy Server trước
2. Chạy Client sau
3. Kiểm tra kết quả trên console của cả Client và Server
4. Thử với nhiều trường hợp khác nhau:
   - Hai ngày hợp lệ
   - Ngày bắt đầu lớn hơn ngày kết thúc
   - Định dạng ngày không đúng
   - Hai ngày giống nhau (kết quả phải là 1)

**Chúc bạn làm bài tốt!** 🚀
