package OnCK.Practice.Bai3.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Server is running on port 5001...");

            while (true) {
                Socket soc = serverSocket.accept();
                System.out.println("Client connected: " + soc.getInetAddress().getHostAddress() + ":" + soc.getPort());

                serverHandle handle = new serverHandle(soc);
                executorService.execute(handle);
            }
        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread {
    private Socket soc;

    public serverHandle(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());

            // Nhận dữ liệu từ client
            int n = dis.readInt();
            System.out.println("Server received: " + n);

            // Xử lý dữ liệu và gửi response
            String response = caculatorPrimitiveRoot(n);

            dos.writeUTF(response);
            dos.flush();
            System.out.println("Server sent: " + response);

            // Đóng kết nối
            dis.close();
            dos.close();
            soc.close();
        } catch (IOException e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }

    private String caculatorPrimitiveRoot(int n) {
        if (!checkExitsPrimitiveRoot(n)) {
            return "ERROR:NOT_VALID";
        }

        List<Integer> coPrimes = getCoprimes(n);
        List<Integer> result = new ArrayList<>();

        for (Integer i : coPrimes) {
            List<Integer> remember = new ArrayList<>();
            for (int j = 1; j < n; j++) {
                int mod = power(i, j, n);

                if (remember.contains(mod))
                    break;

                remember.add(mod);
            }

            if (remember.size() == coPrimes.size())
                result.add(i);
        }

        if (result.size() == 0)
            return "ERROR:NOT_FOUND";

        String msg = "Primitive Root: ";
        for (int r : result) {
            if (r == result.getFirst()) {
                msg += "Min: " + r + " | ";
            }
            msg += " " + r;
        }

        return msg;
    }

    // Hàm tính mũ lấy dư a^b%n
    public int power(int base, int exp, int mod) {
        long res = 1;
        long baseL = base;

        baseL = baseL % mod;

        while (exp > 0) {
            if (exp % 2 == 1) {
                res = (res * baseL) % mod;
            }

            baseL = (baseL * baseL) % mod;
            exp = exp / 2;
        }

        return (int) res;
    }

    private boolean checkExitsPrimitiveRoot(int n) {
        // Chỉ tồn tại khi n là dạng 1,2,4, p^k, 2p^k với p là SNT
        if (n == 1 || n == 2 || n == 4)
            return true;

        List<Integer> list = getPrimeFactors(n);

        if (list.size() == 1)
            return true;

        if (list.size() == 2 && list.contains(2))
            return true;

        return false;
    }

    // Lấy toàn bộ các ước số nguyên tố
    private List<Integer> getPrimeFactors(int n) {
        List<Integer> primeFactors = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (n % i == 0) {
                primeFactors.add(i);
                while (n % i == 0) {
                    n /= i;
                }
            }
        }
        return primeFactors;
    }

    private List<Integer> getCoprimes(int n) {
        List<Integer> results = new ArrayList<>();

        for (int i = 1; i < n; i++) {
            if (gcd(n, i) == 1) {
                results.add(i);
            }
        }
        return results;
    }

    // Hàm hỗ trợ tìm Ước chung lớn nhất (thuật toán Euclid)
    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}