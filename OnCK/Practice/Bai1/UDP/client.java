package OnCK.Practice.Bai1.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class client {
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket;
    private int port = 5000;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new DatagramSocket();

            // Gửi hai ngày đến server theo format "ngày1|ngày2"
            String dates = "1/1/2025|10/1/2025";

            // Tạo DatagramPacket và gửi đến server (port 5000)
            DatagramPacket packet = new DatagramPacket(dates.getBytes(), dates.length(), InetAddress.getLocalHost(),
                    port);
            socket.send(packet);

            System.out.println("Client sent: " + dates);

            // Nhận kết quả từ server
            byte[] buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            int result = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));

            if(result == -1){
                System.out.println("Format Dates is Invalid!");
            }
            else{
                // Hiển thị kết quả
                System.out.println("Client Received Result: " + result);
            }

            // Đóng socket
            socket.close();
        } catch (Exception e) {
            System.out.println("Error in create DatagramSocket!");
            e.printStackTrace();
        }
    }
}
