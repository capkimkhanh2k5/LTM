# BÀI 4: HỆ THỐNG TÍNH TOÁN SỐ HỌC (CONTINUOUS SESSION)

## Đề bài:

Viết chương trình Client-Server (TCP và UDP) mô phỏng một **máy tính bỏ túi online** có khả năng xử lý liên tục nhiều phép tính trong một phiên làm việc.

### Điểm mới so với các bài trước:
1. **Xử lý liên tục (Continuous Processing):** Client không đóng kết nối sau mỗi request. Client gửi lệnh -> Server trả lời -> Client gửi lệnh tiếp... cho đến khi gửi lệnh `EXIT`.
2. **Duy trì trạng thái (Stateful):** Server cần lưu trữ lịch sử tính toán của từng Client trong phiên đó.
3. **Xử lý chuỗi biểu thức:** Server phải có khả năng parse và tính toán biểu thức toán học.

---

## Yêu cầu chức năng:

### Client:
1. Kết nối đến Server.
2. Hiển thị menu hoặc cho phép người dùng nhập lệnh liên tục từ bàn phím.
3. Gửi lệnh đến Server và hiển thị kết quả trả về.
4. Chỉ thoát khi người dùng nhập `EXIT`.

### Server:
1. Chấp nhận kết nối từ Client.
2. **Vòng lặp xử lý:**
   - Nhận lệnh từ Client.
   - Xử lý lệnh.
   - Gửi kết quả.
   - Lặp lại cho đến khi nhận lệnh `EXIT` hoặc Client ngắt kết nối.
3. **Hỗ trợ các lệnh sau:**
   - `CALC <biểu_thức>`: Tính giá trị biểu thức. Hỗ trợ `+`, `-`, `*`, `/`, `^` (lũy thừa).
     - Ví dụ: `CALC (10 + 5) * 2`
   - `PRIME <n>`: Kiểm tra n có phải số nguyên tố không.
   - `FACTOR <n>`: Phân tích n thành thừa số nguyên tố.
   - `HISTORY`: Xem lại 5 phép tính gần nhất trong phiên này.
   - `EXIT`: Ngắt kết nối.

---

## Format Giao thức (Protocol):

**1. Lệnh tính toán:**
- Request: `CALC (10+2)*3`
- Response: `OK:36` hoặc `ERROR:Invalid Expression`

**2. Lệnh kiểm tra SNT:**
- Request: `PRIME 17`
- Response: `OK:TRUE` hoặc `OK:FALSE`

**3. Lệnh phân tích thừa số:**
- Request: `FACTOR 12`
- Response: `OK:2*2*3`

**4. Lệnh xem lịch sử:**
- Request: `HISTORY`
- Response: 
```
OK:
1. (10+2)*3 = 36
2. PRIME 17 = TRUE
```

**5. Lệnh thoát:**
- Request: `EXIT`
- Response: `GOODBYE` (Sau đó Server đóng kết nối với Client này)

---

## Gợi ý thực hiện:

### 1. Xử lý liên tục trên Server (TCP):

Thay vì đóng kết nối ngay, hãy dùng vòng lặp `while` bên trong `run()` của thread xử lý:

```java
public void run() {
    try {
        // Mở stream
        while (true) {
            String request = dis.readUTF();
            if (request.equals("EXIT")) {
                dos.writeUTF("GOODBYE");
                break; // Thoát vòng lặp để đóng kết nối
            }
            
            String result = processRequest(request);
            dos.writeUTF(result);
            dos.flush();
        }
        // Đóng kết nối
        socket.close();
    } catch (IOException e) {
        // Xử lý khi client ngắt đột ngột
    }
}
```

### 2. Xử lý biểu thức toán học:
Bạn có thể dùng:
- **Cách đơn giản:** Chỉ hỗ trợ 2 số (VD: `a + b`).
- **Cách nâng cao (Khuyên dùng):** Dùng thư viện `ScriptEngine` của Java hoặc tự viết thuật toán **Shunting-yard** để chuyển trung tố sang hậu tố (Reverse Polish Notation) rồi tính.

**Ví dụ dùng ScriptEngine (đơn giản nhất):**
```java
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

ScriptEngineManager mgr = new ScriptEngineManager();
ScriptEngine engine = mgr.getEngineByName("JavaScript");
String result = engine.eval("10 + 5 * 2").toString();
```
*(Lưu ý: ScriptEngine có thể bị deprecated ở Java mới, nếu không dùng được hãy tự viết hàm parse đơn giản)*

---

## Cấu trúc thư mục:

```
OnCK/Practice/Bai4/
├── DE.md
├── TCP/
│   ├── client.java
│   └── server.java
└── UDP/
    ├── client.java
    └── server.java
```

*(Với UDP, khái niệm "kết nối" không tồn tại, nhưng bạn có thể giả lập bằng cách Client gửi liên tục và Server phản hồi liên tục. Tuy nhiên, TCP phù hợp hơn cho bài này)*

**Chúc bạn thành công!** 🚀
