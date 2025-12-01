package OnCK.Practice.Bai3.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

            // TODO: Parse số p từ receivedData
            // TODO: Kiểm tra p có phải số nguyên tố không
            // TODO: Tìm primitive roots của p
            // TODO: Build response string

            String response = "Response from server";

            // Gửi response về client
            byte[] buffer = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    buffer,
                    buffer.length,
                    packet.getAddress(),
                    packet.getPort());
            socket.send(sendPacket);

            System.out.println("Server sent: " + response);
        } catch (Exception e) {
            System.out.println("Error in serverHandle!");
            e.printStackTrace();
        }
    }
}
