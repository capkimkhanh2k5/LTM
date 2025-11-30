package OnCK.CK2021.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class client{
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket;

    public static void main(String[] args) {
        new client();
    }

    public client() {

        try {
            socket = new DatagramSocket();

            String date = "29/1/2025";
            byte[] buffer = date.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 5000);
            socket.send(packet);
            
            System.out.println("Client sent: " + date);

            buffer = new byte[1024];

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String date_receive = new String (packet.getData(), 0, packet.getLength()).trim();
            System.out.println("Client received: " + date_receive);
        } catch (Exception e) {
            System.out.println("Error in create DatagramSocket!");
            e.printStackTrace();
        }
    }
}
