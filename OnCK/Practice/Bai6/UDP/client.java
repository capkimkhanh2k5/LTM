package OnCK.Practice.Bai6.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class client {
    private DatagramSocket socket;
    private int port = 5000;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new DatagramSocket();

            // TODO: Gửi dữ liệu đến server
            String message = "Hello Server";
            byte[] sendBuffer = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    sendBuffer,
                    sendBuffer.length,
                    InetAddress.getLocalHost(),
                    port);
            socket.send(sendPacket);
            System.out.println("Client sent: " + message);

            // TODO: Nhận dữ liệu từ server
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            System.out.println("Client received: " + response);

            // Đóng socket
            socket.close();
        } catch (Exception e) {
            System.out.println("Error in client!");
            e.printStackTrace();
        }
    }
}
