package OnCK.CK2023.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class client {
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            System.out.println("Error in create DatagramSocket!");
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String x = "";
        try {
            DatagramPacket tt = new DatagramPacket(x.getBytes(), x.length(), InetAddress.getLocalHost(), 5000);
            socket.send(tt);

            byte[] buffer_receive = new byte[1024];
            DatagramPacket packet_receive = new DatagramPacket(buffer_receive, buffer_receive.length);
            socket.receive(packet_receive);

            String rs = new String(packet_receive.getData(), 0, packet_receive.getLength()).trim();

        } catch (IOException e1) {
            System.out.println("Error in send DatagramPacket!");
            e1.printStackTrace();
        }
    }
}
