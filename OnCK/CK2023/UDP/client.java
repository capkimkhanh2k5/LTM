package OnCK.CK2023.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class client {
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new DatagramSocket();

            int n = 3019;
            int t = countCoPrimes(n);

            while(true){
                //Gửi n
                DatagramPacket packet = new DatagramPacket(new String(String.valueOf(n)).getBytes(), (String.valueOf(n)).length(), InetAddress.getLocalHost(), 5000);
                socket.send(packet);

                byte[] buffer_receive = new byte[1024];

                //Nhận m
                DatagramPacket packet_receive = new DatagramPacket(buffer_receive, buffer_receive.length);
                socket.receive(packet_receive);

                String rs = new String(packet_receive.getData(), 0, packet_receive.getLength()).trim();
                int m = Integer.parseInt(rs);

                if(m == -1){
                    System.out.println("Server has finished because max try find M!");
                    break;
                }

                System.out.println("Client received: " + rs);

                if(checkValid(m, n, t)){
                    System.out.println("Index " + m + " is valid!");

                    String done = String.valueOf(1);
                    DatagramPacket packet_done = new DatagramPacket(done.getBytes(), done.length(), packet_receive.getAddress(), packet_receive.getPort());
                    socket.send(packet_done);

                    break;
                }
                else{
                    System.out.println("Index " + m + " is not valid!");
                }
            }
        }catch(Exception e){
            System.out.println("Error in create DatagramSocket!");
            e.printStackTrace();
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
                return false;
            }
            arr.add(remainder);
        }

        return true;
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
