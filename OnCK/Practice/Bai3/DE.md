# BÀI 3: TÌM PRIMITIVE ROOT (CĂN NGUYÊN THỦY)

## Đề bài:

Viết chương trình Client-Server (TCP và UDP) để tìm **primitive root** (căn nguyên thủy) của một số nguyên dương `n`.

### Khái niệm:

Cho số nguyên `n ≥ 1`, số `g` (với gcd(g, n) = 1) được gọi là **primitive root modulo n** nếu:
- Order của g modulo n bằng φ(n)
- Tức là: `g^φ(n) ≡ 1 (mod n)` và không tồn tại số mũ nhỏ hơn φ(n) thỏa mãn

**Điều kiện tồn tại:** Primitive root modulo n **chỉ tồn tại** khi n thuộc một trong các dạng sau:
- n = 1, 2, 4
- n = p^k (lũy thừa của số nguyên tố lẻ p, với k ≥ 1)
- n = 2·p^k (với p là số nguyên tố lẻ, k ≥ 1)

---

## Yêu cầu:

### Client:
1. Gửi số nguyên dương `n` đến Server
2. Nhận và hiển thị kết quả từ Server

### Server:
1. Nhận số `n` từ Client
2. **Kiểm tra n có thuộc các dạng có primitive root không**:
   - Kiểm tra n = 1, 2, hoặc 4
   - Kiểm tra n = p^k (lũy thừa của SNT lẻ)
   - Kiểm tra n = 2·p^k
3. Nếu hợp lệ, tìm primitive roots:
   - Tính φ(n)
   - Với mỗi g từ 1 đến n-1 (gcd(g,n)=1) -> là các số nguyên tố cùng nhau:
     - Kiểm tra order(g) = φ(n)
   - Trả về primitive root nhỏ nhất và danh sách tối đa 5 primitive roots
4. Trả về kết quả theo format

### Format gửi/nhận:

**Request (Client → Server):**
```
n
```

**Response (Server → Client):**
```
Success: "g_min|g1,g2,g3,g4,g5"
Error:   "ERROR:NOT_VALID" (n không thuộc dạng có primitive root)
         "ERROR:NOT_FOUND" (tìm không ra - lý thuyết không xảy ra nếu n hợp lệ)
```

**Server phải xử lý đa luồng.**

---

## Ví dụ minh họa:

### ✅ Case 1: n = 7 (Số nguyên tố lẻ - dạng p^1)

**Request:**
```
7
```

**Phân tích:**
1. Kiểm tra dạng: 7 là SNT lẻ → n = p^1 ✓ (hợp lệ)
2. Tính φ(7) = 6
3. Tìm primitive roots:
   - Kiểm tra g = 1: 1^i ≡ 1 → order(1) = 1 ≠ 6 ✗
   - Kiểm tra g = 2: 
     - 2^1=2, 2^2=4, 2^3=1 (mod 7) → order(2) = 3 ≠ 6 ✗
   - Kiểm tra g = 3:
     - 3^1=3, 3^2=2, 3^3=6, 3^4=4, 3^5=5, 3^6=1 (mod 7)
     - Tạo ra {3,2,6,4,5,1} = 6 phần tử → order(3) = 6 ✓
   - Kiểm tra g = 4:
     - 4^1=4, 4^2=2, 4^3=1 (mod 7) → order(4) = 3 ✗
   - Kiểm tra g = 5:
     - 5^1=5, 5^2=4, 5^3=6, 5^4=2, 5^5=3, 5^6=1 (mod 7)
     - Tạo ra 6 phần tử → order(5) = 6 ✓
   - Kiểm tra g = 6:
     - 6^1=6, 6^2=1 (mod 7) → order(6) = 2 ✗

**Kết quả:** Primitive roots của 7 là {3, 5}

**Response:**
```
3|3,5
```

---

### ✅ Case 2: n = 9 (= 3²)

**Request:**
```
9
```

**Phân tích:**
1. Kiểm tra: 9 = 3² (lũy thừa của SNT lẻ) ✓
2. φ(9) = 9·(1 - 1/3) = 6
3. Tìm primitive roots: {2, 5}

**Response:**
```
2|2,5
```

---

## Các trường hợp lỗi:

### ❌ Case 3: n = 8 (= 2³)

**Request:**
```
8
```

**Phân tích:**
- 8 = 2³ không thuộc dạng có primitive root
- (Chỉ 2^1=2 và 2^2=4 mới có)

**Response:**
```
ERROR:NOT_VALID
```

---

### ❌ Case 4: n = 12 (= 2² × 3)

**Request:**
```
12
```

**Phân tích:**
- 12 = 2² × 3 không thuộc dạng (1, 2, 4, p^k, 2p^k)

**Response:**
```
ERROR:NOT_VALID
```

---

### ❌ Case 5: n = 15 (= 3 × 5)

**Request:**
```
15
```

**Phân tích:**
- 15 = 3 × 5 (tích 2 SNT khác nhau) không thuộc các dạng

**Response:**
```
ERROR:NOT_VALID
```

---

## Cấu trúc thư mục:

```
OnCK/Practice/Bai3/
├── TCP/
│   ├── client.java
│   └── server.java
└── UDP/
    ├── client.java
    └── server.java
```

## Yêu cầu kỹ thuật:

- **UDP:** Port 7000, Package: `OnCK.Practice.Bai3.UDP`
- **TCP:** Port 7001, Package: `OnCK.Practice.Bai3.TCP`
- Server in console: request received, response sent
- Client in console: kết quả

**Chúc bạn làm bài tốt!** 🧮💪
