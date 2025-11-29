package OnCK.CK2023.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class client implements Runnable {
    private static final long serialVersionUID = 1L;

    private Socket soc;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            soc = new Socket("localhost", 5001);
        } catch (Exception e) {
            System.out.println("Error in connection!");
        }

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {

        // Ramdom 2 -> 10^9
        int n = 14;
        System.out.println("Client send n = " + n + " to server!");

        try {
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
            dos.writeInt(n);

            dos.writeInt(0);
            dos.flush();

            int t = countCoPrimes(n);

            DataInputStream dis = new DataInputStream(soc.getInputStream());

            while (true) {
                try {

                    int m = dis.readInt();

                    if (m == -1) {
                        System.out.println("Not find M!");
                        break;
                    }

                    if (checkValid(m, n, t)) {
                        System.out.println("So " + m + " hop le!");

                        dos.writeInt(1);
                        dos.flush();

                        break;
                    } else {
                        System.out.println("So " + m + " khong hop le!");

                        dos.writeInt(0);
                        dos.flush();
                    }

                } catch (Exception e) {
                    System.out.println("Error in max connection!");
                    break;
                }
            }

            dos.close();
            dis.close();

            soc.close();
        } catch (Exception e) {
            System.out.println("Error in connection Server!");
        }
    }

    // Kiểm tra m hợp lệ
    private boolean checkValid(int m, int n, int t) {
        ArrayList<Integer> arr = new ArrayList<>();

        for (int i = 1; i <= t; i++) {
            int tmp = (int) Math.pow(m, i);
            int remainder = tmp % n;

            // Kiểm tra xem remainder đã tồn tại trong mảng chưa
            if (arr.contains(remainder)) {
                return false; // Tìm thấy trùng lặp
            }
            arr.add(remainder);
        }

        return true; // Không có trùng lặp, m hợp lệ
    }

    // Tìm số SNT cùng nhau với n
    public int countCoPrimes(int n) {
        int result = n;

        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                result -= result / i;

                while (n % i == 0) {
                    n /= i;
                }
            }
        }

        if (n > 1) {
            result -= result / n;
        }

        return result;
    }
}
