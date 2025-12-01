package OnCK.Practice.Bai3.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) {
        new server();
    }

    public server() {
        try {
            System.out.println("Server is running on port 7000...");
            DatagramSocket socket = new DatagramSocket(7000);

            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                byte[] buffer = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                serverHandle handle = new serverHandle(socket, packet);
                executorService.submit(handle);
            }

        } catch (Exception e) {
            System.out.println("Error in server!");
            e.printStackTrace();
        }
    }
}

class serverHandle extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;

    public serverHandle(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            // Nhận dữ liệu từ client
            String receivedData = new String(packet.getData(), 0, packet.getLength()).trim();
            System.out.println("Server received: " + receivedData);

            int n = Integer.parseInt(receivedData);

            String response = caculatorPrimitiveRoot(n);

            // Gửi response về client
            byte[] buffer = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
            socket.send(sendPacket);

            System.out.println("Server sent: " + response);
        } catch (Exception e) {
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
