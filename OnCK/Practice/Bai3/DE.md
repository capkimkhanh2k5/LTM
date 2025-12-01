# BÀI 3: TÌM PRIMITIVE ROOT (CĂN NGUYÊN THỦY)

## Đề bài:

Viết chương trình Client-Server (TCP và UDP) để tìm **primitive root** (căn nguyên thủy) của một số nguyên tố.

### Khái niệm:

Cho số nguyên tố `p`, số `g` được gọi là **primitive root modulo p** nếu:
- `g^1 mod p, g^2 mod p, ..., g^(p-1) mod p` tạo ra **tất cả** các số từ 1 đến p-1 (không trùng lặp)
- Hay nói cách khác: tập hợp `{g^i mod p | i = 1..p-1}` chứa đúng (p-1) phần tử khác nhau

### Yêu cầu:

**Client:**
1. Gửi số nguyên tố `p` đến Server
2. Nhận kết quả từ Server:
   - Primitive root nhỏ nhất `g`
   - Danh sách 5 primitive roots đầu tiên (nếu có)
3. Hiển thị kết quả

**Server:**
1. Nhận số `p` từ Client
2. Kiểm tra `p` có phải số nguyên tố không
3. Tính φ(p) = p - 1 (Euler's totient function)
4. Tìm **tất cả** primitive roots của `p`:
   - Với mỗi `g` từ 2 đến p-1:
     - Tính `g^1 mod p, g^2 mod p, ..., g^(p-1) mod p`
     - Kiểm tra xem có đủ (p-1) phần tử khác nhau không
5. Trả về kết quả:
   - Nếu `p` không phải SNT: `"ERROR:NOT_PRIME"`
   - Nếu không tìm thấy: `"ERROR:NOT_FOUND"`
   - Nếu tìm thấy: `"g_min|g1,g2,g3,g4,g5"`

**Server phải xử lý đa luồng.**

### Format gửi/nhận:

**Client → Server:** 
```
"<số_nguyên>"
Ví dụ: "7"
```

**Server → Client:**
```
Success: "<g_nhỏ_nhất>|<g1>,<g2>,<g3>,<g4>,<g5>"
Error: "ERROR:NOT_PRIME" hoặc "ERROR:NOT_FOUND"
```

### Ví dụ:

**Test case 1: p = 7**
```
Client gửi: "7"
Server trả về: "3|3,5"
Giải thích:
- φ(7) = 6
- g = 3: 3^1=3, 3^2=2, 3^3=6, 3^4=4, 3^5=5, 3^6=1 (mod 7) → 6 phần tử khác nhau ✓
- g = 5: 5^1=5, 5^2=4, 5^3=6, 5^4=2, 5^5=3, 5^6=1 (mod 7) → 6 phần tử khác nhau ✓
- Primitive roots của 7: [3, 5]
```

**Test case 2: p = 11**
```
Client gửi: "11"
Server trả về: "2|2,6,7,8"
Giải thích:
- φ(11) = 10
- Primitive roots của 11: [2, 6, 7, 8]
- Nhỏ nhất: 2
```

**Test case 3: p = 23**
```
Client gửi: "23"
Server trả về: "5|5,7,10,11,14"
Primitive roots của 23: [5, 7, 10, 11, 14, 15, 17, 19, 20, 21]
```

**Test case 4: p = 12 (không phải SNT)**
```
Client gửi: "12"
Server trả về: "ERROR:NOT_PRIME"
```

### Gợi ý thực hiện:

1. **Kiểm tra số nguyên tố:**
```java
boolean isPrime(int n) {
    if (n < 2) return false;
    for (int i = 2; i * i <= n; i++) {
        if (n % i == 0) return false;
    }
    return true;
}
```

2. **Kiểm tra primitive root:**
```java
boolean isPrimitiveRoot(int g, int p) {
    Set<Integer> set = new HashSet<>();
    for (int i = 1; i < p; i++) {
        int value = modPow(g, i, p);
        set.add(value);
    }
    return set.size() == p - 1;
}
```

3. **Tính lũy thừa modulo:**
```java
int modPow(int base, int exp, int mod) {
    int result = 1;
    base = base % mod;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result = (result * base) % mod;
        }
        exp = exp >> 1;
        base = (base * base) % mod;
    }
    return result;
}
```

### Cấu trúc thư mục:

```
OnCK/Practice/Bai3/
├── TCP/
│   ├── client.java
│   └── server.java
└── UDP/
    ├── client.java
    └── server.java
```

### Yêu cầu kỹ thuật:

- **UDP:** Port 7000, Package: `OnCK.Practice.Bai3.UDP`
- **TCP:** Port 7001, Package: `OnCK.Practice.Bai3.TCP`
- Server in ra console request/response
- Client in ra console kết quả

### Thử thách thêm:

1. Tối ưu thuật toán tìm primitive root (dừng sớm khi tìm đủ 5)
2. Cache kết quả đã tính
3. Xử lý số lớn hơn (p > 1000)
4. Thêm thống kê: tổng số primitive roots, tỷ lệ %

**Chúc bạn làm bài tốt!** 🧮💪
