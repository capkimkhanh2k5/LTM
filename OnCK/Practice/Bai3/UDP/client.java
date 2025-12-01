package OnCK.Practice.Bai3.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class client {
    private DatagramSocket socket;
    private int port = 7000;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new DatagramSocket();

            // TODO: Gửi số nguyên tố p đến server
            String message = "23";
            byte[] sendBuffer = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    sendBuffer,
                    sendBuffer.length,
                    InetAddress.getLocalHost(),
                    port);
            socket.send(sendPacket);
            System.out.println("Client sent: " + message);

            // TODO: Nhận kết quả từ server
            byte[] receiveBuffer = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            System.out.println("Client received: " + response);

            // TODO: Hiển thị kết quả (parse response)

            // Đóng socket
            socket.close();
        } catch (Exception e) {
            System.out.println("Error in client!");
            e.printStackTrace();
        }
    }
}
