package Template.UDP;

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
            System.out.println("Server is running in port 5000...");
            DatagramSocket socket = new DatagramSocket(5000);

            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                byte[] buffer = new byte[1024];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                serverHandle handle = new serverHandle(socket, packet);
                
                executorService.execute(handle);
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
            String input = new String(packet.getData(), 0, packet.getLength()).trim();

            System.out.println("Server received: " + input);

            String rs = "," + "," + ",";
            byte[] buffer = rs.getBytes();

            packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
            
            socket.send(packet);
        } catch (Exception e) {
            System.out.println("Error in send DatagramPacket!");
            e.printStackTrace();
        }
    }
}